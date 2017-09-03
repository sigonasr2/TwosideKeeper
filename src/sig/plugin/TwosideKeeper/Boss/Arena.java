package sig.plugin.TwosideKeeper.Boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.Box;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MaterialData;

/**
 * Holds data about an arena.
 * @param <E>
 */
public class Arena<E> {
	HashMap<Block,MaterialData> oldblocklist = new HashMap<Block,MaterialData>();
	List<Material> wallmats = new ArrayList<Material>();
	List<Material> floormats = new ArrayList<Material>();
	List<Material> roofmats = new ArrayList<Material>();
	List<Material> insidemats = new ArrayList<Material>();
	List<Chunk> temporarychunks = new ArrayList<Chunk>();
	List<Player> arenaplayers = new ArrayList<Player>();
	Box box;
	World world;
	E test;
	
	public Arena(World world, int x, int y, int z, int w, int h, int d,Material arena_mat) {
		this.world = world;
		this.box = new Box(x,y,z,w,h,d);
		this.wallmats.add(arena_mat);
		this.floormats.add(arena_mat);
		this.roofmats.add(arena_mat);
	}
	
	public Arena(World world, int x, int y, int z, int w, int h, int d,Material...arena_mats) {
		this.world = world;
		this.box = new Box(x,y,z,w,h,d);
		for (Material mat : arena_mats) {
			this.wallmats.add(mat);
			this.floormats.add(mat);
			this.roofmats.add(mat);
		}
	}
	
	public Arena(World world, int x, int y, int z, int w, int h, int d,Material insidemat, Material...arena_mats) {
		this.world = world;
		this.box = new Box(x,y,z,w,h,d);
		for (Material mat : arena_mats) {
			this.wallmats.add(mat);
			this.floormats.add(mat);
			this.roofmats.add(mat);
		}
		this.insidemats.add(insidemat);
	}
	
	public Arena(World world, int x, int y, int z, int w, int h, int d,Material insidemat, Material wallmat, Material floormat, Material roofmat) {
		this.world = world;
		this.box = new Box(x,y,z,w,h,d);
		this.wallmats.add(wallmat);
		this.floormats.add(floormat);
		this.roofmats.add(roofmat);
		this.insidemats.add(insidemat);
	}
	
	public Arena(World world, int x, int y, int z, int w, int h, int d, Material[] insidemats, Material[] wallmats, Material[] floormats, Material[] roofmats) {
		this.world = world;
		this.box = new Box(x,y,z,w,h,d);
		for (Material mat : wallmats) {
			this.wallmats.add(mat);
		}
		for (Material mat : floormats) {
			this.floormats.add(mat);
		}
		for (Material mat : roofmats) {
			this.roofmats.add(mat);
		}
		for (Material mat : insidemats) {
			this.insidemats.add(mat);
		}
	}
	
	public void runTick() {
		for (Player p : arenaplayers) {
			if (p==null || !p.isValid()) {
				TwosideKeeper.ScheduleRemoval(arenaplayers,p);
			}
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			TwosideKeeper.log("Checking for "+p.getName(), 0);
			if (p.getGameMode()==GameMode.SURVIVAL) {
				if (arenaplayers.contains(p)) {
					if (!insideBoundaries(p)) {
						p.teleport(new Location(world,box.getX()+(box.getWidth()/2),box.getY()+(box.getHeight()/2),box.getZ()+(box.getDepth()/2)));
						p.sendMessage(ChatColor.GREEN+"You cannot leave this arena!");
					}
				} else {
					if (insideBoundaries(p)) {
						p.teleport(new Location(world,box.getX()+(box.getWidth()/2),box.getY()+(box.getHeight()*2),box.getZ()+(box.getDepth()/2)));
						p.sendMessage(ChatColor.GREEN+"You cannot enter this arena!");
					}
				}
			}
		}
	}
	
	private boolean insideBoundaries(Player p) {
		int x = p.getLocation().getBlockX();
		int y = p.getLocation().getBlockY();
		int z = p.getLocation().getBlockZ();
		return (x>=box.getX() && x<=box.getX()+box.getWidth() &&
				y>=box.getY() && y<=box.getY()+box.getHeight() &&
				z>=box.getZ() && z<=box.getZ()+box.getDepth());
	}

	public void AddPlayers(Player...players) {
		for (Player p : players) {
			arenaplayers.add(p);
		}
	}
	
	public void RemovePlayers(Player...players) {
		for (Player p : players) {
			arenaplayers.remove(p);
		}
	}
	
	public void AssembleArena() {
		final int x = box.getX();
		final int y = box.getY();
		final int z = box.getZ();
		AssembleArena(x,y,z,box.clone());
	}

	public void DisassembleArena() {
		DisassembleArena(-1);
	}
	
