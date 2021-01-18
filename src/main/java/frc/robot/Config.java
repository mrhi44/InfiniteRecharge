package frc.robot;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;

/**
 * Load configuration parameters from the disk. This makes changing parameters
 * possible without having to re-deploy the code.
 */
public class Config {

    private final Properties props = new Properties();
    private final String propFileName;

    public Config() throws IOException {
        this("config.prop");
    }

    public Config(String propFileName) throws IOException {
        /* On the roboRIO, this is /home/lvuser/deploy */
        File deployDir = Filesystem.getDeployDirectory();
        File propFile = new File(deployDir, propFileName);
        props.load(new FileInputStream(propFile));
        this.propFileName = propFile.getAbsolutePath();
    }

    public String getString(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot get a null key.");
        }
        String val = props.getProperty(key);
        if (val == null) {
            throw new IllegalArgumentException("Property '" + key + "' not found in '" + propFileName + "'.");
        }
        /*
         * This is to let us know if we are hitting this method too often. Ideally, we
         * want to cache all config properties in the classes that they are used in, so
         * we should only see these warnings during robot initialization. If we see them
         * periodically throughout the code's execution, we know we have a performance issue.
         */
        DriverStation.reportWarning("Retrieved key [" + key + "] with value [" + val + "]", false);
        return val;
    }

    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
}