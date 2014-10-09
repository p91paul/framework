package applica.framework.modules;

import applica.framework.AppContext;
import applica.framework.SystemUtils;
import applica.framework.annotations.Action;
import applica.framework.data.Entity;
import applica.framework.data.hibernate.annotations.IgnoreMapping;
import applica.framework.library.options.OptionsManager;
import applica.framework.library.options.PropertiesOptionManager;
import applica.framework.library.utils.FileWalker;
import applica.framework.library.utils.FileWalkerListener;
import applica.framework.modules.hibernate.Configurer;
import applica.framework.modules.hibernate.Mapper;
import applica.framework.utils.TypeUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
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

    private boolean isWebApp(String path) {
        return new File(SystemUtils.multiplatformPath(FilenameUtils.concat(path, "src/main/webapp"))).exists();
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

            String projectRoot = AppContext.current().appPath(File.separator);

            List<Class<? extends Entity>> entities = new ArrayList<>();
            List<String> targetDirs = new ArrayList<>();
            List<String> jars = new ArrayList<>();
            List<String> webAppRoots = new ArrayList();
            FileWalker walker = new FileWalker();
            walker.walk(projectRoot, new FileWalkerListener() {
                @Override
                public void onFile(File directory, File file) {
                    if (FilenameUtils.getExtension(file.getAbsolutePath()).equals("jar")) {
                        jars.add(file.getAbsolutePath());
                    }
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

            //scan for web applications
            File projectRootFile = new File(projectRoot);
            for (File file : projectRootFile.listFiles()) {
                if (file.isDirectory()) {
                    if (isWebApp(file.getAbsolutePath())) {
                        webAppRoots.add(file.getAbsolutePath());
                    }
                }
            }

            if (webAppRoots.size() == 0) {
                throw new RuntimeException("No webapps found");
            }

            URL[] urls = new URL[targetDirs.size() + jars.size()];
            int index = 0;
            for (String targetDir : targetDirs) {
                urls[index++] = new URL(String.format("file://%s/", targetDir));
            }

            for (String jar : jars) {
                urls[index++] = new URL(String.format("jar:file://%s!/", jar));
            }

            URLClassLoader cl = URLClassLoader.newInstance(urls);

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
                            //remove .class extension and first separator
                            className = className.substring(1, className.length() - 6);
                            //substitute separator
                            className = className.replace(File.separator.charAt(0), '.');

                            String resourcesPath = getResourcesPath(targetDir);

                            try {
                                Class c = cl.loadClass(className);
                                if (c.getAnnotation(IgnoreMapping.class) == null) {

                                    if (TypeUtils.isEntity(c)) {
                                        entities.add(c);
                                    }
                                }

                            } catch(Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onDirectory(File file) {

                    }
                });
            }

            Configurer configurer = new Configurer();
            configurer.setEntities(entities);
            for (String root : webAppRoots) {
                String optionsPath = SystemUtils.multiplatformPath(FilenameUtils.concat(root, "src/main/webapp/WEB-INF/options.properties"));
                String configurationPath = SystemUtils.multiplatformPath(FilenameUtils.concat(root, "src/main/resources/hibernate.cfg.xml"));
                String configurationCustomPath = SystemUtils.multiplatformPath(FilenameUtils.concat(root, "src/main/resources/.hibernate.cfg.xml"));

                if (new File(configurationCustomPath).exists()) {
                    FileUtils.copyFile(new File(configurationCustomPath), new File(configurationPath));
                    System.out.println(String.format("Configuration copied from custom: %s", configurationPath));
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

                    System.out.println(String.format("Configuration created: %s", configurationPath));
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

            List<String> targetDirs = new ArrayList<>();
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
                    if (file.getAbsolutePath().endsWith(SystemUtils.multiplatformPath("target/classes"))) {
                        if (!file.getAbsolutePath().contains("WEB-INF")) {
                            targetDirs.add(file.getAbsolutePath());
                        }
                    }
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
                            //remove .class extension and first separator
                            className = className.substring(1, className.length() - 6);
                            //substitute separator
                            className = className.replace(File.separator.charAt(0), '.');

                            String resourcesPath = getResourcesPath(targetDir);

                            try {
                                Class c = cl.loadClass(className);

                                if (c.getAnnotation(IgnoreMapping.class) == null) {
                                    if (TypeUtils.isEntity(c)) {
                                        mapEntity(c, resourcesPath);
                                    }
                                }

                            } catch(Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onDirectory(File file) {

                    }
                });
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
                System.out.println(String.format("Mapping copied from custom: %s", type.getName()));
            } else {
                FileUtils.forceMkdir(new File(FilenameUtils.getFullPath(xmlPath)));
                Mapper mapper = new Mapper(type);
                FileUtils.writeStringToFile(new File(xmlPath), mapper.map(), "UTF-8");

                System.out.println(String.format("Mapping created: %s", type.getName()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getResourcesPath(String targetDir) {
        String moduleBase = targetDir.replace("target" + File.separator + "classes", "");
        String resourcesBase = String.format("%s%s", moduleBase, SystemUtils.multiplatformPath("src/main/resources/"));
        return resourcesBase;
    }

}