	public void DisassembleArena(int amt) {
		//amt is how many blocks to process in one tick.
		int processed=0;
		for (Block b : oldblocklist.keySet()) {
			Chunk c = b.getChunk();
			if (!TwosideKeeper.temporary_chunks.contains(c)) {
				TwosideKeeper.temporary_chunks.add(c);
			}
			c.load();
			if (!temporarychunks.contains(c)) {
				temporarychunks.add(c);
			}
			MaterialData dat = oldblocklist.get(b);
			if (b.getType()!=dat.getType() || b.getData()!=dat.getData()) {
				b.setType(dat.getType());
				b.setData(dat.getData());
			}
			if (amt!=-1) {TwosideKeeper.ScheduleRemoval(oldblocklist, b);}
			processed++;
			if (processed>amt && amt!=-1) {
				break;
			}
		}
		if (amt!=-1 && oldblocklist.size()>0) {
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{DisassembleArena(amt);}, 1);
		}
	}

	private void AssembleArena(int curx, int cury, int curz, Box box) {
		for (int i=0;i<8;i++) {
			Block b = world.getBlockAt(curx, cury, curz);
			Chunk c = b.getChunk();
			if (!TwosideKeeper.temporary_chunks.contains(c)) {
				TwosideKeeper.temporary_chunks.add(c);
			}
			c.load();
			if (!temporarychunks.contains(c)) {
				temporarychunks.add(c);
			}
			while (b.getState()!=null && b.getState() instanceof InventoryHolder) {
				if (curx>box.getX()+box.getWidth()) {
					curx=box.getX();
					cury++;
				}
				if (cury>box.getY()+box.getHeight()) {
					cury=box.getY();
					curz++;
				}
				if (curz<box.getZ()+box.getDepth()) {
					ConvertBlock(curx, cury, curz, box, b);
					curx++;
				}
			}
			if (curx>box.getX()+box.getWidth()) {
				curx=box.getX();
				cury++;
			}
			if (cury>box.getY()+box.getHeight()) {
				cury=box.getY();
				curz++;
			}
			if (curz<box.getZ()+box.getDepth()) {
				ConvertBlock(curx, cury, curz, box, b);
				curx++;
			}
		}
		if (curz<=box.getZ()+box.getDepth()) {
			final int x = curx;
			final int y = cury;
			final int z = curz;
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				AssembleArena(x,y,z,box);
			}, 1);
		} else {
			ClearAllTemporaryChunks();
		}
	}

	private void ResetBlock(int curx, int cury, int curz, Box box, Block b) {
		if (oldblocklist.containsKey(b)) {
			MaterialData dat = oldblocklist.get(b);
			if (b.getType()!=dat.getType() || b.getData()!=dat.getData()) {
				b.setType(dat.getType());
				b.setData(dat.getData());
			}
			oldblocklist.remove(b);
		}
	}

	public void ConvertBlock(int curx, int cury, int curz, Box box, Block b) {
		if (isWallBlock(curx,cury,curz,box)) {
			oldblocklist.put(b,new MaterialData(b.getType(),b.getData()));
			b.setType(wallmats.get((int)(Math.random()*wallmats.size())));
		} else
		if (isRoofBlock(curx,cury,curz,box)) {
			oldblocklist.put(b,new MaterialData(b.getType(),b.getData()));
			b.setType(roofmats.get((int)(Math.random()*roofmats.size())));
		} else
		if (isFloorBlock(curx,cury,curz,box)) {
			oldblocklist.put(b,new MaterialData(b.getType(),b.getData()));
			b.setType(floormats.get((int)(Math.random()*floormats.size())));
		} else
		{
			oldblocklist.put(b,new MaterialData(b.getType(),b.getData()));
			if (insidemats.size()>0) {
				if (b.getType()!=Material.AIR && !b.isLiquid()) {
					SoundUtils.playGlobalSound(b.getLocation(), Sound.BLOCK_METAL_BREAK, 0.3f, 0.3f);
				}
				b.setType(insidemats.get((int)(Math.random()*insidemats.size())));
			} else {
				b.setType(Material.AIR);
			}
		}
		/*final int x = curx++;
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			AssembleArena(x,cury,curz,box);
		}, 1);*/
	}
	
	private boolean isFloorBlock(int curx, int cury, int curz, Box box2) {
		return cury==box.getY();
	}

	private boolean isRoofBlock(int curx, int cury, int curz, Box box2) {
		return cury==box.getY()+box.getHeight();
	}

	private boolean isWallBlock(int curx, int cury, int curz, Box box) {
		return curx==box.getX() || curx==box.getWidth()+box.getX() ||
				curz==box.getZ() || curz==box.getZ()+box.getDepth();
	}
	
	public void Cleanup() {
		//ClearAllTemporaryChunks();
		DisassembleArena();
	}

	private void ClearAllTemporaryChunks() {
		TwosideKeeper.temporary_chunks.removeAll(temporarychunks);
		temporarychunks.clear();
	}
}
