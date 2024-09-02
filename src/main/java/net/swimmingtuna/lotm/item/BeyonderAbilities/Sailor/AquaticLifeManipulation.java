package net.swimmingtuna.lotm.item.BeyonderAbilities.Sailor;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import net.swimmingtuna.lotm.LOTM;
import net.swimmingtuna.lotm.caps.BeyonderHolder;
import net.swimmingtuna.lotm.caps.BeyonderHolderAttacher;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber(modid = LOTM.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AquaticLifeManipulation extends Item {
    public AquaticLifeManipulation(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player pPlayer, InteractionHand hand) {
        if (!level.isClientSide()) {
            BeyonderHolder holder = BeyonderHolderAttacher.getHolder(pPlayer).orElse(null);
            if (holder != null) {
                if (!holder.isSailorClass()) {
                    pPlayer.displayClientMessage(Component.literal("You are not of the Sailor pathway").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE), true);
                } else if (holder.getSpirituality() < 125) {
                    pPlayer.displayClientMessage(Component.literal("You need 125 spirituality in order to use this").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE), true);
                } else if (holder.isSailorClass() && holder.getCurrentSequence() <= 3 && holder.useSpirituality(125)) {
                    useItem(pPlayer);
                    if (!pPlayer.getAbilities().instabuild) {
                        pPlayer.getCooldowns().addCooldown(this, 200);
                    }
                }
            }
        }
        return super.use(level, pPlayer, hand);
    }

    public static void useItem(Player pPlayer) {
        BeyonderHolder holder = BeyonderHolderAttacher.getHolder(pPlayer).orElse(null);
        int sequence = holder.getCurrentSequence();
        if (!pPlayer.level().isClientSide()) {
            List<LivingEntity> aquaticEntities = pPlayer.level().getEntitiesOfClass(LivingEntity.class, pPlayer.getBoundingBox().inflate(50), entity -> entity instanceof WaterAnimal);
            if (!aquaticEntities.isEmpty()) {
                LivingEntity nearestAquaticEntity = aquaticEntities.stream().min(Comparator.comparingDouble(pPlayer::distanceTo)).orElse(null);
                if (nearestAquaticEntity != null) {
                    List<Player> nearbyPlayers = nearestAquaticEntity.level().getEntitiesOfClass(Player.class, nearestAquaticEntity.getBoundingBox().inflate(200 - (sequence * 20)));
                    Player nearestPlayer = nearbyPlayers.stream().filter(player -> player != pPlayer).min(Comparator.comparingDouble(nearestAquaticEntity::distanceTo)).orElse(null);
                    if (nearestPlayer != null) {
                        if (holder != null) {
                            if (sequence >= 2) {
                                pPlayer.sendSystemMessage(Component.literal("Nearest Player is " + nearestPlayer.getName().getString() + ". Pathway is " + holder.getCurrentClass()));
                            } else if (sequence <= 1) {
                                pPlayer.sendSystemMessage(Component.literal("Nearest Player is " + nearestPlayer.getName().getString() + ". Pathway is " + holder.getCurrentClass() + " sequence " + holder.getCurrentSequence()));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        if (!Screen.hasShiftDown()) {
            componentList.add(Component.literal("Upon use, communicates with any aquatic life around the player, if there is any, they communicate back with the information of any player within a range of the spoken to aquatic animal\n" +
                    "Spirituality Used: 100\n" +
                    "Cooldown: 2 seconds").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.BLUE));
        }
        super.appendHoverText(pStack, level, componentList, tooltipFlag);
    }
}
