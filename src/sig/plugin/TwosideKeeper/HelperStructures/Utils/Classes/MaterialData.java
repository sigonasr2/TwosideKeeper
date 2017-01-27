package sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes;

import org.bukkit.Material;

public class MaterialData {
	Material type;
	Byte data;
	
	public MaterialData(Material type, Byte data) {
		this.type=type;
		this.data=data;
	}

	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public Byte getData() {
		return data;
	}

	public void setData(Byte data) {
		this.data = data;
	}
	
	
}
