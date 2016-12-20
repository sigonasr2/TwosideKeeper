package sig.plugin.TwosideKeeper;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.glow.GlowAPI;

import aPlugin.DiscordMessageSender;
import net.minecraft.server.v1_9_R1.EnumParticle;
import sig.plugin.TwosideKeeper.HelperStructures.ArtifactAbility;
import sig.plugin.TwosideKeeper.HelperStructures.BankSession;
import sig.plugin.TwosideKeeper.HelperStructures.ItemSet;
import sig.plugin.TwosideKeeper.HelperStructures.MonsterDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.ServerType;
import sig.plugin.TwosideKeeper.HelperStructures.WorldShop;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Effects.LavaPlume;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.InventoryUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.MessageUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;

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
			
			if (TwosideKeeper.LAST_DEAL!=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
				//This means the deal of the day has to be updated!
				TwosideKeeper.LAST_DEAL = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
				TwosideKeeper.DEAL_OF_THE_DAY_ITEM = WorldShop.generateItemDealOftheDay(1);
				if (TwosideKeeper.SERVER_TYPE!=ServerType.QUIET) {
					DecimalFormat df = new DecimalFormat("0.00");
					aPlugin.API.discordSendRaw("*The Deal of the Day has been updated!*\n **"+GenericFunctions.UserFriendlyMaterialName(TwosideKeeper.DEAL_OF_THE_DAY_ITEM)+"**  ~~$"+df.format(WorldShop.getBaseWorldShopPrice(TwosideKeeper.DEAL_OF_THE_DAY_ITEM))+"~~  $"+df.format(WorldShop.getBaseWorldShopPrice(TwosideKeeper.DEAL_OF_THE_DAY_ITEM)*0.8)+"  **20% Off!**");
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
	    	//TwosideKeeper.outputArmorDurability(p);
			if (!p.isDead()) { 
				PlayerStructure pd = (PlayerStructure)TwosideKeeper.playerdata.get(p.getUniqueId());
				
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
				
				if (!aPlugin.API.isAFK(p)) {
					if (TwosideKeeper.TwosideShops.IsPlayerUsingTerminal(p) &&
							(TwosideKeeper.TwosideShops.GetSession(p).GetSign().getBlock()==null || TwosideKeeper.TwosideShops.GetSession(p).IsTimeExpired())) {
						p.sendMessage(ChatColor.RED+"Ran out of time! "+ChatColor.WHITE+"Shop session closed.");
						TwosideKeeper.TwosideShops.RemoveSession(p);
					}
					
					GenericFunctions.RemoveNewDebuffs(p);
					
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
						GenericFunctions.sendActionBarMessage(p, TwosideKeeper.drawVelocityBar(pd.velocity,pd.highwinderdmg), true);
					}
					if (pd.target!=null && !pd.target.isDead() && pd.target.getLocation().getWorld().equals(p.getWorld()) && pd.target.getLocation().distanceSquared(p.getLocation())>256) {
						pd.target=null;
					}
					
					if (pd.lasthittarget+20*15<=serverTickTime && pd.storedbowxp>0 && GenericFunctions.isArtifactEquip(p.getEquipment().getItemInMainHand()) &&
							p.getEquipment().getItemInMainHand().getType()==Material.BOW) {
						AwakenedArtifact.addPotentialEXP(p.getEquipment().getItemInMainHand(), pd.storedbowxp, p);
						TwosideKeeper.log("Added "+pd.storedbowxp+" Artifact XP", 4);
						pd.storedbowxp=0;
					}
					
					if (p.getFireTicks()>0 && p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
						int duration = GenericFunctions.getPotionEffectDuration(PotionEffectType.FIRE_RESISTANCE, p);
						int lv = GenericFunctions.getPotionEffectLevel(PotionEffectType.FIRE_RESISTANCE, p);
						if (lv>10) {lv=10;}
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.FIRE_RESISTANCE, duration-(20*(10-lv)), lv, p, true);
					}
					
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

					ItemStack[] equips = p.getEquipment().getArmorContents();

					for (ItemStack equip : equips) {
						if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, equip) &&
								p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
							GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED,20,1,p);
						}
					}
					if (ArtifactAbility.containsEnchantment(ArtifactAbility.SHADOWWALKER, p.getEquipment().getItemInMainHand()) &&
							p.isOnGround() && p.getLocation().getY()>=0 && p.getLocation().getY()<=255 && p.getLocation().add(0,0,0).getBlock().getLightLevel()<=4) {
						GenericFunctions.logAndApplyPotionEffectToEntity(PotionEffectType.SPEED,20,1,p);
						//log("Apply speed. The light level here is "+p.getLocation().add(0,-1,0).getBlock().getLightLevel(),2);
					}
					
					//PopulatePlayerBlockList(p,15,15,2,5,false);
					PopRandomLavaBlock(p);
					GenericFunctions.sendActionBarMessage(p, "");
					GenericFunctions.AutoRepairItems(p);
					
					if (GenericFunctions.hasStealth(p)) {GenericFunctions.DamageRandomTool(p);}
					
					//See if this player is sleeping.
					if (p.isSleeping()) {
						p.setHealth(Bukkit.getPlayer(pd.name).getMaxHealth()); //Heals the player fully when sleeping.
					}
					
					//We need to see if this player's damage reduction has changed recently. If so, notify them.
					//Check damage reduction by sending an artifical "1" damage to the player.
					if (!p.isDead()) {TwosideKeeper.log("Player is not dead.",5); TwosideKeeper.setPlayerMaxHealth(p);}
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setSuffix(TwosideKeeper.createHealthbar(((p.getHealth())/p.getMaxHealth())*100,p));
					p.getScoreboard().getTeam(p.getName().toLowerCase()).setPrefix(GenericFunctions.PlayerModePrefix(p));
					
					if (PlayerMode.isBarbarian(p)) {
						AutoConsumeFoods(p);
					}
				}

				p.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(20*(1.0d-CustomDamage.CalculateDamageReduction(1,p,null))+subtractVanillaArmorBar(p.getEquipment().getArmorContents()));

				ItemStack[] equips = p.getEquipment().getArmorContents();
				
				if (pd.lastcombat+(20*60)<serverTickTime) {
					pd.vendetta_amt=0;
					pd.thorns_amt=0;
					pd.weaponcharges=0;
				}
				if (pd.vendetta_amt>0 && pd.lastvendettastack+200<serverTickTime) {
					pd.vendetta_amt=0;
				}
				if (pd.lastattacked+(20*5)<serverTickTime) {
					pd.lastattacked=0;
					pd.lifestealstacks=0;
				}
				
				if (pd.damagepool>0 && pd.damagepooltime+20<=serverTickTime) {
					double transferdmg = CustomDamage.getTransferDamage(p);
					TwosideKeeper.log("Transfer Dmg is "+transferdmg+". Damage Pool: "+pd.damagepool, 5);
					CustomDamage.ApplyDamage(transferdmg, null, p, null, "Damage Pool", CustomDamage.IGNORE_DAMAGE_TICK|CustomDamage.TRUEDMG|CustomDamage.IGNOREDODGE);
					if (pd.damagepool-transferdmg<=0) {
						pd.damagepool=0;
					} else {
						pd.damagepool-=transferdmg;
					}
				}
				
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
				}
				
			}
	    	//TwosideKeeper.outputArmorDurability(p,">");
		}
		
		MaintainMonsterData();
		
		PartyManager.SetupParties();
		
		TwosideKeeper.TwosideSpleefGames.TickEvent();
	}


	public static void runFilterCubeCollection(Player p) {
		if (InventoryUtils.hasFullInventory(p) && InventoryUtils.isCarryingFilterCube(p)) {
			List<Entity> ents = p.getNearbyEntities(0.25, 0.25, 0.25);
			for (Entity ent : ents) {
				if (ent instanceof Item && GenericFunctions.itemCanBeSuckedUp((Item)ent)) {
					Item it = (Item)ent;
					if (it.getPickupDelay()<it.getTicksLived()) {
			    		ItemStack[] remaining = InventoryUtils.insertItemsInFilterCube(p, it.getItemStack());
			    		if (remaining.length==0) {
			    			SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(it.getItemStack()));
			    			it.remove();
			    			return;
			    		}
					}
				}
			}
		}
	}

	public static void runVacuumCubeSuckup(Player p) {
		if (InventoryUtils.isCarryingVacuumCube(p)) {
			//Suck up nearby item entities.
			List<Entity> ents = p.getNearbyEntities(6, 6, 6);
			for (Entity ent : ents) {
				if (ent instanceof Item && GenericFunctions.itemCanBeSuckedUp((Item)ent)) {
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
					if (deltay>0.01) {
						yvel=-SPD*deltay*4;
					} else
					if (deltay<-0.01) {
						yvel=SPD*deltay*4;
					}
					if (deltaz>0.25) {
						zvel=-SPD*(Math.min(10, Math.abs(deltaz)));
					} else
					if (deltaz<-0.25) {
						zvel=SPD*(Math.min(10, Math.abs(deltaz)));
					}
					if (Math.abs(deltax)<0.25 &&
							Math.abs(deltay)<0.25 &&
							Math.abs(deltaz)<0.25 &&
							InventoryUtils.hasFullInventory(p) &&
							((Item)ent).getPickupDelay()<((Item)ent).getTicksLived()) {
						//Collect this item.
						if (((Item)ent).getItemStack().getType().isBlock()) {
							ItemStack[] remaining = InventoryUtils.insertItemsInVacuumCube(p, ((Item) ent).getItemStack());
							if (remaining.length==0) {
				    			SoundUtils.playGlobalSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, SoundUtils.DetermineItemPitch(((Item) ent).getItemStack()));
								ent.remove();
								return;
							}
						}
					} else {
						ent.setVelocity(new Vector(xvel,yvel,zvel));
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
		for (UUID id : data) {
			LivingEntityStructure ms = TwosideKeeper.livingentitydata.get(id);
			if (!ms.m.isValid()) {
				//TwosideKeeper.monsterdata.remove(data);
				TwosideKeeper.ScheduleRemoval(TwosideKeeper.livingentitydata, ms);
				TwosideKeeper.ScheduleRemoval(data, id);
				TwosideKeeper.log("Removed Monster Structure for "+id+".", 5);
			} else {
				AddEliteStructureIfOneDoesNotExist(ms);
				if (ms.GetTarget()!=null && ms.GetTarget().isValid() && !ms.GetTarget().isDead()) {
					//Randomly move this monster a tiny bit in case they are stuck.
					double xdir=((ms.m.getLocation().getX()>ms.GetTarget().getLocation().getX())?-0.25:0.25)+(Math.random()/8)-(Math.random()/8);
					double zdir=((ms.m.getLocation().getZ()>ms.GetTarget().getLocation().getZ())?-0.25:0.25)+(Math.random()/8)-(Math.random()/8);
					ms.m.setVelocity(ms.m.getVelocity().add(new Vector(xdir,0,zdir)));
				}
				ms.UpdateGlow();
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
				TwosideKeeper.elitemonsters.add(GenericFunctions.getProperEliteMonster((Monster)(ms.m)));
			}
		}
	}

	private void randomlyAggroNearbyEndermen(Player p) {
		//List<LivingEntity> ents = GenericFunctions.getNearbyMobs(p.getLocation(), 16);
		List<Monster> ments = CustomDamage.trimNonMonsterEntities(p.getNearbyEntities(16, 16, 16)); 
		for (Monster m : ments) {
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