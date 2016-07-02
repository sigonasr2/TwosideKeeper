package sig.plugin.TwosideKeeper.HelperStructures;

import org.bukkit.Material;

public class LootStructure {
	Material mat;
	boolean hardened_item;
	int amt;
	ArtifactItem art;
	
	public LootStructure(Material mat, boolean ishardened) {
		this.mat=mat;
		this.hardened_item=ishardened;
		this.amt=1;
		this.art=null;
	}
	public LootStructure(Material mat, int amt) {
		this.mat=mat;
		this.amt=amt;
		this.hardened_item=false;
		this.art=null;
	}
	public LootStructure(Material mat) {
		this.mat=mat;
		this.amt=1;
		this.hardened_item=false;
		this.art=null;
	}
	public LootStructure(ArtifactItem art, int amt) {
		this.mat=null;
		this.amt=1;
		this.hardened_item=false;
		this.art=art;
	}
	
	public Material GetMaterial() {
		return mat;
	}
	public boolean GetHardened() {
		return hardened_item;
	}
	public int GetAmount() {
		return amt;
	}
	public ArtifactItem GetArtifact() {
		return art;
	}
}
