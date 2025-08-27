package com.yzf.keypreset.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
public class ScrollStringWidget extends AbstractWidget {
    private final Font font; // 字体
    private String text; // 文本
    private int scrollOffset = 0; // 滚动偏移量
    private float scrollOffsetF = 0f;  // 浮点偏移量
    private final float scrollSpeed = 0.7f; // 滚动速度
    private final int maxWidth; // 最大宽度
    private boolean scrolling = false; // 是否正在滚动

    public ScrollStringWidget(int x, int y, int width, int height, String text) {
        super(x, y, width, height, Component.literal(text));
        this.font = Minecraft.getInstance().font;
        this.text = text;
        this.maxWidth = width;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int x1 = getX();
        int y1 = getY();
        int x2 = x1 + width;
        int y2 = y1 + height;

        // 绘制背景（可选）
        guiGraphics.fill(x1, y1, x2, y2, 0x80000000); // 半透明背景

        // 绘制边框
        guiGraphics.hLine(x1, x2 - 1, y1, 0xFFAAAAAA); // 上边
        guiGraphics.hLine(x1, x2 - 1, y2 - 1, 0xFFAAAAAA); // 下边
        guiGraphics.vLine(x1, y1, y2 - 1, 0xFFAAAAAA); // 左边
        guiGraphics.vLine(x2 - 1, y1, y2 - 1, 0xFFAAAAAA); // 右边

        // 设置内边距
        int padding = 5;
        int textWidth = font.width(text);

        // 开启裁剪区域（减去内边距）
        guiGraphics.enableScissor(x1 + padding, y1 + padding, x2 - padding, y2 - padding);

        // 计算显示的偏移量（加上内边距）
        int drawX = x1 + padding - (int) scrollOffsetF;
        int drawY = y1 + (height - 8) / 2;

        guiGraphics.drawString(font, text, drawX, drawY, 0xFFFFFF, false);

        guiGraphics.disableScissor();

        // 滚动逻辑
        if (textWidth > (maxWidth - padding * 2)) { // 减去两倍内边距
            scrolling = true;
            scrollOffsetF += scrollSpeed;
            if (scrollOffsetF > textWidth) {
                scrollOffsetF = -maxWidth; // 循环滚动
            }
        } else {
            scrolling = false;
            scrollOffsetF = 0;
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }

    public void setText(String newText) {
        this.text = newText;
        this.setMessage(Component.literal(newText));
        this.scrollOffset = 0;
    }
}
