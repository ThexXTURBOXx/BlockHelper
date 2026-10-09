package mcp.mobius.waila.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import net.minecraft.src.mod_BlockHelper;

public final class WailaExceptionHandler {

    public static boolean printAll = false;

    private static final Set<String> errs = new HashSet<String>();

    private WailaExceptionHandler() {
        throw new UnsupportedOperationException();
    }

    public static void handleErr(Throwable t, Class<?> context) {
        handleErr(t, context.getName());
    }

    public static void handleErr(Throwable t, String context) {
        if (printAll || errs.add(context)) {
            //Throwable working = t;

            mod_BlockHelper.LOG.log(Level.WARNING, "Caught unhandled exception [" + context + "]: ", t);

            // I want the full details
            /*while (working != null) {
                if (working != t) {
                    mod_BlockHelper.LOG.log(Level.WARNING, "Caused by: " + working);
                }
                for (StackTraceElement elem : working.getStackTrace()) {
                    mod_BlockHelper.LOG.log(
                            Level.WARNING,
                            elem.getClassName() + "." + elem.getMethodName() + ":" + elem.getLineNumber());
                    if (working == t && elem.getClassName().contains("waila")) break;
                }

                working = working.getCause();
            }*/
        }
    }

}
