package frc.robot;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Load configuration parameters from the disk. This makes changing parameters
 * possible without having to re-deploy the code.
 */
public class Config {

    /* This file is manually deployed and adjusted */
    private static final File configFile = new File(Filesystem.getDeployDirectory(), "config.prop");

    /* This file is automatically deployed by Gradle */
    private static final File configDefaultFile = new File(Filesystem.getDeployDirectory(), "config.default.prop");

    private final Properties props = new Properties();

    public Config() throws IOException {
        /* Load the defaults first */
        props.load(new FileInputStream(configDefaultFile));

        if (configFile.exists()) {
            /* Load the regular config next */
            props.load(new FileInputStream(configFile));
        }
    }

    public String getString(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot get a null key.");
        }
        String val = props.getProperty(key);
        if (val == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found in any config file.");
        }
        /*
         * This is to let us know if we are hitting this method too often. Ideally, we
         * want to cache all config properties in the classes that they are used in, so
         * we should only see these warnings during robot initialization. If we see them
         * periodically throughout the code's execution, we know we have a performance
         * issue.
         */
        DriverStation.reportWarning("Retrieved key [" + key + "] with value [" + val + "]", false);
        SmartDashboard.putString("Config/" + key, val);
        return val;
    }

    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
}