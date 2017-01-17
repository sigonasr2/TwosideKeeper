package sig.plugin.TwosideKeeper.Logging;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

public class HeartbeatLogger {
	LinkedHashMap<String,Average> values = new LinkedHashMap<String,Average>();
	
	public HeartbeatLogger() {
		
	}
	
	public void AddEntry(String name, int value) {
		if (values.containsKey(name)) {
			Average avg = values.get(name);
			avg.add(value);
		} else {
			Average avg = new Average();
			avg.add(value);
			values.put(name, avg);
		}
	}
	
	public String outputReport() {
		StringBuilder stringy = new StringBuilder("");
		for (Entry<String,Average> ent : values.entrySet()) {
			Average avg = ent.getValue();
			String str = ent.getKey();
			DecimalFormat df = new DecimalFormat("0.00");
			stringy.append(ChatColor.AQUA+str+": ["+(avg.trend>0?"^":avg.trend==0?"-":"v")+"]"+ChatColor.YELLOW+df.format(avg.avg/1000000d)+"ms ["+avg.count+"]"+ChatColor.RED+"H:"+df.format(avg.high/1000000d)+"ms"+ChatColor.GREEN+" L:"+df.format(avg.low/1000000d)+"ms "+ChatColor.LIGHT_PURPLE+"(Last: "+df.format(avg.last/1000000d)+"ms)\n"+ChatColor.RESET);
		}
		return stringy.toString();
	}
}
