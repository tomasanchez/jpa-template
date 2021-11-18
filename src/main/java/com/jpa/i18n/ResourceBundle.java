package com.jpa.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Resource bundles contain locale-specific texts.
 * 
 * <br>
 * </br>
 * 
 * When your program needs a locale-specific resource, a text for example, your program can load it
 * from the resource bundle that is appropriate for the current user's locale. In this way, you can
 * write program code that is largely independent of the user's locale isolating most, if not all,
 * of the locale-specific information in resource bundles.
 * 
 * <br>
 * </br>
 * 
 * Resoursce Bundle is loaded from a properties file. These files must be in the directory
 * 
 * <br>
 * </br>
 * 
 * /resources/locales/i18n-{ISO 639-1}.properties
 * 
 * @author Tomás Sánchez
 * @since 1.0
 * @see <a>https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes<a/>
 */
public class ResourceBundle {

    private static final String DEF_LANG = "en";
    private String lang;
    private Properties properties;

    public ResourceBundle() {
        this(defaultLanguage());
    }

    public ResourceBundle(String lang) {
        this.setLang(lang);
    }

    /* =========================================================== */
    /* Getters & Setter ------------------------------------------ */
    /* =========================================================== */

    /**
     * Obtiene el lenguaje default.
     * 
     * @return el fallback language
     */
    public static String defaultLanguage() {
        return DEF_LANG;
    }

    /**
     * Returns a locale-specific string value for the given key.
     * 
     * @param key the given key
     * @return the text value
     */
    public Object getText(String key) {
        return getProperties().get(key);
    }

    public String getLang() {
        return lang;
    }

    public Properties getProperties() {
        return this.properties;
    }

    /**
     * Updates the i18n locales language.
     * 
     * ? If needed loads properties
     * 
     * @param lang the language to update.
     * @return the resource boundle.
     */
    public ResourceBundle setLang(String lang) {

        if (!Objects.isNull(getLang()) && lang.startsWith(getLang())
                && !Objects.isNull(getProperties())) {
            return this;
        }

        if (Objects.isNull(lang) || lang.isEmpty() || lang.length() < 2) {
            this.lang = DEF_LANG;
        } else {
            this.lang = lang.substring(0, 2);
        }

        return this.loadProperties();
    }

    /* =========================================================== */
    /* Internal Methods ------------------------------------------ */
    /* =========================================================== */

    /**
     * Loads properties from file.
     * 
     * <br>
     * </br>
     * 
     * * Must follow ISO 639-1
     * 
     * <br>
     * </br>
     * 
     * ! IMPORTANT: Files must be named i18-{iso-code}.properties
     * 
     * <br>
     * </br>
     * 
     * ? If file in lag does not exists, loads default: i18n-{DEF_LANG}.properties
     * 
     * @return The resource bundle.
     * @see <a>https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes<a/>
     */
    private ResourceBundle loadProperties() {
        this.properties = new Properties();

        String fileName = "locales/i18n-".concat(this.getLang()).concat(".properties");

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (Objects.isNull(inputStream)) {
            inputStream = getClass().getClassLoader().getResourceAsStream(
                    fileName.replace("-".concat(this.getLang()), "-".concat(DEF_LANG)));
            this.lang = defaultLanguage();
        }

        try {
            properties.load(inputStream);
            properties.put("lang", this.getLang());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }
}
