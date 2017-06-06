package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

public class TextUtils {

	public static ChatColor RandomColor() {
		ChatColor[] colors = {ChatColor.AQUA,
				ChatColor.BLACK,
				ChatColor.BLUE,ChatColor.DARK_AQUA,
				ChatColor.DARK_BLUE,ChatColor.DARK_GRAY,
				ChatColor.DARK_GREEN,ChatColor.DARK_PURPLE,
				ChatColor.DARK_RED,ChatColor.GOLD,
				ChatColor.GRAY,ChatColor.GREEN,
				ChatColor.LIGHT_PURPLE,ChatColor.RED,
				ChatColor.WHITE,ChatColor.YELLOW};
		
		return colors[((int)(Math.random()*colors.length))];
	}


	public static ChatColor RandomDarkColor() {
		ChatColor[] choices = new ChatColor[]{ChatColor.DARK_AQUA,ChatColor.DARK_BLUE,ChatColor.DARK_GRAY,ChatColor.DARK_GREEN,ChatColor.DARK_PURPLE,ChatColor.DARK_RED,ChatColor.GOLD};
		return choices[(int)(Math.random()*choices.length)];
	}
	
	public static void GenerateListOfItems(List<? extends Object> items, StringBuilder str) {
		for (int i=0;i<items.size();i++) {
			if (i==0) {
				str.append(items.get(i));
			} else {
				if (items.size()==2) {
					str.append(" and "+items.get(i));
				} else {
					if (i==items.size()-1) {
						str.append(", and "+items.get(i));
					} else {
						str.append(", "+items.get(i));
					}
				}
			}
		}
	}
	
	public static ChatColor GetColorBasedOnPercent(double pct) {
		if (pct>0.75) {
			return ChatColor.DARK_GREEN;
		} else
		if (pct>0.5) {
			return ChatColor.GREEN;
		} else
		if (pct>0.33) {
			return ChatColor.YELLOW;
		} else
		if (pct>0.25) {
			return ChatColor.GOLD;
		} else
		if (pct>0.1) {
			return ChatColor.RED;
		} else
		{
			return ChatColor.DARK_RED;
		}
	}
	
	public static String outputHashmap(HashMap map) {
		StringBuilder builder = new StringBuilder();
		for (Object o : map.keySet()) {
			Object val = map.get(o);
			builder.append(o.toString()+": ");
			if (val instanceof List) {
				builder.append("\n");
				boolean first=true;
				for (Object obj : (List)val) {
					if (first) {
						builder.append("  "+obj.toString());
					} else {
						builder.append("\n  "+obj.toString());
					}
				}
			} else 
			if (val instanceof Map) {
				builder.append("\n");
				boolean first=true;
				for (Object obj : ((Map) val).keySet()) {
					if (first) {
						builder.append("  "+obj.toString()+": "+((Map) val).get(obj).toString());
					} else {
						builder.append("\n  "+obj.toString()+": "+((Map) val).get(obj).toString());
					}
				}
			} else {
				builder.append(val.toString());
			}
			builder.append("\n");
		}
		return builder.toString();
	}


	public static boolean hasNoSpaceBeforeAndAfter(String str1, String str2, List<String> baselist) {
		//int pos = str
		for (int i=0;i<baselist.size();i++) {
			String s = baselist.get(i);
			if (s.contains(str1) && s.contains(str2)) {
				int pos1 = s.indexOf(str1);
				int pos2 = s.indexOf(str2);
				if (pos1>0 || pos2+str2.length()<s.length()) {
					return false;
				}
			}
		}
		return true;
	}
}
