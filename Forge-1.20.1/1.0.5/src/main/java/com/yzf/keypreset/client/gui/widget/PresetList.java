package com.yzf.keypreset.client.gui.widget;

import com.yzf.keypreset.util.PresetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;

public class PresetList extends ObjectSelectionList<PresetListEntry> {
    public PresetList(Minecraft mc, int width, int height, int top, int bottom, int entryHeight) {
        super(mc, width, height, top, bottom, entryHeight);
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
}