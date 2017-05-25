package sig.plugin.TwosideKeeper.Monster;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.potion.PotionEffectType;

import sig.plugin.TwosideKeeper.Buff;
import sig.plugin.TwosideKeeper.CustomDamage;
import sig.plugin.TwosideKeeper.CustomMonster;
import sig.plugin.TwosideKeeper.LivingEntityStructure;
import sig.plugin.TwosideKeeper.MonsterController;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.Events.EntityChannelCastEvent;
import sig.plugin.TwosideKeeper.HelperStructures.Channel;
import sig.plugin.TwosideKeeper.HelperStructures.LivingEntityDifficulty;
import sig.plugin.TwosideKeeper.HelperStructures.Spell;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.SoundUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes.MixedDamage;

public class DarkSpider extends CustomMonster{
	
	Knight linked_knight;
	
	final static int[] SILENCE_DURATIONS = new int[]{200,100,60};
	final static int[] MINION_HEALTH = new int[]{900,3000,18000};
	List<LivingEntity> temp_spiders = new ArrayList<LivingEntity>();
	final Spell SPIDERSUMMON = new Spell("Summon Spider",new int[]{100,60,40},new int[]{600,500,400});
	final Spell ULTRABURST = new Spell("UltraBurst",new int[]{80,80,80},new int[]{1200,900,800}, new MixedDamage[]{MixedDamage.v(200),MixedDamage.v(1000),MixedDamage.v(50,0.95)});
	MixedDamage[] BASIC_ATTACK_DAMAGE = new MixedDamage[]{MixedDamage.v(20),MixedDamage.v(40),MixedDamage.v(40, 0.01)};
	
	int randomness = 16;

	public DarkSpider(LivingEntity m) {
		super(m);
		LivingEntityStructure les = LivingEntityStructure.GetLivingEntityStructure(m);
		MonsterController.convertLivingEntity(m, LivingEntityDifficulty.NORMAL);
		les.setCustomLivingEntityName(m, ChatColor.DARK_RED+"Dark Spider");
		m.setMaxHealth(100000);
		m.setHealth(m.getMaxHealth());
		m.setAI(true);
	}
	
	public void runTick() {
		if (canCastSpells()) { //SPELL CASTS HERE.
			castSpiderSummon();
		}
	}
	
	private void castSpiderSummon() {
		CastSpell(SPIDERSUMMON);
	}
	
