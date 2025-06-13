package space.zoommax.utils.lang;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class LocalizationManager {
    private final Map<String, Map<String, Object>> translations = new HashMap<>();
    private Locale currentLocale = Locale.ENGLISH; // Default locale
    private final Locale fallbackLocale = Locale.ENGLISH; // Fallback locale

    // Load translations from JSON files in the specified directory
    public void loadTranslations(Path directory) throws IOException {
        try (Stream<Path> list = Files.list(directory)) {
            list
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> {
                        try {
                            System.out.println("Loading translations from " + path);
                            String language = path.getFileName().toString().replace(".json", "");
                            Map<String, Object> langTranslations = new ObjectMapper().readValue(path.toFile(), Map.class);
                            translations.put(language, langTranslations);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to load translations from " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load translations from directory", e);
        }
    }

    // Load translations from resources if not already loaded
    public void loadTranslationsFromResources(String resourcePath) {
        try {
            for (String language : new String[]{"default_en_US", "default_ru_RU"}) { // Add supported languages here
                String resourceFile = resourcePath + "/" + language + ".json";
                try (InputStream inputStream = getClass().getResourceAsStream(resourceFile)) {
                    if (inputStream != null) {
                        Map<String, Object> langTranslations = new ObjectMapper().readValue(inputStream, Map.class);
                        translations.putIfAbsent(language, langTranslations);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load translations from resources", e);
        }
    }

    // Set the current locale
    public void setLocale(Locale locale) {
        currentLocale = locale;
    }

    // Get a translation for a specific key, with optional formatting arguments
    public String getTranslation(String key, Object... args) {
        String language = currentLocale.getLanguage();
        String fallbackLanguage = fallbackLocale.getLanguage();

        String translation = getTranslationForLanguage(language, key);

        if (translation == null && !language.equals(fallbackLanguage)) {
            translation = getTranslationForLanguage(fallbackLanguage, key);
        }

        if (translation == null) {
            translation = key; // If no translation is found, return the key
        }

        return formatTranslation(translation, args);
    }

    // Get a translation for a specific key and language, with optional formatting arguments
    public String getTranslationForLanguage(String language, String key, Object... args) {
        String translation = getTranslationForLanguage(language, key);

        if (translation == null) {
            translation = key; // If no translation is found, return the key
        }

        return formatTranslation(translation, args);
    }

    // Helper method to get a translation for a specific language
    public String getTranslationForLanguage(String language, String key) {
        Map<String, Object> langTranslations = translations.getOrDefault(language, new HashMap<>());
        return getValueFromNestedMap(langTranslations, key);
    }

    // Recursive method to get a value from a potentially nested map
    private String getValueFromNestedMap(Map<String, Object> map, String key) {
        String[] keys = key.split("\\."); // Split key by dot to handle nested keys
        Map<String, Object> currentMap = map;

        for (int i = 0; i < keys.length - 1; i++) {
            Object obj = currentMap.get(keys[i]);
            if (obj instanceof Map) {
                currentMap = (Map<String, Object>) obj;
            } else {
                System.err.println("Invalid translation key: " + key);
                return null; // If the path doesn't exist, return null
            }
        }

        Object value = currentMap.get(keys[keys.length - 1]);
        return value != null ? value.toString() : null;
    }

    // Helper method to format a translation with arguments
    private String formatTranslation(String translation, Object... args) {
        return MessageFormat.format(translation, args);
    }

    // Check if a translation exists for the current locale
    public boolean hasTranslation(String key) {
        String language = currentLocale.getLanguage();
        return translations.containsKey(language) && getValueFromNestedMap(translations.get(language), key) != null;
    }
}
