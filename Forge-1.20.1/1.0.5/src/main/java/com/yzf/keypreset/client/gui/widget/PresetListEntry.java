package com.yzf.keypreset.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class PresetListEntry extends ObjectSelectionList.Entry<PresetListEntry> {
    private final PresetList parent;
    public final String text; // 条目文本

    PresetListEntry(PresetList parent,String text) {
        this.parent = parent;
        this.text = text;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                text,
                left + (width / 2),
                top + 4,
                !hovered && !isFocused() ? 0x6b6b6b : 0xFFFFFF //将悬浮与获取焦点的条目高亮
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
