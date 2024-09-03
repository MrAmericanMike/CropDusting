package top.mramericanmike.cropdusting;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import top.mramericanmike.cropdusting.config.ModConfig;
import top.mramericanmike.cropdusting.sounds.ModSounds;

@Mod(Constants.MOD_ID)
public class CropDusting {

    public CropDusting(IEventBus modEventBus, ModContainer modContainer) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);
        ModSounds.SOUND_EVENTS.register(modEventBus);
    }
}