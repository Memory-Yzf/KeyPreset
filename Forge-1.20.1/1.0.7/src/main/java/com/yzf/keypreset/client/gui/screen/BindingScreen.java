package com.yzf.keypreset.client.gui.screen;

import com.google.gson.JsonObject;
import com.yzf.keypreset.client.gui.widget.BindingButtonWidget;
import com.yzf.keypreset.client.gui.widget.MutableButtonWidget;
import com.yzf.keypreset.client.gui.widget.PresetListWidget;
import com.yzf.keypreset.client.gui.widget.ScrollTextWidget;
import com.yzf.keypreset.util.BindingManager;
import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BindingScreen extends Screen {
    private final Screen parent;
    // 组件
    private StringWidget title;
    private PresetListWidget presetList;
    private MutableButtonWidget[] addButtons;
    private MutableButtonWidget[] deleteButtons;
    private ScrollTextWidget[] presetNames;
    private BindingButtonWidget[] bindingButtons;
    private MutableButtonWidget[] resetButtons;
    private MutableButtonWidget refreshButton;
    private MutableButtonWidget presetButton;
    private MutableButtonWidget configButton;
    private MutableButtonWidget doneButton;
    // 单次初始化控制
    private boolean init_onceControl;
    private boolean body_onceControl;

    protected BindingScreen(Screen parent) {
        super(Constant.BINDING_TITLE);
        this.parent = parent;
        this.init_onceControl = true;
        this.body_onceControl = true;
    }

    @Override
    protected void init() {
        HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
        GridLayout bodyGrid = new GridLayout();
        GridLayout bottomGrid = new GridLayout();
        GridLayout.RowHelper bodyAdder = bodyGrid.createRowHelper(1);
        GridLayout.RowHelper bottomAdder = bottomGrid.createRowHelper(4);


        // =============== 初始化 ===============
        if (init_onceControl) {
            init_onceControl = false;
            // 标题
            title = new StringWidget(Constant.BINDING_TITLE, font);
            // 预设列表
            presetList = new PresetListWidget(Minecraft.getInstance(), 20);
            // 添加按钮组
            addButtons = new MutableButtonWidget[5];
            addButtons[0] = new MutableButtonWidget(Component.literal("+"));
            addButtons[1] = new MutableButtonWidget(Component.literal("+"));
            addButtons[2] = new MutableButtonWidget(Component.literal("+"));
            addButtons[3] = new MutableButtonWidget(Component.literal("+"));
            addButtons[4] = new MutableButtonWidget(Component.literal("+"));
            // 删除按钮组
            deleteButtons = new MutableButtonWidget[5];
            deleteButtons[0] = new MutableButtonWidget(Component.literal("-"));
            deleteButtons[1] = new MutableButtonWidget(Component.literal("-"));
            deleteButtons[2] = new MutableButtonWidget(Component.literal("-"));
            deleteButtons[3] = new MutableButtonWidget(Component.literal("-"));
            deleteButtons[4] = new MutableButtonWidget(Component.literal("-"));
            // 滚动文本组
            presetNames = new ScrollTextWidget[5];
            presetNames[0] = new ScrollTextWidget(Component.empty(), font);
            presetNames[1] = new ScrollTextWidget(Component.empty(), font);
            presetNames[2] = new ScrollTextWidget(Component.empty(), font);
            presetNames[3] = new ScrollTextWidget(Component.empty(), font);
            presetNames[4] = new ScrollTextWidget(Component.empty(), font);
            // 按键绑定按钮组
            bindingButtons = new BindingButtonWidget[5];
            bindingButtons[0] = new BindingButtonWidget();
            bindingButtons[1] = new BindingButtonWidget();
            bindingButtons[2] = new BindingButtonWidget();
            bindingButtons[3] = new BindingButtonWidget();
            bindingButtons[4] = new BindingButtonWidget();
            // 重置按钮
            resetButtons = new MutableButtonWidget[5];
            resetButtons[0] = new MutableButtonWidget(Constant.RESET);
            resetButtons[1] = new MutableButtonWidget(Constant.RESET);
            resetButtons[2] = new MutableButtonWidget(Constant.RESET);
            resetButtons[3] = new MutableButtonWidget(Constant.RESET);
            resetButtons[4] = new MutableButtonWidget(Constant.RESET);
            // 底部按钮
            refreshButton = new MutableButtonWidget(Constant.REFRESH);
            presetButton = new MutableButtonWidget(Constant.PRESET_TITLE);
            configButton = new MutableButtonWidget(Constant.OPEN_CONFIG);
            doneButton = new MutableButtonWidget(Constant.DONE);
        }


        // =============== 中间区域组件属性 ===============
        // 基础变量
        int body_widget_count = 5;
        int body_height = height - layout.getFooterHeight() - layout.getHeaderHeight();
        int body_list_width = width / 4;
        int body_general_height = 20;
        int body_button_width = (int) (width * 0.75 * 0.1);
        int body_fileName_width = (int) (width * 0.75 * 0.3);
        int body_bindKey_width = (int) (width * 0.75 * 0.25);
        int body_general_rowSpacing = 6;
        int body_general_columnSpacing = 2;
        int body_general_x0 = width / 4 + 10;
        int body_general_y0 = layout.getHeaderHeight() + ((body_height - (body_general_height * body_widget_count + (body_general_rowSpacing * (body_widget_count - 1)))) / 2);

        // 尺寸与位置属性
        presetList.setWidth(body_list_width);
        presetList.setHeight(body_height);
        presetList.setPosition(0, layout.getHeaderHeight());
        for (int i = 0; i < body_widget_count; i++) {
            addButtons[i].setWidth(body_button_width);
            addButtons[i].setHeight(body_general_height);
            addButtons[i].setPosition(body_general_x0, body_general_y0 + i * (body_general_height + body_general_rowSpacing));
            deleteButtons[i].setWidth(body_button_width);
            deleteButtons[i].setHeight(body_general_height);
            deleteButtons[i].setPosition(addButtons[i].getX() + addButtons[i].getWidth() + body_general_columnSpacing, addButtons[i].getY());
            presetNames[i].setWidth(body_fileName_width);
            presetNames[i].setHeight(body_general_height);
            presetNames[i].setPosition(deleteButtons[i].getX() + deleteButtons[i].getWidth() + body_general_columnSpacing, addButtons[i].getY());
            bindingButtons[i].setWidth(body_bindKey_width);
            bindingButtons[i].setHeight(body_general_height);
            bindingButtons[i].setPosition(presetNames[i].getX() + presetNames[i].getWidth() + body_general_columnSpacing, addButtons[i].getY());
            resetButtons[i].setWidth(body_button_width);
            resetButtons[i].setHeight(body_general_height);
            resetButtons[i].setPosition(bindingButtons[i].getX() + bindingButtons[i].getWidth() + body_general_columnSpacing, addButtons[i].getY());
        }

        // 激活状态
        if (body_onceControl) {
            body_onceControl = false;
            for (int i = 0; i < body_widget_count; i++) {
                addButtons[i].active = false;
                deleteButtons[i].active = false;
                resetButtons[i].active = false;
            }
        }

        // 事件属性
        for (int i = 0; i < body_widget_count; i++) {
            int index = i;
            addButtons[i].setPressAction(b -> {
                if (presetList.getSelected() != null) {
                    String text = presetList.getSelected().text;
                    Config.savePresetName(index + 1, text);
                }
            });
            deleteButtons[i].setPressAction(b -> Config.deletePresetName(index + 1));
            bindingButtons[i].setOnBindingChanged(b -> {
                Config.saveKeyBinding(index + 1, b.getConfigValue());
                b.setFocused(false);
            });
            resetButtons[i].setPressAction(b -> {
                Config.deleteKeyBinding(index + 1);
                bindingButtons[index].setMessage(Component.empty());
            });
            bindingButtons[i].setMessage(Config.getKeyBindingDisplayName(index + 1));
        }


        // =============== 底部区域组件属性 ===============
        // 基础变量
        int bottom_general_Width = (int) ((width * 0.9) / 4);
        int bottom_general_Height = 20;
        int bottom_general_Spacing = 4;

        // 尺寸属性
        refreshButton.setWidth(bottom_general_Width);
        refreshButton.setHeight(bottom_general_Height);
        presetButton.setWidth(bottom_general_Width);
        presetButton.setHeight(bottom_general_Height);
        configButton.setWidth(bottom_general_Width);
        configButton.setHeight(bottom_general_Height);
        doneButton.setWidth(bottom_general_Width);
        doneButton.setHeight(bottom_general_Height);

        // 按钮事件
        refreshButton.setPressAction(b -> presetList.reload());
        presetButton.setPressAction(b -> Minecraft.getInstance().setScreen(new PresetScreen(parent)));
        configButton.setPressAction(b -> BindingManager.openConfigFolder());
        doneButton.setPressAction(b -> Minecraft.getInstance().setScreen(parent));

        // ================ 应用布局 ================
        bodyAdder.addChild(new SpacerElement(width, body_height));
        addRenderableWidget(presetList);

        for (int i = 0; i < body_widget_count; i++) {
            addRenderableWidget(addButtons[i]);
            addRenderableWidget(deleteButtons[i]);
            addRenderableWidget(presetNames[i]);
            addRenderableWidget(bindingButtons[i]);
            addRenderableWidget(resetButtons[i]);
        }

        bottomAdder.addChild(refreshButton);
        bottomAdder.addChild(presetButton);
        bottomAdder.addChild(configButton);
        bottomAdder.addChild(doneButton);
        bottomGrid.spacing(bottom_general_Spacing);

        layout.addToHeader(title);
        layout.addToContents(bodyGrid);
        layout.addToFooter(bottomGrid);
        layout.arrangeElements();
        layout.visitChildren(le -> le.visitWidgets(this::addRenderableWidget));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        for (MutableButtonWidget btn : addButtons) {
            btn.active = presetList.getSelected() != null;
        }

        JsonObject config = Config.loadConfig();
        for (int i = 0; i < presetNames.length; i++) {
            String key = "preset_binding_" + (i + 1);
            presetNames[i].setText(config.has(key) ? config.get(key).getAsString() : "");
        }

        for (int i = 0; i < deleteButtons.length; i++) {
            String key = "preset_binding_" + (i + 1);
            deleteButtons[i].active = config.has(key) && !config.get(key).getAsString().isEmpty();
        }

        for (int i = 0; i < resetButtons.length; i++) {
            resetButtons[i].active = !Config.getKeyBinding(i + 1).isEmpty();
        }

        if (!refreshButton.isHovered()) refreshButton.setFocused(false);
        if (!presetButton.isHovered()) presetButton.setFocused(false);
        if (!configButton.isHovered()) configButton.setFocused(false);
        if (!doneButton.isHovered()) doneButton.setFocused(false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (BindingButtonWidget btn : bindingButtons) {
            if (btn.handleKeyPress(keyCode, scanCode)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (BindingButtonWidget btn : bindingButtons) {
            if (btn.handleKeyRelease()) return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (BindingButtonWidget btn : bindingButtons) {
            if (btn.handleMousePress(button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (BindingButtonWidget btn : bindingButtons) {
            if (btn.handleMouseRelease(button)) return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}