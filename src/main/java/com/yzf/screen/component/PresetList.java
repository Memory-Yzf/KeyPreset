package com.yzf.screen.component;

import com.yzf.KeyPresetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


// 预设列表
public class PresetList extends ObjectSelectionList<PresetList.PresetListEntry> {
    public PresetList(Minecraft mc, int width, int height, int topMargin, int entryHeight) {
        super(mc, width, height, topMargin, entryHeight);
        addOption();
    }

    // 添加选项方法
    public void addOption() {
        for (var preset : KeyPresetManager.getPresets()) {
            addEntry(new PresetListEntry(preset)); // 往列表内添加选项
        }
    }

    // 列表重载方法
    public void reload() {
        clearEntries(); // 清空列表选项
        addOption(); //添加选项
    }

    @Override
    protected int getScrollbarPosition() {
        return width - 6; // 重写滚动条位置，使其保持在列表右侧边缘
    }

    @Override
    public int getRowWidth() {
        return width - 30; // 重写列表行宽，使其不会超出列表
    }

    // 预设列表选项
    public static class PresetListEntry extends ObjectSelectionList.Entry<PresetListEntry> {
        public final String text; // 条目文本

        PresetListEntry(String text) {
            this.text = text;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(text);
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            guiGraphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    text,
                    left + (width / 2),
                    top + 4,
                    !hovered && !isFocused() ? 0x6b6b6b : 0xFFFFFF //将悬浮与获取焦点的条目高亮
            );
        }
    }
}