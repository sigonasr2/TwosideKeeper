package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	public static String[] readFromFile(String filename) {
		File file = new File(filename);
		//System.out.println(file.getAbsolutePath());
		List<String> contents= new ArrayList<String>();
		if (file.exists()) {
			try(
					FileReader fw = new FileReader(filename);
				    BufferedReader bw = new BufferedReader(fw);)
				{
					String readline = bw.readLine();
					do {
						if (readline!=null) {
							//System.out.println(readline);
							contents.add(readline);
							readline = bw.readLine();
						}} while (readline!=null);
					fw.close();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return contents.toArray(new String[contents.size()]);
	}
}
