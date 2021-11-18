package com.jpa.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

/**
 * Contains locale-specific texts.
 * 
 * <br>
 * </br>
 * 
 * If you need a locale-specific text within your application, you can use the resource bundle to
 * load the locale-specific file from the server and access the texts of it.
 * 
 * @author Tomás Sánchez
 * @since 1.0
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

    public ResourceBundle setLang(String lang) {

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
