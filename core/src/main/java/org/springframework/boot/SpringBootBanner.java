package org.springframework.boot;

import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

public class SpringBootBanner implements Banner {
    private static final String BANNER =
            "  ____             _               _                    _           \n" +
                    " / ___| _ __  _ __(_)_ __   __ _  | |    ___   __ _  __| | ___ _ __ \n" +
                    " \\___ \\| '_ \\| '__| | '_ \\ / _` | | |   / _ \\ / _` |/ _` |/ _ \\ '__|\n" +
                    "  ___) | |_) | |  | | | | | (_| | | |__| (_) | (_| | (_| |  __/ |   \n" +
                    " |____/| .__/|_|  |_|_| |_|\\__, | |_____\\___/ \\__,_|\\__,_|\\___|_|   \n" +
                    "       |_|                 |___/                                    \n";

    private static final String SPRING_BOOT = " :: Spring Boot :: ";
    private static final String APSARAS = " :: Spring Loader :: ";

    private static final int STRAP_LINE_SIZE = 68;

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
        printStream.print(BANNER);
        String version = SpringBootVersion.getVersion();
        version = (version != null) ? " (v" + version + ")" : "";
        StringBuilder padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE - (version.length() + SPRING_BOOT.length())) {
            padding.append(" ");
        }
        printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_BOOT, AnsiColor.DEFAULT, padding.toString(),
                AnsiStyle.FAINT, version));
        version = " (v1.0-SNAPSHOT)";
        padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE - (version.length() + APSARAS.length())) {
            padding.append(" ");
        }
        printStream.println(AnsiOutput.toString(AnsiColor.GREEN, APSARAS, AnsiColor.DEFAULT, padding.toString(),
                AnsiStyle.FAINT, version));
        printStream.println();
    }
}
