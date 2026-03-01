package sirbasic.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class ConfigLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ConfigLoader() {}

    public static SIRConfig loadSIRConfig(String resourcePath) {
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null)
                throw new IllegalArgumentException("Config resource not found: " + resourcePath);
            return MAPPER.readValue(is, SIRConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config: " + resourcePath, e);
        }
    }
}
