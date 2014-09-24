package applica.framework.security.authorization;

import java.util.List;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 24/09/14
 * Time: 19:17
 */
public class Permissions {

    private static Permissions s_instance;

    public static Permissions instance() {
        if (s_instance == null) {
            s_instance = new Permissions();
        }

        return s_instance;
    }

    private List<PermissionInfo> permissionInfos;

    private Permissions() {

    }

    public void register(String permission, Authorization authorization) {

    }

    private class PermissionInfo {
        private String permission;
        private Authorization authorization;

        private PermissionInfo(String path, Authorization authorization) {
            this.permission = path;
            this.authorization = authorization;
        }

        private PermissionInfo() {
        }

        public String getPermission() {
            return permission;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }

        public Authorization getAuthorization() {
            return authorization;
        }

        public void setAuthorization(Authorization authorization) {
            this.authorization = authorization;
        }
    }

}
