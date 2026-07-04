package com.Fsq_tconstruct.MySeared_melter.client;

import com.Fsq_tconstruct.MySeared_melter.CustomMelterBlockEntity;
import com.Fsq_tconstruct.MySeared_melter.CustomMelterContainerMenu;
import com.Fsq_tconstruct.MySeared_melter.ProcessingInventory;

import com.Fsq_tconstruct.fsq_tconstruct;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import slimeknights.mantle.client.screen.ElementScreen;
import slimeknights.mantle.client.screen.ScalableElementScreen;
import slimeknights.tconstruct.library.client.GuiUtil;
import slimeknights.tconstruct.smeltery.block.entity.module.FuelModule;
import slimeknights.tconstruct.smeltery.client.screen.module.GuiFuelModule;

public class CustomMelterScreen extends AbstractContainerScreen<CustomMelterContainerMenu> {

    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(fsq_tconstruct.MODID, "textures/gui/custom_melter.png");

    private static final ElementScreen FUEL_SLOT = new ElementScreen(BACKGROUND, 176, 52, 18, 36, 256, 256);
    private static final ElementScreen FUEL_TANK = new ElementScreen(BACKGROUND, 194, 52, 14, 38, 256, 256);

    private static final ProgressBars PROGRESS_BARS = new ProgressBars(
            new ScalableElementScreen(BACKGROUND, 176, 150, 3, 16, 256, 256),
            new ScalableElementScreen(BACKGROUND, 179, 150, 3, 16, 256, 256),
            new ScalableElementScreen(BACKGROUND, 182, 150, 3, 16, 256, 256),
            new ScalableElementScreen(BACKGROUND, 185, 150, 3, 16, 256, 256)
    );

    private final GuiFuelModule fuel;
    private final ProcessingInventory processingInventory;

    public CustomMelterScreen(CustomMelterContainerMenu container, Inventory inv, Component name) {
        super(container, inv, name);
        CustomMelterBlockEntity be = container.getTile();
        if (be != null) {
            FuelModule fuelModule = be.getFuelModule();
            this.fuel = new GuiFuelModule(this, fuelModule, 153, 32, 12, 36, 152, 15, container.isHasFuelSlot(), BACKGROUND);
            this.processingInventory = be.getItemHandler();
        } else {
            this.fuel = null;
            this.processingInventory = null;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, x, y, partialTicks);
        this.renderTooltip(graphics, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        GuiUtil.drawBackground(graphics, this, BACKGROUND);

        if (fuel != null) {
            if (menu.isHasFuelSlot()) {
                FUEL_SLOT.draw(graphics, leftPos + 150, topPos + 31);
            } else {
                FUEL_TANK.draw(graphics, leftPos + 152, topPos + 31);
            }
            fuel.draw(graphics);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        int checkX = mouseX - this.leftPos;
        int checkY = mouseY - this.topPos;

        if (fuel != null) {
            fuel.renderHighlight(graphics, checkX, checkY);
        }

        drawHeatBars(graphics);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderTooltip(graphics, mouseX, mouseY);

        drawHeatTooltips(graphics, mouseX, mouseY);

        if (fuel != null) {
            fuel.addTooltip(graphics, mouseX, mouseY, true);
        }
    }

    private void drawHeatBars(GuiGraphics graphics) {
        if (processingInventory == null || menu.getTile() == null) return;
        int temperature = menu.getTile().getFuelModule().getTemperature();
        int currentTime = processingInventory.get(0);
        int requiredTime = processingInventory.get(1);
        int requiredTemp = processingInventory.get(2);
        int inputSlotCount = processingInventory.getInputSlots();
        for (int i = 0; i < inputSlotCount && i < menu.slots.size(); i++) {
            var slot = menu.slots.get(i);
            if (slot.hasItem()) {
                ScalableElementScreen bar = PROGRESS_BARS.progress;
                float progress = 1f;

                if (requiredTime == 0) {
                    bar = PROGRESS_BARS.unmeltable;
                } else if (requiredTemp > temperature) {
                    bar = PROGRESS_BARS.noHeat;
                } else if (currentTime < 0) {
                    bar = PROGRESS_BARS.noSpace;
                } else if (currentTime <= requiredTime) {
                    progress = (float) currentTime / (float) requiredTime;
                }

                GuiUtil.drawProgressUp(graphics, bar, slot.x - 4, slot.y, progress);
            }
        }
    }

    private void drawHeatTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        if (processingInventory == null || menu.getTile() == null) return;
        int checkX = mouseX - leftPos;
        int checkY = mouseY - topPos;
        int temperature = menu.getTile().getFuelModule().getTemperature();
        int currentTime = processingInventory.get(0);
        int requiredTime = processingInventory.get(1);
        int requiredTemp = processingInventory.get(2);
        int inputSlotCount = processingInventory.getInputSlots();

        for (int i = 0; i < inputSlotCount && i < menu.slots.size(); i++) {
            var slot = menu.slots.get(i);
            if (slot.hasItem()) {
                int barX = slot.x - 4;
                int barY = slot.y - 1;
                if (GuiUtil.isHovered(checkX, checkY, barX, barY, PROGRESS_BARS.width() + 1, PROGRESS_BARS.height() + 2)) {
                    Component tooltip = null;
                    if (requiredTime == 0) {
                        tooltip = Component.translatable("gui.fsq_tconstruct.custom_melter.no_recipe");
                    } else if (requiredTemp > temperature) {
                        tooltip = Component.translatable("gui.fsq_tconstruct.custom_melter.no_heat");
                    } else if (currentTime < 0) {
                        tooltip = Component.translatable("gui.fsq_tconstruct.custom_melter.no_space");
                    }

                    if (tooltip != null) {
                        graphics.renderTooltip(font, tooltip, mouseX, mouseY);
                    }
                    break;
                }
            }
        }
    }

    private record ProgressBars(ScalableElementScreen progress, ScalableElementScreen noHeat, ScalableElementScreen noSpace, ScalableElementScreen unmeltable) {
        public int width() { return progress.w; }
        public int height() { return progress.h; }
    }
}
