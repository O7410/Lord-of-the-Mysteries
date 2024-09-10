package net.swimmingtuna.lotm.item.BeyonderAbilities.Sailor;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.swimmingtuna.lotm.LOTM;
import net.swimmingtuna.lotm.caps.BeyonderHolder;
import net.swimmingtuna.lotm.caps.BeyonderHolderAttacher;
import net.swimmingtuna.lotm.entity.EndStoneEntity;
import net.swimmingtuna.lotm.entity.NetherrackEntity;
import net.swimmingtuna.lotm.entity.StoneEntity;
import net.swimmingtuna.lotm.init.BeyonderClassInit;
import net.swimmingtuna.lotm.init.EntityInit;
import net.swimmingtuna.lotm.init.ItemInit;
import net.swimmingtuna.lotm.networking.LOTMNetworkHandler;
import net.swimmingtuna.lotm.networking.packet.MatterAccelerationBlockC2S;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber(modid = LOTM.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MatterAccelerationBlocks extends Item {

    public MatterAccelerationBlocks(Properties properties) { //IMPORTANT!!!! FIGURE OUT HOW TO MAKE THIS WORK BY CLICKING ON A
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!player.level().isClientSide()) {

            // If no block or entity is targeted, proceed with the original functionality
            BeyonderHolder holder = BeyonderHolderAttacher.getHolderUnwrap(player);
            if (!holder.currentClassMatches(BeyonderClassInit.SAILOR)) {
                player.displayClientMessage(Component.literal("You are not of the Sailor pathway").withStyle(ChatFormatting.BOLD, ChatFormatting.BLUE), true);
            }
            if (holder.getSpirituality() < 1500) {
                player.displayClientMessage(Component.literal("You need 2000 spirituality in order to use this").withStyle(ChatFormatting.BOLD, ChatFormatting.BLUE), true);
            }
            if (holder.currentClassMatches(BeyonderClassInit.SAILOR) && holder.getCurrentSequence() <= 0 && holder.useSpirituality(2000)) {
                useItem(player);
                if (!player.getAbilities().instabuild)
                    player.getCooldowns().addCooldown(this, 300);
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("Upon use, summons 10 blocks from around the player, making the next left clicks the player does shoot them towards the direction they look at with incredible speed\n" +
                "Spirituality Used: 2000\n" +
                "Cooldown: 15 seconds").withStyle(ChatFormatting.BOLD, ChatFormatting.BLUE));
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
    }
    public static void useItem(Player player) {
        if (!player.level().isClientSide()) {
            player.getPersistentData().putInt("matterAccelerationBlockTimer", 480);
            Level level = player.level();
            BlockPos playerPos = player.blockPosition();
            BlockPos surfacePos = findSurfaceBelow(level, playerPos);

            if (surfacePos != null) {
                for (int i = 0; i < 10; i++) {
                    BlockPos posToRemove = surfacePos.below(i);
                    level.destroyBlock(posToRemove, false);
                    if (level.dimension() == Level.OVERWORLD) {
                        StoneEntity stoneEntity = new StoneEntity(EntityInit.STONE_ENTITY.get(), player.level());
                        float randomStayX;
                        do { randomStayX = (float) ((Math.random() * 6) - 3);} while (randomStayX > -0.5 && randomStayX < 0.5);
                        float randomStayY = (float) ((Math.random() * 6) - 3);
                        float randomStayZ = (float) ((Math.random() * 6) - 3);
                        int randomXRot = (int) ((Math.random() * 10) - 5);
                        int randomYRot = (int) ((Math.random() * 10) - 5);
                        stoneEntity.setStoneStayAtX(randomStayX);
                        stoneEntity.setStoneStayAtY(randomStayY);
                        stoneEntity.setStoneStayAtZ(randomStayZ);
                        stoneEntity.setOwner(player);
                        stoneEntity.setRemoveAndHurt(true);
                        stoneEntity.setSent(false);
                        stoneEntity.setPos(surfacePos.getX() + 0.5, surfacePos.getY() + 1, surfacePos.getZ() + 0.5);
                        stoneEntity.setShouldntDamage(true);


                        player.level().addFreshEntity(stoneEntity);
                    }
                    if (level.dimension() == Level.NETHER) {
                        NetherrackEntity stoneEntity = new NetherrackEntity(EntityInit.NETHERRACK_ENTITY.get(), player.level());
                        float randomStayX;
                        do { randomStayX = (float) ((Math.random() * 6) - 3);} while (randomStayX > -0.5 && randomStayX < 0.5);
                        float randomStayY = (float) ((Math.random() * 6) - 3);
                        float randomStayZ = (float) ((Math.random() * 6) - 3);
                        int randomXRot = (int) ((Math.random() * 10) - 5);
                        int randomYRot = (int) ((Math.random() * 10) - 5);
                        stoneEntity.setNetherrackStayAtX(randomStayX);
                        stoneEntity.setNetherrackStayAtY(randomStayY);
                        stoneEntity.setNetherrackStayAtZ(randomStayZ);
                        stoneEntity.setOwner(player);
                        stoneEntity.setRemoveAndHurt(true);
                        stoneEntity.setSent(false);
                        stoneEntity.setPos(surfacePos.getX() + 0.5, surfacePos.getY() + 1, surfacePos.getZ() + 0.5);
                        stoneEntity.setShouldDamage(false);
                        stoneEntity.setNetherrackXRot(randomXRot);
                        stoneEntity.setNetherrackYRot(randomYRot);

                        player.level().addFreshEntity(stoneEntity);
                    }
                    if (level.dimension() == Level.NETHER) {
                        EndStoneEntity stoneEntity = new EndStoneEntity(EntityInit.ENDSTONE_ENTITY.get(), player.level());
                        float randomStayX;
                        do { randomStayX = (float) ((Math.random() * 6) - 3);} while (randomStayX > -0.5 && randomStayX < 0.5);
                        float randomStayY = (float) ((Math.random() * 6) - 3);
                        float randomStayZ = (float) ((Math.random() * 6) - 3);
                        int randomXRot = (int) ((Math.random() * 10) - 5);
                        int randomYRot = (int) ((Math.random() * 10) - 5);
                        stoneEntity.setEndstoneStayAtX(randomStayX);
                        stoneEntity.setEndstoneStayAtY(randomStayY);
                        stoneEntity.setEndstoneStayAtZ(randomStayZ);
                        stoneEntity.setOwner(player);
                        stoneEntity.setRemoveAndHurt(true);
                        stoneEntity.setSent(false);
                        stoneEntity.setPos(surfacePos.getX() + 0.5, surfacePos.getY() + 1, surfacePos.getZ() + 0.5);
                        stoneEntity.setShouldntDamage(true);
                        stoneEntity.setEndstoneXRot(randomXRot);
                        stoneEntity.setEndstoneYRot(randomYRot);
                        player.level().addFreshEntity(stoneEntity);
                    }
                    if (level.dimension() != Level.OVERWORLD && level.dimension() != Level.NETHER && level.dimension() != Level.END) {
                        StoneEntity stoneEntity = new StoneEntity(EntityInit.STONE_ENTITY.get(), player.level());
                        float randomStayX;
                        do { randomStayX = (float) ((Math.random() * 6) - 3);} while (randomStayX > -0.5 && randomStayX < 0.5);
                        float randomStayY = (float) ((Math.random() * 6) - 3);
                        float randomStayZ = (float) ((Math.random() * 6) - 3);
                        int randomXRot = (int) ((Math.random() * 10) - 5);
                        int randomYRot = (int) ((Math.random() * 10) - 5);
                        stoneEntity.setStoneStayAtX(randomStayX);
                        stoneEntity.setStoneStayAtY(randomStayY);
                        stoneEntity.setStoneStayAtZ(randomStayZ);
                        stoneEntity.setOwner(player);
                        stoneEntity.setRemoveAndHurt(true);
                        stoneEntity.setSent(false);
                        stoneEntity.setPos(surfacePos.getX() + 0.5, surfacePos.getY() + 1, surfacePos.getZ() + 0.5);
                        stoneEntity.setShouldntDamage(true);
                        stoneEntity.setStoneXRot(randomXRot);
                        stoneEntity.setStoneYRot(randomYRot);
                        player.level().addFreshEntity(stoneEntity);
                    }
                }
            }
        }
    }

    private static BlockPos findSurfaceBelow(Level level, BlockPos startPos) {
        for (int y = startPos.getY(); y >= level.getMinBuildHeight(); y--) {
            BlockPos checkPos = new BlockPos(startPos.getX(), y, startPos.getZ());
            if (isOnSurface(level, checkPos)) {
                return checkPos;
            }
        }
        return null; // No surface found
    }

    private static boolean isOnSurface(Level level, BlockPos pos) {
        return level.canSeeSky(pos.above()) || !level.getBlockState(pos.above()).isSolid();
    }
    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        LOTMNetworkHandler.sendToServer(new MatterAccelerationBlockC2S());
        ItemStack heldItem = player.getMainHandItem();
        int activeSlot = player.getInventory().selected;
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof MatterAccelerationBlocks) {
            player.getInventory().setItem(activeSlot, new ItemStack(ItemInit.MATTER_ACCELERATION_SELF.get()));
            heldItem.shrink(1);
        }
    }
    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        int x = player.getPersistentData().getInt("matterAccelerationBlockTimer");
        if (x >= 1) {
            Vec3 lookDirection = player.getLookAngle().normalize().scale(20);
            if (player.level().dimension() == Level.OVERWORLD) {
                StoneEntity stoneEntity = player.level().getEntitiesOfClass(StoneEntity.class, player.getBoundingBox().inflate(10))
                        .stream()
                        .min(Comparator.comparingDouble(zombie -> zombie.distanceTo(player)))
                        .orElse(null);
                if (stoneEntity != null) {
                    stoneEntity.setDeltaMovement(lookDirection);
                    stoneEntity.setSent(true);
                    stoneEntity.setShouldntDamage(false);
                    stoneEntity.setTickCount(440);
                }
                if (stoneEntity == null) {
                    player.getPersistentData().putInt("matterAccelerationBlockTimer", 0);
                }
            }
            if (player.level().dimension() == Level.NETHER) {
                NetherrackEntity netherrackEntity = player.level().getEntitiesOfClass(NetherrackEntity.class, player.getBoundingBox().inflate(10))
                        .stream()
                        .min(Comparator.comparingDouble(zombie -> zombie.distanceTo(player)))
                        .orElse(null);
                if (netherrackEntity != null) {
                    netherrackEntity.setDeltaMovement(lookDirection);
                    netherrackEntity.setSent(true);
                    netherrackEntity.setShouldDamage(true);
                    netherrackEntity.setTickCount(440);
                }
                if (netherrackEntity == null) {
                    player.getPersistentData().putInt("matterAccelerationBlockTimer", 0);
                }
            }
            if (player.level().dimension() == Level.END) {
                EndStoneEntity endStoneEntity = player.level().getEntitiesOfClass(EndStoneEntity.class, player.getBoundingBox().inflate(10))
                        .stream()
                        .min(Comparator.comparingDouble(zombie -> zombie.distanceTo(player)))
                        .orElse(null);
                if (endStoneEntity != null) {
                    endStoneEntity.setDeltaMovement(lookDirection);
                    endStoneEntity.setSent(true);
                    endStoneEntity.setShouldntDamage(false);
                    endStoneEntity.setTickCount(440);
                }
                if (endStoneEntity == null) {
                    player.getPersistentData().putInt("matterAccelerationBlockTimer", 0);
                }
            }
        } else {
            int activeSlot = player.getInventory().selected;
            ItemStack heldItem = player.getMainHandItem();
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof MatterAccelerationBlocks) {
                player.getInventory().setItem(activeSlot, new ItemStack(ItemInit.MATTER_ACCELERATION_SELF.get()));
                heldItem.shrink(1);
            }
        }
    }
}
