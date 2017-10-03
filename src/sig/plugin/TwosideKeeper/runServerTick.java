package sig.plugin.TwosideKeeper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;

import sig.plugin.TwosideKeeper.HelperStructures.CustomModel;
import sig.plugin.TwosideKeeper.HelperStructures.DamageLabel;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.BlockModifyQueue;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.ColoredParticle;

public class runServerTick implements Runnable{
	final int queuespd = 8;

	@Override
	public void run() {
		for (int i=queuespd;i>0;i--) {
			if (TwosideKeeper.blockqueue.size()>0) {
				BlockModifyQueue bmq = TwosideKeeper.blockqueue.remove(0);
				bmq.run();
			}
		}
		for (int i=0;i<TwosideKeeper.labelqueue.size();i++) {
			if (!TwosideKeeper.labelqueue.get(i).run()) {
				TwosideKeeper.labelqueue.remove(i--);
			}
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			if (!aPluginAPIWrapper.isAFK(p)) {
				//pd.myModel.displayModel(p.getLocation());
				if (pd.myPet!=null) {
					pd.myPet.run();
				}
				if (PlayerMode.isSummoner(p)) {
					//long timer = System.nanoTime();
					LivingEntity targetent = aPlugin.API.rayTraceTargetEntity(p, 16);
					if (targetent!=null) {
						LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(targetent);
						if (LivingEntityStructure.isFriendly(p,targetent)) {
							les.setGlow(p, Color.DARK_AQUA);
						} else {
							les.setGlow(p, Color.DARK_GRAY);
						}
						if (pd.lastTarget!=null && pd.lastTarget!=targetent) {
							LivingEntityStructure les2 = LivingEntityStructure.GetLivingEntityStructure(pd.lastTarget);
							les2.setGlow(p, null);
							pd.lastTarget.setGlowing(false);
							GlowAPI.setGlowing(pd.lastTarget, null, p);
							pd.lastTarget=null;
						}
						pd.lastTarget=targetent;
					}
					//TwosideKeeper.log("Time Execution took: "+((System.nanoTime()-timer)/1000000)+"ms", 1);
				}
				if (pd.mouseoverhealthbar && pd.lastGrabbedTarget+10<=TwosideKeeper.getServerTickTime()) {
					LivingEntity targetent = aPlugin.API.rayTraceTargetEntity(p, 16);
					if (targetent!=null && (!(targetent instanceof ArmorStand) || (targetent instanceof ArmorStand && ((ArmorStand)targetent).isVisible())) &&
							!targetent.hasPotionEffect(PotionEffectType.INVISIBILITY) && (pd.lastViewedTarget==null || !pd.lastViewedTarget.equals(targetent.getUniqueId()))
							&& targetent.hasLineOfSight(p)) {
						pd.customtitle.updateCombatBar(p, targetent);
						pd.lastGrabbedTarget=TwosideKeeper.getServerTickTime();
						pd.lastViewedTarget = targetent.getUniqueId();
					}
				}
			}
		}
		runServerHeartbeat.resetDamageQueue();
		/*if (Bukkit.getPlayer("sigonasr2")!=null) {
			Player p = Bukkit.getPlayer("sigonasr2");

			if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()!=Material.AIR &&
					!p.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid()) {
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				pd.customtitle.modifyLargeCenterTitle(ChatColor.RED+"A", 5);
			}
		}*/
		/*
		/*if (Bukkit.getPlayer("sigonasr2")!=null) {
		 for (int i=0;i<200;i++) {
			ColoredParticle.RED_DUST.send(p.getEyeLocation().add(
				p.getLocation().getDirection()).add(0,-0.05*i,0)
				, 20, 0, 0, 0);
				}
				
		}*/
	}
	
}
