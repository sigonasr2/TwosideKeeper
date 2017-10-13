package sig.plugin.TwosideKeeper.HelperStructures.Utils;
import java.lang.reflect.Field;

public class JavaUtils {
	public JavaUtils clone() {
		JavaUtils newpos = new JavaUtils();
		for (Field f : this.getClass().getDeclaredFields()) {
			if (ReflectUtils.isCloneable(f)) {
				try {
					f.set(newpos, f.get(this));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return newpos;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName()+"(");
		boolean first=false;
		for (Field f : this.getClass().getDeclaredFields()) {
			if (!ReflectUtils.isCloneable(f)) {
				if (!first) {
					try {
						sb.append(f.getName()+"="+f.get(this));
						first=true;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				} else {
					try {
						sb.append(","+f.getName()+"="+f.get(this));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
