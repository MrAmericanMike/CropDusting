package top.mramericanmike.cropdusting.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import top.mramericanmike.cropdusting.config.ModConfig;
import top.mramericanmike.cropdusting.sounds.ModSounds;

import java.util.Random;

public class Utilities {

    private static final Random RAND = new Random();

    public static boolean isBonemealable(Block checkBlock) {
        return checkBlock instanceof BonemealableBlock;
    }

    public static boolean isCactusOrSugarCaneOrBamboo(Block checkBlock) {
        return checkBlock instanceof CactusBlock
                || checkBlock instanceof SugarCaneBlock
                || checkBlock instanceof BambooStalkBlock;
    }

    public static void doUpdate(Level level, BlockPos blockPos, BlockState blockState) {
        if (!level.isClientSide()) {
            blockState.randomTick((ServerLevel) level, blockPos, level.random);
        }
    }

    public static int randInt(int min, int max) {
        return RAND.nextInt((max - min) + 1) + min;
    }

    private static void spawnParticles(Player player, SimpleParticleType simpleParticleType) {
        Vec3 playerPos = player.position();
        ServerLevel serverLevel = (ServerLevel) player.level();
        int yaw = (int) player.getYRot();
        if (yaw < 0) {
            yaw += 360;
        }
        yaw += 22;
        yaw %= 360;
        int facing = yaw / 45;
        double xOff = 0.0D;
        double zOff = 0.0D;
        double offset = 0.25D;

        switch (facing) {
            case 0:
                zOff = -offset;
                break;
            case 1:
                xOff = offset;
                zOff = -offset;
                break;
            case 2:
                xOff = offset;
                break;
            case 3:
                xOff = offset;
                zOff = offset;
                break;
            case 4:
                zOff = offset;
                break;
            case 5:
                xOff = -offset;
                zOff = offset;
                break;
            case 6:
                xOff = -offset;
                break;
            case 7:
                xOff = -offset;
                zOff = -offset;
                break;
        }

        serverLevel.sendParticles(simpleParticleType,
                playerPos.x + xOff,
                playerPos.y + 0.5,
                playerPos.z + zOff,
                5,
                0D,
                0.1D,
                0D,
                0.05D);
    }

    private static void playFartSounds(Player player) {
        player.level().playSound(null,
                player.position().x,
                player.position().y,
                player.position().z,
                ModSounds.FARTS.value(),
                SoundSource.PLAYERS);
    }

    public static void doParticlesAndFarts(Player player) {
        if (randInt(1, ModConfig.chancesForParticlesOrFarts) == 1) {
            if (ModConfig.doParticles) {
                if (randInt(1, 2) == 1) {
                    spawnParticles(player, ParticleTypes.DUST_PLUME);
                } else {
                    spawnParticles(player, ParticleTypes.POOF);
                }
            }
            if (ModConfig.playFartSounds) {
                playFartSounds(player);
            }
        }
    }
}
