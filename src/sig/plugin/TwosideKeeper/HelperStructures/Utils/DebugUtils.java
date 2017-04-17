package sig.plugin.TwosideKeeper.HelperStructures.Utils;

import sig.plugin.TwosideKeeper.TwosideKeeper;

public class DebugUtils {
	
	public static void showStackTrace() {
		StackTraceElement[] stacktrace = new Throwable().getStackTrace();
		StringBuilder stack = new StringBuilder("Mini stack tracer:");
		for (int i=0;i<Math.min(10, stacktrace.length);i++) {
			stack.append("\n"+stacktrace[i].getClassName()+": **"+stacktrace[i].getFileName()+"** "+stacktrace[i].getMethodName()+"():"+stacktrace[i].getLineNumber());
		}
		TwosideKeeper.log("Trace:"+stack, 0);
	}
}
