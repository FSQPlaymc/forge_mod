package com.Fsq_tconstruct.fluid;

import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluidBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, fsq_tconstruct.MODID);

    public static final RegistryObject<LiquidBlock> MOLTEN_AMETHYST_DIAMOND_BLOCK = BLOCKS.register("molten_amethyst_diamond",
            () -> new LiquidBlock(() -> (FlowingFluid) ModFluids.MOLTEN_AMETHYST_DIAMOND.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE).noCollission().strength(100f).noLootTable()));
    public static final RegistryObject<LiquidBlock> MOLTEN_WITHER_BONE_BLOCK = BLOCKS.register("molten_wither_bone",
            () -> new LiquidBlock(() -> (FlowingFluid) ModFluids.MOLTEN_WITHER_BONE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK).noCollission().strength(100f).noLootTable()));
    public static final RegistryObject<LiquidBlock> MOLTEN_DRAGON_STEEL_BLOCK = BLOCKS.register("molten_dragon_steel",
            () -> new LiquidBlock(() -> (FlowingFluid) ModFluids.MOLTEN_DRAGON_STEEL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED).noCollission().strength(100f).noLootTable()));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
