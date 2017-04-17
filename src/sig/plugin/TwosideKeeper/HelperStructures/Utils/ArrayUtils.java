package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class ArrayUtils {
	public static String toString(Object[] items) {
		StringBuilder string = new StringBuilder();
		boolean first=false;
		for (Object i : items) {
			if (i!=null) {
				if (!first) {
					string.append(i.toString());
					first=true;
				} else {
					string.append(","+i.toString());
				}
			}
		}
		return string.toString();
	}

	public static String[] combineArguments(String[] args) {
		List<String> newargs = new ArrayList<String>();
		String collective = "";
		Character lookingfor = ' ';
		for (int i=0;i<args.length;i++) {
			if ((args[i].charAt(0)=='\"' ||
					args[i].charAt(0)=='\'') &&
					collective.length()==0) {
				collective = args[i].substring(Math.min(args[i].length(),1), args[i].length());
				lookingfor = args[i].charAt(0);
			} else
			if (collective.length()>0) {
				if (args[i].charAt(args[i].length()-2)!='\\' && args[i].charAt(args[i].length()-1)==lookingfor) {
					collective = collective + " " + args[i].substring(0, Math.max(0,args[i].length()-1));
					newargs.add(collective);
					collective = "";
					lookingfor = ' ';
				} else {
					collective = collective + " " + args[i];
					TwosideKeeper.log(collective, 0);
				}
			} else {
				newargs.add(args[i]);
			}
		}
		return newargs.toArray(new String[newargs.size()]);
	}
}
