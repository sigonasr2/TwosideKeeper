package sig.plugin.TwosideKeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class AutoUpdatePlugin implements Runnable {
	List<Plugin> plugins;
	boolean restarting=false;
	org.bukkit.plugin.Plugin plug=null;
	
	public AutoUpdatePlugin(org.bukkit.plugin.Plugin plug) {
		plugins = new ArrayList<Plugin>();
		this.plug=plug;
	}
	
	@Override
	public void run() {
		FetchPlugins();
	}
	
	void FetchPlugins(){
		for (int i=0;i<plugins.size();i++) {
			try {
				FileUtils.copyURLToFile(new URL(plugins.get(i).url), new File(TwosideKeeper.filesave,"updates/"+plugins.get(i).name));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//After that's done, check the hash.
			FileInputStream file = null;
			try {
				file = new FileInputStream(new File(TwosideKeeper.filesave,"updates/"+plugins.get(i).name));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String md5 = null;
			try {
				md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if ((plugins.get(i).hash==null || !md5.equalsIgnoreCase(plugins.get(i).hash))) {
				//This plugin is different! Update the hash for it. Prepare for a restart of the server!
				final int ii=i;
				Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("TwosideKeeper"), new Runnable() {
					@Override
					public void run() {
						if (!TwosideKeeper.restarting_server) {
							if (Bukkit.getOnlinePlayers().size()!=0) {
								aPlugin.API.discordSendRawItalicized("The server has detected a new version of "+ChatColor.YELLOW+plugins.get(ii).name+". The server will restart in 3 minutes!");
								Bukkit.broadcastMessage("The server has detected a new version of "+ChatColor.YELLOW+plugins.get(ii).name+". The server will restart in 3 minutes!\n\n"+ChatColor.GRAY+ChatColor.ITALIC+"If all players leave, the update will occur immediately.");
							} else {
								aPlugin.API.discordSendRawItalicized("The server has detected a new version of "+ChatColor.YELLOW+plugins.get(ii).name+".");
								Bukkit.broadcastMessage("The server has detected a new version of "+ChatColor.YELLOW+plugins.get(ii).name+"."+ChatColor.GRAY+ChatColor.ITALIC+"If all players leave, the update will occur immediately.");
							}
							restarting=true;
							TwosideKeeper.restarting_server=true;
							//Save the new plugin hash.
						
						} else {
							aPlugin.API.discordSendRawItalicized("The server has detected a new version of "+ChatColor.YELLOW+plugins.get(ii).name+".");
							Bukkit.broadcastMessage("The server has detected a new version of "+ChatColor.YELLOW+plugins.get(ii).name+"."+ChatColor.GRAY+ChatColor.ITALIC+"If all players leave, the update will occur immediately.");
						}
						if (restarting) {
							TwosideKeeper.updateServer();
						}
				}},1);
				TwosideKeeper.log("New hash: "+md5, 2);
				plugins.get(i).hash = md5;
				SaveHash(plugins.get(i));
				
				//Move the file to the new location.
				/*try {
					FileUtils.copyFile(new File(TwosideKeeper.filesave,"updates/"+plugins.get(i).name),
							new File(TwosideKeeper.filesave,"../"+plugins.get(i).name+".jar"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		}
	}
	
	public void AddPlugin(String name, String url) {
		plugins.add(new Plugin(name,url));
	}
	
	public void LoadHash(Plugin pluginname) {
		//Read from the server config.
		File config = new File(TwosideKeeper.filesave,"hashes.data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		pluginname.hash = workable.getString(pluginname.name+"/HASH");
	}
	public void SaveHash(Plugin pluginname) {
		File config = new File(TwosideKeeper.filesave,"hashes.data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		workable.set(pluginname.name+"/HASH",pluginname.hash);
		try {
			workable.save(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void SaveAllHashes(List<Plugin> pluginlist) {
		File config = new File(TwosideKeeper.filesave,"hashes.data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		for (int i=0;i<pluginlist.size();i++) {
			workable.set(pluginlist.get(i).name+"/HASH",pluginlist.get(i).hash);
		}
		try {
			workable.save(config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Plugin {
	String name;
	String hash;
	String url;
	
	public Plugin(String name,String url) {
		this.name=name;
		this.url=url;
		this.hash=FetchHash(); //Try to fetch the hash.
	}
	
	public Plugin(String name,String hash,String url) {
		this.name=name;
		this.url=url;
		this.hash=hash;
	}
	
	public String FetchHash() {
		File config = new File(TwosideKeeper.filesave,"hashes.data");
		FileConfiguration workable = YamlConfiguration.loadConfiguration(config);
		return workable.getString(this.name+"/HASH");
	}
}
