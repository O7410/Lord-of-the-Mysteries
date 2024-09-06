package net.swimmingtuna.lotm.item.BeyonderAbilities.Spectator.FinishedItems;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;
import net.swimmingtuna.lotm.LOTM;
import net.swimmingtuna.lotm.caps.BeyonderHolder;
import net.swimmingtuna.lotm.caps.BeyonderHolderAttacher;
import net.swimmingtuna.lotm.events.ReachChangeUUIDs;
import net.swimmingtuna.lotm.init.BeyonderClassInit;
import net.swimmingtuna.lotm.spirituality.ModAttributes;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = LOTM.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManipulateMovement extends Item {
    private final LazyOptional<Multimap<Attribute, AttributeModifier>> lazyAttributeMap = LazyOptional.of(() -> createAttributeMap());


    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pSlot) {
        if (pSlot == EquipmentSlot.MAINHAND) {
            return lazyAttributeMap.orElseGet(() -> createAttributeMap());
        }
        return super.getDefaultAttributeModifiers(pSlot);
    }

    private Multimap<Attribute, AttributeModifier> createAttributeMap() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
        attributeBuilder.putAll(super.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND));
        attributeBuilder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(ReachChangeUUIDs.BEYONDER_ENTITY_REACH, "Reach modifier", 300, AttributeModifier.Operation.ADDITION)); //adds a 12 block reach for interacting with entities
        attributeBuilder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(ReachChangeUUIDs.BEYONDER_BLOCK_REACH, "Reach modifier", 300, AttributeModifier.Operation.ADDITION)); //adds a 12 block reach for interacting with blocks, p much useless for this item
        return attributeBuilder.build();
    }

    public ManipulateMovement(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player pPlayer = pContext.getPlayer();
        BeyonderHolder holder = BeyonderHolderAttacher.getHolderUnwrap(pPlayer);
        int sequence = holder.getCurrentSequence();
        AttributeInstance dreamIntoReality = pPlayer.getAttribute(ModAttributes.DIR.get());
        if (!pPlayer.level().isClientSide()) {
            if (!holder.currentClassMatches(BeyonderClassInit.SPECTATOR)) {
                pPlayer.displayClientMessage(Component.literal("You are not of the Spectator pathway").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA), true);
            }
            if (holder.getSpirituality() < (int) 200 / dreamIntoReality.getValue()) {
                pPlayer.displayClientMessage(Component.literal("You need spirituality" + ((int) 200 / dreamIntoReality.getValue()) + " in order to use this").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA), true);
            }
        }
        if (holder.currentClassMatches(BeyonderClassInit.SPECTATOR) && sequence <= 4 && holder.useSpirituality(200)) {
            boolean x = pPlayer.getPersistentData().getBoolean("manipulateMovementBoolean");
            if (!x) {
                pPlayer.getPersistentData().putBoolean("manipulateMovementBoolean", true);
                BlockPos pos = pContext.getClickedPos();
                pPlayer.getPersistentData().putInt("manipulateMovementX", pos.getX());
                pPlayer.getPersistentData().putInt("manipulateMovementY", pos.getY());
                pPlayer.getPersistentData().putInt("manipulateMovementZ", pos.getZ());
                pPlayer.displayClientMessage(Component.literal("Manipulate Movement Position is " + pos.getX() + " " + pos.getY() + " " + pos.getZ()).withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA), true);

            }
            if (x) {
                pPlayer.getPersistentData().remove("manipulateMovementX");
                pPlayer.getPersistentData().remove("manipulateMovementY");
                pPlayer.getPersistentData().remove("manipulateMovementZ");
                pPlayer.getPersistentData().putBoolean("manipulateMovementBoolean", false);
                pPlayer.displayClientMessage(Component.literal("Manipulate Movement Position Reset").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA), true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        if (!Screen.hasShiftDown()) {
            componentList.add(Component.literal("Upon use, all living entities 150 blocks around you move to the location you clicked on\n" +
                    "Left Click for Apply Manipulation\n" +
                    "Spirituality Used: 200\n" +
                    "Cooldown: 30 seconds").withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(pStack, level, componentList, tooltipFlag);
    }
}