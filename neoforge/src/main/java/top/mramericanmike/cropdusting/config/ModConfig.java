package top.mramericanmike.cropdusting.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import top.mramericanmike.cropdusting.Constants;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue HORIZONTAL_RANGE = BUILDER
            .comment("Horizontal range")
            .defineInRange("horizontalRange", 4, 1, 9);

    private static final ModConfigSpec.IntValue VERTICAL_RANGE = BUILDER
            .comment("Vertical range")
            .defineInRange("verticalRange", 1, 0, 4);

    private static final ModConfigSpec.IntValue CROP_DUST_EVERY_TICS = BUILDER
            .comment("Tics in between Crop Dusting")
            .defineInRange("everyTics", 20, 10, 200);

    private static final ModConfigSpec.IntValue CHANCES = BUILDER
            .comment("1 in 'x' chances the player will spawn particles and fart (Depends on the other settings been set to true)")
            .defineInRange("chancesPool", 10, 2, 100);

    private static final ModConfigSpec.BooleanValue DO_PARTICLES = BUILDER
            .comment("Spawn some particles behind player simulating farts")
            .define("doParticles", true);

    private static final ModConfigSpec.BooleanValue PLAY_FART_SOUNDS = BUILDER
            .comment("Play fart sounds at random")
            .define("playFartSounds", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int horizontalRange;
    public static int verticalRange;
    public static int everyTics;
    public static int chancesForParticlesOrFarts;
    public static boolean doParticles;
    public static boolean playFartSounds;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        horizontalRange = HORIZONTAL_RANGE.get();
        verticalRange = VERTICAL_RANGE.get();
        everyTics = CROP_DUST_EVERY_TICS.get();
        chancesForParticlesOrFarts = CHANCES.get();
        doParticles = DO_PARTICLES.get();
        playFartSounds = PLAY_FART_SOUNDS.get();
    }
}
