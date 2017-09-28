package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import sig.plugin.TwosideKeeper.TwosideKeeper;
import sig.plugin.TwosideKeeper.HelperStructures.Book;
import sig.plugin.TwosideKeeper.HelperStructures.Common.GenericFunctions;

public class BookUtils {
	public static void GiveBookToPlayer(Player p,Book book) {
		File file = book.getBookFile();
		if (file.exists()) {
			ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta meta = (BookMeta)bookItem.getItemMeta();
			String[] contents = FileUtils.readFromFile(book.getBookFilepath());
			List<String> bookContents = new ArrayList<String>();
			for (int i=0;i<contents.length;i++) {
				bookContents.add(ChatColor.translateAlternateColorCodes('&', contents[i]).replace("\\n", "\n"));
			}
			//meta.setTitle(bookContents.remove(0));
			meta.setDisplayName(bookContents.remove(0));
			meta.setAuthor(bookContents.remove(0));
			meta.setPages(bookContents);
			bookItem.setItemMeta(meta);
			GenericFunctions.giveItem(p, bookItem);
		} else {
			try {
				TwosideKeeper.log("WARNING! Book "+book.name()+" does not exist! Create a file in "+file.getCanonicalPath(), 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
