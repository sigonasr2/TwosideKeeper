package sig.plugin.TwosideKeeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class BossMonster {
	private String name;
	private double maxhp;
	private double hp_regen;
	private double damagereduction;
	private double attackstrength;
	private double movespd;
	private LivingEntity m;
	private Collection<PotionEffect> buffs;
	protected BossBar bar = null;
	protected List<Player> targetlist = new ArrayList<Player>();
	protected List<Player> participantlist = new ArrayList<Player>();
	protected HashMap<String,Double> dpslist = new HashMap<String,Double>();
	protected String arrow = "->";
	int scroll=0;
	
	public BossMonster(String name, double maxhp, double attackstrength, LivingEntity m) {
		super();
		this.name = name;
		this.maxhp = maxhp;
		this.attackstrength = attackstrength;
		this.m = m;
		this.damagereduction=0.0;
		this.buffs=null;
		this.movespd=LivingEntityStructure.GetLivingEntityStructure(m).original_movespd;
		this.hp_regen=0;
		this.bar = m.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SOLID, BarFlag.CREATE_FOG);
	}

	public BossMonster(String name, double maxhp, double damagereduction, double attackstrength, LivingEntity m) {
		super();
		this.name = name;
		this.maxhp = maxhp;
		this.damagereduction = damagereduction;
		this.attackstrength = attackstrength;
		this.m = m;
		this.buffs=null;
		this.movespd=LivingEntityStructure.GetLivingEntityStructure(m).original_movespd;
		this.hp_regen=0;
		this.bar = m.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SOLID, BarFlag.CREATE_FOG);
	}

	public BossMonster(String name, double maxhp, double damagereduction, double attackstrength, LivingEntity m,
			Collection<PotionEffect> buffs) {
		super();
		this.name = name;
		this.maxhp = maxhp;
		this.damagereduction = damagereduction;
		this.attackstrength = attackstrength;
		this.m = m;
		this.buffs = buffs;
		this.movespd=LivingEntityStructure.GetLivingEntityStructure(m).original_movespd;
		this.hp_regen=0;
		this.bar = m.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SOLID, BarFlag.CREATE_FOG);
	}

	public BossMonster(String name, double maxhp, double damagereduction, double attackstrength, double movespd,
			LivingEntity m, Collection<PotionEffect> buffs) {
		super();
		this.name = name;
		this.maxhp = maxhp;
		this.damagereduction = damagereduction;
		this.attackstrength = attackstrength;
		this.movespd = movespd;
		this.m = m;
		this.buffs = buffs;
		this.hp_regen=0;
		this.bar = m.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SOLID, BarFlag.CREATE_FOG);
	}
	
	public BossMonster(String name, double maxhp, double hp_regen, double damagereduction, double attackstrength,
			double movespd, LivingEntity m, Collection<PotionEffect> buffs) {
		super();
		this.name = name;
		this.maxhp = maxhp;
		this.hp_regen = hp_regen;
		this.damagereduction = damagereduction;
		this.attackstrength = attackstrength;
		this.movespd = movespd;
		this.m = m;
		this.buffs = buffs;
		this.bar = m.getServer().createBossBar(GenericFunctions.getDisplayName(m), BarColor.WHITE, BarStyle.SOLID, BarFlag.CREATE_FOG);
	}
	public double getMaxhp() {
		return maxhp;
	}
	public double getHp_regen() {
		return hp_regen;
	}
	public double getDamagereduction() {
		return damagereduction;
	}
	public double getAttackstrength() {
		return attackstrength;
	}
	public double getMovespd() {
		return movespd;
	}
	public LivingEntity getM() {
		return m;
	}
	public Collection<PotionEffect> getBuffs() {
		return buffs;
	}
	public String getName() {
		return name;
	}
	
	public void runTick() {
		increaseBarTextScroll();
		createBossHealthbar();
	}
	
	protected void increaseBarTextScroll() {
		scroll++;
		switch (scroll%22) {
			case 11:{
				arrow="  -";
			}break;
			case 12:{
				arrow="   ";
			}break;
			case 13:{
				arrow=">  ";
			}break;
			case 14:{
				arrow="->";
			}break;
		}
	}

	protected void createBossHealthbar() {
		List<Player> currentplayers = bar.getPlayers();
		for (int i=0;i<currentplayers.size();i++) {
			if (!targetlist.contains(currentplayers.get(i))) {
				bar.removePlayer(currentplayers.get(i));
			}
		}
		bar.setProgress(m.getHealth()/m.getMaxHealth());
		bar.setTitle(GenericFunctions.getDisplayName(m) + ((m instanceof Monster && ((Monster)m).getTarget()!=null && (((Monster)m).getTarget() instanceof Player))?(ChatColor.DARK_AQUA+" "+arrow+" "+ChatColor.YELLOW+((Player)((Monster)m).getTarget()).getName()):""));
		for (int i=0;i<targetlist.size();i++) {
			if (!currentplayers.contains(targetlist.get(i))) {
				bar.addPlayer(targetlist.get(i));
			}
		}
	}
	
}
