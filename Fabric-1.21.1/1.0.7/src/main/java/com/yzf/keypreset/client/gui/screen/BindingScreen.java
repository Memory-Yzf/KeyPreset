package com.yzf.keypreset.client.gui.screen;

import com.google.gson.JsonObject;
import com.yzf.keypreset.client.gui.widget.BindingButtonWidget;
import com.yzf.keypreset.client.gui.widget.MutableButtonWidget;
import com.yzf.keypreset.client.gui.widget.PresetListWidget;
import com.yzf.keypreset.client.gui.widget.ScrollTextWidget;
import com.yzf.keypreset.util.BindingManager;
import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

public class BindingScreen extends Screen {
    private final Screen parent;
    // 组件
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
        // 布局组件
        ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
        GridWidget bodyGrid = new GridWidget();
        GridWidget bodyGrid_L = new GridWidget();
        GridWidget bodyGrid_R = new GridWidget();
        GridWidget bodyGrid_R_1 = new GridWidget();
        GridWidget bodyGrid_R_2 = new GridWidget();
        GridWidget bodyGrid_R_3 = new GridWidget();
        GridWidget bodyGrid_R_4 = new GridWidget();
        GridWidget bodyGrid_R_5 = new GridWidget();
        GridWidget bottomGrid = new GridWidget();
        // 网格布局
        GridWidget.Adder bodyAdder = bodyGrid.createAdder(2);
        GridWidget.Adder bodyAdder_L = bodyGrid_L.createAdder(1);
        GridWidget.Adder bodyAdder_R = bodyGrid_R.createAdder(5);
        GridWidget.Adder bodyAdder_R_1 = bodyGrid_R_1.createAdder(1);
        GridWidget.Adder bodyAdder_R_2 = bodyGrid_R_2.createAdder(1);
        GridWidget.Adder bodyAdder_R_3 = bodyGrid_R_3.createAdder(1);
        GridWidget.Adder bodyAdder_R_4 = bodyGrid_R_4.createAdder(1);
        GridWidget.Adder bodyAdder_R_5 = bodyGrid_R_5.createAdder(1);
        GridWidget.Adder bottomAdder = bottomGrid.createAdder(4);


