package sig.plugin.TwosideKeeper.HelperStructures;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class CustomPotion extends CustomItem{
	
	int max_amplifier;
	int min_amplifier;
	List<PotionEffect> effects;

	public CustomPotion(ItemStack item,List<PotionEffect> effects, int min_amplifier, int max_amplifier) {
		super(item);
		this.min_amplifier=min_amplifier;
		this.max_amplifier=max_amplifier;
		this.effects=effects;
	}
	
	public ItemStack getItemStack(int amt) {
		ItemStack temp = item.clone();
		temp.setAmount(amt);
		if (temp.getItemMeta() instanceof PotionMeta) {
			PotionMeta pm = (PotionMeta)temp.getItemMeta();
			for (int i=0;i<effects.size();i++) {
				PotionEffect pe = effects.get(i);
				pm.addCustomEffect(new PotionEffect(pe.getType(),pe.getDuration(),(int)((Math.random()*(max_amplifier-min_amplifier)))+min_amplifier),true);
			}
			temp.setItemMeta(pm);
		}
		return temp;
	}
	
	public ItemStack getItemStack() {
		return this.getItemStack(1);
	}

}
