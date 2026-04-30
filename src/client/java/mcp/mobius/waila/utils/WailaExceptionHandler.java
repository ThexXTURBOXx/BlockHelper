package mcp.mobius.waila.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import mcp.mobius.waila.api.ITaggedList;
import net.minecraft.src.mod_BlockHelper;

public final class WailaExceptionHandler {

    private WailaExceptionHandler() {
        throw new UnsupportedOperationException();
    }

    private static final Set<String> errs = new HashSet<String>();

    public static void handleErr(Throwable t, Class<?> context, ITaggedList<String, String> currenttip) {
        handleErr(t, context.getName(), currenttip);
    }

    public static void handleErr(Throwable t, String context, ITaggedList<String, String> currenttip) {
        if (errs.add(context)) {
            Throwable working = t;

            while (working != null) {
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
            }

            mod_BlockHelper.LOG.log(Level.WARNING, "Caught unhandled exception: [" + context + "] " + t);
        }
        if (currenttip != null)
            currenttip.add("<ERROR>");
    }

}
