package common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class RuntimeConfig {
    private static Logger log = Logger.getLogger(RuntimeConfig.class.getName());
    private Properties properties = new Properties();
    private String fileName = "./src/main/resources/framework.properties";

    /**
     * Read runtime configuration file
     */
    RuntimeConfig() {
        File propertiesFile = new File(fileName);
        try {
            this.properties.load(new FileInputStream(propertiesFile));
        } catch (IOException e) {
            log.info("File $fileName not found");
            e.printStackTrace();
            exit(-1);
        }
    }

    /**
     * Get profile directory
     *
     * @return profile directory
     */
    String getProfileDir() {
        return this.properties.getProperty("triangleProfilesDir");
    }

    /**
     * Get default profile
     *
     * @return default profile
     */
    String getDefaultProfile() {
        return this.properties.getProperty("triangleProfileDefault");
    }
}
