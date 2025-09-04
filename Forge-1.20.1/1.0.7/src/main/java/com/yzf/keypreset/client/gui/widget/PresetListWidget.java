package com.yzf.keypreset.client.gui.widget;

import com.yzf.keypreset.util.PresetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class PresetListWidget extends ObjectSelectionList<PresetListWidget.PresetListEntry> {
    public PresetListWidget(Minecraft mc, int entryHeight) {
        super(mc, 0, 0, 0, 0, entryHeight);
        addEntry();
    }

    public void addEntry() {
        for (var preset : PresetManager.getPresets()) {
            addEntry(new PresetListEntry(this, preset));
        }
    }

    public void reload() {
        clearEntries();
        addEntry();
        // 滚动条位置是否超出最大滚动量
        if (this.getScrollAmount() > this.getMaxScroll()) {
            // 将滚动条位置设置为最大滚动量
            this.setScrollAmount(this.getMaxScroll());
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPosition(int x, int y) {
        this.x0 = x;
        this.x1 = x + this.width;
        this.y0 = y;
        this.y1 = y + this.height;
    }

    @Override
    protected int getScrollbarPosition() {
        // 重写滚动条位置，使其保持在列表右侧边缘
        return width - 6;
    }

    @Override
    public int getRowWidth() {
        // 重写列表行宽，使其不会超出列表
        return width - 30;
    }

    @Override
    protected void renderItem(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int index, int left, int top, int width, int height) {
        super.renderItem(guiGraphics, mouseX, mouseY, partialTick, index, left, top, width, height);
        // 条件：当前项为选中项
        if (isSelectedItem(index)) {
            renderSelection(
                    guiGraphics,
                    top,
                    width,
                    height,
                    0xFFFFFFFF,
                    0xFF000000
            );
        }
    }

    public class PresetListEntry extends ObjectSelectionList.Entry<PresetListEntry> {
        private final PresetListWidget parent;
        // 条目名称
        public final String text;

        public PresetListEntry(PresetListWidget parent, String text) {
            this.parent = parent;
            this.text = text;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(text);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.parent.setSelected(this);
            return true;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
            boolean selected = PresetListWidget.this.getSelected() == this;
            // 渲染文本
            guiGraphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    text,
                    left + (width / 2),
                    top + 4,
                    (selected || hovered) ? 0xFFFFFF : 0x6b6b6b
            );
        }
    }
}