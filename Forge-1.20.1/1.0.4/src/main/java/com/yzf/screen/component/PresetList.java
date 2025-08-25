package com.yzf.screen.component;

import com.yzf.KeyPresetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class PresetList extends ObjectSelectionList<PresetList.PresetListEntry> {
    public PresetList(Minecraft mc, int width, int height, int top, int bottom, int entryHeight) {
        super(mc, width, height, top, bottom, entryHeight);
        addEntry();
    }

    public void addEntry() {
        for (var preset : KeyPresetManager.getPresets()) {
            addEntry(new PresetListEntry(this, preset));
        }
    }

    public void reload() {
        clearEntries();
        addEntry();
        if (this.getScrollAmount() > this.getMaxScroll()) {
            this.setScrollAmount(this.getMaxScroll());
        }
    }

    @Override
    protected int getScrollbarPosition() {
        return width - 6;
    }

    @Override
    public int getRowWidth() {
        return width - 30;
    }

    // 预设列表选项
    public static class PresetListEntry extends Entry<PresetListEntry> {
        private final PresetList parent;
        public final String text;

        public PresetListEntry(PresetList parent, String text) {
            this.parent = parent;
            this.text = text;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int listWidth, int listHeight, int mouseX, int mouseY, boolean hovered, float partialTick) {
            graphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    text,
                    x + (listWidth / 2),
                    y + 4,
                    !hovered && !isFocused() ? 0x6b6b6b : 0xFFFFFF
            );
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.parent.setSelected(this);
            return true;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(text);
        }
    }
}