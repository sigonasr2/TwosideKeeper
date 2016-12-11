package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;

import net.minecraft.server.v1_9_R1.EnumParticle;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.aPluginAPIWrapper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class LavaPlume {
	FallingBlock fb;
	int lavayreached;
	int delaytimer;
	Location lavaplumeloc;
	List<TemporaryLava> lavablocks;
	boolean negativevel=false;
	boolean state1=true,state2=true,state3=true,state4=true;
	/**
	 * 
	 * @param delay Delay in ticks.
	 */
	public LavaPlume(int delay, Location loc) {
		//this.fb=fb;
		this.lavablocks=new ArrayList<TemporaryLava>();
		//this.lavayreached=fb.getLocation().getBlockY();
		this.delaytimer=(int)((delay/20d)*4);
		this.lavaplumeloc = loc.clone().add(0,1,0);
	}
	/*
	 * Returns false if this block is invalid.
	 */
	public boolean runTick() {
		if (this.delaytimer>=0) {
			this.delaytimer--;
			if (this.delaytimer!=0) {
				aPluginAPIWrapper.sendParticle(this.lavaplumeloc.clone().add(0,Math.random()*5,0), EnumParticle.DRIP_LAVA, (float)Math.random(),(float)Math.random(),(float)Math.random(), (float)Math.random(), 10);
			} else {
				FallingBlock fallblock = this.lavaplumeloc.clone().getWorld().spawnFallingBlock(this.lavaplumeloc.clone().add(0,1,0), Material.REDSTONE_BLOCK, (byte)0);
				fallblock.setMetadata("DESTROY", new FixedMetadataValue(TwosideKeeper.plugin,true));
				fallblock.setVelocity(new Vector(0,(float)((Math.random()*2)+2),0));
				for (Player pl : Bukkit.getOnlinePlayers()) {
					GlowAPI.setGlowing(fallblock, GlowAPI.Color.YELLOW, pl);
				}
				this.fb = fallblock;
				this.lavayreached = this.lavaplumeloc.getBlockY();
				return RunLavaTick();
			}
			return true;
		}
		else {
			return RunLavaTick();
		}
	}
	private boolean RunLavaTick() {
		for (TemporaryLava tl : lavablocks) {
			if (!tl.runTick()) {
				TwosideKeeper.ScheduleRemoval(lavablocks, tl);
			}
		}
		if (fb==null || !fb.isValid()) {
			//Load up the chunk and see if we can remove it.
			if (this.lavablocks.size()>0) {
				return true;
			} else {
				//Cleared for official deleting.
				if (!fb.isValid()) {
					fb.remove();
				}
				return false;
			}
		} else {
			if (fb.getLocation().getY()>lavayreached) {
				for (int y=lavayreached;y<fb.getLocation().getY();y++) {
					//Set the 4 blocks around it to lava.
					List<Block> blocklist = new ArrayList<Block>();
					int rely = (int)(y-fb.getLocation().getY());
					if (state1 && !UpdateLavaBlock(fb.getLocation().add(1,rely,0).getBlock())) {state1=false;}
					if (state2 && !UpdateLavaBlock(fb.getLocation().add(-1,rely,0).getBlock())) {state2=false;}
					if (state3 && !UpdateLavaBlock(fb.getLocation().add(0,rely,1).getBlock())) {state3=false;}
					if (state4 && !UpdateLavaBlock(fb.getLocation().add(0,rely,-1).getBlock())) {state4=false;}
				}
				lavayreached=(int)fb.getLocation().getY();
			} else 
			if (fb.getVelocity().getY()<0) {
				fb.remove();
			}
			return true;
		}
	}
	
	private boolean UpdateLavaBlock(Block lavamod) {
		if (lavamod.getType()==Material.AIR || lavamod.getType()==Material.LAVA) {
			if (lavamod.getType()==Material.AIR) {
				lavamod.setType(Material.LAVA);
				lavamod.setData((byte)8);
			}
			this.lavablocks.add(new TemporaryLava(lavamod,(int)(3*fb.getVelocity().getY())+6));
			if (Math.random()<=0.1) {
				SoundUtils.playGlobalSound(lavamod.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.6f, 0.6f);
				SmallFireball sf = (SmallFireball)fb.getWorld().spawnEntity(lavamod.getLocation(), EntityType.SMALL_FIREBALL);
				sf.setDirection(new Vector(Math.random()-0.5,-Math.random()*0.5,Math.random()-0.5));
				sf.setVelocity(new Vector(Math.random()-0.5,-Math.random()*0.5,Math.random()-0.5));
			}
			SoundUtils.playGlobalSound(fb.getLocation(), Sound.BLOCK_LAVA_POP, 1.0f, 1.0f);
			return true;
		} else {
			//TwosideKeeper.log("Triggered for type "+lavamod.getType(), 0);
			return false;
		}
	}
	
	public void Cleanup() {
		//Delete the falling block associated with itself.
		if (fb!=null) {
			fb.remove();
		}
		//Delete all Temporary Lava associated with this lava plume.
		for (TemporaryLava tl : lavablocks) {
			tl.Cleanup();
		}
	}
}
