package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import java.util.Calendar;

public class TimeUtils {
	public static int GetCurrentDayOfWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}
}
