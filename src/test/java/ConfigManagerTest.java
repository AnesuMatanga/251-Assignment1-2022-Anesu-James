import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigManagerTest {

    @Test
    public void getConfigProperty() {
        ConfigManager fontManger = new ConfigManager();
        assertEquals(14, fontManger.getConfigProperty("font_size"));
        assertNull(fontManger.getConfigProperty("config_setting_that_doesnt_exist"));
    }
}