package applica.framework;

import applica.framework.modules.Modules;
import org.apache.commons.cli.*;
import org.springframework.util.StringUtils;

import java.util.Properties;

public class Applica {

    public static final String VERSION = "1.4-RELEASE";

    public static String frameworkHome = null;

    public static void main(String[] args) {
        frameworkHome = System.getenv("APPLICAFRAMEWORK_HOME");
        if (!StringUtils.hasLength(frameworkHome)) {
            System.err.println("Please set APPLICAFRAMEWORK_HOME environment variable");
            System.exit(1);
            return;
        }

        Modules.instance().scan();

        Options options = new Options();
        options.addOption("h", "help", false, "This help");
        options.addOption("v", "version", false, "Displays framework version");
        options.addOption("c", "command", true, "The command to execute");
        options.addOption(OptionBuilder
                .withArgName("parameter=value")
                .hasArgs(2)
                .withValueSeparator()
                .withDescription("Specify a parameter value")
                .create("D"));

        CommandLineParser parser = new BasicParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption("version")) {
                System.out.println("Applica Framework version " + VERSION);
            } else if (commandLine.hasOption("help")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("applica --command module:action -Dparameter=value. Use applica --command modules:show to display all modules", options);
            } else {
                Properties properties = commandLine.getOptionProperties("D");
                Modules.instance().call(commandLine.getOptionValue("c"), properties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
