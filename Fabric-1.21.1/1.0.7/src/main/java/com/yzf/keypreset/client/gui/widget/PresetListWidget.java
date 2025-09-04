package com.yzf.keypreset.client.gui.widget;

import com.yzf.keypreset.util.PresetManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class PresetListWidget extends AlwaysSelectedEntryListWidget<PresetListWidget.PresetListEntry> {
    public PresetListWidget(MinecraftClient mc, int width, int height, int topMargin, int entryHeight) {
        super(mc, width, height, topMargin, entryHeight);
        addOption();
    }

    public void addOption() {
        for (var preset : PresetManager.getPresets()) {
            addEntry(new PresetListEntry(preset));
        }
    }

    public void reload() {
        clearEntries();
        addOption();
        // 滚动条位置是否超出最大滚动量
        if (this.getScrollAmount() > this.getMaxScroll()) {
            // 将滚动条位置设置为最大滚动量
            this.setScrollAmount(this.getMaxScroll());
        }
    }

    @Override
    protected int getScrollbarX() {
        // 重写滚动条位置，使其保持在列表右侧边缘
        return width - 6;
    }

    @Override
    public int getRowWidth() {
        // 重写列表行宽，使其不会超出列表
        return width - 30;
    }

    @Override
    protected void renderEntry(DrawContext context, int mouseX, int mouseY, float delta, int index, int x, int y, int entryWidth, int entryHeight) {
        super.renderEntry(context, mouseX, mouseY, delta, index, x, y, entryWidth, entryHeight);
        if (this.isSelectedEntry(index)) {
            this.drawSelectionHighlight(
                    context,
                    y,
                    entryWidth,
                    entryHeight,
                    0xFFFFFFFF,
                    0xFF000000
            );
        }
    }

    public class PresetListEntry extends AlwaysSelectedEntryListWidget.Entry<PresetListEntry> {
        public final String text;

        public PresetListEntry(String text) {
            this.text = text;
        }

        @Override
        public @NotNull Text getNarration() {
            return Text.literal(text);
        }

        @Override
        public void render(DrawContext context, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            boolean selected = PresetListWidget.this.getSelectedOrNull() == this;
            context.drawCenteredTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    text,
                    left + (width / 2),
                    top + 4,
                    (selected || hovered) ? 0xFFFFFF : 0x6b6b6b
            );
        }
    }
}