package applica.framework.modules;

import applica.framework.AppContext;
import applica.framework.SystemUtils;
import applica.framework.annotations.Action;
import applica.framework.data.Entity;
import applica.framework.data.hibernate.annotations.IgnoreMapping;
import applica.framework.library.options.PropertiesOptionManager;
import applica.framework.library.utils.FileWalker;
import applica.framework.library.utils.FileWalkerListener;
import applica.framework.modules.hibernate.Configurer;
import applica.framework.modules.hibernate.Mapper;
import applica.framework.utils.TypeUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 01/10/14
 * Time: 19:02
 */

@applica.framework.annotations.Module("hibernate")
public class HibernateModule implements Module {

    void p(String message, Object... params) {
        System.out.println(String.format(message, params));
    }

    private boolean isWebApp(String path) {
        return new File(SystemUtils.multiplatformPath(FilenameUtils.concat(path, "src/main/webapp"))).exists();
    }

    private List<String> getWebAppPaths() {
        List<String> webAppRoots = new ArrayList<>();
        String projectRoot = AppContext.current().appPath(File.separator);
        //scan for web applications
        File projectRootFile = new File(projectRoot);
        for (File file : projectRootFile.listFiles()) {
            if (file.isDirectory()) {
                if (isWebApp(file.getAbsolutePath())) {
                    webAppRoots.add(file.getAbsolutePath());
                }
            }
        }

        return webAppRoots;
    }

    private List<String> getWebAppMappingIncludes(String webApp) {
        return AppContext.current().getMappingIncludes(webApp);
    }

    private List<String> getAllClassesPaths(List<String> targetDirs) {
        List<String> classes = new ArrayList<>();
        FileWalker walker = new FileWalker();

        for (String targetDir : targetDirs) {
            walker.walk(targetDir, new FileWalkerListener() {
                @Override
                public void onFile(File directory, File file) {
                    //check if folder not has .ignoremapping file
                    if (new File(FilenameUtils.concat(directory.getAbsolutePath(), ".ignoremapping")).exists()) {
                        return;
                    }

                    if (FilenameUtils.getExtension(file.getAbsolutePath()).equals("class")) {
                        //remove absolute path
                        String className = file.getAbsolutePath().replace(targetDir, "");
                        classes.add(className);
                    }
                }

                @Override
                public void onDirectory(File file) {

                }
            });
        }

        return classes;
    }

    private List<String> getProjectsTargetDirs(List<String> projectPaths) {
        List<String> targetDirs = new ArrayList<>();
        FileWalker walker = new FileWalker();
        for (String projectPath : projectPaths) {
            walker.walk(projectPath, new FileWalkerListener() {
                @Override
                public void onFile(File directory, File file) {

                }

                @Override
                public void onDirectory(File file) {
                    if (file.getAbsolutePath().endsWith(SystemUtils.multiplatformPath("target/classes"))) {
                        if (!file.getAbsolutePath().contains("WEB-INF")) {
                            targetDirs.add(file.getAbsolutePath());
                        }
                    }
                }
            });
        }
        return targetDirs;
    }

