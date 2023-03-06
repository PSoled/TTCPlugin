package com.totalcraft.soled;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ConsoleFilter implements Filter {
        @Override
        public boolean isLoggable(LogRecord record) {
            return !record.getMessage().contains("lost connection");
        }

        public static void register(Logger logger) {
            logger.setFilter(new ConsoleFilter());
        }
    }

