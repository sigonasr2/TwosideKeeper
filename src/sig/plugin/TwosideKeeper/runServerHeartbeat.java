package sig.plugin.TwosideKeeper;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.glow.GlowAPI.Color;

import aPlugin.DiscordMessageSender;
import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.MinecraftServer;
import sig.plugin.TwosideKeeper.Events.InventoryUpdateEvent;
import sig.plugin.TwosideKeeper.Events.InventoryUpdateEvent.UpdateReason;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.BankSession;
import sig.plugin.TwosideKeeper.HelperStructures.DamageStructure;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.LavaPlume;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.InventoryUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.ItemCubeUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MessageUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.PlayerUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HolidayEvents.Christmas;

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
		long time = System.nanoTime();
		if (serverTickTime-TwosideKeeper.LASTSERVERCHECK>=TwosideKeeper.SERVERCHECKERTICKS) { //15 MINUTES (DEFAULT)
			if (TwosideKeeper.LAST_DEAL!=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
				//This means the deal of the day has to be updated!
				TwosideKeeper.LAST_DEAL = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				TwosideKeeper.DEAL_OF_THE_DAY_ITEM = WorldShop.generateItemDealOftheDay(1);
				TwosideKeeper.DEAL_OF_THE_DAY_PCT = WorldShop.generatePercentOffForDealOftheDay();
				if (TwosideKeeper.SERVER_TYPE!=ServerType.QUIET) {
					DecimalFormat df = new DecimalFormat("0.00");
					DecimalFormat df2 = new DecimalFormat("0");
					aPlugin.API.discordSendRaw("*The Deal of the Day has been updated!*\n **"+GenericFunctions.UserFriendlyMaterialName(TwosideKeeper.DEAL_OF_THE_DAY_ITEM)+"**  ~~$"+df.format(WorldShop.getBaseWorldShopPrice(TwosideKeeper.DEAL_OF_THE_DAY_ITEM))+"~~  $"+df.format(WorldShop.getBaseWorldShopPrice(TwosideKeeper.DEAL_OF_THE_DAY_ITEM)*(1-TwosideKeeper.DEAL_OF_THE_DAY_PCT))+"  **"+df2.format(TwosideKeeper.DEAL_OF_THE_DAY_PCT*100)+"% Off!**");
					//MessageUtils.announceMessage("The Deal of the Day has been updated!");
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(ChatColor.AQUA+""+ChatColor.ITALIC+"The Deal of the Day has been updated!");
					TwosideKeeper.AnnounceDealOfTheDay(p);
				}
			}
			
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
		TwosideKeeper.HeartbeatLogger.AddEntry("MOTD", (int)(System.nanoTime()-time));time=System.nanoTime();
		
		if (Bukkit.getWorld("world").getTime()>=12000 || Bukkit.getWorld("world").isThundering()) {
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
		TwosideKeeper.HeartbeatLogger.AddEntry("Sleep Check", (int)(System.nanoTime()-time));time=System.nanoTime();
		//See if each player needs to regenerate their health.
		long playerchecktime = System.nanoTime();
		for (Player p : Bukkit.getOnlinePlayers()) {
	    	//TwosideKeeper.outputArmorDurability(p);
			if (!p.isDead()) { 
				PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
				
				if (p.getName().equalsIgnoreCase("Orni")) {
					CustomDamage.ApplyDamage(10, null, p, null, "Orni",CustomDamage.TRUEDMG|CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.IGNOREDODGE);
				}

				TwosideKeeper.HeartbeatLogger.AddEntry("Orni", (int)(System.nanoTime()-time));time=System.nanoTime();
				if (p.isSprinting() && pd.lastsprintcheck+(20*5)<serverTickTime) {
					pd.lastsprintcheck=serverTickTime;
					GenericFunctions.ApplySwiftAegis(p);
				}
				TwosideKeeper.HeartbeatLogger.AddEntry("Swift Aegis Application", (int)(System.nanoTime()-time));time=System.nanoTime();
				if (TwosideKeeper.banksessions.containsKey(p.getUniqueId())) {
					//See if it expired.
					BankSession bs = (BankSession)TwosideKeeper.banksessions.get(p.getUniqueId());
					if (bs.isSessionExpired()) {
						TwosideKeeper.banksessions.remove(p.getUniqueId());
					}
				}
				TwosideKeeper.HeartbeatLogger.AddEntry("Bank Session Removal", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				/*
				if (GenericFunctions.isRanger(p) &&
						GenericFunctions.getBowMode(p.getEquipment().getItemInMainHand())==BowMode.SNIPE) {
					p.removePotionEffect(PotionEffectType.SLOW);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20,5));
				}*/
				
				if (TwosideKeeper.TwosideShops.PlayerHasPurchases(p)) {
					TwosideKeeper.TwosideShops.PlayerSendPurchases(p);
				}
				TwosideKeeper.HeartbeatLogger.AddEntry("Purchase Notification Sending", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				long notafktime = System.nanoTime();
				if (!aPlugin.API.isAFK(p)) {
					EndShopSession(p);
					TwosideKeeper.HeartbeatLogger.AddEntry("End Shop Session", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					GenericFunctions.RemoveNewDebuffs(p);
					TwosideKeeper.HeartbeatLogger.AddEntry("Remove New Debuffs", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					ModifyDasherSetSpeedMultiplier(p);
					TwosideKeeper.HeartbeatLogger.AddEntry("Dasher Speed Multiplier", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					ManageHighwinder(p, pd);
					TwosideKeeper.HeartbeatLogger.AddEntry("Highwinder", (int)(System.nanoTime()-time));time=System.nanoTime();
					RemoveInvalidTarget(p, pd);
					TwosideKeeper.HeartbeatLogger.AddEntry("Remove Bad Target", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					GiveArtifactBowXP(serverTickTime, p, pd);
					TwosideKeeper.HeartbeatLogger.AddEntry("Artifact Bow XP", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					ReduceFireResistanceDuration(p);
					TwosideKeeper.HeartbeatLogger.AddEntry("Reduce Fire Resist", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					ControlTheEnd(p, pd);
					TwosideKeeper.HeartbeatLogger.AddEntry("The End", (int)(System.nanoTime()-time));time=System.nanoTime();

					ItemStack[] equips = p.getEquipment().getArmorContents();

					ShadowWalkerApplication(p, equips);
					TwosideKeeper.HeartbeatLogger.AddEntry("Shadow Walker", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					
					//PopulatePlayerBlockList(p,15,15,2,5,false);
					PopRandomLavaBlock(p);
					TwosideKeeper.HeartbeatLogger.AddEntry("Pop Lava", (int)(System.nanoTime()-time));time=System.nanoTime();
					GenericFunctions.sendActionBarMessage(p, "");
					GenericFunctions.AutoRepairItems(p);
					TwosideKeeper.HeartbeatLogger.AddEntry("Auto Repair", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					if (GenericFunctions.hasStealth(p)) {GenericFunctions.DamageRandomTool(p);
					TwosideKeeper.HeartbeatLogger.AddEntry("Damage Random Tool", (int)(System.nanoTime()-time));time=System.nanoTime();}
					
					//See if this player is sleeping.
					HealForSleeping(p, pd);
					TwosideKeeper.HeartbeatLogger.AddEntry("Heal for Sleeping", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					//We need to see if this player's damage reduction has changed recently. If so, notify them.
					//Check damage reduction by sending an artifical "1" damage to the player.
					ManagePlayerScoreboardAndHealth(p);
					TwosideKeeper.HeartbeatLogger.AddEntry("Scoreboard/Health Management", (int)(System.nanoTime()-time));time=System.nanoTime();
					
					if (PlayerMode.isBarbarian(p)) {
						AutoConsumeFoods(p);
						TwosideKeeper.HeartbeatLogger.AddEntry("Auto Consume Food", (int)(System.nanoTime()-time));time=System.nanoTime();
					}
				}
				TwosideKeeper.HeartbeatLogger.AddEntry(ChatColor.BOLD+"->Not AFK Functions"+ChatColor.RESET, (int)(System.nanoTime()-notafktime));

				ModifyArmorBar(p);
				TwosideKeeper.HeartbeatLogger.AddEntry("Modify Armor Bar", (int)(System.nanoTime()-time));time=System.nanoTime();

				ItemStack[] equips = p.getEquipment().getArmorContents();
				
				ResetVendetta(serverTickTime, pd);
				TwosideKeeper.HeartbeatLogger.AddEntry("Vendetta Reset", (int)(System.nanoTime()-time));time=System.nanoTime();
				ResetLifestealStacks(serverTickTime, pd);	
				TwosideKeeper.HeartbeatLogger.AddEntry("Lifesteal Reset", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				ManagePlayerLink(p, pd);
				TwosideKeeper.HeartbeatLogger.AddEntry("Player Link", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				DepleteDamagePool(serverTickTime, p, pd);
				TwosideKeeper.HeartbeatLogger.AddEntry("Damage Pool", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				AdventurerModeSetExhaustion(p);
				TwosideKeeper.HeartbeatLogger.AddEntry("Adventurer Mode Exhaustion", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				CalculateHealthRegeneration(serverTickTime, p, pd, equips);
				
				ResetSwordCombo(serverTickTime, p, pd); 
				TwosideKeeper.HeartbeatLogger.AddEntry("Reset Sword Combo", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				ResetSlayerAggro(serverTickTime, p, pd);
				TwosideKeeper.HeartbeatLogger.AddEntry("Reset Slayer Aggro", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				ApplyCometRegenBonus(p);
				TwosideKeeper.HeartbeatLogger.AddEntry("Comet Regen Application", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				DasherFoodRegenPerk(p);
				TwosideKeeper.HeartbeatLogger.AddEntry("Dasher Food Regen", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				GivePartyNightVision(p);
				TwosideKeeper.HeartbeatLogger.AddEntry("Party Night Vision", (int)(System.nanoTime()-time));time=System.nanoTime();
				
				adjustMiningFatigue(p);
				TwosideKeeper.HeartbeatLogger.AddEntry("Adjust Mining Fatigue", (int)(System.nanoTime()-time));time=System.nanoTime();
			}
	    	//TwosideKeeper.outputArmorDurability(p,">");
		}
		TwosideKeeper.HeartbeatLogger.AddEntry(ChatColor.BOLD+"->Full Player Checks"+ChatColor.RESET, (int)(System.nanoTime()-playerchecktime));
		
		ManageSnowmanHunt();
		TwosideKeeper.HeartbeatLogger.AddEntry("Snowman Hunt", (int)(System.nanoTime()-time));time=System.nanoTime();
		
		CheckAndAnnounceWeather();
		TwosideKeeper.HeartbeatLogger.AddEntry("Check/Announce Weather", (int)(System.nanoTime()-time));time=System.nanoTime();
		
		Christmas.ChristmasHeartbeat();
		TwosideKeeper.HeartbeatLogger.AddEntry("Christmas Heartbeat", (int)(System.nanoTime()-time));time=System.nanoTime();
		
		MaintainMonsterData();
		TwosideKeeper.HeartbeatLogger.AddEntry("Monster Management", (int)(System.nanoTime()-time));time=System.nanoTime();
		
		PartyManager.SetupParties();
		TwosideKeeper.HeartbeatLogger.AddEntry("Setup Parties", (int)(System.nanoTime()-time));time=System.nanoTime();
		
		TwosideKeeper.TwosideSpleefGames.TickEvent();
		TwosideKeeper.HeartbeatLogger.AddEntry("Spleef Tick", (int)(System.nanoTime()-time));time=System.nanoTime();
		
		performTimingsReport();
		TwosideKeeper.HeartbeatLogger.AddEntry("Server Lag Activation", (int)(System.nanoTime()-time));time=System.nanoTime();
		
		resetPigmanAggro();
		TwosideKeeper.HeartbeatLogger.AddEntry("Reset Pigman Aggro", (int)(System.nanoTime()-time));time=System.nanoTime();
	}

	public static void resetDamageQueue() {
		for (int i=0;i<8;i++) {
			if (TwosideKeeper.damagequeuelist.size()>0) {
				TwosideKeeper.damagequeuelist.remove(0).run();
			} else {
				break;
			}
		}
		TwosideKeeper.damagequeue=TwosideKeeper.damagequeuelist.size();
	}

	private void adjustMiningFatigue(Player p) {
		if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING) && ((GenericFunctions.getPotionEffectLevel(PotionEffectType.SLOW_DIGGING, p)==2))) {
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SLOW_DIGGING, 6000, 20, p, true);
		}
	}

	private void resetPigmanAggro() {
		if (TwosideKeeper.lastPigmanAggroTime+20<TwosideKeeper.getServerTickTime()) {
			TwosideKeeper.pigmanAggroCount=0;
		}
	}

	private void ManagePlayerScoreboardAndHealth(Player p) {
		long time=System.nanoTime();
		if (!p.isDead()) {TwosideKeeper.log("Player is not dead.",5); TwosideKeeper.setPlayerMaxHealth(p);}
		TwosideKeeper.HeartbeatLogger.AddEntry("==Scoreboard/Health Management - Set Player Max Health", (int)(System.nanoTime()-time));time=System.nanoTime();
		if (p.getScoreboard().getTeam(p.getName().toLowerCase())==null) {
			p.getScoreboard().registerNewTeam(p.getName().toLowerCase()).addPlayer(p);
			TwosideKeeper.HeartbeatLogger.AddEntry("==Scoreboard/Health Management - Register New Team", (int)(System.nanoTime()-time));time=System.nanoTime();
		}
		p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(TwosideKeeper.createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
		TwosideKeeper.HeartbeatLogger.AddEntry("==Scoreboard/Health Management - Set Suffix", (int)(System.nanoTime()-time));time=System.nanoTime();
		p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
		TwosideKeeper.HeartbeatLogger.AddEntry("==Scoreboard/Health Management - Set Prefix", (int)(System.nanoTime()-time));time=System.nanoTime();
	}

	private void HealForSleeping(Player p, PlayerStructure pd) {
		if (p.isSleeping()) {
			p.setHealth(Bukkit.getPlayer(pd.name).getMaxHealth()); //Heals the player fully when sleeping.
		}
	}

	private void ShadowWalkerApplication(Player p, ItemStack[] equips) {
		for (ItemStack equip : equips) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, equip) &&
					p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=7) {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED,20,1,p);
			}
		}
		if (p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=7) {
			if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getItemInMainHand())) {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED,20,1,p);
			}
			//log("Apply speed. The light level here is "+p.getLocation().add(0,-1,0).getBlock().getLightLevel(),2);
		}
	}

	private void ControlTheEnd(Player p, PlayerStructure pd) {
		if (p.getWorld().getName().equalsIgnoreCase("world_the_end")) {
			if (!pd.endnotification) {
				pd.endnotification=true;
				playEndWarningNotification(p);
			}
			randomlyAggroNearbyEndermen(p);
		} else {
			if (pd.endnotification) {
				pd.endnotification=false;
			}
		}
	}

	private void ReduceFireResistanceDuration(Player p) {
		if (p.getFireTicks()>0 && p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
			int duration = GenericFunctions.getPotionEffectDuration(PotionEffectType.FIRE_RESISTANCE, p);
			int lv = GenericFunctions.getPotionEffectLevel(PotionEffectType.FIRE_RESISTANCE, p);
			if (lv>10) {lv=10;}
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.FIRE_RESISTANCE, duration-(20*(10-lv)), lv, p, true);
		}
	}

	private void GiveArtifactBowXP(final long serverTickTime, Player p, PlayerStructure pd) {
		if (pd.lasthittarget+20*15<=serverTickTime && pd.storedbowxp>0 && GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
				p.getEquipment().getItemInMainHand().getType()==Material.BOW) {
			AwakenedArtifact.addPotentialEXP(p.getEquipment().getItemInMainHand(), pd.storedbowxp, p);
			TwosideKeeper.log("Added "+pd.storedbowxp+" Artifact XP", 4);
			pd.storedbowxp=0;
		}
	}

	private void RemoveInvalidTarget(Player p, PlayerStructure pd) {
		if (pd.target!=null && !pd.target.isDead() && pd.target.getLocation().getWorld().equals(p.getWorld()) && pd.target.getLocation().distanceSquared(p.getLocation())>256) {
			pd.target=null;
		}
	}

	private void ManageHighwinder(Player p, PlayerStructure pd) {
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
			GenericFunctions.sendActionBarMessage(p, TwosideKeeper.drawVelocityBar(pd.velocity,pd.highwinderdmg),true);
		}
	}

	private void ModifyDasherSetSpeedMultiplier(Player p) {
		if (ItemSet.GetTotalBaseAmount(GenericFunctions.getEquipment(p), p, ItemSet.DASHER)>0) {
			double spdmult = ItemSet.GetTotalBaseAmount(GenericFunctions.getEquipment(p), p, ItemSet.DASHER)/100d;
			aPlugin.API.setPlayerSpeedMultiplier(p, (float)(1.0f+spdmult));
		}
	}

	private void EndShopSession(Player p) {
		if (TwosideKeeper.TwosideShops.IsPlayerUsingTerminal(p) &&
				(TwosideKeeper.TwosideShops.GetSession(p).GetSign().getBlock()==null || TwosideKeeper.TwosideShops.GetSession(p).IsTimeExpired())) {
			p.sendMessage(ChatColor.RED+"Ran out of time! "+ChatColor.WHITE+"Shop session closed.");
			TwosideKeeper.TwosideShops.RemoveSession(p);
		}
	}

	private void ModifyArmorBar(Player p) {
		p.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(20*(1.0d-CustomDamage.CalculateDamageReduction(1,p,null))+subtractVanillaArmorBar(p.getEquipment().getArmorContents()));
	}

	private void ResetVendetta(final long serverTickTime, PlayerStructure pd) {
		if (pd.lastcombat+(20*60)<serverTickTime) {
			pd.vendetta_amt=0;
			pd.thorns_amt=0;
			pd.weaponcharges=0;
		}
		if (pd.vendetta_amt>0 && pd.lastvendettastack+200<serverTickTime) {
			pd.vendetta_amt=0;
		}
	}

	private void ResetLifestealStacks(final long serverTickTime, PlayerStructure pd) {
		if (pd.lastattacked+(20*5)<serverTickTime) {
			pd.lastattacked=0;
			pd.lifestealstacks=0;
		}
	}

	private void ManageSnowmanHunt() {
		if (TwosideKeeper.CHRISTMASEVENT_ACTIVATED) {
			if (TwosideKeeper.LastSnowmanHunt+36000<TwosideKeeper.getServerTickTime() && TwosideKeeper.SnowmanHuntList.size()>7) {
				TwosideKeeper.HuntingForSnowman = TwosideKeeper.SnowmanHuntList.get((int)(Math.random()*TwosideKeeper.SnowmanHuntList.size()));
				aPlugin.API.discordSendRaw("The Hunt is on to kill the Snowman named **"+TwosideKeeper.HuntingForSnowman+"**!");
				Bukkit.broadcastMessage("The Hunt is on to kill the Snowman named "+ChatColor.BOLD+TwosideKeeper.HuntingForSnowman+ChatColor.RESET+"!");
				Bukkit.broadcastMessage(ChatColor.AQUA+"   You will earn Holiday Tokens for successfully completing this mission!");
				TwosideKeeper.LastSnowmanHunt=TwosideKeeper.getServerTickTime();
			}
		}
	}

	private void ManagePlayerLink(Player p, PlayerStructure pd) {
		if (pd.linkplayer!=null && pd.linkplayer.isValid()) {
			GlowAPI.setGlowing(pd.linkplayer, true, p);
			if (pd.lastlinkteleport!=0 && pd.lastlinkteleport+12000<TwosideKeeper.getServerTickTime()) {
				GlowAPI.setGlowing(pd.linkplayer, false, p);
				pd.linkplayer=null;
			}
		} else
		if (pd.linkplayer!=null && !pd.linkplayer.isValid()) {
			GlowAPI.setGlowing(pd.linkplayer, false, p);
			pd.linkplayer=null;
		}
	}

	private void DepleteDamagePool(final long serverTickTime, Player p, PlayerStructure pd) {
		if (pd.damagepool>0 && pd.damagepooltime+20<=serverTickTime) {
			double transferdmg = CustomDamage.getTransferDamage(p)+(pd.damagepool*0.01);
			TwosideKeeper.log("Transfer Dmg is "+transferdmg+". Damage Pool: "+pd.damagepool, 5);
			CustomDamage.ApplyDamage(transferdmg, null, p, null, "Damage Pool", CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.TRUEDMG|CustomDamage.IGNOREDODGE);
			if (pd.damagepool-transferdmg<1) {
				pd.damagepool=0;
			} else {
				pd.damagepool-=transferdmg;
			}
		}
	}

	private void AdventurerModeSetExhaustion(Player p) {
		if (PlayerMode.getPlayerMode(p)==PlayerMode.NORMAL) {
			p.setExhaustion(Math.max(0, p.getExhaustion()-0.5f));
		}
	}

	private void DasherFoodRegenPerk(Player p) {
		if (p.isSprinting() && p.getFoodLevel()<20
				&& ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getArmor(p), p, ItemSet.DASHER, 4)) {
			p.setFoodLevel(p.getFoodLevel()+1);
		}
	}

	private void GivePartyNightVision(Player p) {
		if (ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getArmor(p), p, ItemSet.RUDOLPH, 4)) {
			if (!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
				GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, p, true);
			}
			List<Player> partymembers = PartyManager.getPartyMembers(p);
			for (Player pl : partymembers) {
				if (!pl.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
					GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, pl, true);
				}
			}
		}
	}

	private void ApplyCometRegenBonus(Player p) {
		double regenbuff = ItemSet.GetTotalBaseAmount(GenericFunctions.getEquipment(p), p, ItemSet.COMET);
		if (regenbuff>0) {
			List<Player> partymembers = PartyManager.getPartyMembers(p);
			for (Player pl : partymembers) {
				PlayerStructure pld = PlayerStructure.GetPlayerStructure(pl);
				pld.pctbonusregen=regenbuff/100d;
				pld.pctbonusregentime=TwosideKeeper.getServerTickTime();
			}
		}
	}

	private void ResetSlayerAggro(final long serverTickTime, Player p, PlayerStructure pd) {
		if (PlayerMode.isSlayer(p)) {
			if (pd.lastsneak+50<=serverTickTime &&
					p.isSneaking() &&
					ItemSet.HasSetBonusBasedOnSetBonusCount(GenericFunctions.getBaubles(p), p, ItemSet.MOONSHADOW, 7)) {
				GenericFunctions.deAggroNearbyTargets(p);
				GenericFunctions.applyStealth(p, true);
			}
		}
	}

	private void ResetSwordCombo(final long serverTickTime, Player p, PlayerStructure pd) {
		if (ArtifactAbility.containsEnchantment(ArtifactAbility.COMBO, p.getEquipment().getItemInMainHand()) &&
				pd.last_swordhit+40<serverTickTime) {
			pd.swordcombo=0; //Reset the sword combo meter since the time limit expired.
		}
	}

	private void CalculateHealthRegeneration(final long serverTickTime, Player p, PlayerStructure pd,
			ItemStack[] equips) {
		if (PlayerMode.isDefender(p)) {
			GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.REGENERATION,60,(p.isBlocking())?3:1,p,false);
		}
	}

	public static void performTimingsReport() {
		performTimingsReport(false);
	}

	public static void performTimingsReport(boolean manual) {
		double tps = MinecraftServer.getServer().recentTps[0];
		if ((tps<18 && TwosideKeeper.lastTimingReport+36000<TwosideKeeper.getServerTickTime()) || manual) {
			DecimalFormat df = new DecimalFormat("0.00");
			aPlugin.API.discordSendRawItalicized("**Server is lagging.**\nCurrent TPS: **"+df.format(tps)+"** (Also writing debug timings to log file)");
			if (TwosideKeeper.getServerTickTime()-TwosideKeeper.lastTimingReport>72000) {
				aPlugin.API.takeTimings(1200);
			} else {
				aPlugin.API.takeTimings(3600);
			}
			TwosideKeeper.lastTimingReport=TwosideKeeper.getServerTickTime();
			GenericFunctions.logToFile("["+TwosideKeeper.getServerTickTime()+"] TPS: "+tps+"\n------------------\n"+ChatColor.stripColor(TwosideKeeper.HeartbeatLogger.outputReport()),"logs/"+TwosideKeeper.getServerTickTime()+".txt");
			aPlugin.API.discordPostFileAttachment(new File(TwosideKeeper.filesave, "logs/"+TwosideKeeper.getServerTickTime()+".txt"));
		}
		if (tps<18) {
			GenericFunctions.logToFile("["+TwosideKeeper.getServerTickTime()+"] TPS: "+tps+"\n------------------\n"+ChatColor.stripColor(TwosideKeeper.HeartbeatLogger.outputReport()));
		}
	}

	private void CheckAndAnnounceWeather() {
		if (Bukkit.getWorld("world").hasStorm()) {
			if (!TwosideKeeper.last_announced_storm) {
				TwosideKeeper.last_announced_storm=true;
				for (String user : TwosideKeeper.weather_watch_users) {
					if (Bukkit.getPlayer(UUID.fromString(user))!=null) {
						Player p = Bukkit.getPlayer(UUID.fromString(user));
						p.sendMessage(ChatColor.ITALIC+""+ChatColor.GRAY+"A storm"+((Bukkit.getWorld("world").isThundering())?" (With Thunder)":"")+" is now occuring on the server. (Day "+(int)(TwosideKeeper.getServerTickTime()/48000)+")");
					}
					File config;
					config = new File(TwosideKeeper.filesave,"users/"+user+".data");
					FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
					//aPlugin.DiscordMessageSender.sendPM("A storm"+((Bukkit.getWorld("world").isThundering())?" (With Thunder)":"")+" is now occuring on the server. (Day "+(int)(TwosideKeeper.getServerTickTime()/48000)+")", workable.getString("weatherwatch_user"));
					aPlugin.API.discordSendOnlineDM(workable.getString("weatherwatch_user"), "A storm"+((Bukkit.getWorld("world").isThundering())?" (With Thunder)":"")+" is now occuring on the server. (Day "+(int)(TwosideKeeper.getServerTickTime()/48000)+")");
				}
			}
		}
		else {
			TwosideKeeper.last_announced_storm=false;
		}
	}

	public static void runFilterCubeCollection(Player p, List<UUID> ignoredItems) {
		if (InventoryUtils.hasFullInventory(p) && InventoryUtils.isCarryingFilterCube(p)) {
			List<Entity> ents = p.getNearbyEntities(1, 1, 1);
			int count=0;
			for (Entity ent : ents) {
				if (ent instanceof Item && GenericFunctions.itemCanBeSuckedUp((Item)ent,p)) {
					Item it = (Item)ent;
					if (it.getPickupDelay()<=0) {
						events.PlayerManualPickupItemEvent ev = new events.PlayerManualPickupItemEvent(p, it.getItemStack());
						Bukkit.getPluginManager().callEvent(ev);
						if (!ev.isCancelled()) {
					    	boolean handled = TwosideKeeper.AutoEquipItem(it.getItemStack(), p);
					    	if (!handled) {
					    		ItemStack[] remaining = InventoryUtils.insertItemsInFilterCube(p, it.getItemStack());
					    		if (remaining.length==0) {
					    			InventoryUpdateEvent.TriggerUpdateInventoryEvent(ev.getPlayer(),ev.getItemStack(),UpdateReason.PICKEDUPITEM);
					    			SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(it.getItemStack()));
					    			TwosideKeeper.PlayPickupParticle(p,it);
					    			it.remove();
					    		}
					    	} else {
								InventoryUpdateEvent.TriggerUpdateInventoryEvent(ev.getPlayer(),ev.getItemStack(),UpdateReason.PICKEDUPITEM);
					    		TwosideKeeper.PlayPickupParticle(p,it);
				    			it.remove();
					    	}
						} else {
					    	TwosideKeeper.PlayPickupParticle(p,it);
							it.remove();
						}
					}
					if (it.isValid()) {
						ignoredItems.add(it.getUniqueId());
					}
					count++;
					if (count>=8) {
						return;
					}
				}
			}
		}
	}

	public static void runVacuumCubeSuckup(Player p, List<UUID> ignoredItems) {
		if (InventoryUtils.isCarryingVacuumCube(p)) {
			//Suck up nearby item entities.
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			List<Entity> ents = p.getNearbyEntities(6, 6, 6);
			int count=0;
			for (Entity ent : ents) {
				if (ent instanceof Item && GenericFunctions.itemCanBeSuckedUp((Item)ent,p)) {
					//Pull towards the player.
					double SPD = 0.2;
					double deltax = ent.getLocation().getX()-p.getLocation().getX();
					double deltay = ent.getLocation().getY()-p.getLocation().getY();
					double deltaz = ent.getLocation().getZ()-p.getLocation().getZ();
					double xvel = 0;
					double yvel = 0;
					double zvel = 0;
					if (deltax>0.25) {
						xvel=-SPD*(Math.min(10, Math.abs(deltax)));
					} else
					if (deltax<-0.25) {
						xvel=SPD*(Math.min(10, Math.abs(deltax)));
					}
					/*if (deltay>0.01) {
						yvel=-SPD*deltay*4;
					} else
					if (deltay<-0.01) {
						yvel=SPD*deltay*4;
					}*/
					if (deltaz>0.25) {
						zvel=-SPD*(Math.min(10, Math.abs(deltaz)));
					} else
					if (deltaz<-0.25) {
						zvel=SPD*(Math.min(10, Math.abs(deltaz)));
					}
					if (Math.abs(deltax)<=1 &&
							Math.abs(deltay)<=1 &&
							Math.abs(deltaz)<=1 &&
							InventoryUtils.hasFullInventory(p) &&
							((Item)ent).getPickupDelay()<=0) {
						//Collect this item.
						if (((Item)ent).getItemStack().getType().isBlock()) {
							events.PlayerManualPickupItemEvent ev = new events.PlayerManualPickupItemEvent(p, ((Item) ent).getItemStack());
							Bukkit.getPluginManager().callEvent(ev);
							if (!ev.isCancelled()) {
								ItemStack[] remaining = InventoryUtils.insertItemsInVacuumCube(p, ((Item) ent).getItemStack());
								if (remaining.length==0) {
					    			SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(((Item) ent).getItemStack()));
					    			TwosideKeeper.PlayPickupParticle(p,(Item)ent);
					    			InventoryUpdateEvent.TriggerUpdateInventoryEvent(ev.getPlayer(),ev.getItemStack(),UpdateReason.PICKEDUPITEM);
									ent.remove();
									return;
								}
							} else {
								InventoryUpdateEvent.TriggerUpdateInventoryEvent(ev.getPlayer(),ev.getItemStack(),UpdateReason.PICKEDUPITEM);
								ent.remove();
								return;
							}
							count++;
							if (count>8) {
								return;
							}
						}
					} else {
						if (pd.vacuumsuckup) {
							ent.setVelocity(new Vector(xvel,yvel,zvel));
						}
					}
					if (ent.isValid()) {
						if (ignoredItems.contains(ent.getUniqueId())) {
							pd.ignoreItemsList.add(ent.getUniqueId());
						}
					}
					/*if (ent.getLocation().getX()<p.getLocation().getX()) {
						ent.setVelocity(ent.getVelocity().setX(SPD*(10-Math.min(10,Math.abs()))));
					} else {
						ent.setVelocity(ent.getVelocity().setX(-SPD*(10-Math.min(10,Math.abs(p.getLocation().getX()-ent.getLocation().getX())))));
					}
					if (ent.getLocation().getY()<p.getLocation().getY()) {
						ent.setVelocity(ent.getVelocity().setY(SPD*(10-Math.min(10,Math.abs(p.getLocation().getY()-ent.getLocation().getY())))));
					} else {
						ent.setVelocity(ent.getVelocity().setY(-SPD*(10-Math.min(10,Math.abs(p.getLocation().getY()-ent.getLocation().getY())))));
					}
					if (ent.getLocation().getZ()<p.getLocation().getZ()) {
						ent.setVelocity(ent.getVelocity().setZ(SPD*(10-Math.min(10,Math.abs(p.getLocation().getZ()-ent.getLocation().getZ())))));
					} else {
						ent.setVelocity(ent.getVelocity().setZ(-SPD*(10-Math.min(10,Math.abs(p.getLocation().getZ()-ent.getLocation().getZ())))));
					}*/
				}
			}
		}
	}

	private void PopRandomLavaBlock(Player p) {
		if (p.getWorld().getName().equalsIgnoreCase("world_nether") &&
				PlayerStructure.GetPlayerStructure(p).lastlavaplume+TwosideKeeper.LAVA_PLUME_COOLDOWN<TwosideKeeper.getServerTickTime()) {
			//Choose a random location near the player.
			int randomx=(int)(Math.random()*32)-16;
			int randomz=(int)(Math.random()*32)-16;
			PlayerStructure.GetPlayerStructure(p).lastlavaplume=TwosideKeeper.getServerTickTime();
			//Start a couple blocks above the player. Work our way down until we can't find AIR or we go farther than 5 iterations.
			int yrel=5;
			while (p.getLocation().getBlockY()+yrel>=0) {
				final Block b = p.getLocation().add(randomx,yrel,randomz).getBlock();
				//Schedule this 3 seconds later.
				if (b.getType()==Material.STATIONARY_LAVA) {
					//TwosideKeeper.log("Block ("+b.getLocation()+") is type "+b.getType(), 0);
					//CreateLavaPlumeParticles(b);
					//Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, ()->{}, 20*3);
					CreateLavaPlume(b);
					break;
				} else {
					if (b.getType()==Material.AIR) {
						yrel--;
					} else {
						break;
					}
				}
			}
		}
	}

	private void CreateLavaPlumeParticles(Block b) {
		//aPluginAPIWrapper.sendParticle(b.getLocation().add(0,5,0), EnumParticle.DRIP_LAVA, 0, 0, 0, 0.4f, 100);
		/*for (int i=0;i<100;i++) {
			aPluginAPIWrapper.sendParticle(b.getLocation().add(0,Math.random()*5,0), EnumParticle.DRIP_LAVA, (float)Math.random(),(float)Math.random(),(float)Math.random(), (float)Math.random(), 5);
		}*/
	}

	private void CreateLavaPlume(final Block b) {
		//FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation().add(0,1,0), Material.REDSTONE_BLOCK, (byte)0);
		TwosideKeeper.lavaplume_list.add(new LavaPlume(90,b.getLocation()));
		/*fb.setMetadata("DESTROY", new FixedMetadataValue(TwosideKeeper.plugin,true));
		fb.setVelocity(new Vector(0,(float)((Math.random()*8)+2),0));
		for (Player pl : Bukkit.getOnlinePlayers()) {
			GlowAPI.setGlowing(fb, GlowAPI.Color.YELLOW, pl);
		}*/
	}

	private void AutoConsumeFoods(Player p) {
		if (p.getFoodLevel()<20 && PlayerMode.getPlayerMode(p)==PlayerMode.BARBARIAN) { 
			ItemStack[] contents = p.getInventory().getStorageContents();
			for (int i=0;i<contents.length;i++) {
				if (contents[i]!=null &&
						GenericFunctions.isAutoConsumeFood(contents[i])) {
					SoundUtils.playLocalSound(p, Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
					p.setFoodLevel(Math.min(20, p.getFoodLevel()+1));
					double basepercent = p.getMaxHealth()*0.01;
					GenericFunctions.HealEntity(p,basepercent);
					ItemStack singlecopy = contents[i].clone();
					singlecopy.setAmount(1);
					p.getInventory().removeItem(singlecopy);
				}
			}
		}
	}

	private void MaintainMonsterData() {
		Set<UUID> data= TwosideKeeper.livingentitydata.keySet();
		TwosideKeeper.log("Size: "+TwosideKeeper.livingentitydata.size(), 5);
		long time = System.nanoTime();
		for (UUID id : data) {
			LivingEntityStructure ms = TwosideKeeper.livingentitydata.get(id);
			if (ms.checkedforcubes) {
				ms.checkedforcubes=false;
				TwosideKeeper.HeartbeatLogger.AddEntry("Monster Management - Magma Cube Clear", (int)(System.nanoTime()-time));time=System.nanoTime();
			}
			if (!ms.m.isValid() || ms.m instanceof Player) {
				//TwosideKeeper.monsterdata.remove(data);
				TwosideKeeper.ScheduleRemoval(TwosideKeeper.livingentitydata, ms);
				TwosideKeeper.ScheduleRemoval(data, id);
				TwosideKeeper.ScheduleRemoval(TwosideKeeper.habitat_data.startinglocs, id);
				TwosideKeeper.log("Removed Monster Structure for "+id+".", 5);
				TwosideKeeper.HeartbeatLogger.AddEntry("Monster Management - Removed Monster Structure Data.", (int)(System.nanoTime()-time));time=System.nanoTime();
			} else {
				AddEliteStructureIfOneDoesNotExist(ms);
				TwosideKeeper.HeartbeatLogger.AddEntry("Monster Management - Add Elite Structure", (int)(System.nanoTime()-time));time=System.nanoTime();
				if (ms.GetTarget()!=null && ms.GetTarget().isValid() && !ms.GetTarget().isDead() && ms.m.hasAI()) {
					//Randomly move this monster a tiny bit in case they are stuck.
					double xdir=((ms.m.getLocation().getX()>ms.GetTarget().getLocation().getX())?-0.25:0.25)+(Math.random()/8)-(Math.random()/8);
					double zdir=((ms.m.getLocation().getZ()>ms.GetTarget().getLocation().getZ())?-0.25:0.25)+(Math.random()/8)-(Math.random()/8);
					ms.m.setVelocity(ms.m.getVelocity().add(new Vector(xdir,0,zdir)));
					TwosideKeeper.HeartbeatLogger.AddEntry("Monster Management - Randomly Move this Monster", (int)(System.nanoTime()-time));time=System.nanoTime();
				}
				ms.UpdateGlow();
				TwosideKeeper.HeartbeatLogger.AddEntry("Monster Management - Update Glow", (int)(System.nanoTime()-time));time=System.nanoTime();
			}
		}
	}

	public void AddEliteStructureIfOneDoesNotExist(LivingEntityStructure ms) {
		if (ms.isElite || (ms.m instanceof Monster && MonsterController.getMonsterDifficulty((Monster)(ms.m))==MonsterDifficulty.ELITE)) {
			//Make it glow dark purple.
			//GenericFunctions.setGlowing(m, GlowAPI.Color.DARK_PURPLE);
			boolean hasstruct = false;
			for (EliteMonster em : TwosideKeeper.elitemonsters) {
				if (em.m.equals(ms.m)) {
					hasstruct=true;
				}
			}
			if (!hasstruct) {
				TwosideKeeper.log("Added Elite Monster "+ms.m, 0);
				TwosideKeeper.elitemonsters.add(GenericFunctions.getProperEliteMonster((Monster)(ms.m)));
			}
		}
	}

	private void randomlyAggroNearbyEndermen(Player p) {
		//List<LivingEntity> ents = GenericFunctions.getNearbyMobs(p.getLocation(), 16);
		List<Monster> ments = CustomDamage.trimNonMonsterEntities(p.getNearbyEntities(8, 8, 8)); 
		for (Monster m : ments) {
			if (!m.isDead() && Math.random()<=0.05 && !m.hasPotionEffect(PotionEffectType.GLOWING)) {
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
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_AMBIENT, 1.0f, 1.0f);
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
			}
		},20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				SoundUtils.playLocalSound(p,p.getLocation().add(0,20,0), Sound.ENTITY_GHAST_WARN, 1.0f, 1.0f);
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_AMBIENT, 1.0f, 1.0f);
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
			}
		},23);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				SoundUtils.playLocalSound(p,p.getLocation().add(-10,0,-5), Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_AMBIENT, 1.0f, 1.0f);
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
			}
		},27);
		Bukkit.getScheduler().scheduleSyncDelayedTask(TwosideKeeper.plugin, new Runnable() {
			@Override
			public void run() {
				SoundUtils.playLocalSound(p,p.getLocation().add(-10,0,-5), Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_AMBIENT, 1.0f, 1.0f);
				SoundUtils.playLocalSound(p, Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);
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
			//DiscordMessageSender.sendToSpam(finalstring.toString());
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