    private ClassLoader createClassLoader(List<String> targetDirs) {
        try {
            List<String> jars = new ArrayList<>();
            FileWalker walker = new FileWalker();
            walker.walk(AppContext.current().appPath(File.separator), new FileWalkerListener() {
                @Override
                public void onFile(File directory, File file) {
                    if (FilenameUtils.getExtension(file.getAbsolutePath()).equals("jar")) {
                        jars.add(file.getAbsolutePath());
                    }
                }

                @Override
                public void onDirectory(File file) {

                }
            });

            URL[] urls = new URL[targetDirs.size() + jars.size()];
            int index = 0;
            for (String targetDir : targetDirs) {
                urls[index++] = new URL(String.format("file://%s/", targetDir));
            }

            for (String jar : jars) {
                urls[index++] = new URL(String.format("jar:file://%s!/", jar));
            }

            URLClassLoader cl = URLClassLoader.newInstance(urls);
            return cl;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Action(value = "generate", description = "Generate hibernate configuration file and mappings")
    public void generate(Properties properties) {
        try {
            if (!properties.containsKey("no-rebuild")) {
                Modules.instance().call("project:clean", new Properties());
                Modules.instance().call("project:build", new Properties());
            }

            properties.clear();
            properties.put("no-rebuild", "1");

            Modules.instance().call("hibernate:generate-configuration", properties);
            Modules.instance().call("hibernate:generate-mappings", properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Action(value = "generate-configuration", description = "Generate hibernate configuration file")
    public void generateConfiguration(Properties properties) {
        try {
            if (!properties.containsKey("no-rebuild")) {
                Modules.instance().call("project:clean", new Properties());
                Modules.instance().call("project:build", new Properties());
            }

            List<String> webAppPaths = getWebAppPaths();
            for(String webAppPath : webAppPaths) {
                String webApp = FilenameUtils.getName(webAppPath);
                p("Generating configuration for %s", webApp);

                List<String> includes = getWebAppMappingIncludes(webApp);
                includes.forEach(i -> p("Found include: %s", i));

                List<String> projects = new ArrayList(Arrays.asList(webAppPath));
                includes.forEach(i -> projects.add(AppContext.current().appPath(i)));

                List<String> targetDirs = getProjectsTargetDirs(projects);
                targetDirs.forEach(t -> p("Target dir: %s", AppContext.current().relativePath(t)));

                ClassLoader classLoader = createClassLoader(targetDirs);

                List<String> classesPath = getAllClassesPaths(targetDirs);
                List<Class<? extends Entity>> entities = new ArrayList<>();
                for (String classPath : classesPath) {
                    String className = classPath;
                    //remove .class extension and first separator
                    className = className.substring(1, className.length() - 6);
                    //substitute separator
                    className = className.replace(File.separator.charAt(0), '.');

                    try {
                        Class c = classLoader.loadClass(className);

                        if (c.getAnnotation(IgnoreMapping.class) == null) {
                            if (TypeUtils.isEntity(c)) {
                                entities.add(c);
                                p("Found entity: %s", c.getName());
                            }
                        }

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                Configurer configurer = new Configurer();
                configurer.setEntities(entities);
                String optionsPath = SystemUtils.multiplatformPath(FilenameUtils.concat(webAppPath, "src/main/webapp/WEB-INF/options.properties"));
                String configurationPath = SystemUtils.multiplatformPath(FilenameUtils.concat(webAppPath, "src/main/resources/hibernate.cfg.xml"));
                String configurationCustomPath = SystemUtils.multiplatformPath(FilenameUtils.concat(webAppPath, "src/main/resources/.hibernate.cfg.xml"));

                if (new File(configurationCustomPath).exists()) {
                    FileUtils.copyFile(new File(configurationCustomPath), new File(configurationPath));
                    p("Configuration copied from custom: %s", AppContext.current().relativePath(configurationPath));
                } else {

                    if (!new File(optionsPath).exists()) {
                        throw new RuntimeException("Options file " + optionsPath + " does not exists");
                    }

                    FileInputStream inputStream = new FileInputStream(optionsPath);
                    PropertiesOptionManager options = new PropertiesOptionManager();
                    options.load(inputStream);
                    //closed automatically

                    configurer.setOptions(options);
                    FileUtils.writeStringToFile(new File(configurationPath), configurer.configure(), "UTF-8");

                    p("Configuration created: %s", AppContext.current().relativePath(configurationPath));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Action(value = "generate-mappings", description = "Generate mappings for all entities in project")
    public void generateMappings(Properties properties) {
        try {
            if (!properties.containsKey("no-rebuild")) {
                Modules.instance().call("project:clean", new Properties());
                Modules.instance().call("project:build", new Properties());
            }

            List<String> webAppPaths = getWebAppPaths();
            for(String webAppPath : webAppPaths) {
                String webApp = FilenameUtils.getName(webAppPath);
                p("Generating mappings for %s", webApp);

                List<String> includes = getWebAppMappingIncludes(webApp);
                includes.forEach(i -> p("Found include: %s", i));

                List<String> projects = new ArrayList(Arrays.asList(webAppPath));
                includes.forEach(i -> projects.add(AppContext.current().appPath(i)));

                List<String> targetDirs = getProjectsTargetDirs(projects);
                targetDirs.forEach(t -> p("Target dir: %s", AppContext.current().relativePath(t)));

                ClassLoader classLoader = createClassLoader(targetDirs);

                List<String> classesPath = getAllClassesPaths(targetDirs);
                for (String classPath : classesPath) {
                    String className = classPath;
                    //remove .class extension and first separator
                    className = className.substring(1, className.length() - 6);
                    //substitute separator
                    className = className.replace(File.separator.charAt(0), '.');

                    String resourcesPath = getResourcesPath(webAppPath);

                    try {
                        Class c = classLoader.loadClass(className);

                        if (c.getAnnotation(IgnoreMapping.class) == null) {
                            if (TypeUtils.isEntity(c)) {
                                mapEntity(c, resourcesPath);
                            }
                        }

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mapEntity(Class type, String resourcesPath) {
        Package pak = type.getPackage();
        String path = SystemUtils.multiplatformPath(pak.getName().replace(".", "/"));
        String xmlPath = SystemUtils.multiplatformPath(String.format("%s%s/%s.hbm.xml", resourcesPath, path, type.getSimpleName()));
        String xmlCustomPath = SystemUtils.multiplatformPath(String.format("%s%s/.%s.hbm.xml", resourcesPath, path, type.getSimpleName()));

        try {
            if (new File(xmlCustomPath).exists()) {
                FileUtils.copyFile(new File(xmlCustomPath), new File(xmlPath));

                p("Mapping copied from custom: %s", AppContext.current().relativePath(xmlPath));
            } else {
                FileUtils.forceMkdir(new File(FilenameUtils.getFullPath(xmlPath)));
                Mapper mapper = new Mapper(type);
                FileUtils.writeStringToFile(new File(xmlPath), mapper.map(), "UTF-8");

                p("Mapping created: %s", AppContext.current().relativePath(xmlPath));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getResourcesPath(String webAppPath) {
        String resourcesBase = String.format("%s%s%s", webAppPath, File.separator, SystemUtils.multiplatformPath("src/main/resources/"));
        return resourcesBase;
    }

}
