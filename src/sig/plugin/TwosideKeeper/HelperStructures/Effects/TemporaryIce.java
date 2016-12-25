package sig.plugin.TwosideKeeper.HelperStructures.Effects;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_9_R1.EnumParticle;
import sig.plugin.TwosideKeeper.aPluginAPIWrapper;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

public class TemporaryIce {
	int lifetime=0;
	Entity trappedEntity = null;
	Block b;
	
	public TemporaryIce(int life, Block b, Entity trappedent) {
		this.lifetime=life;
		this.trappedEntity = trappedent;
		this.b=b;
		if (this.trappedEntity instanceof LivingEntity) {
			LivingEntity le = (LivingEntity)this.trappedEntity;
			le.setVelocity(new Vector(0,0,0));
		}
		if (b.getType()==Material.AIR) {
			b.setType(Material.PACKED_ICE);
		}
	}
	
	public TemporaryIce(int life, Block b) {
		this.lifetime=life;
		this.trappedEntity = null;
		this.b=b;
		if (b.getType()==Material.AIR) {
			b.setType(Material.PACKED_ICE);
		}
	}
	
	public int getLifetime() {
		return lifetime;
	}

	public Entity getTrappedEntity() {
		return trappedEntity;
	}

	public Block getBlock() {
		return b;
	}

	public boolean run() {
		lifetime--;
		if (b.getType()==Material.PACKED_ICE) {
			aPluginAPIWrapper.sendParticle(b.getLocation(), EnumParticle.FIREWORKS_SPARK, 0, 1, 0, 1.2f, 3);
			if (lifetime>10) {
				aPlugin.API.sendBlockBreakPacket(b, 2);
			} else
			if (lifetime>5) {
				aPlugin.API.sendBlockBreakPacket(b, 4);
			} else
			if (lifetime>0) {
				aPlugin.API.sendBlockBreakPacket(b, 6);
			}
		}
		if (lifetime<=0) {
			Cleanup();
			return false;
		}
		return true;
	}

	public void Cleanup() {
		if (b.getType()==Material.PACKED_ICE) {
			b.setType(Material.AIR);
			SoundUtils.playGlobalSound(b.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 0.8f);
		}
		if (trappedEntity!=null && trappedEntity.isValid()) {
			if (trappedEntity instanceof LivingEntity) {
				LivingEntity le = (LivingEntity)trappedEntity;
				le.setAI(true);
			}
		}
	}
}
