package sig.plugin.TwosideKeeper.HelperStructures.Utils.Classes;

import java.io.File;
import java.io.FileFilter;

public class InstanceFilter implements FileFilter{

	@Override
	public boolean accept(File arg0) {
		if (arg0.getName().contains("Instance")) {
			return true;
		} else {
			return false;
		}
	}

}
