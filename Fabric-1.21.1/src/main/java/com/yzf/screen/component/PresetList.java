package com.yzf.screen.component;

import com.yzf.KeyPreset;
import com.yzf.KeyPresetManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class PresetList extends AlwaysSelectedEntryListWidget<PresetList.PresetListEntry> {
    public PresetList(MinecraftClient mc, int width, int height, int topMargin, int entryHeight) {
        super(mc, width, height, topMargin, entryHeight);
        addOption();
    }

    // 添加选项方法
    public void addOption() {
        for (var preset : KeyPresetManager.getPresets()) {
            addEntry(new PresetListEntry(preset));
        }
    }

    // 列表重载方法
    public void reload() {
        clearEntries(); // 清空列表选项
        addOption(); //添加选项
    }

    @Override
    protected int getScrollbarX() {
        return width - 6; // 重写滚动条位置，使其保持在列表右侧边缘
    }

    @Override
    public int getRowWidth() {
        return width - 30; // 重写列表行宽，使其不会超出列表
    }

    //条目内部类
    public static class PresetListEntry extends AlwaysSelectedEntryListWidget.Entry<PresetListEntry> {
        public final String text; //条目文本

        public PresetListEntry(String text) {
            super(); //调用父类构构造器
            this.text = text;
        }

        @Override
        public @NotNull Text getNarration() {
            return Text.literal(text);
        }

        //动态渲染
        @Override
        public void render(DrawContext context, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    text,
                    left + (width / 2),
                    top + 4,
                    !hovered && !isFocused() ? 0x6b6b6b : 0xFFFFFF //将悬浮与获取焦点的条目高亮
            );
        }
    }
}