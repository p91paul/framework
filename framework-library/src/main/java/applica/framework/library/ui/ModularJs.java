package applica.framework.library.ui;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModularJs {

    public static final String SESSION_JS_LIST = "modular_js_list";

    private String path;
    private String requestPath;
    private String module;
    private String canonicalName;
    private List<String> imports = new ArrayList<>();
    private String script;
    private int priority;

    public ModularJs(String path) throws ModuleParseException {
        this.path = path;

        load();
    }

    private void load() throws ModuleParseException {
        File file = new File(path);
        if (file.exists()) {
            StringBuffer scriptBuffer = new StringBuffer();

            InputStream input = null;
            try {
                input = FileUtils.openInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String line = null;
                while ((line = reader.readLine()) != null) {

                    if (line.startsWith("//#module")) parseModule(line);
                    else if (line.startsWith("//#import")) parseImports(line);
                    else scriptBuffer.append(line).append("\r\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(input);
            }

            script = scriptBuffer.toString();
        }
    }

    private void parseModule(String line) {
        String value = line.trim().substring("//#module".length()).trim();
        module = value;
    }

    private void parseImports(String line) throws ModuleParseException {
        String value = line.trim().substring("//#import".length()).trim();
        String[] i = value.split(" ");
        for (String im : i) {
            imports.add(im.trim());
        }
    }

    public String getModule() {
        return module;
    }

    public List<String> getImports() {
        return imports;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String getScript() {
        StringBuffer fullScript = new StringBuffer();
        fullScript
                .append(String.format("module(%d, '%s', function(exports) { \r\n", priority, module))
                .append(script)
                .append("\r\n});\r\n");

        return fullScript.toString();
    }

    @Override
    public String toString() {
        return canonicalName;
    }

    public static List<ModularJs> resolveDependencies(List<ModularJs> jss) {
        List<ModularJs> resolved = new ArrayList<>();

        for (ModularJs js : jss) {
            for (String imp : js.getImports()) {
                addDependency(jss, resolved, imp);
            }

            if (!resolved.contains(js)) resolved.add(js);
        }

        int priority = 10;
        for (ModularJs modularJs : resolved) {
            modularJs.setPriority(priority);
            priority += 10;
        }

        return resolved;
    }

    private static void addDependency(List<ModularJs> jss, List<ModularJs> resolved, String imp) {

        //check if is already in resolved list
        for (ModularJs rjs : resolved) {
            if (rjs.getCanonicalName().equals(imp)) return;
        }

        //find js
        ModularJs impjs = null;
        for (ModularJs js : jss) {
            if (js.getCanonicalName().equals(imp)) {
                impjs = js;
                break;
            }
        }

        if (impjs != null) {
            for (String imp2 : impjs.getImports()) {
                addDependency(jss, resolved, imp2);
            }

            resolved.add(impjs);
        }

    }
}
