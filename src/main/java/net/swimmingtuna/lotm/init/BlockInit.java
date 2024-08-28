package net.swimmingtuna.lotm.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.swimmingtuna.lotm.LOTM;
import net.swimmingtuna.lotm.blocks.glass_pane.*;
import net.swimmingtuna.lotm.blocks.spectator_blocks.CathedralBlock;
import net.swimmingtuna.lotm.blocks.spectator_blocks.MindscapeBlock;
import net.swimmingtuna.lotm.blocks.spectator_blocks.MindscapeOutsideBlock;
import net.swimmingtuna.lotm.blocks.spectator_blocks.VisionaryBB;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, LOTM.MOD_ID);

    public static final RegistryObject<Block> VISIONARY_BARRIER_BLOCK = registerBlock("visionary_barrier_block",
            () -> new VisionaryBB(BlockBehaviour.Properties.copy(Blocks.GLASS).noLootTable()));

    public static final RegistryObject<Block> CATHEDRAL_BLOCK = registerBlock("cathedral_block",
            () -> new CathedralBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));

    public static final RegistryObject<Block> MINDSCAPE_BLOCK = registerBlock("mindscape_block",
            () -> new MindscapeBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));

    public static final RegistryObject<Block> MINDSCAPE_OUTSIDE = registerBlock("mindscape_outside",
            () -> new MindscapeOutsideBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));

    public static final RegistryObject<Block> LOTM_DEEPSLATE_BRICKS = registerBlock("lotm_deepslate_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.DEEPSLATE_BRICKS)));

    public static final RegistryObject<Block> LOTM_REDSTONE_BLOCK = registerBlock("lotm_redstone_block",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.NETHER_ORE)));

    public static final RegistryObject<Block> LOTM_SANDSTONE = registerBlock("lotm_sandstone",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_POLISHED_DIORITE = registerBlock("lotm_polished_diorite",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_DARK_OAK_PLANKS = registerBlock("lotm_dark_oak_planks",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LOTM_QUARTZ = registerBlock("lotm_quartz",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_CHISELED_STONE_BRICKS = registerBlock("lotm_chiseled_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_MANGROVE_PLANKS = registerBlock("lotm_mangrove_planks",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LOTM_SPRUCE_PLANKS = registerBlock("lotm_spruce_planks",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LOTM_BIRCH_PLANKS = registerBlock("lotm_birch_planks",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LOTM_BLACK_CONCRETE = registerBlock("lotm_black_concrete",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_STONE = registerBlock("lotm_stone_block",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_BLUE_CONCRETE = registerBlock("lotm_blue_concrete",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_BLACKSTONE = registerBlock("lotm_blackstone",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_WHITE_CONCRETE = registerBlock("lotm_white_concrete",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_POLISHED_ANDESITE = registerBlock("lotm_polished_andesite",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_SPRUCE_LOG = registerBlock("lotm_spruce_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LOTM_POLISHED_BLACKSTONE = registerBlock("lotm_polished_blackstone",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.STONE)));

    public static final RegistryObject<Block> LOTM_SEA_LANTERN = registerBlock("lotm_sea_lantern",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.GLASS).lightLevel((state) -> 15)));

    public static final RegistryObject<Block> LOTM_OAK_LOG = registerBlock("lotm_oak_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LOTM_BOOKSHELF = registerBlock("lotm_bookshelf",
            () -> new Block(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_OUTSIDE.get()).sound(SoundType.WOOD)));



    public static final RegistryObject<Block> LOTM_CHAIN = registerBlock("lotm_chain",
            () -> new ChainBlock(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_BLOCK.get()).sound(SoundType.CHAIN)));


    public static final RegistryObject<Block> LOTM_LANTERN = registerBlock("lotm_lantern",
            () -> new LanternBlock(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_BLOCK.get()).sound(SoundType.LANTERN)));


    public static final RegistryObject<Block> LOTM_LIGHT_BLUE_CARPET = registerBlock("lotm_light_blue_carpet",
            () -> new CarpetBlock(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_BLOCK.get()).sound(SoundType.WOOL)));


    public static final RegistryObject<Block> LOTM_QUARTZ_STAIRS = registerBlock("lotm_quartz_stairs",
            () -> new StairBlock(() -> BlockInit.MINDSCAPE_BLOCK.get().defaultBlockState(),
            BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).sound(SoundType.NETHER_ORE)));

    public static final RegistryObject<Block> LOTM_DEEPSLATEBRICK_STAIRS = registerBlock("lotm_deepslatebrick_stairs",
            () -> new StairBlock(() -> BlockInit.MINDSCAPE_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).sound(SoundType.DEEPSLATE_BRICKS)));

    public static final RegistryObject<Block> LOTM_DARKOAK_STAIRS = registerBlock("lotm_darkoak_stairs",
            () -> new StairBlock(() -> BlockInit.MINDSCAPE_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LOTM_OAK_STAIRS = registerBlock("lotm_oak_stairs",
            () -> new StairBlock(() -> BlockInit.MINDSCAPE_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).sound(SoundType.WOOD)));


    public static final RegistryObject<Block> LOTM_DARKOAK_SLAB = registerBlock("lotm_darkoak_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_BLOCK.get()).sound(SoundType.WOOD)));

    public static final RegistryObject<Block> LOTM_QUARTZ_SLAB = registerBlock("lotm_quartz_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(BlockInit.MINDSCAPE_BLOCK.get()).sound(SoundType.NETHER_ORE)));


    public static final RegistryObject<Block> VISIONARY_BLACK_STAINED_GLASS_PANE = registerBlock("lotm_black_stained_glass_pane",
            () -> new LOTMBlackStainedGlassPane(DyeColor.BLACK, BlockBehaviour.Properties.copy(Blocks.GLASS).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_WHITE_STAINED_GLASS_PANE = registerBlock("lotm_white_stained_glass_pane",
            () -> new LOTMWhiteStainedGlassPane(DyeColor.WHITE, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_LIGHT_GRAY_STAINED_GLASS_PANE = registerBlock("lotm_light_gray_stained_glass_pane",
            () -> new LOTMLightGrayStainedGlassPane(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_GRAY_STAINED_GLASS_PANE = registerBlock("lotm_gray_stained_glass_pane",
            () -> new LOTMGrayStainedGlassPane(DyeColor.GRAY, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_BROWN_STAINED_GLASS_PANE = registerBlock("lotm_brown_stained_glass_pane",
            () -> new LOTMBrownStainedGlassPane(DyeColor.BROWN, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_PURPLE_STAINED_GLASS_PANE = registerBlock("lotm_purple_stained_glass_pane",
            () -> new LOTMPurpleStainedGlassPane(DyeColor.PURPLE, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_MAGENTA_STAINED_GLASS_PANE = registerBlock("lotm_magenta_stained_glass_pane",
            () -> new LOTMMagentaStainedGlassPane(DyeColor.MAGENTA, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_BLUE_STAINED_GLASS_PANE = registerBlock("lotm_blue_stained_glass_pane",
            () -> new LOTMBlueStainedGlassPane(DyeColor.BLUE, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_CYAN_STAINED_GLASS_PANE = registerBlock("lotm_cyan_stained_glass_pane",
            () -> new LOTMCyanStainedGlassPane(DyeColor.CYAN, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_LIGHT_BLUE_STAINED_GLASS_PANE = registerBlock("lotm_light_blue_stained_glass_pane",
            () -> new LOTMLightBlueStainedGlassPane(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_GREEN_STAINED_GLASS_PANE = registerBlock("lotm_green_stained_glass_pane",
            () -> new LOTMGreenStainedGlassPane(DyeColor.GREEN, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_YELLOW_STAINED_GLASS_PANE = registerBlock("lotm_yellow_stained_glass_pane",
            () -> new LOTMYellowStainedGlassPane(DyeColor.YELLOW, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_PINK_STAINED_GLASS_PANE = registerBlock("lotm_pink_stained_glass_pane",
            () -> new LOTMPinkStainedGlassPane(DyeColor.PINK, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_ORANGE_STAINED_GLASS_PANE = registerBlock("lotm_orange_stained_glass_pane",
            () -> new LOTMOrangeStainedGlassPane(DyeColor.ORANGE, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_RED_STAINED_GLASS_PANE = registerBlock("lotm_red_stained_glass_pane",
            () -> new LOTMRedStainedGlassPane(DyeColor.RED, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));
    public static final RegistryObject<Block> VISIONARY_LIME_STAINED_GLASS_PANE = registerBlock("lotm_lime_stained_glass_pane",
            () -> new LOTMLimeStainedGlassPane(DyeColor.LIME, BlockBehaviour.Properties.copy(Blocks.OBSIDIAN).noLootTable()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
