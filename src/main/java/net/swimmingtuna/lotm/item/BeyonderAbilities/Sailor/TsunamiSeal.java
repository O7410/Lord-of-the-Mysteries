package net.swimmingtuna.lotm.item.BeyonderAbilities.Sailor;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.swimmingtuna.lotm.LOTM;
import net.swimmingtuna.lotm.caps.BeyonderHolder;
import net.swimmingtuna.lotm.caps.BeyonderHolderAttacher;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = LOTM.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TsunamiSeal extends Item {
    public TsunamiSeal(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player pPlayer, InteractionHand hand) {
        if (!pPlayer.level().isClientSide()) {
            BeyonderHolder holder = BeyonderHolderAttacher.getHolder(pPlayer).orElse(null);
            if (holder == null || !holder.isSailorClass()) {
                pPlayer.displayClientMessage(Component.literal("You are not of the Sailor pathway").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE), true);
                return super.use(level, pPlayer, hand);
            }
            if (holder.getSpirituality() < 75) {
                pPlayer.displayClientMessage(Component.literal("You need 75 spirituality in order to use this").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE), true);
                return super.use(level, pPlayer, hand);
            }

            BeyonderHolderAttacher.getHolder(pPlayer).ifPresent(tyrantSequence -> {
                if (tyrantSequence.getCurrentSequence() <= 4 && tyrantSequence.useSpirituality(75)) {
                    startTsunami(pPlayer);
                }
                if (!pPlayer.getAbilities().instabuild)
                    pPlayer.getCooldowns().addCooldown(this, 60 * 20); // 60 seconds cooldown
            });
        }
        return super.use(level, pPlayer, hand);
    }

    public static void startTsunami(Player pPlayer) {
        pPlayer.getPersistentData().putInt("sailorTsunami", 600);
        float yaw = pPlayer.getYRot();
        String direction = getDirectionFromYaw(yaw);
        pPlayer.getPersistentData().putString("sailorTsunamiDirection", direction);
        pPlayer.getPersistentData().putInt("sailorTsunamiX", (int) pPlayer.getX());
        pPlayer.getPersistentData().putInt("sailorTsunamiY", (int) pPlayer.getY());
        pPlayer.getPersistentData().putInt("sailorTsunamiZ", (int) pPlayer.getZ());
    }

    private static String getDirectionFromYaw(float yaw) {
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            return "N";
        } else if (yaw >= 45 && yaw < 135) {
            return "E";
        } else if (yaw >= 135 && yaw < 225) {
            return "S";
        } else if (yaw >= 225 && yaw < 315) {
            return "W";
        }
        return "N";
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        if (!Screen.hasShiftDown()) {
            componentList.add(Component.literal("Creates a massive wave of water in front of you\n" +
                    "Spirituality Used: 75\n" +
                    "Cooldown: 60 seconds"));
        }
        super.appendHoverText(pStack, level, componentList, tooltipFlag);
    }

    @SubscribeEvent
    public static void tsunamiTick(TickEvent.PlayerTickEvent event) {
        Player pPlayer = event.player;
        if (!pPlayer.level().isClientSide() && event.phase == TickEvent.Phase.END) {
            CompoundTag tag = pPlayer.getPersistentData();
            int tsunami = tag.getInt("sailorTsunami");
            if (tsunami >= 1) {
                tag.putInt("sailorTsunami", tsunami - 5);
                summonTsunami(pPlayer);
            } else {
                tag.remove("sailorTsunamiDirection");
                tag.remove("sailorTsunamiX");
                tag.remove("sailorTsunamiY");
                tag.remove("sailorTsunamiZ");
            }
        }
    }

    public static void summonTsunami(Player pPlayer) {
        CompoundTag tag = pPlayer.getPersistentData();
        int playerX = tag.getInt("sailorTsunamiX");
        int playerY = tag.getInt("sailorTsunamiY");
        int playerZ = tag.getInt("sailorTsunamiZ");
        int tsunami = tag.getInt("sailorTsunami");
        String direction = tag.getString("sailorTsunamiDirection");

        int offsetX = 0;
        int offsetZ = 0;

        switch (direction) {
            case "N":
                offsetZ = 1;
                break;
            case "E":
                offsetX = -1;
                break;
            case "S":
                offsetZ = -1;
                break;
            case "W":
                offsetX = 1;
                break;
        }

        int waveWidth = 80;
        int waveHeight = 10;
        int startDistance = 85;

        for (int w = -waveWidth / 2; w < waveWidth / 2; w++) {
            for (int h = 0; h < waveHeight; h++) {
                int x = playerX + (offsetX * startDistance) + (offsetX * (200 - tsunami) / 5);
                int y = playerY + h;
                int z = playerZ + (offsetZ * startDistance) + (offsetZ * (200 - tsunami) / 5);

                if (offsetX == 0) {
                    x += w;
                } else {
                    z += w;
                }

                BlockPos blockPos = new BlockPos(x, y, z);
                if (pPlayer.level().getBlockState(blockPos).isAir()) {
                    pPlayer.level().setBlock(blockPos, Blocks.WATER.defaultBlockState(), 3);
                }
            }
        }

        // Create AABB representing the tsunami area
        AABB tsunamiAABB = new AABB(
                playerX + (offsetX * startDistance) + (offsetX * (200 - tsunami) / 5) - waveWidth / 2,
                playerY,
                playerZ + (offsetZ * startDistance) + (offsetZ * (200 - tsunami) / 5) - waveWidth / 2,
                playerX + (offsetX * startDistance) + (offsetX * (200 - tsunami) / 5) + waveWidth / 2,
                playerY + waveHeight,
                playerZ + (offsetZ * startDistance) + (offsetZ * (200 - tsunami) / 5) + waveWidth / 2
        );
        pPlayer.level().getEntitiesOfClass(LivingEntity.class, tsunamiAABB).forEach(livingEntity -> {
            if (livingEntity != pPlayer) {
                if (livingEntity.getMaxHealth() >= 300 || livingEntity instanceof Player) {
                    pPlayer.getPersistentData().putInt("sailorTsunami", 0);
                    livingEntity.getPersistentData().putInt("sailorSeal", 1200);
                    livingEntity.getPersistentData().putInt("sailorSealX", (int) livingEntity.getX());
                    livingEntity.getPersistentData().putInt("sailorSeaY", (int) livingEntity.getY());
                    livingEntity.getPersistentData().putInt("sailorSealZ", (int) livingEntity.getZ());
                }
            }
        });
    }

    @SubscribeEvent // seal effect is happening too far ahead, also sending entities into the air for some reason?? also try to get rid of water spawned after seal effect.
    public static void sealHandler(LivingEvent.LivingTickEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (!livingEntity.level().isClientSide()) {
            CompoundTag tag = livingEntity.getPersistentData();
            int sealCounter = tag.getInt("sailorSeal");
            if (sealCounter >= 3) {
                int sealX = tag.getInt("sailorSealX");
                int sealY = tag.getInt("sailorSealY");
                int sealZ = tag.getInt("sailorSealZ");
                livingEntity.teleportTo(sealX, sealY, sealZ);
                BlockPos playerPos = livingEntity.blockPosition();
                Level level = livingEntity.level();
                double radius = 6.0;
                double minRemovalRadius = 6.0;
                double maxRemovalRadius = 11.0;

                // Create a sphere of water around the player
                for (int x = (int) -radius; x <= radius; x++) {
                    for (int y = (int) -radius; y <= radius; y++) {
                        for (int z = (int) -radius; z <= radius; z++) {
                            double distance = Math.sqrt(x * x + y * y + z * z);
                            if (distance <= radius) {
                                BlockPos blockPos = playerPos.offset(x, y, z);
                                if (level.getBlockState(blockPos).isAir() && !level.getBlockState(blockPos).is(Blocks.WATER)) {
                                    level.setBlock(blockPos, Blocks.WATER.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
                for (int x = (int) -maxRemovalRadius; x <= maxRemovalRadius; x++) {
                    for (int y = (int) -maxRemovalRadius; y <= maxRemovalRadius; y++) {
                        for (int z = (int) -maxRemovalRadius; z <= maxRemovalRadius; z++) {
                            double distance = Math.sqrt(x * x + y * y + z * z);
                            if (distance <= maxRemovalRadius && distance >= minRemovalRadius) {
                                BlockPos blockPos = playerPos.offset(x, y, z);
                                if (level.getBlockState(blockPos).getBlock() == Blocks.WATER) {
                                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
                tag.putInt("sailorSeal", sealCounter - 1);
                if (sealCounter % 20 == 0) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 40, 1, false, false));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 3, false, false));
                }
            }
            if (sealCounter == 1) {
                double minRemovalRadius = 6.0;
                double maxRemovalRadius = 11.0;
                BlockPos playerPos = livingEntity.blockPosition();
                Level level = livingEntity.level();
                for (int x = (int) -maxRemovalRadius; x <= maxRemovalRadius; x++) {
                    for (int y = (int) -maxRemovalRadius; y <= maxRemovalRadius; y++) {
                        for (int z = (int) -maxRemovalRadius; z <= maxRemovalRadius; z++) {
                            double distance = Math.sqrt(x * x + y * y + z * z);
                            if (distance <= maxRemovalRadius && distance >= minRemovalRadius) {
                                BlockPos blockPos = playerPos.offset(x, y, z);
                                if (level.getBlockState(blockPos).getBlock() == Blocks.WATER) {
                                    level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void sealItemCanceler(PlayerInteractEvent.RightClickItem event) {
        Player pPlayer = event.getEntity();
        if (!pPlayer.level().isClientSide()) {
            CompoundTag tag = pPlayer.getPersistentData();
            int sealCounter = tag.getInt("sailorSeal");
            if (sealCounter >= 1) {
                event.setCanceled(true);
            }
        }
    }
}
