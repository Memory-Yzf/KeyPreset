package com.yzf.keypreset.client.gui.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MutableButtonWidget extends ButtonWidget {
    private PressAction action;

    public MutableButtonWidget(Text message) {
        super(0, 0, 0, 0, message, b -> {
        }, DEFAULT_NARRATION_SUPPLIER);
    }

    public void setPressAction(PressAction action) {
        this.action = action;
    }

    @Override
    public void onPress() {
        if (action != null) {
            action.onPress(this);
        }
    }
}