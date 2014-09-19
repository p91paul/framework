package applica.framework.library.i18n;

import applica.framework.library.ApplicationContextProvider;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 5/17/13
 * Time: 10:06 AM
 */
public class Localized {

    private Localization localization;

    protected Localization getLocalization() {
        if(localization == null) {
            localization = new Localization(ApplicationContextProvider.provide());
        }

        return localization;
    }

}
