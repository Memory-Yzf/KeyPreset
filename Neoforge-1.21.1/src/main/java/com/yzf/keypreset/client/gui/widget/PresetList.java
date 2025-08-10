package com.yzf.keypreset.client.gui.widget;

import com.yzf.keypreset.KeyPresetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;


// 预设列表
public class PresetList extends ObjectSelectionList<PresetListEntry> {
    public PresetList(Minecraft mc, int width, int height, int topMargin, int entryHeight) {
        super(mc, width, height, topMargin, entryHeight);
        addEntry();
    }

    // 添加条目方法
    public void addEntry() {
        for (var preset : KeyPresetManager.getPresets()) {
            addEntry(new PresetListEntry(preset)); // 往列表内添加选项
        }
    }

    // 列表重载方法
    public void reload() {
        clearEntries(); // 清空列表选项
        addEntry(); //添加条目
        // 滚动条位置是否超出最大滚动量
        if (this.getScrollAmount() > this.getMaxScroll()) {
            this.setScrollAmount(this.getMaxScroll()); // 将滚动条位置设置为最大滚动量
        }
    }

    @Override
    protected int getScrollbarPosition() {
        return width - 6; // 重写滚动条位置，使其保持在列表右侧边缘
    }

    @Override
    public int getRowWidth() {
        return width - 30; // 重写列表行宽，使其不会超出列表
    }
}