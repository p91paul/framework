package applica.framework;

import applica.framework.editors.FileEditor;
import applica.framework.library.utils.FileWalker;
import applica.framework.library.utils.FileWalkerListener;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Applica {

    public static final String VERSION = "1.4-RELEASE";

    public static final String APPNAME_PH = "_APPNAME_";
    public static final String OPTION_VERSION = "version";
    public static final String OPTION_NEW = "new";

    public static void main(String[] args) {
        String frameworkHome = System.getenv("APPLICAFRAMEWORK_HOME");
        if (!StringUtils.hasLength(frameworkHome)) {
            System.err.println("Please set APPLICAFRAMEWORK_HOME environment variable");
            System.exit(1);
            return;
        }

        String[] appPaths = {
                "_APPNAME_"
        };

        Options options = new Options();
        options.addOption(OPTION_NEW, true, "Create a new app with specified name");
        options.addOption(OPTION_VERSION, false, "Displays framework version");

        CommandLineParser parser = new BasicParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption(OPTION_VERSION)) {
                System.out.println("Applica Framework version " + VERSION);
            } else if (commandLine.hasOption(OPTION_NEW)) {
                List<String> editableExtensions = Arrays.asList("java", "xml", "vm", "properties");

                String appName = commandLine.getOptionValue(OPTION_NEW);
                File root = new File("./" + appName);
                if (root.exists()) {
                    System.err.println("Directory " + appName + " already exists");
                    System.exit(2);
                    return;
                }

                root.mkdirs();

                for (String appPath : appPaths) {
                    FileWalker fileWalker = new FileWalker();
                    fileWalker.walk(String.format("%s/%s", frameworkHome, appPath), new FileWalkerListener() {
                        @Override
                        public void onFile(File directory, File file) {
                            String newDirPath = removeFirstSlash(directory.getAbsolutePath().replace(frameworkHome, "").replace(APPNAME_PH, appName));
                            File newDirectory = new File(newDirPath);
                            if (!newDirectory.exists()) {
                                newDirectory.mkdirs();
                                //System.out.println(String.format("Created directory %s", newDirPath));
                            }

                            String newPath = removeFirstSlash(file.getAbsolutePath().replace(frameworkHome, "").replace(APPNAME_PH, appName));
                            if (editableExtensions.contains(FilenameUtils.getExtension(newPath))) {
                                FileEditor editor = new FileEditor();
                                editor.setSource(file);
                                editor.setDestination(new File(newPath));
                                editor.setSearch(APPNAME_PH);
                                editor.setReplace(appName);
                                editor.save();
                            } else {
                                try { FileUtils.copyFile(file, new File(newPath)); } catch (IOException e) { }
                            }

                            System.out.println(newPath);
                        }

                        @Override
                        public void onDirectory(File file) {

                        }
                    });
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static String removeFirstSlash(String path) {
        if (path.startsWith("/")) {
            return path.substring(1);
        }

        return path;
    }


}