        // =============== 初始化 ===============
        if (init_onceControl) {
            init_onceControl = false;
            // 预设列表
            presetList = new PresetListWidget(MinecraftClient.getInstance(), 0, 0, 0, 20);
            // 删除按钮
            deleteButtons = new MutableButtonWidget[5];
            deleteButtons[0] = new MutableButtonWidget(Text.literal("-"));
            deleteButtons[1] = new MutableButtonWidget(Text.literal("-"));
            deleteButtons[2] = new MutableButtonWidget(Text.literal("-"));
            deleteButtons[3] = new MutableButtonWidget(Text.literal("-"));
            deleteButtons[4] = new MutableButtonWidget(Text.literal("-"));
            // 滚动文本
            presetNames = new ScrollTextWidget[5];
            presetNames[0] = new ScrollTextWidget(Text.empty(), textRenderer);
            presetNames[1] = new ScrollTextWidget(Text.empty(), textRenderer);
            presetNames[2] = new ScrollTextWidget(Text.empty(), textRenderer);
            presetNames[3] = new ScrollTextWidget(Text.empty(), textRenderer);
            presetNames[4] = new ScrollTextWidget(Text.empty(), textRenderer);
            // 添加按钮
            addButtons = new MutableButtonWidget[5];
            addButtons[0] = new MutableButtonWidget(Text.literal("+"));
            addButtons[1] = new MutableButtonWidget(Text.literal("+"));
            addButtons[2] = new MutableButtonWidget(Text.literal("+"));
            addButtons[3] = new MutableButtonWidget(Text.literal("+"));
            addButtons[4] = new MutableButtonWidget(Text.literal("+"));
            // 按键绑定按钮
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


        // =============== 中间区域 ===============
        // 基础属性
        int body_widget_count = 5;
        int body_button_width = (int) (width * 0.75 * 0.1);
        int body_top_Width = (int) (width * 0.75 * 0.3);
        int body_bottom_Width = (int) (width * 0.75 * 0.25);
        int body_general_height = 20;
        int body_row_spacing = 6;
        int body_top_bottom_Margin = ((height - layout.getHeaderHeight() - layout.getFooterHeight()) - (body_general_height * body_widget_count) - (body_row_spacing * (body_widget_count - 1))) / 2;

        // 尺寸属性
        presetList.setWidth(width / 4);
        presetList.setHeight(height - layout.getHeaderHeight() - layout.getFooterHeight());
        for (int i = 0; i < body_widget_count; i++) {
            addButtons[i].setWidth(body_button_width);
            addButtons[i].setHeight(body_general_height);
            deleteButtons[i].setWidth(body_button_width);
            deleteButtons[i].setHeight(body_general_height);
            presetNames[i].setWidth(body_top_Width);
            presetNames[i].setHeight(body_general_height);
            bindingButtons[i].setWidth(body_bottom_Width);
            bindingButtons[i].setHeight(body_general_height);
            resetButtons[i].setWidth(body_button_width);
            resetButtons[i].setHeight(body_general_height);
        }

        // 初始属性
        for (int i = 0; i < body_widget_count; i++) {
            bindingButtons[i].setMessage(Config.getKeyBindingDisplayName(i + 1));
        }

        // 激活属性
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
                if (presetList.getSelectedOrNull() != null) {
                    String text = presetList.getSelectedOrNull().text;
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
                bindingButtons[index].setMessage(Text.empty());
            });
        }


        // =============== 底部区域 ===============
        // 组件属性
        int bottom_general_width = (int) ((width * 0.9) / 4);
        int bottom_general_height = 20;
        int bottom_general_spacing = 4;

        // 尺寸属性
        refreshButton.setWidth(bottom_general_width);
        refreshButton.setHeight(bottom_general_height);
        presetButton.setWidth(bottom_general_width);
        presetButton.setHeight(bottom_general_height);
        configButton.setWidth(bottom_general_width);
        configButton.setHeight(bottom_general_height);
        doneButton.setWidth(bottom_general_width);
        doneButton.setHeight(bottom_general_height);

        // 事件属性
        refreshButton.setPressAction(b -> presetList.reload());
        presetButton.setPressAction(b -> MinecraftClient.getInstance().setScreen(new PresetScreen(parent)));
        configButton.setPressAction(b -> BindingManager.openConfigFolder());
        doneButton.setPressAction(b -> MinecraftClient.getInstance().setScreen(parent));


        // ================ 应用布局 ================
        for (MutableButtonWidget add : addButtons) {
            bodyAdder_R_1.add(add);
        }
        for (MutableButtonWidget delete : deleteButtons) {
            bodyAdder_R_2.add(delete);
        }
        for (ScrollTextWidget name : presetNames) {
            bodyAdder_R_3.add(name);
        }
        for (BindingButtonWidget binding : bindingButtons) {
            bodyAdder_R_4.add(binding);
        }
        for (MutableButtonWidget reset : resetButtons) {
            bodyAdder_R_5.add(reset);
        }
        bodyGrid_R_1.setRowSpacing(body_row_spacing);
        bodyGrid_R_2.setRowSpacing(body_row_spacing);
        bodyGrid_R_3.setRowSpacing(body_row_spacing);
        bodyGrid_R_4.setRowSpacing(body_row_spacing);
        bodyGrid_R_5.setRowSpacing(body_row_spacing);

        bodyAdder_L.add(presetList);

        bodyAdder_R.add(bodyGrid_R_1, Positioner.create().marginLeft(10).marginTop(body_top_bottom_Margin));
        bodyAdder_R.add(bodyGrid_R_2, Positioner.create().marginLeft(3).marginTop(body_top_bottom_Margin));
        bodyAdder_R.add(bodyGrid_R_3, Positioner.create().marginLeft(3).marginTop(body_top_bottom_Margin));
        bodyAdder_R.add(bodyGrid_R_4, Positioner.create().marginLeft(3).marginTop(body_top_bottom_Margin));
        bodyAdder_R.add(bodyGrid_R_5, Positioner.create().marginLeft(3).marginTop(body_top_bottom_Margin));

        bodyAdder.add(new EmptyWidget((int) (width * 0.25), 0));
        bodyAdder.add(new EmptyWidget((int) (width * 0.75), 0));
        bodyAdder.add(bodyGrid_L);
        bodyAdder.add(bodyGrid_R);

        bottomAdder.add(refreshButton);
        bottomAdder.add(presetButton);
        bottomAdder.add(configButton);
        bottomAdder.add(doneButton);
        bottomGrid.setColumnSpacing(bottom_general_spacing);

        layout.addHeader(Constant.BINDING_TITLE, textRenderer);
        layout.addBody(bodyGrid);
        layout.addFooter(bottomGrid);
        layout.refreshPositions();
        layout.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        super.render(context, mouseX, mouseY, partialTick);
        for (ButtonWidget btn : addButtons) {
            btn.active = presetList.getSelectedOrNull() != null;
        }

        JsonObject config = Config.loadConfig();
        for (int i = 0; i < presetNames.length; i++) {
            String key = "preset_binding_" + (i + 1);
            presetNames[i].setMessage(Text.literal(config.has(key) ? config.get(key).getAsString() : ""));
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