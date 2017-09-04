package sig.plugin.TwosideKeeper.HelperStructures;

public enum SpellScrollItem {
	//BASIC SPELLS (OFFENSE)
	FIREBALL,    //Simple fireball with splash damage.
	WINDBLAST,    //Small AOE, increased knockback.
	ARCANEBOLT,    //Simple single target damage. Highest basic spell damage for single targets.
	ICYWINDS,    //Large area AOE, low damage, reduces movement speed.
	//BASIC SPELLS (UTILITY)
	LIGHTHEAL,    //Basic healing spell, heals 10HP. Can only be applied to the caster.
	BULWARK,    //Basic defensive spell, temporarily adds a small amount of damage reduction (multiplicative, 30%?). Can only be applied to the caster.
	LIGHTSTEP,    //Simple utility spell, temporarily increases movement speed by a small amount. Can only be applied to the caster.
	EMPOWERMENT,    //Simple augmentation spell, temporarily increases damage dealt by a small amount (2.5%?). Can only be applied to the caster.
	//SECONDARY SPELLS (OFFENSE)
	POISONOUSHEX,    //A single target spell that applies a poison to a target for 10 seconds. Stacks up to 10 times.
	ENTANGLINGROOTS,    //A single target spell that deals a minor amount of damage and roots a target for 10 seconds. Repeated applications within 20 seconds have diminishing effects (50%, stacks indefinitely).
	RADIANCE,    //An AOE spell with circular radius, and short range. Deals minor damage and staggers (slows) all targets hit.
	DETONATE,    //A spell with a delayed explosion, and a small AOE radius. Does not agro on the initial placement.
	//SECONDARY SPELLS (UTILITY)
	MAGICDOMAIN,    //A domain class spell. Creates an area zone at the caster's location that increases magic damage dealt by players while within the domain.
	WEIGHTLESSNESS,    //A spell that affects only the caster. Temporarily increases jump height and massively decreases fall damage taken.
	INVISIBILITY,    //A spell that affects only the caster. Temporarily provides invisibility to the user.
	REJUVINATION,    //A spell that affects only the caster. Temporarily provides Regeneration 3 to the user. (5 seconds?)
	//RANK 1 COMBINATION SPELLS (OFFENSE)
	FIREBLAST,    //(FIREBALL+FIREBALL) A stronger fireball spell with increased damage and range.
	HOWLINGGALE,    //(WINDBLAST+WINDBLAST) An improved wind spell with massively increased knockback, and wider AOE. Also deals a small amount of damage.
	ARCANEMISSILE,    //(ARCANEBOLT+ARCANEBOLT) A single target spell. Initially weaker than ARCANEBOLT, but successful hits increase the damage of further ARCANEMISSILES by 10% temporarily. Further hits refresh the duration and stack, up to 200% base damage. (Starts at about 50% the damage of ARCANEBOLT).
	FREEZINGCLOUD,    //(ICYWINDS+ICYWINDS) Extremely wide area AOE, low damage, reduces movement speed. (This may be underpowered on release?).
	//RANK 2 COMBINATION SPELLS (OFFENSE)
	TOXICROOTS,    //(POISONOUSHEX+ENTANGLINGROOTS) A single target spell. Applies Poison 5 to the target, and roots them for 20 seconds. Shares diminishing effects with ENTANGLINGROOTS. Deals minor damage (Same as ARCANEBOLT).
	ARCANEBARRAGE,    //(ARCANEMISSILE+ARCANEBOLT) A channeled spell that fires a barrage of arcane arrows at a target. Caster's spellpower temporarily rises during the duration of the channel, causing each successive arrow to deal increasing damage.
	NOVA,    //(DETONATE+RADIANCE) An instantaneous spell centered on the caster with a circular AOE radius. Medium range, and deals massive damage, with a very long cooldown. (30 minutes anyone?)
	//RANK 3 COMBINATION SPELLS (OFFENSE)
	ANNIHILATE,    //(NOVA+ARCANEBARRAGE) A CHANNELED SPELL WITH INFINITE DURATION. The caster is cannot move during the channel, and each successive hit deals increasing damage. Increases damage taken by 50%. 
	CHANGESTATE_SPELLEFFECT_FORM_BARBEQUE_BIRD,    //PHOENIXDRIVE. The caster temporarily gains the aspect of the phoenix. While active, the caster periodically pulses flame damage, and gains a single offensive spell to use, Inferno.
		INFERNO,    //An extremely powerful fireball with small AOE damage. Only accessible while under the effects of PHOENIXDRIVE.
	
	//RANK 1 COMBINATION SPELLS (UTILITY)
	SOOTHINGLIGHT,    //(LIGHTHEAL+LIGHTHEAL) A significantly stronger self target healing spell than LIGHTHEAL. Heals 35HP
	RESTORATIVELIGHTS,    //(LIGHTHEAL+MAGICDOMAIN) A domain class spell. Heals 5HP per second for 4 seconds to all party members in an area.
	
	//RANK 2 COMBINATION SPELLS (UTILITY)
	GRAVITYWELL,    //(MAGICDOMAIN+ICYWINDS) A domain class spell. Temporarily slows all hostile targets in an area. 
	//RANK 3 COMBINATION SPELLS (UTILITY)
	LOCKDOWN,    //(GRAVITYWELL+?) A domain class spell. Temporarily prevents all non-ability movement movement in an area. Both players and monsters take 100% increased damage for the duration.
	
}