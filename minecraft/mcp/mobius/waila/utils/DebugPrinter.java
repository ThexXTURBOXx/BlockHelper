package mcp.mobius.waila.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import mcp.mobius.waila.mod_BlockHelper;

public class DebugPrinter {

    private final Logger logger;
    private final int frequency;
    private final AtomicInteger current = new AtomicInteger();

    public DebugPrinter(int frequency) {
        this(mod_BlockHelper.LOG, frequency);
    }

    public DebugPrinter(Logger logger, int frequency) {
        this.logger = logger;
        this.frequency = frequency;
    }

    public void print(String message) {
        print(Level.FINEST, message);
    }

    public void print(Level level, String message) {
        if (current.getAndIncrement() % frequency == 0)
            logger.log(level, message);
    }

}
