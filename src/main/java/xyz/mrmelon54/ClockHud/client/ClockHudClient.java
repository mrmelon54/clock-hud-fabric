package xyz.mrmelon54.ClockHud.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import xyz.mrmelon54.ClockHud.config.ConfigStructure;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ClockHudClient implements ClientModInitializer {
    private ConfigStructure config;
    private static ClockHudClient instance;

    public static ClockHudClient getInstance() {
        return instance;
    }

    @Override
    public void onInitializeClient() {
        instance = this;

        AutoConfig.register(ConfigStructure.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ConfigStructure.class).getConfig();
    }

    public ConfigStructure getConfig() {
        return config;
    }
}
