package applica.framework.modules;

import applica.framework.AppContext;
import applica.framework.ApplicationContextProvider;
import applica.framework.SystemUtils;
import applica.framework.annotations.Action;
import applica.framework.library.SimpleItem;
import applica.framework.library.utils.FileWalker;
import applica.framework.library.utils.FileWalkerListener;
import applica.framework.modules.hibernate.Mapper;
import applica.framework.utils.TypeUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 01/10/14
 * Time: 19:02
 */

@applica.framework.annotations.Module("hibernate")
public class HibernateModule implements Module {

    @Action(value = "generate-mappings", description = "Generate mappings for all entities in project")
    public void scan(Properties properties) {
        try {
            if (properties.containsKey("no-rebuild")) {
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
                                if (TypeUtils.isEntity(c)) {
                                    mapEntity(c, resourcesPath);
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
        System.out.println(String.format("Mapping %s...", type.getName()));

        Package pak = type.getPackage();
        String path = SystemUtils.multiplatformPath(pak.getName().replace(".", "/"));
        String xmlPath = SystemUtils.multiplatformPath(String.format("%s%s/%s.hbm.xml", resourcesPath, path, type.getSimpleName()));

        try {
            FileUtils.forceMkdir(new File(FilenameUtils.getFullPath(xmlPath)));
            Mapper mapper = new Mapper(type);
            FileUtils.writeStringToFile(new File(xmlPath), mapper.getXml(), "UTF-8");
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
