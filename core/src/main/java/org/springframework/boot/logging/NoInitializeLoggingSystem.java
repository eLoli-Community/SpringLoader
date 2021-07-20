package org.springframework.boot.logging;

public class NoInitializeLoggingSystem extends Slf4JLoggingSystem{
    public NoInitializeLoggingSystem(final ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public void beforeInitialize() {
        //
    }

    @Override
    protected String[] getStandardConfigLocations() {
        return new String[0];
    }

    @Override
    protected void loadDefaults(final LoggingInitializationContext initializationContext, final LogFile logFile) {
        //
    }
}
