package mcp.mobius.waila.utils.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class FMLLikeLogFormatter extends Formatter {

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String format(LogRecord record) {
        StringBuilder msg = new StringBuilder();
        msg.append(DATE_FORMAT.format(record.getMillis()));
        Level lvl = record.getLevel();
        if (lvl == Level.FINEST) {
            msg.append(" [FINEST] ");
        } else if (lvl == Level.FINER) {
            msg.append(" [FINER] ");
        } else if (lvl == Level.FINE) {
            msg.append(" [FINE] ");
        } else if (lvl == Level.INFO) {
            msg.append(" [INFO] ");
        } else if (lvl == Level.WARNING) {
            msg.append(" [WARNING] ");
        } else if (lvl == Level.SEVERE) {
            msg.append(" [SEVERE] ");
        } else {
            msg.append(" [").append(lvl.getLocalizedName()).append("] ");
        }

        msg.append(record.getMessage());
        msg.append(System.getProperty("line.separator"));
        Throwable thr = record.getThrown();
        if (thr != null) {
            StringWriter thrDump = new StringWriter();
            thr.printStackTrace(new PrintWriter(thrDump));
            msg.append(thrDump.toString());
        }

        return msg.toString();
    }

}
