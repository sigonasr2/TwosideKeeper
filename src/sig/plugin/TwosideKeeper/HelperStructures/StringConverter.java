package sig.plugin.TwosideKeeper.HelperStructures;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor.EntityType;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class StringConverter {
	public static Object getValue(String val, Class c) {
		switch (c.getSimpleName()) {
			case "Integer":{
				return Integer.parseInt(val);
			}
			case "Double":{
				return Double.parseDouble(val);
			}
			case "Boolean":{
				return Boolean.parseBoolean(val);
			}
			case "EntityType":{
				return EntityType.valueOf(val);
			}
			case "Color":{
				//return Color.(val);
			}
			default:{
				TwosideKeeper.log("WARNING! Could not convert String to Object class type "+c.getSimpleName(), 1);
				return val;
			}
		}
		//return Integer.parseInt(val);
	}
}
