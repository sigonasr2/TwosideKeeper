package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

//NOT USED.
public class ArtifactAbilityGroup {
	public static HashMap<ArtifactAbility,List<ArtifactAbilityGroup>> abilitiesmap = new HashMap();
	List<ArtifactAbility> abilitylist;
	List<Material> itemlist;
	public ArtifactAbilityGroup(Material[] itemlist,ArtifactAbility...abilities) {
		this.abilitylist = new ArrayList<ArtifactAbility>();
		if (abilities!=null) {
			for (int i=0;i<abilities.length;i++) {
				this.abilitylist.add(abilities[i]);
			}
		}
		this.itemlist = new ArrayList<Material>();
		if (itemlist!=null) {
			for (int i=0;i<itemlist.length;i++) {
				this.itemlist.add(itemlist[i]);
			}
		}
	}
	public ArtifactAbilityGroup() {
		abilitylist = new ArrayList<ArtifactAbility>();
		itemlist = new ArrayList<Material>();
	}
	
	public boolean isInGroup(ArtifactAbility a) {
		if (abilitylist.contains(a)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addToGroup(ArtifactAbility a) {
		abilitylist.add(a);
	}
	public boolean itemIsInGroup(ItemStack item) {
		if (itemlist.contains(item.getType())) {
			return true;
		} else {
			return false;
		}
	}
}
