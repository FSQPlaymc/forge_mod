package com.Fsq_tconstruct.MySeared_melter;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import slimeknights.mantle.inventory.SmartItemHandlerSlot;
import slimeknights.mantle.util.sync.ValidZeroDataSlot;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.shared.inventory.TriggeringBaseContainerMenu;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CustomMelterContainerMenu extends TriggeringBaseContainerMenu<CustomMelterBlockEntity> {

    private final Slot[] inputs;
    private boolean hasFuelSlot = false;

    public CustomMelterContainerMenu(int id, @Nullable Inventory inv, @Nullable CustomMelterBlockEntity be) {
        super(ModRegistries.CUSTOM_MELTER_MENU.get(), id, inv, be);

        if (be != null) {
            ProcessingInventory inventory = be.getItemHandler();
            int inputCount = inventory.getInputSlots();
            inputs = new Slot[inventory.getSlots()];

            int[][] positions = {{22, 16}, {40, 16}, {22, 34}, {40, 34}};
            for (int i = 0; i < inputCount; i++) {
                inputs[i] = this.addSlot(new SmartItemHandlerSlot(inventory, i, positions[i][0], positions[i][1]));
            }

            inputs[inputCount] = this.addSlot(new SmartItemHandlerSlot(inventory, inputCount, 112, 25));

            Level world = be.getLevel();
            BlockPos down = be.getBlockPos().below();
            if (world != null && world.getBlockState(down).is(TinkerTags.Blocks.FUEL_TANKS)) {
                BlockEntity te = world.getBlockEntity(down);
                if (te != null) {
                    hasFuelSlot = te.getCapability(ForgeCapabilities.ITEM_HANDLER).filter(handler -> {
                        this.addSlot(new SmartItemHandlerSlot(handler, 0, 151, 32));
                        return true;
                    }).isPresent();
                }
            }

            this.addInventorySlots();

            Consumer<DataSlot> referenceConsumer = this::addDataSlot;
            ValidZeroDataSlot.trackIntArray(referenceConsumer, be.getFuelModule());
            inventory.trackInts(array -> ValidZeroDataSlot.trackIntArray(referenceConsumer, array));
        } else {
            inputs = new Slot[0];
        }
    }

    public CustomMelterContainerMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, getTileEntityFromBuf(buf, CustomMelterBlockEntity.class));
    }

    public boolean isHasFuelSlot() {
        return hasFuelSlot;
    }
}
