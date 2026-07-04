package com.Fsq_tconstruct.MySeared_melter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import slimeknights.mantle.block.entity.NameableBlockEntity;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.smeltery.block.controller.ControllerBlock;
import slimeknights.tconstruct.smeltery.block.entity.module.SolidFuelModule;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomMelterBlockEntity extends NameableBlockEntity {

    private static final Component NAME = Component.translatable("gui.fsq_tconstruct.custom_melter");

    public static final BlockEntityTicker<CustomMelterBlockEntity> SERVER_TICKER =
            (level, pos, state, self) -> self.tick(level, pos, state);

    private final ProcessingInventory processingInventory = new ProcessingInventory(4, this::getLevel);
    private final LazyOptional<IItemHandler> inventoryHolder = LazyOptional.of(() -> processingInventory);

    private final SolidFuelModule fuelModule;
    private int tick;

    public CustomMelterBlockEntity(BlockPos pos, BlockState state) {
        super(ModRegistries.CUSTOM_MELTER_BE.get(), pos, state, NAME);
        this.fuelModule = new SolidFuelModule(this, pos.below());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new CustomMelterContainerMenu(id, inv, this);
    }

    public ProcessingInventory getItemHandler() {
        return processingInventory;
    }

    public SolidFuelModule getFuelModule() {
        return fuelModule;
    }

    private boolean isFormed() {
        BlockState state = getBlockState();
        return state.hasProperty(ControllerBlock.IN_STRUCTURE) && state.getValue(ControllerBlock.IN_STRUCTURE);
    }

    private void tick(Level level, BlockPos pos, BlockState state) {
        if (isFormed()) {
            switch (tick) {
                case 0 -> {
                    if (!fuelModule.hasFuel() && processingInventory.canHeat(fuelModule.findFuel(false))) {
                        fuelModule.findFuel(true);
                    }
                }
                case 2 -> {
                    boolean hasFuel = fuelModule.hasFuel();
                    if (state.getValue(ControllerBlock.ACTIVE) != hasFuel) {
                        level.setBlockAndUpdate(pos, state.setValue(ControllerBlock.ACTIVE, hasFuel));
                        BlockPos down = pos.below();
                        BlockState downState = level.getBlockState(down);
                        if (downState.is(TinkerTags.Blocks.FUEL_TANKS) && downState.hasProperty(ControllerBlock.ACTIVE) && downState.getValue(ControllerBlock.ACTIVE) != hasFuel) {
                            level.setBlockAndUpdate(down, downState.setValue(ControllerBlock.ACTIVE, hasFuel));
                        }
                    }
                    if (hasFuel) {
                        processingInventory.heatItems(fuelModule.getTemperature(), fuelModule.getRate());
                        fuelModule.decreaseFuel(1);
                    } else {
                        processingInventory.coolItems();
                    }
                }
            }
        } else if (tick == 2) {
            if (fuelModule.hasFuel()) {
                fuelModule.decreaseFuel(1);
            } else {
                processingInventory.coolItems();
            }
        }
        tick = (tick + 1) % 4;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryHolder.cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryHolder.invalidate();
    }

    @Override
    protected boolean shouldSyncOnUpdate() {
        return true;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        fuelModule.readFromTag(tag);
        if (tag.contains("inventory")) {
            processingInventory.readFromTag(tag.getCompound("inventory"));
        }
    }

    @Override
    public void saveSynced(CompoundTag tag) {
        super.saveSynced(tag);
        tag.put("inventory", processingInventory.writeToTag());
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        fuelModule.writeToTag(tag);
    }
}
