package com.yzf.keypreset.client.gui.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class MutableButtonWidget extends Button {
    // 触发事件
    private OnPress action;

    public MutableButtonWidget(Component message) {
        super(0, 0, 0, 0, message, b -> {
        }, DEFAULT_NARRATION);
    }

    public void setPressAction(OnPress action) {
        this.action = action;
    }

    @Override
    public void onPress() {
        if (action != null) {
            action.onPress(this);
        }
    }
}