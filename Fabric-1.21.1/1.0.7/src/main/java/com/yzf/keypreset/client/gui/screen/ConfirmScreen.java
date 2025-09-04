package com.yzf.keypreset.client.gui.screen;

import com.yzf.keypreset.util.Constant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfirmScreen extends Screen {
    // 父界面
    private final Screen parent;
    // 确认按钮
    private final ButtonWidget confirmButton;
    // 取消按钮
    private final ButtonWidget cancelButton;
    // 提示文本
    private final Text tooltip;
    // 执行事件
    private final Runnable event;

    public ConfirmScreen(Screen parent, Text tooltip, Runnable event) {
        super(Constant.CONFIRM_TITLE);
        this.parent = parent;
        this.tooltip = tooltip;
        this.event = event;
        confirmButton = ButtonWidget.builder(Constant.CONFIRM, b -> {
            if (event != null) {
                event.run();
            }
            MinecraftClient.getInstance().setScreen(parent);
        }).build();
        cancelButton = ButtonWidget.builder(Constant.CANCEL, b -> MinecraftClient.getInstance().setScreen(parent)).build();
    }

    @Override
    protected void init() {
        int button_Width = width / 6;
        int button_Height = 20;

        confirmButton.setWidth(button_Width);
        confirmButton.setHeight(button_Height);
        confirmButton.setPosition(width / 2 - button_Width - 10, height / 2 + button_Height);
        cancelButton.setWidth(button_Width);
        cancelButton.setHeight(button_Height);
        cancelButton.setPosition(width / 2 + 10, height / 2 + 20);

        addDrawableChild(confirmButton);
        addDrawableChild(cancelButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, tooltip, width / 2, height / 2 - 20, 0xFFD21F);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}