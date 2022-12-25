package net.uku3lig.modtemplate;

import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModTemplate implements ModInitializer {
    // This annotation is provided by Lombok, a very useful library to remove boilerplate code
    // See https://projectlombok.org for more information and documentation
    // It's good practice to use the mod id in the logger's topic to avoid confusion
    @Getter
    private static final Logger log = LoggerFactory.getLogger("modtemplate");

    @Override
    public void onInitialize() {
        // This method is called once when the mod is loaded
        // It can be used to register things, such as keybindings
        log.info("uku was here :D");
    }
}
