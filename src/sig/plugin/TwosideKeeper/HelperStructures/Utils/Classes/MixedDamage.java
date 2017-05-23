package sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes;

public class MixedDamage {
	double dmgcomponent;
	double truepctdmgcomponent;
	double truedmgcomponent;
	
	public MixedDamage(double dmg) {
		this.dmgcomponent=dmg;
		this.truepctdmgcomponent=0;
		this.truedmgcomponent=0;
	}
	public MixedDamage(double dmg,double truepctdmg) {
		this.dmgcomponent=dmg;
		this.truepctdmgcomponent=truepctdmg;
		this.truedmgcomponent=0;
	}
	public MixedDamage(double dmg,double truepctdmg,double truedmg) {
		this.dmgcomponent=dmg;
		this.truepctdmgcomponent=truepctdmg;
		this.truedmgcomponent=truedmg;
	}
	public static MixedDamage v(double dmg) {
		return new MixedDamage(dmg);
	}
	public static MixedDamage v(double dmg,double truepctdmg) {
		return new MixedDamage(dmg,truepctdmg);
	}
	public static MixedDamage v(double dmg,double truepctdmg,double truedmg) {
		return new MixedDamage(dmg,truepctdmg,truedmg);
	}
	public double getDmgComponent() {
		return dmgcomponent;
	}
	public double getTruePctDmgComponent() {
		return truepctdmgcomponent;
	}
	public double getTrueDmgComponent() {
		return truedmgcomponent;
	}

	public boolean hasTruePctDmgComponent() {
		return truepctdmgcomponent>0;
	}
	public boolean hasTrueDmgComponent() {
		return truedmgcomponent>0;
	}
}
