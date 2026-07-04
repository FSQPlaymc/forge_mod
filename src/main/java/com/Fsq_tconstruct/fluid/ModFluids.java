package com.Fsq_tconstruct.fluid;

import com.Fsq_tconstruct.fsq_tconstruct;
import com.Fsq_tconstruct.item.fsq_items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, fsq_tconstruct.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, fsq_tconstruct.MODID);
    public static final RegistryObject<Fluid> MOLTEN_AMETHYST_DIAMOND = FLUIDS.register("molten_amethyst_diamond",
            () -> new ForgeFlowingFluid.Source(properties()));
    public static final RegistryObject<Fluid> MOLTEN_AMETHYST_DIAMOND_FLOWING = FLUIDS.register("molten_amethyst_diamond_flowing",
            () -> new ForgeFlowingFluid.Flowing(properties()));
    public static final RegistryObject<FluidType> MOLTEN_AMETHYST_DIAMOND_TYPE = FLUID_TYPES.register("molten_amethyst_diamond",
            () -> new FluidType(FluidType.Properties.create()
                    .temperature(1250).lightLevel(10).density(2000).viscosity(10000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        @Override
                        public ResourceLocation getStillTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_amethyst_diamond_still");
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_amethyst_diamond_flow");
                        }

                        @Override
                        public int getTintColor() {
                            return 0xFFFFFFFF;
                        }
                    });
                }
            });

    private static ForgeFlowingFluid.Properties properties() {
        return new ForgeFlowingFluid.Properties(
                MOLTEN_AMETHYST_DIAMOND_TYPE, MOLTEN_AMETHYST_DIAMOND, MOLTEN_AMETHYST_DIAMOND_FLOWING)
                .block(ModFluidBlocks.MOLTEN_AMETHYST_DIAMOND_BLOCK)
                .bucket(fsq_items.MOLTEN_AMETHYST_DIAMOND_BUCKET);
    }

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }
}
