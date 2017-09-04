package sig.plugin.TwosideKeeper.PlayerStructures;

import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.PlayerStructure;
import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.DebugUtils;

public enum DefenderStance {
	AGGRESSION("Aggressive",false,false), //0
	BLOCK("Block",true,false), //2
	CHARGE("Charge",false,true), //1
	TANK("Tank",true,true); //3
	
	String name;
	boolean block;
	boolean sneak;
	
	DefenderStance(String realname, boolean requiresBlock, boolean requiresSneak) {
		this.name = realname;
		this.block = requiresBlock;
		this.sneak = requiresSneak;
	}
	
	public static DefenderStance getDefenderStance(Player p) {
		if (PlayerMode.getPlayerMode(p)==PlayerMode.DEFENDER) {
			PlayerStructure pd = PlayerStructure.GetPlayerStructure(p);
			for (DefenderStance check : DefenderStance.values()) {
				if (p.isSneaking()==check.sneak && pd.isBlocking()==check.block) {
					return check;
				}
			}
			TwosideKeeper.log("WARNING! We could not find a stance for Player "+p.getName()+"! None of the states match!", 1);
			DebugUtils.showStackTrace();
			return null;
		} else {
			TwosideKeeper.log("WARNING! We could not find a stance for Player "+p.getName()+"! They are not a Defender! You should probably remove this call.", 1);
			DebugUtils.showStackTrace();
			return null;
		}
	}
}
