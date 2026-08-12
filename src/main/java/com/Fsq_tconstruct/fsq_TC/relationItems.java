package com.Fsq_tconstruct.fsq_TC;

import com.Fsq_tconstruct.fluid.ModFluids;
import com.Fsq_tconstruct.fsq_tconstruct;
import com.Fsq_tconstruct.fsq_TC.Tool.FsqToolParts;
import com.Fsq_tconstruct.fsq_TC.Tool.FsqTools;
import com.Fsq_tconstruct.item.fsq_items;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;

import java.util.function.Consumer;

public class relationItems extends RecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {
    public relationItems(PackOutput output) { super(output); }

    @Override
    public String getModId() {
        return fsq_tconstruct.MODID;
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        // 材料转化配方：将自定义物品转化为 TiC 材料值
        materialRecipe(consumer, fsq_Materials.zxc,
                Ingredient.of(fsq_items.ONE_ITEM.get()), 1, 1,
                "tools/materials/first/item");
        // 晶钻：物品 → 晶钻材质
        materialRecipe(consumer, fsq_Materials.Amethyst_shardDiamond,
                Ingredient.of(fsq_items.Amethyst_shardDiamond.get()), 1, 1,
                "tools/materials/first/amethyst_diamond");
        // 魂之精：物品 → 魂之精材质
        materialRecipe(consumer,fsq_Materials.M_ESSENCE_SOUL,Ingredient.of(fsq_items.ESSENCE_SOUL.get()),1,1,
                "tools/materials/first/essence_soul");
        // 火之精：物品 → 火之精材质
        materialRecipe(consumer,fsq_Materials.FIRE_CRYSTAL,Ingredient.of(fsq_items.FIRE_CRYSTAL.get()),1,1,
                "tools/materials/first/fire_crystal");
        // 凋骨合金：物品 → 凋骨合金材质
        materialRecipe(consumer,fsq_Materials.WITHER_BONE,Ingredient.of(fsq_items.WITHER_BONE.get()),1,1,
                "tools/materials/first/wither_bone");
        // 龙钢：物品 → 龙钢材质
        materialRecipe(consumer,fsq_Materials.DRAGON_STEEL,Ingredient.of(fsq_items.DRAGON_STEEL.get()),1,1,
                "tools/materials/first/dragon_steel");
        // 强化银：物品 → 强化银材质
        materialRecipe(consumer,fsq_Materials.REINFORCED_SILVER,Ingredient.of(fsq_items.REINFORCED_SILVER.get()),1,1,
                "tools/materials/first/reinforced_silver");
        // 涂毒骨：物品 → 涂毒骨材质
        materialRecipe(consumer,fsq_Materials.POISON_COATED_BONE,Ingredient.of(fsq_items.POISON_COATED_BONE.get()),1,1,
                "tools/materials/first/poison_coated_bone");

        // ===== 晶钻 (Amethyst Diamond) 流体及浇筑 =====
        // 材料熔炼配方：将晶钻材质的所有部件熔炼为熔融晶钻流体
        materialMelting(consumer, fsq_Materials.Amethyst_shardDiamond,
                ModFluids.MOLTEN_AMETHYST_DIAMOND.get(), FluidValues.INGOT, "tools/materials/first/");
        // 材料流体映射配方：生成 MaterialFluidRecipe（流体→材质），铸造台需要此配方才能将熔融流体映射回材质，进而铸造出十字结部件
        MaterialFluidRecipeBuilder.material(fsq_Materials.Amethyst_shardDiamond)
                .setFluidAndTemp(new FluidStack(ModFluids.MOLTEN_AMETHYST_DIAMOND.get(), FluidValues.INGOT))
                .save(consumer, location("tools/materials/first/casting/" + fsq_Materials.Amethyst_shardDiamond.getLocation('_').getPath()));
        // 直接物品融化配方：使冶炼炉能直接融化 Amethyst_shardDiamond 物品（该物品为普通 Item，无材质 NBT，无法被 MaterialMeltingRecipe 匹配）
        MeltingRecipeBuilder.melting(Ingredient.of(fsq_items.Amethyst_shardDiamond.get()),
                ModFluids.MOLTEN_AMETHYST_DIAMOND.get(), FluidValues.INGOT, 1.0f)
                .save(consumer, location("tools/materials/first/melting/amethyst_diamond_item"));

        // 十字结部件配方：部件建造器 + 金/砂模具浇铸 + 复合铸造 + 铸造模具制作
        partRecipes(consumer, FsqToolParts.CROSS_KNOT, FsqToolParts.CROSS_KNOT_CAST, 1,
                "tools/parts/", "smeltery/casts/");
        //魂珠配方1 部件建造器 + 金/砂模具浇铸 + 复合铸造 + 铸造模具制作
        partRecipes(consumer, FsqToolParts.SOUL_ORB, FsqToolParts.SOUL_ORB_CAST, 1,
                "tools/parts/", "smeltery/casts/");


        // 西洋剑工具组装配方：在工匠组装台上合成
        toolBuilding(consumer, FsqTools.XJ, "tools/building/");
        // 魂戈工具组装配方：在工匠组装台上合成
        toolBuilding(consumer, FsqTools.HG, "tools/building/");

        // ===== 凋骨合金 (Wither Bone) 流体及浇筑 =====
        // 材料熔炼配方：将凋骨合金材质的所有部件熔炼为熔融凋骨合金流体
        materialMelting(consumer, fsq_Materials.WITHER_BONE,
                ModFluids.MOLTEN_WITHER_BONE.get(), FluidValues.INGOT, "tools/materials/first/");
        // 材料流体映射配方：流体→材质（铸造台浇筑部件用）
        MaterialFluidRecipeBuilder.material(fsq_Materials.WITHER_BONE)
                .setFluidAndTemp(new FluidStack(ModFluids.MOLTEN_WITHER_BONE.get(), FluidValues.INGOT))
                .save(consumer, location("tools/materials/first/casting/" + fsq_Materials.WITHER_BONE.getLocation('_').getPath()));
        // 直接物品融化配方：使冶炼炉能直接融化 wither_bone 物品（普通 Item，无材质 NBT，需单独匹配）
        MeltingRecipeBuilder.melting(Ingredient.of(fsq_items.WITHER_BONE.get()),
                ModFluids.MOLTEN_WITHER_BONE.get(), FluidValues.INGOT, 1.0f)
                .save(consumer, location("tools/materials/first/melting/wither_bone_item"));

        // ===== 龙钢 (Dragon Steel) 流体及浇筑 =====
        // 材料熔炼配方：将龙钢材质的所有部件熔炼为熔融龙钢流体
        materialMelting(consumer, fsq_Materials.DRAGON_STEEL,
                ModFluids.MOLTEN_DRAGON_STEEL.get(), FluidValues.INGOT, "tools/materials/first/");
        // 材料流体映射配方：流体→材质（铸造台浇筑部件用）
        MaterialFluidRecipeBuilder.material(fsq_Materials.DRAGON_STEEL)
                .setFluidAndTemp(new FluidStack(ModFluids.MOLTEN_DRAGON_STEEL.get(), FluidValues.INGOT))
                .save(consumer, location("tools/materials/first/casting/" + fsq_Materials.DRAGON_STEEL.getLocation('_').getPath()));
        // 直接物品融化配方：使冶炼炉能直接融化 dragon_steel 物品
        MeltingRecipeBuilder.melting(Ingredient.of(fsq_items.DRAGON_STEEL.get()),
                ModFluids.MOLTEN_DRAGON_STEEL.get(), FluidValues.INGOT, 1.0f)
                .save(consumer, location("tools/materials/first/melting/dragon_steel_item"));

        // ===== 强化银 (Reinforced Silver) 流体及浇筑 =====
        // 材料熔炼配方：将强化银材质的所有部件熔炼为熔融强化银流体（温度1400）
        materialMelting(consumer, fsq_Materials.REINFORCED_SILVER,
                ModFluids.MOLTEN_REINFORCED_SILVER.get(), FluidValues.INGOT, "tools/materials/first/");
        // 材料流体映射配方：流体→材质（铸造台浇筑部件用）
        MaterialFluidRecipeBuilder.material(fsq_Materials.REINFORCED_SILVER)
                .setFluidAndTemp(new FluidStack(ModFluids.MOLTEN_REINFORCED_SILVER.get(), FluidValues.INGOT))
                .save(consumer, location("tools/materials/first/casting/" + fsq_Materials.REINFORCED_SILVER.getLocation('_').getPath()));
        // 直接物品融化配方：使冶炼炉能直接融化 reinforced_silver 物品
        MeltingRecipeBuilder.melting(Ingredient.of(fsq_items.REINFORCED_SILVER.get()),
                ModFluids.MOLTEN_REINFORCED_SILVER.get(), FluidValues.INGOT, 1.0f)
                .save(consumer, location("tools/materials/first/melting/reinforced_silver_item"));
    }
}