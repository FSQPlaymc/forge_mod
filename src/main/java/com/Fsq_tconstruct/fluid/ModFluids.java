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
    // 流体注册表：用于注册源流体与流动流体
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, fsq_tconstruct.MODID);
    // 流体类型注册表：定义流体的温度、密度、粘滞度、声音与客户端渲染纹理等属性
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, fsq_tconstruct.MODID);

    // ===== 熔融晶钻 (Amethyst Diamond) =====
    // 源流体：冶炼炉/铸造台中的主流体形态
    public static final RegistryObject<Fluid> MOLTEN_AMETHYST_DIAMOND = FLUIDS.register("molten_amethyst_diamond",
            () -> new ForgeFlowingFluid.Source(properties()));
    // 流动流体：流体流动时的派生形态（自动生成）
    public static final RegistryObject<Fluid> MOLTEN_AMETHYST_DIAMOND_FLOWING = FLUIDS.register("molten_amethyst_diamond_flowing",
            () -> new ForgeFlowingFluid.Flowing(properties()));
    // 流体类型：温度1250 光照10 密度2000 粘滞度10000，音效复用岩浆
    public static final RegistryObject<FluidType> MOLTEN_AMETHYST_DIAMOND_TYPE = FLUID_TYPES.register("molten_amethyst_diamond",
            () -> new FluidType(FluidType.Properties.create()
                    .temperature(1250).lightLevel(10).density(2000).viscosity(10000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        // 静态纹理：block/molten_amethyst_diamond_still.png
                        @Override
                        public ResourceLocation getStillTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_amethyst_diamond_still");
                        }

                        // 流动纹理：block/molten_amethyst_diamond_flow.png
                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_amethyst_diamond_flow");
                        }

                        // 染色：白色（使用贴图原始颜色）
                        @Override
                        public int getTintColor() {
                            return 0xFFFFFFFF;
                        }
                    });
                }
            });

    // ===== 熔融凋骨合金 (Wither Bone) =====
    public static final RegistryObject<Fluid> MOLTEN_WITHER_BONE = FLUIDS.register("molten_wither_bone",
            () -> new ForgeFlowingFluid.Source(propertiesWitherBone()));
    public static final RegistryObject<Fluid> MOLTEN_WITHER_BONE_FLOWING = FLUIDS.register("molten_wither_bone_flowing",
            () -> new ForgeFlowingFluid.Flowing(propertiesWitherBone()));
    // 流体类型：温度1350 光照8 密度2500 粘滞度12000
    public static final RegistryObject<FluidType> MOLTEN_WITHER_BONE_TYPE = FLUID_TYPES.register("molten_wither_bone",
            () -> new FluidType(FluidType.Properties.create()
                    .temperature(1350).lightLevel(8).density(2500).viscosity(12000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        // 静态纹理：block/molten_wither_bone_still.png
                        @Override
                        public ResourceLocation getStillTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_wither_bone_still");
                        }

                        // 流动纹理：block/molten_wither_bone_flow.png
                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_wither_bone_flow");
                        }

                        // 染色：深紫黑（凋灵主题色）
                        @Override
                        public int getTintColor() {
                            return 0xFF2d2d3a;
                        }
                    });
                }
            });

    // ===== 熔融龙钢 (Dragon Steel) =====
    public static final RegistryObject<Fluid> MOLTEN_DRAGON_STEEL = FLUIDS.register("molten_dragon_steel",
            () -> new ForgeFlowingFluid.Source(propertiesDragonSteel()));
    public static final RegistryObject<Fluid> MOLTEN_DRAGON_STEEL_FLOWING = FLUIDS.register("molten_dragon_steel_flowing",
            () -> new ForgeFlowingFluid.Flowing(propertiesDragonSteel()));
    // 流体类型：温度1500 光照12 密度3000 粘滞度15000（最高温金属）
    public static final RegistryObject<FluidType> MOLTEN_DRAGON_STEEL_TYPE = FLUID_TYPES.register("molten_dragon_steel",
            () -> new FluidType(FluidType.Properties.create()
                    .temperature(1500).lightLevel(12).density(3000).viscosity(15000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        // 静态纹理：block/molten_dragon_steel_still.png
                        @Override
                        public ResourceLocation getStillTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_dragon_steel_still");
                        }

                        // 流动纹理：block/molten_dragon_steel_flow.png
                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_dragon_steel_flow");
                        }

                        // 染色：暗红（龙钢主题色）
                        @Override
                        public int getTintColor() {
                            return 0xFF5c1010;
                        }
                    });
                }
            });

    // ===== 熔融强化银 (Reinforced Silver) =====
    public static final RegistryObject<Fluid> MOLTEN_REINFORCED_SILVER = FLUIDS.register("molten_reinforced_silver",
            () -> new ForgeFlowingFluid.Source(propertiesReinforcedSilver()));
    public static final RegistryObject<Fluid> MOLTEN_REINFORCED_SILVER_FLOWING = FLUIDS.register("molten_reinforced_silver_flowing",
            () -> new ForgeFlowingFluid.Flowing(propertiesReinforcedSilver()));
    // 流体类型：温度1400 光照6 密度2500 粘滞度10000
    public static final RegistryObject<FluidType> MOLTEN_REINFORCED_SILVER_TYPE = FLUID_TYPES.register("molten_reinforced_silver",
            () -> new FluidType(FluidType.Properties.create()
                    .temperature(1400).lightLevel(6).density(2500).viscosity(10000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        // 静态纹理：block/molten_reinforced_silver_still.png
                        @Override
                        public ResourceLocation getStillTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_reinforced_silver_still");
                        }

                        // 流动纹理：block/molten_reinforced_silver_flow.png
                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "block/molten_reinforced_silver_flow");
                        }

                        // 染色：亮银灰（与材质色 bed4d0 一致）
                        @Override
                        public int getTintColor() {
                            return 0xFFbed4d0;
                        }
                    });
                }
            });

    // ===== 晶钻流体属性 =====
    private static ForgeFlowingFluid.Properties properties() {
        return new ForgeFlowingFluid.Properties(
                MOLTEN_AMETHYST_DIAMOND_TYPE, MOLTEN_AMETHYST_DIAMOND, MOLTEN_AMETHYST_DIAMOND_FLOWING)
                .block(ModFluidBlocks.MOLTEN_AMETHYST_DIAMOND_BLOCK)
                .bucket(fsq_items.MOLTEN_AMETHYST_DIAMOND_BUCKET);
    }

    // ===== 凋骨合金流体属性 =====
    private static ForgeFlowingFluid.Properties propertiesWitherBone() {
        return new ForgeFlowingFluid.Properties(
                MOLTEN_WITHER_BONE_TYPE, MOLTEN_WITHER_BONE, MOLTEN_WITHER_BONE_FLOWING)
                .block(ModFluidBlocks.MOLTEN_WITHER_BONE_BLOCK)
                .bucket(fsq_items.MOLTEN_WITHER_BONE_BUCKET);
    }

    // ===== 龙钢流体属性 =====
    private static ForgeFlowingFluid.Properties propertiesDragonSteel() {
        return new ForgeFlowingFluid.Properties(
                MOLTEN_DRAGON_STEEL_TYPE, MOLTEN_DRAGON_STEEL, MOLTEN_DRAGON_STEEL_FLOWING)
                .block(ModFluidBlocks.MOLTEN_DRAGON_STEEL_BLOCK)
                .bucket(fsq_items.MOLTEN_DRAGON_STEEL_BUCKET);
    }

    // ===== 强化银流体属性 =====
    private static ForgeFlowingFluid.Properties propertiesReinforcedSilver() {
        return new ForgeFlowingFluid.Properties(
                MOLTEN_REINFORCED_SILVER_TYPE, MOLTEN_REINFORCED_SILVER, MOLTEN_REINFORCED_SILVER_FLOWING)
                .block(ModFluidBlocks.MOLTEN_REINFORCED_SILVER_BLOCK)
                .bucket(fsq_items.MOLTEN_REINFORCED_SILVER_BUCKET);
    }

    // 注册流体类型与流体到 Mod 事件总线
    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }
}
