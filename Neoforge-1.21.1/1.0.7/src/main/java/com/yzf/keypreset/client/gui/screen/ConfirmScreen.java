package com.yzf.keypreset.client.gui.screen;

import com.yzf.keypreset.util.Constant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ConfirmScreen extends Screen {
    // 父界面
    private final Screen parent;
    // 确认按钮
    private final Button confirmButton;
    // 取消按钮
    private final Button cancelButton;
    // 提示文本
    private final Component tooltip;
    // 执行事件
    private final Runnable event;

    protected ConfirmScreen(Screen parent, Component tooltip, Runnable event) {
        super(Constant.CONFIRM_TITLE);
        this.parent = parent;
        this.tooltip = tooltip;
        this.event = event;
        confirmButton = Button.builder(Constant.CONFIRM, b -> {
            if (event != null) {
                event.run();
            }
            Minecraft.getInstance().setScreen(parent);
        }).build();

        cancelButton = Button.builder(Constant.CANCEL, b -> Minecraft.getInstance().setScreen(parent)).build();
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

        addRenderableWidget(confirmButton);
        addRenderableWidget(cancelButton);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font, tooltip, width / 2, height / 2 - 20, 0xFFD21F);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public Screen getParent() {
        return parent;
    }

    public Runnable getEvent() {
        return event;
    }
}