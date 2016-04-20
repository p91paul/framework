package applica.framework;

import applica.framework.modules.CommandLineParser;
import applica.framework.modules.Modules;
import org.springframework.util.StringUtils;

import java.util.Properties;

public class Applica {

    public static final String VERSION = "1.4-SNAPSHOT";

    public static String frameworkHome = null;
    public static String javaHome = null;
    public static String mavenHome = null;

    public static void main(String[] args) {
        frameworkHome = System.getenv("APPLICAFRAMEWORK_HOME");
        if (!StringUtils.hasLength(frameworkHome)) {
            System.err.println("Please set APPLICAFRAMEWORK_HOME environment variable");
            System.exit(1);
            return;
        }

        javaHome = System.getenv("JAVA_HOME");
        if (!StringUtils.hasLength(frameworkHome)) {
            System.err.println("Please set JAVA_HOME environment variable");
            System.exit(1);
            return;
        }

        mavenHome = System.getenv("M2_HOME");
        if (!StringUtils.hasLength(mavenHome)) {
            System.err.println("Please set M2_HOME environment variable");
            System.exit(1);
            return;
        }

        Modules.instance().scan();

        CommandLineParser parser = new CommandLineParser();
        try {
            parser.parse(args);

            if (parser.getCommand().equals("version")) {
                System.out.println("Applica Framework version " + VERSION);
            } else if (parser.getCommand().equals("help")) {
                printUsage();
            } else {
                Modules.instance().call(parser.getCommand(), parser.getProperties());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printUsage() {
        System.out.println("usage");
    }

}
