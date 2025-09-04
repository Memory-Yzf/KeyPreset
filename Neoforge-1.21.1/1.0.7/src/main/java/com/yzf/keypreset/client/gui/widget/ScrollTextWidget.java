package com.yzf.keypreset.client.gui.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.network.chat.Component;

public class ScrollTextWidget extends AbstractStringWidget {
    // 滚动偏移量
    private float scrollOffset;
    // 滚动方向
    private boolean scrollingDirection;
    // 暂停开始时间
    private long pauseTime;

    public ScrollTextWidget(Component text, Font font) {
        super(0, 0, 0, 0, text, font);
        this.scrollOffset = 0f;
        this.scrollingDirection = true;
        this.pauseTime = 0;
    }

    public void setText(String text) {
        setMessage(Component.literal(text));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // 组件坐标
        int x1 = getX();
        int y1 = getY();
        int x2 = x1 + width;
        int y2 = y1 + height;

        // 当前文本
        Component currentText = getMessage();
        // 文本渲染器
        Font font = getFont();
        // 条件：渲染器为空 || 当前文本为空
        // 文本宽度
        int textWidth = font.width(currentText);
        // 内边距
        int padding = 5;
        // 内部宽度
        int innerWidth = width - padding * 2;
        // 绘制位置X
        int drawX = x1 + padding - (int) scrollOffset;
        // 绘制位置Y
        int drawY = y1 + (height - 8) / 2;

        // 填充背景
        guiGraphics.fill(x1, y1, x2, y2, 0x80000000);
        // 上边框
        guiGraphics.hLine(x1, x2 - 1, y1, 0xFFAAAAAA);
        // 下边框
        guiGraphics.hLine(x1, x2 - 1, y2 - 1, 0xFFAAAAAA);
        // 左边框
        guiGraphics.vLine(x1, y1, y2 - 1, 0xFFAAAAAA);
        // 右边框
        guiGraphics.vLine(x2 - 1, y1, y2 - 1, 0xFFAAAAAA);
        // 开启裁剪
        guiGraphics.enableScissor(x1 + padding, y1 + padding, x2 - padding, y2 - padding);
        // 绘制文本
        guiGraphics.drawString(font, currentText, drawX, drawY, 0xFFFFFF, false);
        // 关闭裁剪
        guiGraphics.disableScissor();

        // 条件：文本宽度大于内部宽度
        if (textWidth > innerWidth) {
            // 当前时间
            long now = System.currentTimeMillis();
            // 条件：暂停时间大于0 && 当前时间 - 暂停时间 < 1500
            if (pauseTime > 0 && now - pauseTime < 1500) {
                return;
            }
            // 暂停事件归零
            pauseTime = 0;
            // 滚动速度
            float scrollSpeed = 0.7f;
            // 条件：滚动方向（true=左，false=右）
            if (scrollingDirection) {
                // 滚动偏移量 += 滚动速度
                scrollOffset += scrollSpeed;
                // 条件：滚动偏移量 >= 文本宽度 - 内部宽度
                if (scrollOffset >= (textWidth - innerWidth)) {
                    // 改变滚动方向
                    scrollingDirection = false;
                    // 暂停时间 = 当前时间
                    pauseTime = now;
                }
            } else {
                // 滚动偏移量 -= 滚动速度
                scrollOffset -= scrollSpeed;
                // 条件：滚动偏移量 <= 0
                if (scrollOffset <= 0) {
                    // 改变滚动方向
                    scrollingDirection = true;
                    // 暂停时间 = 当前时间
                    pauseTime = now;
                }
            }
        } else {
            // 滚动偏移量 = 0
            scrollOffset = 0;
        }
    }
}