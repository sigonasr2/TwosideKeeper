package sig.plugin.TwosideKeeper.Monster;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.EffectPool;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.TemporaryBlock;

public class Bloodmite extends CustomMonster{
	
	long lastBloodPool=TwosideKeeper.getServerTickTime();
	SniperSkeleton main;

	public Bloodmite(LivingEntity m) {
		super(m);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		les.setCustomLivingEntityName(m, ChatColor.RED+"Bloodmite");
		m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.12f);
	}
	
	public void setMainEntity(SniperSkeleton ss) {
		main = ss;
	}
	
	public void runTick() {
		if (lastBloodPool+90<=TwosideKeeper.getServerTickTime()) {
			TemporaryBlock.createTemporaryBlockCircle(m.getLocation(), 1, Material.WOOL, (byte)14, 20*30, "BLOODPOOL");
			new EffectPool(m.getLocation(),1,20*30,Color.fromRGB(255, 0, 0));
			lastBloodPool=TwosideKeeper.getServerTickTime();
		}
	}
	
	public void cleanup() {
		if (main!=null) {
			main.bloodmites.remove(m);
		}
	}
}
