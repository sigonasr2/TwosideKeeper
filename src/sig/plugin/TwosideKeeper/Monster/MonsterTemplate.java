package sig.plugin.TwosideKeeper.Monster;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.entity.EntityType;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.StringConverter;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.FileUtils;
import sig.plugin.TwosideKeeper.HelperStructures.Utils.TextUtils;

public class MonsterTemplate {
	public static HashMap<String,MonsterTemplateProperty> masterKeyMap = new HashMap<String,MonsterTemplateProperty>(); 
	private HashMap<String,MonsterTemplateProperty> keyMap = new HashMap<String,MonsterTemplateProperty>(); 
	
	
	private String[] filedata;
	
	public static void InitializeMasterMonsterTemplateKeyMap() {
		masterKeyMap.put("name",new MonsterTemplateProperty("name",String.class));
		masterKeyMap.put("hp",new MonsterTemplateProperty("hp",Double.class));
		masterKeyMap.put("entity",new MonsterTemplateProperty("entity",EntityType.class));
		masterKeyMap.put("outlinecolor",new MonsterTemplateProperty("outlinecolor",Color.class));
		masterKeyMap.put("boss",new MonsterTemplateProperty("boss",Boolean.class));
		masterKeyMap.put("movespdmult",new MonsterTemplateProperty("movespdmult",Double.class));
		masterKeyMap.put("damage",new MonsterTemplateProperty("damage",Double.class));
		masterKeyMap.put("damageReduction",new MonsterTemplateProperty("damageReduction",Double.class));
		masterKeyMap.put("lifesteal",new MonsterTemplateProperty("lifesteal",Double.class));
		masterKeyMap.put("critChance",new MonsterTemplateProperty("critChance",Double.class));
		masterKeyMap.put("critDamage",new MonsterTemplateProperty("critDamage",Double.class));
		masterKeyMap.put("armorPen",new MonsterTemplateProperty("armorPen",Double.class));
		masterKeyMap.put("lootrating",new MonsterTemplateProperty("lootrating",Integer.class));
		masterKeyMap.put("spawntype",new MonsterTemplateProperty("spawntype",SpawnType.class));
		masterKeyMap.put("spawnchance",new MonsterTemplateProperty("spawnchance",Double.class));
		masterKeyMap.put("timeToLive",new MonsterTemplateProperty("timeToLive",Integer.class));
	}
	
	public MonsterTemplate(File f) {
		filedata = FileUtils.readFromFile(f.getAbsolutePath());
		keyMap = (HashMap<String, MonsterTemplateProperty>)masterKeyMap.clone();
		//TwosideKeeper.log(TextUtils.outputHashmap(masterKeyMap),1);
		TwosideKeeper.log("=============", 1);
		//TwosideKeeper.log(TextUtils.outputHashmap(keyMap),1);
		for (int i=0;i<filedata.length;i++) {
			TwosideKeeper.log("Reading filedata "+filedata[i], 1);
			handleProperty(filedata[i]);
		}
		TwosideKeeper.log(TextUtils.outputHashmap(keyMap),1);
	}
	
	public Object getValue(String keyName) {
		MonsterTemplateProperty mt = keyMap.get(keyName);
		return StringConverter.getValue((String)mt.getValue(),mt.getClass());
	}
	
	private void handleProperty(String line) {
		int openBrace=-1;
		int closeBrace=-1;
		String property=null;
		for (int marker=0;marker<line.length();marker++) {
			if (OpenBraceFound(line, openBrace, marker)) {
				TwosideKeeper.log("  Found Open Brace @ "+marker, 1);
				openBrace = marker;
			} else
			if (CloseBraceFound(line, openBrace, closeBrace, marker)) {
				TwosideKeeper.log("  Found Close Brace @ "+marker, 1);
				closeBrace = marker;
			}
			
			if (BothBracesFound(openBrace, closeBrace)) {
				property = GetProperty(line, openBrace,closeBrace);
				TwosideKeeper.log("Checking property ["+property+"]", 1);

				String val = "";
				if (property!=null) {
					for (int marker2=closeBrace+1;marker2<line.length();marker2++) {
						if (line.charAt(marker2)!='[') {
							val += line.charAt(marker2);
						} else {
							addValueToMap(property,val);
							marker = marker2;
							openBrace = marker;
							closeBrace = -1;
							property=null;
							break;
						}
						if (marker2+1==line.length()) {
							addValueToMap(property,val);
							marker = marker2;
							openBrace = -1;
							closeBrace = -1;
							property=null;
							break;
						}
					}
				}
			}
		}
	}

	private void addValueToMap(String propertyName, String value) {
		if (keyMap.containsKey(propertyName)) {
			MonsterTemplateProperty prop = keyMap.get(propertyName);
			prop.setValue(value);
			keyMap.put(propertyName,prop);
		} else {
			TwosideKeeper.log("WARNING! Key ["+propertyName+"] does not exist!", 1);
		}
	}

	private String GetProperty(String line, int openBrace, int closeBrace) {
		return line.substring(openBrace+1, closeBrace);
	}

	public boolean BothBracesFound(int openBrace, int closeBrace) {
		return openBrace>=0 && closeBrace>=0;
	}

	public boolean CloseBraceFound(String line, int openBrace, int closeBrace, int marker) {
		return line.charAt(marker)==']' && openBrace>=0 && closeBrace==-1;
	}

	public boolean OpenBraceFound(String line, int openBrace, int marker) {
		return line.charAt(marker)=='[' && openBrace==-1;
	}
}

class MonsterTemplateProperty {
	String keyName;
	Object val;
	Class<? extends Object> c;
	public MonsterTemplateProperty(String key, Class<? extends Object> c) {
		this.keyName=key;
		this.c = c;
	}
	
	public void setValue(Object val) {
		this.val=val;
	}
	
	public Object getValue() {
		return val;
	}
	
	public String toString() {
		return "["+keyName+";"+"("+c.getSimpleName()+")"+val+"]";
	}
}

enum SpawnType {
	ENVIRONMENTAL, //Randomly (Checks for spawns every second)
	FRIENDLY, //Spawn with friendly mobs
	NORMALMOB, //Spawn with regular mobs
	DISTANCE, //Farther from home = Higher chance
	UNDERGROUND, //Farther down = Higher chance
}