package net.swimmingtuna.lotm.item.BeyonderAbilities;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.swimmingtuna.lotm.beyonder.api.BeyonderClass;
import net.swimmingtuna.lotm.caps.BeyonderHolder;
import net.swimmingtuna.lotm.caps.BeyonderHolderAttacher;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public abstract class SimpleAbilityItem extends Item implements Ability {

    protected final Supplier<? extends BeyonderClass> requiredClass;
    protected final int requiredSequence;
    protected final int requiredSpirituality;
    protected final int cooldown;

    protected SimpleAbilityItem(Properties properties, BeyonderClass requiredClass, int requiredSequence, int requiredSpirituality, int cooldown) {
        this(properties, () -> requiredClass, requiredSequence, requiredSpirituality, cooldown);
    }

    protected SimpleAbilityItem(Properties properties, Supplier<? extends BeyonderClass> requiredClass, int requiredSequence, int requiredSpirituality, int cooldown) {
        super(properties);
        this.requiredClass = requiredClass;
        this.requiredSequence = requiredSequence;
        this.requiredSpirituality = requiredSpirituality;
        this.cooldown = cooldown;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            BeyonderHolder holder = BeyonderHolderAttacher.getHolderUnwrap(player);

            boolean hasAllRequirements = checkAll(player);
            if (!hasAllRequirements) return InteractionResultHolder.fail(player.getItemInHand(hand));

            if (holder.useSpirituality(this.requiredSpirituality)) {
                if (!player.isCreative()) {
                    player.getCooldowns().addCooldown(this, this.cooldown);
                }
                useAbility(level, player, hand);
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    protected boolean checkAll(Player player) {
        return checkAll(player, this.requiredClass.get(), this.requiredSequence, this.requiredSpirituality);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (!level.isClientSide()) {
            BeyonderHolder holder = BeyonderHolderAttacher.getHolderUnwrap(player);

            if (!checkAll(player)) return InteractionResult.FAIL;

            if (holder.useSpirituality(this.requiredSpirituality)) {
                return useAbilityOnBlock(context);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (!player.level().isClientSide()) {
            BeyonderHolder holder = BeyonderHolderAttacher.getHolderUnwrap(player);

            boolean hasAllRequirements = checkAll(player);
            if (!hasAllRequirements) return InteractionResult.FAIL;

            if (holder.useSpirituality(this.requiredSpirituality)) {
                return useAbilityOnEntity(stack, player, interactionTarget, usedHand);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {

        tooltipComponents.add(getSpiritualityUsedText(this.requiredSpirituality));
        tooltipComponents.add(getCooldownText(this.cooldown));
        tooltipComponents.add(getPathwayText(this.requiredClass.get()));
        tooltipComponents.add(getClassText(this.requiredSequence, this.requiredClass.get()));

        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    public static Component getSpiritualityUsedText(int requiredSpirituality) {
        return Component.literal("Spirituality Used: ").append(Component.literal(String.valueOf(requiredSpirituality)).withStyle(ChatFormatting.YELLOW));
    }

    public static Component getCooldownText(int cooldown) {
        return Component.literal("Cooldown: ").append(Component.literal(getTextForTicks(cooldown)).withStyle(ChatFormatting.YELLOW));
    }

    public static Component getPathwayText(BeyonderClass beyonderClass) {
        return Component.literal("Pathway: ").append(Component.literal(beyonderClass.sequenceNames().get(9)).withStyle(beyonderClass.getColorFormatting()));
    }

    public static Component getClassText(int requiredSequence, BeyonderClass beyonderClass) {
        return Component.literal("Class: ").append(Component.literal(requiredSequence + " - " + beyonderClass.sequenceNames().get(requiredSequence))
                .withStyle(beyonderClass.getColorFormatting()));
    }

    protected void baseHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    private static String getTextForTicks(int ticks) {
        int min = ticks / 1200;
        double sec = (double) (ticks % 1200) / 20;
        StringBuilder stringBuilder = new StringBuilder();
        if (min > 0) {
            stringBuilder.append(min).append(" Minute");
            if (min != 1) {
                stringBuilder.append("s");
            }
            if (sec > 0) {
                stringBuilder.append(" ");
            }
        }
        if (sec > 0) {
            stringBuilder.append(sec).append(" Second");
            if (sec != 1) {
                stringBuilder.append("s");
            }
        }
        return stringBuilder.toString();
    }

    private boolean sequenceAdvancedEnough(BeyonderHolder holder) {
        return holder.getCurrentSequence() <= this.requiredSequence;
    }

    private boolean requiredClassMatches(BeyonderHolder holder) {
        return holder.currentClassMatches(this.requiredClass);
    }

    private boolean spiritualitySufficient(BeyonderHolder holder) {
        return holder.getSpirituality() >= this.requiredSpirituality;
    }

    public static boolean checkRequiredClass(BeyonderHolder holder, Player player, BeyonderClass requiredClass) {
        if (!holder.currentClassMatches(requiredClass)) {
            String name = requiredClass.sequenceNames().get(9);

            player.displayClientMessage(
                    Component.literal("You are not of the ").withStyle(ChatFormatting.AQUA).append(
                            Component.literal(name).withStyle(ChatFormatting.YELLOW)).append(
                                    Component.literal(" Pathway").withStyle(ChatFormatting.AQUA)), true);
            return false;
        }
        return true;
    }

    public static boolean checkRequiredSequence(BeyonderHolder holder, Player player, int requiredSequence) {
        if (holder.getCurrentSequence() > requiredSequence) {
            player.displayClientMessage(
                    Component.literal("You need to be sequence ").withStyle(ChatFormatting.AQUA).append(
                            Component.literal(String.valueOf(requiredSequence)).withStyle(ChatFormatting.YELLOW)).append(
                                    Component.literal(" or lower to use this").withStyle(ChatFormatting.AQUA)), true);
            return false;
        }
        return true;
    }

    public static boolean checkSpirituality(BeyonderHolder holder, Player player, int requiredSpirituality) {
        if (holder.getSpirituality() < requiredSpirituality) {
            player.displayClientMessage(
                    Component.literal("You need ").withStyle(ChatFormatting.AQUA).append(
                            Component.literal(String.valueOf(requiredSpirituality)).withStyle(ChatFormatting.YELLOW)).append(
                            Component.literal(" spirituality to use this").withStyle(ChatFormatting.AQUA)), true);
            return false;
        }
        return true;
    }

    public static boolean checkAll(Player player, BeyonderClass requiredClass, int requiredSequence, int requiredSpirituality) {
        BeyonderHolder holder = BeyonderHolderAttacher.getHolderUnwrap(player);
        return checkRequiredClass(holder, player, requiredClass) && checkRequiredSequence(holder, player, requiredSequence) && checkSpirituality(holder, player, requiredSpirituality);
    }
}
