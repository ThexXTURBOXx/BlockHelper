package mcp.mobius.waila.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.mod_BlockHelper;

public final class WailaExceptionHandler {

    private WailaExceptionHandler() {
        throw new UnsupportedOperationException();
    }

    private static final List<String> errs = new ArrayList<String>();

    public static void handleErr(Throwable t, String context, ITaggedList<String, String> currenttip) {
        if (!errs.contains(context)) {
            errs.add(context);

            for (StackTraceElement elem : t.getStackTrace()) {
                mod_BlockHelper.LOG.log(Level.WARNING, String.format("%s.%s:%s", elem.getClassName(),
                        elem.getMethodName(), elem.getLineNumber()));
                if (elem.getClassName().contains("waila")) break;
            }

            mod_BlockHelper.LOG.log(Level.WARNING, String.format("Caught unhandled exception : [%s] %s", context, t));
        }
        if (currenttip != null)
            currenttip.add("<ERROR>");
    }

}
