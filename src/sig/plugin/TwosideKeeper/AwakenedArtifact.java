package sig.plugin.TwosideKeeper;

import org.bukkit.inventory.ItemStack;

public class AwakenedArtifact {
	public static ItemStack convertToAwakenedArtifact(ItemStack artifact) {
		ItemStack item = artifact.clone();
		item = Artifact.convert(item,false);
		GenericFunctions.
		return item;
	}
}
