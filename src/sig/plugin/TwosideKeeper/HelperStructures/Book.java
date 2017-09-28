package sig.plugin.TwosideKeeper.HelperStructures;

import java.io.File;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public enum Book {
	BEGINNERSGUIDE("NewPlayerGuide.txt"),
	COMMANDGUIDE("CommandGuide.txt"),
	STRIKERGUIDE("StrikerGuide.txt"),
	DEFENDERGUIDE("DefenderGuide.txt"),
	RANGERGUIDE("RangerGuide.txt"),
	SLAYERGUIDE("SlayerGuide.txt"),
	BARBARIANGUIDE("BarbarianGuide.txt"),
	ADVENTURERGUIDE("AdventurerGuide.txt");
	
	String fileLoc;
	
	Book(String loc) {
		this.fileLoc=loc;
	}
	
	public File getBookFile() {
		return new File(TwosideKeeper.plugin.getDataFolder()+"/books/"+this.fileLoc);
	}
	
	public String getBookFilepath() {
		return TwosideKeeper.plugin.getDataFolder()+"/books/"+this.fileLoc;
	}
}
