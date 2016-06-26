package sig.plugin.TwosideKeeper.HelperStructures;

public enum ServerType {
	MAIN(0,""), //The main server. All announcements are made to Discord.
	TEST(1,"Test"), //The test server. Reload and shutdown announcements are not made to Discord. Only restarts will be announced.
	QUIET(2,"Private"); //No announcements to Discord are made.
	
	int val;
	String name;
	
	ServerType(int val, String name) {
		this.val=val;
		this.name=name;
	}
	
	public int GetValue() {
		return this.val;
	}
	public static ServerType GetTypeFromValue(int val) {
		for (int i=0;i<ServerType.values().length;i++) {
			if (ServerType.values()[i].GetValue()==val) {
				return ServerType.values()[i];
			}
		}
		return ServerType.TEST; //Return the default if none exists.
	}
	public String GetServerName() {
		if (this.name.length()>0) {
			return this.name+" ";
		} else {
			return this.name;
		}
	}
}
