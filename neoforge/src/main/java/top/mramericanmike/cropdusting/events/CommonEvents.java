package top.mramericanmike.cropdusting.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.mramericanmike.cropdusting.Constants;
import top.mramericanmike.cropdusting.config.ModConfig;
import top.mramericanmike.cropdusting.utils.Utilities;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEvents {

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            CompoundTag playerData = player.getPersistentData();
            if (!playerData.contains(Constants.NEW_PLAYER)) {
                playerData.putBoolean(Constants.NEW_PLAYER, true);
                playerData.putInt(Constants.PLAYER_TIMER, ModConfig.everyTics);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        CompoundTag playerData = player.getPersistentData();
        // Server and has the timer and is Crouching
        if (playerData.contains(Constants.PLAYER_TIMER) && !player.level().isClientSide() && player.isCrouching()) {
            boolean foundGrowable = false;
            int timer = playerData.getInt(Constants.PLAYER_TIMER);
            if (timer <= 0) {
                BlockPos centerPosition = new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());
                for (int xAxis = -ModConfig.horizontalRange; xAxis <= ModConfig.horizontalRange; xAxis++) {
                    for (int zAxis = -ModConfig.horizontalRange; zAxis <= ModConfig.horizontalRange; zAxis++) {
                        for (int yAxis = -ModConfig.verticalRange; yAxis <= ModConfig.verticalRange; yAxis++) {
                            BlockPos effectPosition = new BlockPos(centerPosition.offset(xAxis, yAxis, zAxis));
                            BlockState checkBlockState = player.level().getBlockState(effectPosition);
                            Block checkBlock = checkBlockState.getBlock();
                            if (Utilities.isBonemealable(checkBlock)) {
                                foundGrowable = true;
                                Utilities.doUpdate(player.level(), effectPosition, checkBlockState);
                            } else if (Utilities.isCactusOrSugarCaneOrBamboo(checkBlock)) {
                                foundGrowable = true;
                                Utilities.doUpdate(player.level(), effectPosition, checkBlockState);
                                BlockPos effectPositionAbove = new BlockPos(centerPosition.offset(xAxis, yAxis + 1, zAxis));
                                BlockState checkBlockStateAbove = player.level().getBlockState(effectPositionAbove);
                                Block checkBlockAbove = checkBlockStateAbove.getBlock();
                                if (Utilities.isCactusOrSugarCaneOrBamboo(checkBlockAbove)) {
                                    Utilities.doUpdate(player.level(), effectPositionAbove, checkBlockStateAbove);
                                }
                            }
                        }//END Y AXIS
                    }//END Z AXIS
                }//END X AXIS
                playerData.putInt(Constants.PLAYER_TIMER, ModConfig.everyTics);
                if (foundGrowable) {
                    Utilities.doParticlesAndFarts(player);
                }
            } else {
                playerData.putInt(Constants.PLAYER_TIMER, timer - 1);
            }
        }
    }
}