	public void runChannelCastEvent(EntityChannelCastEvent ev) {
		switch (ev.getAbilityName()) {
			case "Summon Spider":{
				//Create another Spider.
				DarkSpiderMinion dsm = InitializeDarkSpiderMinion(linked_knight);
				dsm.linked_knight = linked_knight;
				SPIDERSUMMON.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
			case "UltraBurst":{
				playUltraBurst();
				ULTRABURST.setLastCastedTime(TwosideKeeper.getServerTickTime());
			}break;
		}
	}

	private void playUltraBurst() {
		SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0f, 1.0f);
		for (int i=0;i<3;i++) {
			Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
				SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0f, 1.2f);}, (i+1)*10);
		}
		Bukkit.getScheduler().runTaskLater(TwosideKeeper.plugin, ()->{
			aPlugin.API.sendSoundlessExplosion(m.getLocation(), 6);
			m.getWorld().spawnParticle(Particle.LAVA, m.getLocation(), 30);
			m.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, m.getLocation(), 2);
			SoundUtils.playGlobalSound(m.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.7f);
			
			DealSpellDamageToNearbyPlayers(ULTRABURST,50,true,false,3);
			m.remove();
		}, 40);
	}

	private void DealSpellDamageToNearbyPlayers(Spell spell, int range, boolean knockup, boolean dodgeable, double knockupamt) {
		List<Player> players = GenericFunctions.DealDamageToNearbyPlayers(m.getLocation(), spell.getDamageValues()[getDifficultySlot()].getDmgComponent(), range, knockup, dodgeable, knockupamt, m, spell.getName(), false);
		if (spell.getDamageValues()[getDifficultySlot()].hasTruePctDmgComponent()) {GenericFunctions.DealDamageToNearbyPlayers(m.getLocation(), spell.getDamageValues()[getDifficultySlot()].getTruePctDmgComponent(), range, knockup, dodgeable, knockupamt, m, spell.getName(), false,true);}
		if (spell.getDamageValues()[getDifficultySlot()].hasTrueDmgComponent()) {GenericFunctions.DealDamageToNearbyPlayers(m.getLocation(), spell.getDamageValues()[getDifficultySlot()].getTrueDmgComponent(), range, knockup, dodgeable, knockupamt, m, spell.getName(), true);}
		for (Player p : players) {
			GenericFunctions.addStackingPotionEffect(p, PotionEffectType.CONFUSION, 20*6, 1);
		}
	}

	private DarkSpiderMinion InitializeDarkSpiderMinion(Knight knight) {
		LivingEntity knight_ent = knight.GetMonster();
		CaveSpider s = (CaveSpider)knight_ent.getWorld().spawnEntity(m.getLocation(), EntityType.CAVE_SPIDER);
		DarkSpiderMinion dsm = new DarkSpiderMinion(s);
		TwosideKeeper.custommonsters.put(s.getUniqueId(), dsm);
		s.setMaxHealth(MINION_HEALTH[getDifficultySlot()]);
		s.setHealth(s.getMaxHealth());
		temp_spiders.add(s);
		return dsm;
	}
	
	public MixedDamage getBasicAttackDamage() {
		return BASIC_ATTACK_DAMAGE[getDifficultySlot()];
	}

	protected void CastSpell(Spell spell) {
		if (hasValidKnight() &&
				cooldownIsAvailable(spell.getLastCastedTime(),spell)) {
			Channel.createNewChannel(m, spell.getName(), spell.getCastTimes()[getDifficultySlot()]);
		}
	}

	private boolean cooldownIsAvailable(long spell_timer, Spell spell) {
		return spell_timer+spell.getCooldowns()[getDifficultySlot()]<=TwosideKeeper.getServerTickTime();
	}

	public static double getDamageReduction() {
		return 1.0;
	}

	public static Spider InitializeDarkSpider(LivingEntity sourceKnight) {
		Spider s = (Spider)sourceKnight.getWorld().spawnEntity(sourceKnight.getLocation(), EntityType.SPIDER);
		DarkSpider ds = new DarkSpider(s);
		TwosideKeeper.custommonsters.put(s.getUniqueId(), ds);
		return s;
	}
	
	public static boolean isDarkSpider(LivingEntity m) {
		return (m instanceof Spider && !(m instanceof CaveSpider)) &&
				m.getMaxHealth()==100000 &&
				MonsterController.getLivingEntityDifficulty(m)==LivingEntityDifficulty.NORMAL;
	}
	
	public LivingEntityDifficulty getDifficulty() {
		if (hasValidKnight()) {
			return MonsterController.getLivingEntityDifficulty(linked_knight.GetMonster());
		} else {
			return LivingEntityDifficulty.T1_MINIBOSS;
		}
	}

	private boolean hasValidKnight() {
		return linked_knight!=null && linked_knight.GetMonster()!=null && linked_knight.GetMonster().isValid();
	}
	
	public int getDifficultySlot() {
		switch (getDifficulty()) {
			case T1_MINIBOSS:{
				return 0;
			}
			case T2_MINIBOSS:{
				return 1;
			}
			case T3_MINIBOSS:{
				return 2;
			}
			default:{
				TwosideKeeper.log("WARNING! Could not get proper difficulty slot for Difficulty "+getDifficulty()+". Defaulting to slot 0.", 1);
				return 0;
			}
		}
	}
	
	public boolean canCastSpells() {
		return Math.random()<=1/16d && !Buff.hasBuff(m, "SILENCE") && hasValidKnight() && linked_knight.startedfight && !Channel.isChanneling(m);
	}
	
	public void onHitEvent(LivingEntity damager, double damage) {
		Buff.addBuff(
				m, "SILENCE", new Buff(
				"Silence",SILENCE_DURATIONS[getDifficultySlot()],
				0,Color.BLUE,ChatColor.WHITE+"…",false));
		if (Channel.isChanneling(m)) {
			Channel.stopChanneling(m);
		}
		if (hasValidKnight() &&
				(damager instanceof Player)) {
			linked_knight.addParticipant((Player)damager);
		}
	}
	
	public void cleanup() {
		for (LivingEntity l : temp_spiders) {
			if (l!=null && l.isValid()) {
				if (TwosideKeeper.custommonsters.containsKey(l.getUniqueId())) {
					CustomMonster cm = TwosideKeeper.custommonsters.get(l.getUniqueId());
					cm.cleanup();
				}
				l.remove();
			}
		}
		m.remove();
	}
}
