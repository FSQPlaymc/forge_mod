package com.Fsq_tconstruct.fsq_TC;

import com.Fsq_tconstruct.fluid.ModFluids;
import com.Fsq_tconstruct.fsq_tconstruct;
import com.Fsq_tconstruct.fsq_TC.Tool.FsqToolParts;
import com.Fsq_tconstruct.fsq_TC.Tool.FsqTools;
import com.Fsq_tconstruct.item.fsq_items;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;

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
        materialRecipe(consumer, fsq_Materials.Amethyst_shardDiamond,
                Ingredient.of(fsq_items.Amethyst_shardDiamond.get()), 1, 1,
                "tools/materials/first/amethyst_diamond");
        materialRecipe(consumer,fsq_Materials.M_ESSENCE_SOUL,Ingredient.of(fsq_items.ESSENCE_SOUL.get()),1,1,
                "tools/materials/first/essence_soul");
        materialRecipe(consumer,fsq_Materials.FIRE_CRYSTAL,Ingredient.of(fsq_items.FIRE_CRYSTAL.get()),1,1,
                "tools/materials/first/fire_crystal");

        // 材料熔炼配方：将材料熔化为流体
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
        toolBuilding(consumer, FsqTools.HJ, "tools/building/");
    }
}