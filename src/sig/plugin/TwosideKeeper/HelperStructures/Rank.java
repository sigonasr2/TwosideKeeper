package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Color;

/**
 * Rank structure
 * 
 * Defines a rank a player can be on the server.
 * Each rank has a color and a "canX" permission
 * for each permission they can do.
 * 
 * @author Joshua Sigona
 *
 */
public class Rank {
	public String name;
	public Color color;
	public boolean 
		canBreak,
		canBuild,
		canChat,
		canFly,
		canItemGive;
	/**
	 * Rank sets up a rank. You have to define specific permissions with 'true' and 'false' separately.
	 * @param name
	 * @param color
	 */
	public Rank(String name, Color color) {
		this.name = name;
		this.color = color;
		
		//Set default permissions.
		this.canBreak=this.canBuild=this.canChat=true;
		this.canFly=this.canItemGive=false;
	}
}
