package sig.plugin.TwosideKeeper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Removes an object from a collection. For thread safe delaying purposes.
 *
 */
public class ThreadSafeCollection implements Runnable{
	
	Collection<? extends Object> obj = null;
	Object removal = null;
	Set<? extends Object> obj2 = null;
	List<? extends Object> obj3 = null;
	HashMap<? extends Object,? extends Object> obj4 = null;
	
	public ThreadSafeCollection(Collection<? extends Object> obj, Object remove) {
		this.obj=obj;
		this.removal=remove;
	}

	public ThreadSafeCollection(Set<? extends Object> obj, Object remove) {
		this.obj2=obj;
		this.removal=remove;
	}
	
	public ThreadSafeCollection(List<? extends Object> obj, Object remove) {
		this.obj3=obj;
		this.removal=remove;
	}
	
	public ThreadSafeCollection(HashMap<? extends Object,? extends Object> obj, Object remove) {
		this.obj4=obj;
		this.removal=remove;
	}

	@Override
	public void run() {
		if (this.obj!=null) {
			this.obj.remove(this.removal);
		} else 
		if (this.obj2!=null) {
			this.obj2.remove(this.removal);
		} else
		if (this.obj3!=null) {
			this.obj3.remove(removal);
		} else
		if (this.obj4!=null) {
			this.obj4.remove(removal);
		}
	}

}
