package org.springframework.boot.logging;

import java.util.List;
import java.util.function.Function;

class DelegatingLoggingSystemFactory implements LoggingSystemFactory {

    private final Function<ClassLoader, List<LoggingSystemFactory>> delegates;

    public DelegatingLoggingSystemFactory(Function<ClassLoader, List<LoggingSystemFactory>> delegates) {
        this.delegates = delegates;
    }

    @Override
    public LoggingSystem getLoggingSystem(ClassLoader classLoader) {
        return new NoInitializeLoggingSystem(classLoader);
    }

}
