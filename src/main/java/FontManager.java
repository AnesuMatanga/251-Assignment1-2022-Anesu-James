import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class FontManager {
    //Method to get properties set in the Config.yml file
    public Object getConfigProperty(String property) {
        InputStream inputStream = null;

        {
            try {
                inputStream = new FileInputStream(new File("target/config.yml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);
        Object propertyValue = data.get(property);

        return propertyValue;
    }
}
