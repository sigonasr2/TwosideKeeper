package sig.plugin.TwosideKeeper;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import aPlugin.DiscordMessageSender;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.BankSession;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

final class runServerHeartbeat implements Runnable {
	/**
	 * 
	 */
	private final TwosideKeeper ServerHeartbeat;

	/**
	 * @param twosideKeeper
	 */
	runServerHeartbeat(TwosideKeeper twosideKeeper) {
		ServerHeartbeat = twosideKeeper;
	}

	@SuppressWarnings("deprecation")
	public void run(){
	  TwosideKeeper.log("Server time passed: "+(Bukkit.getWorld("world").getFullTime()-TwosideKeeper.STARTTIME)+". New Server Time: "+(Bukkit.getWorld("world").getFullTime()-TwosideKeeper.STARTTIME+TwosideKeeper.SERVERTICK),5);
		//Bukkit.getWorld("world").setFullTime(Bukkit.getWorld("world").getFullTime()-10); //LEGACY CODE.
	  	TwosideKeeper.adjustServerTime(10);
		//WORK IN PROGRESS: Lamp updating code TO GO HERE.
	  	
	  	sendAllLoggedMessagesToSpam();
		
		//SAVE SERVER SETTINGS.
		final long serverTickTime = TwosideKeeper.getServerTickTime();
		if (serverTickTime-TwosideKeeper.LASTSERVERCHECK>=TwosideKeeper.SERVERCHECKERTICKS) { //15 MINUTES (DEFAULT)
			ServerHeartbeat.saveOurData();
			
			//Advertisement messages could go here.
			//MOTD: "Thanks for playing on Sig's Minecraft!\n*bCheck out http://z-gamers.net/mc for update info!\n*aReport any bugs you find at http://zgamers.domain.com/mc/"
			ServerHeartbeat.getMOTD();
			ServerHeartbeat.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('*', TwosideKeeper.MOTD));
			TwosideKeeper.habitat_data.increaseHabitationLevels();
			TwosideKeeper.habitat_data.startinglocs.clear();
			for (int i=0;i<Bukkit.getOnlinePlayers().size();i++) {
				Player p = (Player)(Bukkit.getOnlinePlayers().toArray()[i]);
				PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
				pd.hitlist.clear();
			}
			/*
			getServer().broadcastMessage("Thanks for playing on Sig's Minecraft!");
			getServer().broadcastMessage(ChatColor.AQUA+"Check out http://z-gamers.net/mc for update info!");
			getServer().broadcastMessage(" ");
			*/
			//End Advertisements.
			TwosideKeeper.LASTSERVERCHECK=serverTickTime;
		}
		
