package applica.framework.modules;

import applica.framework.AppContext;
import applica.framework.Applica;
import applica.framework.SystemUtils;
import applica.framework.annotations.Action;
import applica.framework.editors.FileEditor;
import applica.framework.library.utils.FileWalker;
import applica.framework.library.utils.FileWalkerListener;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 01/10/14
 * Time: 19:02
 */
@applica.framework.annotations.Module("project")
public class ProjectModule implements Module {

    public static final String APPNAME_PH = "_APPNAME_";

    private static String removeFirstSlash(String path) {
        if (path.startsWith("/")) {
            return path.substring(1);
        }

        return path;
    }

    @Action(value = "create", description = "Create a new app with specified name")
    public void newProject(Properties properties) {
        List<String> editableExtensions = Arrays.asList("java", "xml", "vm", "properties", "manifest");

        Assert.isTrue(properties.containsKey("name"), "missing name. Specify -Dname=<value>");
        Assert.isTrue(properties.containsKey("archetype"), "missing archetype. Specify -Darchetype=<value>");

        String appName = (String) properties.get("name");
        String archetype = (String) properties.get("archetype");

        File root = new File("./" + appName);
        if (root.exists()) {
            System.err.println("Directory " + appName + " already exists");
            System.exit(2);
            return;
        }

        root.mkdirs();

        String[] appPaths = {
                SystemUtils.multiplatformPath(String.format("archetypes/%s/_APPNAME_", archetype))
        };

        for (String appPath : appPaths) {
            FileWalker fileWalker = new FileWalker();
            fileWalker.walk(String.format("%s/%s", Applica.frameworkHome, appPath), new FileWalkerListener() {
                @Override
                public void onFile(File directory, File file) {
                    String newDirPath = removeFirstSlash(directory.getAbsolutePath()
                            .replace(Applica.frameworkHome, "")
                            .replace(SystemUtils.multiplatformPath(String.format("archetypes/%s/", archetype)), "")
                            .replace(APPNAME_PH, appName));
                    File newDirectory = new File(newDirPath);
                    if (!newDirectory.exists()) {
                        newDirectory.mkdirs();
                        //System.out.println(String.format("Created directory %s", newDirPath));
                    }

                    String newPath = removeFirstSlash(file.getAbsolutePath()
                            .replace(Applica.frameworkHome, "")
                            .replace(SystemUtils.multiplatformPath(String.format("archetypes/%s/", archetype)), "")
                            .replace(APPNAME_PH, appName));
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

    @Action(value = "build", description = "Build the project")
    public void build(Properties properties) {
        try {
            System.out.println(String.format("Building %s...", AppContext.current().getAppName()));
            Process process = Runtime.getRuntime().exec(getMaven() + " package");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action(value = "clean", description = "Clean the project")
    public void clean(Properties properties) {
        try {
            System.out.println(String.format("Cleaning %s...", AppContext.current().getAppName()));
            Process process = Runtime.getRuntime().exec(getMaven() + " clean");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMaven() {
        return SystemUtils.multiplatformPath(String.format(FilenameUtils.getFullPathNoEndSeparator(Applica.mavenHome) + "/bin/mvn"));
    }

}
