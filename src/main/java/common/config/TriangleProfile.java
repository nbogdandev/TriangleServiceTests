package common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.utils.types.profile.Profile;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class TriangleProfile {
    private static Logger log = Logger.getLogger(TriangleProfile.class.getName());
    private Profile profileConfig;

    /**
     * Read and parse profile configuration
     */
    public TriangleProfile() {
        RuntimeConfig runtimeConfig = new RuntimeConfig();
        String profile = runtimeConfig.getDefaultProfile();
        String profilePath = runtimeConfig.getProfileDir() + profile;
        File file = new File(profilePath);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.profileConfig = objectMapper.readValue(file, Profile.class);
        } catch (IOException e) {
            log.info("File $profilePath not found");
            e.printStackTrace();
            exit(-1);
        }
    }

    /**
     * Get profile config
     *
     * @return profileConfig
     */
    Profile getProfileConfig() {
        return this.profileConfig;
    }

    /**
     * Get Application Url
     *
     * @return
     */
    public String getAppUrl() {
        return this.getProfileConfig().url;
    }

    /**
     * Get Auth Header Name
     *
     * @return
     */
    public String getAuthHeaderName() {
        return this.getProfileConfig().authHeaderName;
    }

    /**
     * Get Auth Header Value
     *
     * @return
     */
    public String getAuthHeaderValue() {
        return this.getProfileConfig().authHeaderValue;
    }
}
