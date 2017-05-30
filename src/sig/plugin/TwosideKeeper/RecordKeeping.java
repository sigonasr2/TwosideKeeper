package sig.plugin.TwosideKeeper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import sig.plugin.TwosideKeeper.HelperStructures.PlayerMode;

public class RecordKeeping {
	String name;
	List<Record> recordlist;
	Record leader;
	boolean reverse;
	
	public RecordKeeping(String displayName) {
		this.name=displayName;
		recordlist = new ArrayList<Record>();
		loadRecordsFromConfig();
		this.reverse=false;
	}
	public RecordKeeping(String displayName, boolean reverse) {
		this.name=displayName;
		recordlist = new ArrayList<Record>();
		loadRecordsFromConfig();
		this.reverse=reverse;
	}
	
	public String getName() {
		return name;
	}
	
	public void cleanup() {
		saveRecordsToConfig();
	}
	
	public void resetRecords() {
		recordlist.clear();
		leader=null;
		saveRecordsToConfig();
	}
	
	public void saveRecordsToConfig() {
		File file = new File(TwosideKeeper.plugin.getDataFolder()+"/records/"+ChatColor.stripColor(name)+".data");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try(
			FileWriter fw = new FileWriter(file, false);
		    BufferedWriter bw = new BufferedWriter(fw);)
		{
			for(int i=0;i<recordlist.size();i++) {
				bw.write(recordlist.get(i).getName()+","+recordlist.get(i).getScore()+","+recordlist.get(i).getMode().name());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadRecordsFromConfig() {
		File file = new File(TwosideKeeper.plugin.getDataFolder()+"/records/"+ChatColor.stripColor(name)+".data");

		if (file.exists()) {
			try(
					FileReader fw = new FileReader(file);
				    BufferedReader bw = new BufferedReader(fw);)
				{
					String readline = bw.readLine();
					int lines = 0;
					do {
						if (readline!=null) {
							lines++;
							String[] split = readline.split(",");
							String name = split[0];
							double score = Double.parseDouble(split[1]);
							PlayerMode mode = PlayerMode.valueOf(split[2]);
							recordlist.add(new Record(name,score,mode));
							readline = bw.readLine();
						}} while (readline!=null);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		sortRecords();
		if (recordlist.size()>0) {
			leader = recordlist.get(0);
		}
	}
	
	public void addRecord(String name, double score, PlayerMode mostusedmode) {
		for (Record r : recordlist) {
			if (r.getName().equalsIgnoreCase(name)) {
				if ((reverse && r.getScore()>score) || (!reverse && r.getScore()<score)) {
					DecimalFormat df = new DecimalFormat("0.00");
					Player p = Bukkit.getPlayer(name);
					if (p!=null) {
						p.sendMessage("Your score for "+ChatColor.YELLOW+this.name+ChatColor.RESET+" has improved! "+ChatColor.GRAY+""+ChatColor.STRIKETHROUGH+ChatColor.ITALIC+df.format(r.getScore())+""+ChatColor.RESET+" -> "+ChatColor.GREEN+df.format(score));
					}
					r.setScore(score);
					r.setMode(mostusedmode);
					sortRecords();
					DetermineIfNewHighScoreAchieved(r);
				}
				return;
			}
		}
		Record r = new Record(name,score,mostusedmode);
		recordlist.add(r);
		leader = recordlist.get(0);
		sortRecords();
		DetermineIfNewHighScoreAchieved(r);
	}
	
	public void addRecord(Player p, double score, PlayerMode mostusedmode) {
		addRecord(p.getName(),score,mostusedmode);
	}
	
	private void DetermineIfNewHighScoreAchieved(Record newRecordAdded) {
		Record currentRecord = recordlist.get(0);
		if (currentRecord.equals(newRecordAdded) && (recordlist.size()==1 || 
				leader.equals(currentRecord))) {
			DecimalFormat df = new DecimalFormat("0.00");
			Bukkit.broadcastMessage(newRecordAdded.getMode().getColor()+"("+newRecordAdded.getMode().getAbbreviation()+")"+ChatColor.RESET+ChatColor.GREEN+newRecordAdded.getName()+ChatColor.RESET+" has completed "+ChatColor.AQUA+ChatColor.BOLD+name+ChatColor.RESET+" with a score of "+ChatColor.YELLOW+ChatColor.BOLD+df.format(newRecordAdded.getScore()));
			aPlugin.API.discordSendRaw(newRecordAdded.getName()+" has completed **"+name+"** with a score of "+ChatColor.YELLOW+ChatColor.BOLD+df.format(newRecordAdded.getScore()));
		} else
		if (currentRecord.equals(newRecordAdded) && recordlist.size()>1) {
			Record recordbeat = recordlist.get(1);//Get the record we beat.
			DecimalFormat df = new DecimalFormat("0.00");
			Bukkit.broadcastMessage(newRecordAdded.getMode().getColor()+"("+newRecordAdded.getMode().getAbbreviation()+")"+ChatColor.RESET+ChatColor.GREEN+newRecordAdded.getName()+ChatColor.RESET+" has beat "+recordbeat.getMode().getColor()+"("+recordbeat.getMode().getAbbreviation()+")"+ChatColor.RESET+ChatColor.GREEN+recordbeat.getName()+ChatColor.RESET+" in "+ChatColor.AQUA+ChatColor.BOLD+name+ChatColor.RESET+" with a score of "+ChatColor.YELLOW+ChatColor.BOLD+df.format(newRecordAdded.getScore())+"!!");
			aPlugin.API.discordSendRaw("*("+newRecordAdded.getMode().getAbbreviation()+")*"+newRecordAdded.getName()+" has beat *("+recordbeat.getMode().getAbbreviation()+")*"+recordbeat.getName()+" in **"+name+"** with a score of "+ChatColor.YELLOW+ChatColor.BOLD+df.format(newRecordAdded.getScore())+"!!");
			leader = currentRecord;
		}
	}

	public void displayRecords(Player p) {
		displayRecords(p,Math.min(10,recordlist.size()));
	}
	
	public void displayRecords(Player p, int amtToShow) {
		if (recordlist.size()>0) {
			p.sendMessage(ChatColor.AQUA+"Records for "+ChatColor.BOLD+name);
			for (int i=0;i<amtToShow;i++) {
				p.sendMessage("  "+ChatColor.GRAY+(i+1)+"."+ChatColor.YELLOW+" "+recordlist.get(i).getScore()+"  -  "+recordlist.get(i).getMode().getColor()+"("+recordlist.get(i).getMode().getAbbreviation()+")"+ChatColor.RESET+ChatColor.GREEN+recordlist.get(i).getName());
			}
		} else {
			p.sendMessage(ChatColor.WHITE+"No Records set for "+ChatColor.BOLD+name+ChatColor.RESET+" yet!");
		}
	}
	
	public void announceRecords() {
		announceRecords(Math.min(10,recordlist.size()));
	}
	
	public void announceRecords(int amtToShow) {
		/*Bukkit.getServer().broadcastMessage(ChatColor.AQUA+"Records for "+ChatColor.BOLD+name);
		for (int i=0;i<amtToShow;i++) {
			Bukkit.getServer().broadcastMessage("  "+ChatColor.GRAY+(i+1)+"."+ChatColor.YELLOW+" "+recordlist.get(i).getScore()+"  -  "+recordlist.get(i).getName());
		}*/
		if (recordlist.size()>0) {
			aPlugin.API.discordSendRaw("Records for **"+name+"**:");
			//aPlugin.API.discordSendRaw("```");
			StringBuilder sb = new StringBuilder("```");
			for (int i=0;i<amtToShow;i++) {
				sb.append((i+1)+". "+recordlist.get(i).getScore()+"  -  ("+recordlist.get(i).getMode().getAbbreviation()+")"+recordlist.get(i).getName()+"\n");
			}
			sb.append("```");
			aPlugin.API.discordSendRaw(sb.toString());
		} else {
			aPlugin.API.discordSendRaw("*No Records for **"+name+"** yet*");
		}
	}

	private void sortRecords() {
		List<Record> sortedrecords = new ArrayList<Record>();
		while (recordlist.size()>0) {
			Record bestrecord = null;
			int slot = 0;
			for (int i=0;i<recordlist.size();i++) {
				Record rec = recordlist.get(i);
				if (bestrecord==null ||
						(reverse && rec.getScore()<bestrecord.getScore()) || (!reverse && rec.getScore()>bestrecord.getScore())) {
					bestrecord = rec;
					slot = i;
				}
			}
			sortedrecords.add(recordlist.remove(slot));
		}
		recordlist = sortedrecords;
	}
}
class Record{
	String name;
	double score;
	PlayerMode mode;
	Record(String name,double score,PlayerMode mode) {
		this.name=name;
		this.score=score;
		this.mode=mode;
	}
	String getName() {
		return name;
	}
	double getScore() {
		return score;
	}
	PlayerMode getMode() {
		return mode;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public void setMode(PlayerMode mode) {
		this.mode = mode;
	}
}