		if (Bukkit.getWorld("world").getTime()>=12000) {
			Collection<? extends Player> players = ServerHeartbeat.getServer().getOnlinePlayers();
			//Count the number of players sleeping. Compare to "sleepingplayers" count.
			TwosideKeeper.log("[DEBUG] Time: "+Bukkit.getWorld("world").getTime()+" Full Time: "+Bukkit.getWorld("world").getFullTime() + " SERVERTICKTIME: "+serverTickTime,4);
			 //This functionality only makes sense when two or more players are on.
			int sleeping=0;
			for (Player p : players) {
				if (p.isSleeping()) {
					if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
						PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
						pd.slayermodehp=p.getMaxHealth();
					}
					p.setHealth(p.getMaxHealth());
					sleeping++;
				}
			}
			if (ServerHeartbeat.sleepingPlayers!=sleeping) {
				ServerHeartbeat.sleepingPlayers=sleeping;
				if (players.size()>1) {
					ServerHeartbeat.getServer().broadcastMessage(ChatColor.GOLD+""+ServerHeartbeat.sleepingPlayers+" Player"+(ServerHeartbeat.sleepingPlayers!=1?"s are":" is")+" in bed "+ChatColor.WHITE+"("+ServerHeartbeat.sleepingPlayers+"/"+(players.size()/2)+")");
				}
			}
			if (ServerHeartbeat.sleepingPlayers>=Math.max(players.size()/2,1)) {
				//Make it the next day.
				if (players.size()>1) {
					ServerHeartbeat.getServer().broadcastMessage(ChatColor.GOLD+"Enough Players sleeping! It's now morning!");
				}
				/*Bukkit.getWorld("world").setFullTime(Bukkit.getWorld("world").getFullTime()+10);
				
				SERVERTICK=getServerTickTime();*/
				long temptime = Bukkit.getWorld("world").getFullTime();
				Bukkit.getWorld("world").setTime(0);
				TwosideKeeper.time_passed+=temptime-Bukkit.getWorld("world").getFullTime();
				Bukkit.getWorld("world").setThundering(false);
				/*
				STARTTIME=Bukkit.getWorld("world").getFullTime();
				LASTSERVERCHECK=getServerTickTime();*/
				//Make sure we keep SERVERTICK in check.
				ServerHeartbeat.sleepingPlayers=0;
			}
		}
		
		//See if each player needs to regenerate their health.
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.isDead()) { 
				PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
				GenericFunctions.RemoveNewDebuffs(p);
				
				if (p.isSprinting() && pd.lastsprintcheck+(20*5)<serverTickTime) {
					pd.lastsprintcheck=serverTickTime;
					GenericFunctions.ApplySwiftAegis(p);
				}
				
				if (TwosideKeeper.banksessions.containsKey(p.getUniqueId())) {
					//See if it expired.
					BankSession bs = (BankSession)TwosideKeeper.banksessions.get(p.getUniqueId());
					if (bs.isSessionExpired()) {
						TwosideKeeper.banksessions.remove(p.getUniqueId());
					}
				}
				
				/*
				if (GenericFunctions.isRanger(p) &&
						GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.SNIPE) {
					p.removePotionEffect(PotionEffectType.SLOW);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20,5));
				}*/
				
				if (TwosideKeeper.TwosideShops.PlayerHasPurchases(p)) {
					TwosideKeeper.TwosideShops.PlayerSendPurchases(p);
				}
				
				if (TwosideKeeper.TwosideShops.IsPlayerUsingTerminal(p) &&
						(TwosideKeeper.TwosideShops.GetSession(p).GetSign().getBlock()==null || TwosideKeeper.TwosideShops.GetSession(p).IsTimeExpired())) {
					p.sendMessage(ChatColor.RED+"Ran out of time! "+ChatColor.WHITE+"Shop session closed.");
					TwosideKeeper.TwosideShops.RemoveSession(p);
				}

				pd.highwinder=ArtifactAbility.containsEnchantment(ArtifactAbility.HIGHWINDER, p.getEquipment().getItemInMainHand());
				if (pd.highwinder) {
					pd.highwinderdmg=GenericFunctions.getAbilityValue(ArtifactAbility.HIGHWINDER, p.getEquipment().getItemInMainHand());
				}
				if (93.182445*pd.velocity>4.317) {
					pd.velocity/=2;
				} else {
					pd.velocity=0;
				}
				if (pd.highwinder && pd.target!=null && !pd.target.isDead()) {
					GenericFunctions.sendActionBarMessage(p, TwosideKeeper.drawVelocityBar(pd.velocity,pd.highwinderdmg));
				}
				if (pd.target!=null && !pd.target.isDead() && pd.target.getLocation().getWorld().equals(p.getWorld()) && pd.target.getLocation().distanceSquared(p.getLocation())>256) {
					pd.target=null;
				}
				
				if (pd.lasthittarget+20*15<=serverTickTime && pd.storedbowxp>0 && GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
						p.getEquipment().getItemInMainHand().getType()==Material.BOW) {
					AwakenedArtifact.addPotentialEXP(p.getEquipment().getItemInMainHand(), pd.storedbowxp, p);
					TwosideKeeper.log("Added "+pd.storedbowxp+" Artifact XP", 2);
					pd.storedbowxp=0;
				}
				
				if (p.getFireTicks()>0 && p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
					int duration = GenericFunctions.getPotionEffectDuration(PotionEffectType.FIRE_RESISTANCE, p);
					int lv = GenericFunctions.getPotionEffectLevel(PotionEffectType.FIRE_RESISTANCE, p);
					if (lv>10) {lv=10;}
					GenericFunctions.logAndApplyPotionEffectToPlayer(PotionEffectType.FIRE_RESISTANCE, duration-(20*(10-lv)), lv, p, true);
				}
				
				if (GenericFunctions.hasStealth(p)) {GenericFunctions.DamageRandomTool(p);}

				p.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(20*(1.0d-CustomDamage.CalculateDamageReduction(1,p,null))+subtractVanillaArmorBar(p.getEquipment().getArmorContents()));

				ItemStack[] equips = p.getEquipment().getArmorContents();
				
				if (pd.last_regen_time+TwosideKeeper.HEALTH_REGENERATION_RATE<=serverTickTime) {
					pd.last_regen_time=serverTickTime;
					//See if this player needs to be healed.
					if (p!=null &&
							!p.isDead() && //Um, don't heal them if they're dead...That's just weird.
							p.getHealth()<p.getMaxHealth() &&
							p.getFoodLevel()>=16) {
						
						if (PlayerMode.getPlayerMode(p)!=PlayerMode.SLAYER || pd.lastcombat+(20*60)<serverTickTime) {
							double totalregen = 1+(p.getMaxHealth()*0.05);
							double bonusregen = 0.0;
							bonusregen += ItemSet.TotalBaseAmountBasedOnSetBonusCount(GenericFunctions.getEquipment(p), p, ItemSet.ALIKAHN, 4, 4);
							totalregen += bonusregen;
							for (ItemStack equip : equips) {
								if (GenericFunctions.isArtifactEquip(equip)) {
									double regenamt = GenericFunctions.getAbilityValue(ArtifactAbility.HEALTH_REGEN, equip);
									 bonusregen += regenamt;
									 TwosideKeeper.log("Bonus regen increased by "+regenamt,5);
										if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equip)) {
											totalregen /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, equip)?2:1;
										}
								}
							}
							if (ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())) {
								totalregen /= ArtifactAbility.containsEnchantment(ArtifactAbility.GREED, p.getEquipment().getItemInMainHand())?2:1;
							}
							
							p.setHealth((p.getHealth()+totalregen>p.getMaxHealth())?p.getMaxHealth():p.getHealth()+totalregen);
							
							if (PlayerMode.getPlayerMode(p)==PlayerMode.SLAYER) {
								pd.slayermodehp=p.getHealth();
							}		
						}
					}
				}
				
				if (p.getWorld().getName().equalsIgnoreCase("world_the_end")) {
					if (pd.endnotification+72000<serverTickTime) {
						pd.endnotification=serverTickTime;
						playEndWarningNotification(p);
					}
					randomlyAggroNearbyEndermen(p);
				}
				
				//See if this player is sleeping.
				if (p.isSleeping()) {
					p.setHealth(Bukkit.getPlayer(pd.name).getMaxHealth()); //Heals the player fully when sleeping.
				}
				//We need to see if this player's damage reduction has changed recently. If so, notify them.
				//Check damage reduction by sending an artifical "1" damage to the player.
				if (!p.isDead()) {TwosideKeeper.log("Player is not dead.",5); TwosideKeeper.setPlayerMaxHealth(p);}
				p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(TwosideKeeper.createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
				p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));

				for (ItemStack equip : equips) {
					if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, equip) &&
							p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
						GenericFunctions.logAndApplyPotionEffectToPlayer(PotionEffectType.SPEED,20,1,p);
					}
				}
				if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getItemInMainHand()) &&
						p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
					GenericFunctions.logAndApplyPotionEffectToPlayer(PotionEffectType.SPEED,20,1,p);
					//log("Apply speed. The light level here is "+p.getLocation().add(0,-1,0).getBlock().getLightLevel(),2);
				}
				
				if (ArtifactAbility.containsEnchantment(ArtifactAbility.COMBO, p.getEquipment().getItemInMainHand()) &&
						pd.last_swordhit+40<serverTickTime) {
					pd.swordcombo=0; //Reset the sword combo meter since the time limit expired.
				} 
				
				if (PlayerMode.isSlayer(p)) {
					if (pd.lastsneak+50<=serverTickTime &&
							p.isSneaking() &&
							ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getHotbarItems(p), p, ItemSet.MOONSHADOW, 7)) {
						GenericFunctions.deAggroNearbyTargets(p);
						GenericFunctions.applyStealth(p, true);
					}
					/*List<Monster> mobs = GenericFunctions.getNearbyMobs(p.getLocation(), 16);
					for (Monster m : mobs) {
						if (!GenericFunctions.isIsolatedTarget(m,p) &&
								!GenericFunctions.isSpecialGlowMonster(m) &&
								m.getLocation().distanceSquared(p.getLocation())<196) {GlowAPI.setGlowing(m, GlowAPI.Color.WHITE, p);}
						if (GenericFunctions.isIsolatedTarget(m,p) &&
								!GenericFunctions.isSpecialGlowMonster(m) &&
								GenericFunctions.GetNearbyMonsterCount(m, 8)>0) {
							GlowAPI.setGlowing(m, false, p);
						}
					}*/
				} else {
					/*List<Monster> mobs = GenericFunctions.getNearbyMobs(p.getLocation(), 16);
					for (Monster m : mobs) {
						if (GenericFunctions.isIsolatedTarget(m,p) &&
								!GenericFunctions.isSpecialGlowMonster(m)) {GlowAPI.setGlowing(m, false, p);}
					}*/
				}
				
				GenericFunctions.sendActionBarMessage(p, "");
				GenericFunctions.AutoRepairItems(p);
			}
		}
		
		MaintainMonsterData();
		
		PartyManager.SetupParties();
		
		TwosideKeeper.TwosideSpleefGames.TickEvent();
	}

	private void MaintainMonsterData() {
		Set<UUID> data= TwosideKeeper.monsterdata.keySet();
		TwosideKeeper.log("Size: "+TwosideKeeper.monsterdata.size(), 2);
		for (UUID id : data) {
			MonsterStructure ms = TwosideKeeper.monsterdata.get(id);
			if (!ms.m.isValid()) {
				//TwosideKeeper.monsterdata.remove(data);
				TwosideKeeper.ScheduleRemoval(TwosideKeeper.monsterdata, ms);
				TwosideKeeper.ScheduleRemoval(data, id);
				TwosideKeeper.log("Removed Monster Structure for "+id+".", 2);
			} else {
				AddEliteStructureIfOneDoesNotExist(ms);
				ms.UpdateGlow();
			}
		}
	}

	public void AddEliteStructureIfOneDoesNotExist(MonsterStructure ms) {
		if (ms.isElite || MonsterController.getMonsterDifficulty(ms.m)==MonsterDifficulty.ELITE) {
			//Make it glow dark purple.
			//GenericFunctions.setGlowing(m, GlowAPI.Color.DARK_PURPLE);
			boolean hasstruct = false;
			for (EliteMonster em : TwosideKeeper.elitemonsters) {
				if (em.m.equals(ms.m)) {
					hasstruct=true;
				}
			}
			if (!hasstruct) {
				TwosideKeeper.elitemonsters.add(new EliteMonster(ms.m));
			}
		}
	}

	private void randomlyAggroNearbyEndermen(Player p) {
		List<Monster> ents = GenericFunctions.getNearbyMobs(p.getLocation(), 16);
		for (Monster m : ents) {
			if (Math.random()<=0.05 && !m.hasPotionEffect(PotionEffectType.GLOWING)) {
				m.setTarget(p);
			}
		}
	}

	private void playEndWarningNotification(Player p) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.GOLD+"A Mysterious Entity glares at you...");
			}
		},1);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.BLUE+" \"You DO NOT BELONG HERE.\"");
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0f, 1.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
			}
		},20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				p.playSound(p.getLocation().add(0,20,0), Sound.ENTITY_GHAST_WARN, 1.0f, 1.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0f, 1.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
			}
		},23);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				p.playSound(p.getLocation().add(-10,0,-5), Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0f, 1.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
			}
		},27);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				p.playSound(p.getLocation().add(-10,0,-5), Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_AMBIENT, 1.0f, 1.0f);
				p.playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
			}
		},30);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.RED+"You cannot identify The End properly!");
			}
		},90);
	}

	private void sendAllLoggedMessagesToSpam() {
		StringBuilder finalstring = new StringBuilder();
		for (String msg : TwosideKeeper.log_messages) {
			finalstring.append(msg+"\n");
		}
		TwosideKeeper.log_messages.clear();
		if (finalstring.length()>0) {
			DiscordMessageSender.sendToSpam(finalstring.toString());
		}
	}

	private double subtractVanillaArmorBar(ItemStack[] armorContents) {
		double lostamt = 0.0d;
		for (ItemStack equip : armorContents) {
			if (equip!=null &&
					equip.getType()!=Material.AIR) {
				switch (equip.getType()) {
					case LEATHER_HELMET:{
						lostamt-=1;
					}break;
					case LEATHER_CHESTPLATE:{
						lostamt-=3;
					}break;
					case LEATHER_LEGGINGS:{
						lostamt-=2;
					}break;
					case LEATHER_BOOTS:{
						lostamt-=1;
					}break;
					case GOLD_HELMET:{
						lostamt-=2;
					}break;
					case GOLD_CHESTPLATE:{
						lostamt-=5;
					}break;
					case GOLD_LEGGINGS:{
						lostamt-=3;
					}break;
					case GOLD_BOOTS:{
						lostamt-=1;
					}break;
					case CHAINMAIL_HELMET:{
						lostamt-=2;
					}break;
					case CHAINMAIL_CHESTPLATE:{
						lostamt-=5;
					}break;
					case CHAINMAIL_LEGGINGS:{
						lostamt-=4;
					}break;
					case CHAINMAIL_BOOTS:{
						lostamt-=1;
					}break;
					case IRON_HELMET:{
						lostamt-=2;
					}break;
					case IRON_CHESTPLATE:{
						lostamt-=6;
					}break;
					case IRON_LEGGINGS:{
						lostamt-=5;
					}break;
					case IRON_BOOTS:{
						lostamt-=2;
					}break;
					case DIAMOND_HELMET:{
						lostamt-=3;
					}break;
					case DIAMOND_CHESTPLATE:{
						lostamt-=8;
					}break;
					case DIAMOND_LEGGINGS:{
						lostamt-=6;
					}break;
					case DIAMOND_BOOTS:{
						lostamt-=3;
					}break;
					default:{
						
					}
				}
			}
		}
		return lostamt;
	}
}