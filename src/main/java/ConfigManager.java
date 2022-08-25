import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class ConfigManager {
    //Method to get properties set in the Config.yml file such as Font properties using snakeyml

    public Object getConfigProperty(String property) {
        InputStream inputStream = null;

        {
            try {
                inputStream = new FileInputStream("config.yml");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error when reading from config.yml file.");
            }
        }

        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);
        return data.get(property);
    }
}
