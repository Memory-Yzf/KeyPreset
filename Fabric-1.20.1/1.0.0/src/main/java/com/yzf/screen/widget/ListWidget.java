package com.yzf.screen.widget;

import com.yzf.KeyPreset;
import com.yzf.preset.PresetManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

public class ListWidget extends AlwaysSelectedEntryListWidget<ListWidget.Entry> {
    public ListWidget(MinecraftClient minecraftClient, int width, int height, int top, int entryHeight) {
        super(minecraftClient, width, height, top, entryHeight);
        loadEntries();
    }

    //加载预设方法
    //使用 PresetManager.getPresets() 获取所有预设，并将
    //其添加到列表中，然后使用 split() 去掉扩展名，保留主名称
    public void loadEntries() {
        for (var preset : PresetManager.getPresets()) {
            addEntry(new Entry(preset.split("\\.")[0]));
        }
    }

    //重载方法（清空列表，再次获取所有预设）
    public void reload() {
        clearEntries();
        loadEntries();
    }

    //条目内部类
    public static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
        public final String text; //条目文本

        public Entry(String text) {
            super(); //调用父类构构造器
            this.text = text;
        }

        //动态渲染
        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, text, KeyPreset.centerX(), y + 3, !hovered && !isFocused() ? 0xC0C0C0 : 0xFFFFFF);
        }

        //重载复述功能，使其能够朗读条目文本
        @Override
        public Text getNarration() {
            return Text.of(text);
        }
    }
}
