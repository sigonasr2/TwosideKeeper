package sig.plugin.TwosideKeeper.HelperStructures.Common;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Recipe;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class JobRecipe {
	String name = "";
	Recipe rec;
	
	public JobRecipe(String name, Recipe rec) {
		this.name=name;
		this.rec=rec;
	}
	
	public static void InitializeJobRecipes() {
		Map<String,Recipe> newrecipes = aPlugin.API.getAddedRecipes();
		for (String namer : newrecipes.keySet()) {
			Recipe r = newrecipes.get(namer);
			TwosideKeeper.jobrecipes.add(new JobRecipe(namer,r));
		}
	}
}
