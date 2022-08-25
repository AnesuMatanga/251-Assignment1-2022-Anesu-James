import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigManagerTest {

    @Test
    public void getConfigProperty() {
        ConfigManager fontManger = new ConfigManager();
        assertEquals(14, fontManger.getConfigProperty("font_size"));
        assertNull(fontManger.getConfigProperty("config_setting_that_doesnt_exist"));
    }

    @Test
    public void checkConfigProperty() {
        ConfigManager fontFamily = new ConfigManager();
        assertEquals("Monospaced", fontFamily.getConfigProperty("font_family"));
        assertNull(fontFamily.getConfigProperty("config_setting_doesn't_exist"));
    }
}
