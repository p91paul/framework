package applica.framework.security;

import applica.framework.data.Filter;
import applica.framework.data.LoadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PasswordUtils {

    public static String generateRandom() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(32, random).toString(32);
    }

}
