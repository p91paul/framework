package applica.documentation.domain.model;

import applica.framework.data.SEntity;
import applica.framework.security.Role;

import java.util.Date;
import java.util.List;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 28/10/13
 * Time: 17:08
 */
public class User extends SEntity implements applica.framework.security.User {

    private String username;
    private String mail;
    private String password;
    private boolean active;
    private Date registrationDate;
    private String activationCode;
    private String image;
    private List<applica.documentation.domain.model.Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public List<? extends Role> getRoles() {
        return roles;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRoles(List<applica.documentation.domain.model.Role> roles) {
        this.roles = roles;
    }


}
