package sig.plugin.TwosideKeeper.HolidayEvents;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Common.BlockModifyQueue;

public class TreeBuilder{
	int height = 8;
	double radius = 10;
	double initialradius = 10;
	int branches = 10;
	int spacing = 3;
	int baserad = 0;
	Location loc;
	
	public static TreeBuilder BuildNewTree(Location loc, int height, double radius, int baseradius) {
		TreeBuilder builder = new TreeBuilder(loc,height,radius,baseradius);
		builder.BuildTree();
		return builder;
	}
	
	public TreeBuilder(Location loc, int height, double radius, int baseradius) {
		this.height=height;
		this.radius=radius;
		this.initialradius=radius;
		this.branches=height+2;
		this.loc=loc.clone();
		this.baserad=baseradius;
		this.spacing=height/6+1;
	}
	
	public void BuildTree() {
		//BuildTreeBase(loc,baserad,height*spacing);
		for (int y=0;y<height;y++) {
			//int buildy = (y*spacing)+spacing;
			
			if ((double)radius/4<baserad) {
				BuildTreeBase(loc,0,spacing);
			} else {
				BuildTreeBase(loc,baserad,spacing);
			}
			
			loc.add(0, spacing, 0);
			double randoffsetdir = java.util.concurrent.ThreadLocalRandom.current().nextDouble()*360;
			for (int split=branches;split>0;split--) {
				double randminoroffsetdir = (java.util.concurrent.ThreadLocalRandom.current().nextDouble()*(360/(double)branches))/4;
				TreeBranch branch = new TreeBranch(loc,(360/(double)branches)*(double)split+randoffsetdir+randminoroffsetdir,radius);
				branch.build();
			}
			radius-=(initialradius/(double)height);
			branches--;
		}
	}

	private void BuildTreeBase(Location loc, int width, int height) {
		Location currentloc = loc.clone().add(-1,0,-1);
		for (int i=0;i<height;i++) {
			for (int x=-(width-1);x<=width+1;x++) {
				for (int z=-(width-1);z<=width+1;z++) {
					Block b = currentloc.clone().add(x, 0, z).getBlock();
					if (b.getType()==Material.AIR || b.getType()==Material.SNOW || b.getType()==Material.LEAVES) {
						b.setType(Material.LOG);
						b.setData((byte)13);
					}
				}
			}
			currentloc.add(0,1,0);
		}
	}
}

class TreeBranch{
	double dir; //0-360 degrees.
	double length;
	int twigs;
	Location baseloc;
	TreeBranch(Location loc, double dir, double length) {
		this.dir=dir;
		this.length=length;
		this.baseloc=loc.clone();
		this.twigs=(int)length/2;
	}
	
	void build() {
		//Builds a line of blocks for this branch based on given dir and length.
		double velx = Math.sin(Math.toRadians(dir));
		double vely = java.util.concurrent.ThreadLocalRandom.current().nextDouble()*0.1-0.2;
		double velz = Math.cos(Math.toRadians(dir));
		double x = velx; //Offset X.
		double z = velz; //Offset Z.
		double y = vely;
		for (int i=0;i<length;i++) {
			Location newloc = baseloc.clone();
			Block b = newloc.add(x, y, z).getBlock();
			/*if (b.getType()==Material.AIR) {
				b.setType(Material.LEAVES);
				b.setData((byte)5);
			}*/
			TwosideKeeper.blockqueue.add(new BlockModifyQueue(b,Material.AIR,(byte)0,Material.LEAVES,(byte)5));
			if (Math.random()<=0.4) {
				TwosideKeeper.blockqueue.add(new BlockModifyQueue(b.getRelative(0, 1, 0),Material.AIR,(byte)0,Material.SNOW,(byte)0));
			} else 
			if (Math.random()<=0.1) {
				TwosideKeeper.blockqueue.add(new BlockModifyQueue(b.getRelative(0, 1, 0),Material.AIR,(byte)0,Material.SNOW,(byte)1));
			}
			if (Math.random()<=0.1) {
				TreeTwig twig = new TreeTwig(newloc,(int)(java.util.concurrent.ThreadLocalRandom.current().nextDouble()*twigs),length/4);
				twig.CreateTwigs();
			}
			x+=velx;
			y+=vely;
			z+=velz;
		}
	}
}

class TreeTwig{
	//Creates a fan of leaves projecting outward from a branch.
	Location loc;
	int split; //Number of splits.
	double length; //Length in meters.
	TreeTwig(Location loc, int twigs, double length) {
		this.loc=loc.clone();
		this.split=twigs;
		this.length=length;
	}
	void CreateTwigs() {
		double randoffsetdir = java.util.concurrent.ThreadLocalRandom.current().nextDouble()*360;
		for (int i=0;i<split;i++) {
			Location newloc = loc.clone();
			double dir = (double)360/split*i+randoffsetdir;
			double randspread = (java.util.concurrent.ThreadLocalRandom.current().nextDouble()*length)-(length/2);
			double angle = Math.atan2(Math.sin(dir), Math.cos(dir));
			double angle2 = Math.atan2(randspread, length);
			double xvel = Math.sin(angle);
			double yvel = Math.sin(angle2);
			double zvel = Math.cos(angle);
			for (int j=0;j<length;j++) {
				Block b = newloc.add(xvel,yvel,zvel).getBlock();
				/*if (b.getType()==Material.AIR) {
					b.setType(Material.LEAVES);
					b.setData((byte)4);
				}*/
				if (b.getType()==Material.AIR) {
					TwosideKeeper.blockqueue.add(new BlockModifyQueue(b,Material.AIR,(byte)0,Material.LEAVES,(byte)4));
					if (Math.random()<=0.2) {
						TwosideKeeper.blockqueue.add(new BlockModifyQueue(b.getRelative(0, 1, 0),Material.AIR,(byte)0,Material.SNOW,(byte)0));
					} else 
					if (Math.random()<=0.1) {
						TwosideKeeper.blockqueue.add(new BlockModifyQueue(b.getRelative(0, 1, 0),Material.AIR,(byte)0,Material.SNOW,(byte)1));
					}
				} else {
					break;
				}
			}
		}
	}
}