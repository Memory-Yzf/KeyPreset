package com.yzf.keypreset.client.gui.screen;

import com.google.gson.JsonObject;
import com.yzf.keypreset.client.gui.widget.MutableButtonWidget;
import com.yzf.keypreset.client.gui.widget.PresetListWidget;
import com.yzf.keypreset.mixin.KeyBindsScreenAccessor;
import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import com.yzf.keypreset.util.PresetManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;

public class PresetScreen extends Screen {
    private final Screen parent;
    // 组件
    private TextWidget tooltip_1;
    private TextWidget tooltip_2;
    private PresetListWidget presetList;
    private TextFieldWidget editBox;
    private MutableButtonWidget saveButton;
    private MutableButtonWidget deleteButton;
    private MutableButtonWidget loadButton;
    private MutableButtonWidget coverButton;
    private MutableButtonWidget refreshButton;
    private MutableButtonWidget bindingButton;
    private MutableButtonWidget folderButton;
    private MutableButtonWidget doneButton;
    // 单次初始化控制
    private boolean bottomButton_control;

    public PresetScreen(Screen parent) {
        super(Constant.PRESET_TITLE);
        this.parent = parent;
        this.bottomButton_control = true;
    }

    @Override
    protected void init() {
        // 布局组件
        ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
        GridWidget bodyGrid = new GridWidget();
        GridWidget bodyGrid_R = new GridWidget();
        GridWidget bodyGrid_R_1 = new GridWidget();
        GridWidget bodyGrid_R_2 = new GridWidget();
        GridWidget bottomGrid = new GridWidget();
        // 网格布局
        GridWidget.Adder bodyAdder = bodyGrid.createAdder(2);
        GridWidget.Adder bodyAdder_R = bodyGrid_R.createAdder(1);
        GridWidget.Adder bodyAdder_R_1 = bodyGrid_R_1.createAdder(2);
        GridWidget.Adder bodyAdder_R_2 = bodyGrid_R_2.createAdder(2);
        GridWidget.Adder bottomAdder = bottomGrid.createAdder(4);


        // =============== 初始化 ===============
        if (bottomButton_control) {
            bottomButton_control = false;
            // 预设列表
            presetList = new PresetListWidget(MinecraftClient.getInstance(), 0, 0, 0, 20);
            // 提示文本
            tooltip_1 = new TextWidget(Constant.ENTER_TOOLTIP, textRenderer);
            // 禁止字符
            tooltip_2 = new TextWidget(Text.literal("\\ / : * ? \" < > |"), textRenderer);
            // 编辑框
            editBox = new TextFieldWidget(textRenderer, 0, 0, 0, 0, Text.empty());
            // 中间按钮
            saveButton = new MutableButtonWidget(Constant.SAVE);
            deleteButton = new MutableButtonWidget(Constant.DELETE);
            loadButton = new MutableButtonWidget(Constant.LOAD);
            coverButton = new MutableButtonWidget(Constant.COVER);
            // 底部按钮
            refreshButton = new MutableButtonWidget(Constant.REFRESH);
            bindingButton = new MutableButtonWidget(Constant.BINDING_TITLE);
            folderButton = new MutableButtonWidget(Constant.OPEN_FOLDER);
            doneButton = new MutableButtonWidget(Constant.DONE);
        }


        // =============== 中间区域 ===============
        // 基础属性
        int body_button_width = (width / 2 - (width / 6)) / 2;
        int body_general_height = 20;
        // 尺寸属性
        presetList.setWidth(width / 2);
        presetList.setHeight(height - layout.getHeaderHeight() - layout.getFooterHeight());
        tooltip_1.setWidth((int) ((double) width / 2 * 0.9));
        tooltip_2.setWidth((int) ((double) width / 2 * 0.9));
        tooltip_2.visible = false;
        editBox.setWidth(width / 2 - (width / 8));
        editBox.setHeight(body_general_height);
        saveButton.setWidth(body_button_width);
        saveButton.setHeight(body_general_height);
        deleteButton.setWidth(body_button_width);
        deleteButton.setHeight(body_general_height);
        loadButton.setWidth(body_button_width);
        loadButton.setHeight(body_general_height);
        coverButton.setWidth(body_button_width);
        coverButton.setHeight(body_general_height);
        // 位置属性
        tooltip_1.alignCenter();
        tooltip_2.alignCenter();
        // 颜色属性
        tooltip_2.setTextColor(0xfb4b4b);
        // 激活属性
        tooltip_2.visible = false;
        saveButton.active = false;
        deleteButton.active = false;
        loadButton.active = false;
        coverButton.active = false;
        // 事件属性
        editBox.setChangedListener(this::validateEditBox);
        saveButton.setPressAction(b -> {
            saveButton.active = false;
            PresetManager.savePreset(editBox.getText());
            editBox.setText("");
            presetList.reload();
        });
        deleteButton.setPressAction(b -> MinecraftClient.getInstance().setScreen(new ConfirmScreen(this, Constant.DELETE_TOOLTIP, () -> {
            if (presetList.getSelectedOrNull() != null) {
                String text = presetList.getSelectedOrNull().text;
                if (Constant.PRESET_FOLDER.resolve(text + ".json").toFile().exists()) {
                    PresetManager.deletePreset(text);
                    JsonObject config = Config.loadConfig();
                    for (int i = 1; i < 6; i++) {
                        String key = "preset_binding_" + i;
                        if (config.has(key) && config.get(key).getAsString().equals(text)) {
                            Config.deletePresetName(i);
                        }
                    }
                }
                presetList.reload();
            }
        }
        )));
        loadButton.setPressAction(b -> {
            if (presetList.getSelectedOrNull() != null) {
                String text = presetList.getSelectedOrNull().text;
                if (Constant.PRESET_FOLDER.resolve(text + ".json").toFile().exists()) {
                    PresetManager.loadPreset(text);
                    MinecraftClient.getInstance().setScreen(parent);
                    if (MinecraftClient.getInstance().currentScreen instanceof KeybindsScreen) {
                        ((KeyBindsScreenAccessor) MinecraftClient.getInstance().currentScreen).getControlsListWidget().update();
                    }
                }
                presetList.reload();
            }
        });
        coverButton.setPressAction(b -> MinecraftClient.getInstance().setScreen(new ConfirmScreen(this, Constant.COVER_TOOLTIP, () -> {
            if (presetList.getSelectedOrNull() != null) {
                PresetManager.savePreset(presetList.getSelectedOrNull().text);
                presetList.reload();
            }
        }
        )));


        // =============== 底部区域 ===============
        // 基础变量
        int bottom_button_width = (int) ((width * 0.9) / 4);
        int bottom_button_height = 20;
        int bottom_button_spacing = 4;

        // 尺寸属性
        refreshButton.setWidth(bottom_button_width);
        refreshButton.setHeight(bottom_button_height);
        bindingButton.setWidth(bottom_button_width);
        bindingButton.setHeight(bottom_button_height);
        folderButton.setWidth(bottom_button_width);
        folderButton.setHeight(bottom_button_height);
        doneButton.setWidth(bottom_button_width);
        doneButton.setHeight(bottom_button_height);

        // 事件属性
        refreshButton.setPressAction(b -> presetList.reload());
        bindingButton.setPressAction(b -> MinecraftClient.getInstance().setScreen(new BindingScreen(parent)));
        folderButton.setPressAction(b -> PresetManager.openPresetFolder());
        doneButton.setPressAction(b -> MinecraftClient.getInstance().setScreen(parent));


        // ================ 应用布局 ================
        bodyAdder_R_1.add(saveButton);
        bodyAdder_R_1.add(coverButton);
        bodyGrid_R_1.setColumnSpacing(1);

        bodyAdder_R_2.add(loadButton);
        bodyAdder_R_2.add(deleteButton);
        bodyGrid_R_2.setColumnSpacing(1);

        bodyAdder_R.add(tooltip_1, Positioner.create().margin((width / 2 - tooltip_1.getWidth()) / 2, (height - layout.getHeaderHeight() - layout.getFooterHeight()) / 2 - 45, 0, 8));
        bodyAdder_R.add(tooltip_2, Positioner.create().margin((width / 2 - tooltip_2.getWidth()) / 2, 0, 0, 10));
        bodyAdder_R.add(editBox, Positioner.create().margin((width / 2 - editBox.getWidth()) / 2, 0, 0, 15));
        bodyAdder_R.add(bodyGrid_R_1, Positioner.create().margin((width / 2 - (body_button_width * 2)) / 2, 0, 0, 1));
        bodyAdder_R.add(bodyGrid_R_2, Positioner.create().margin((width / 2 - (body_button_width * 2)) / 2, 0, 0, 0));

        bodyAdder.add(new EmptyWidget(width / 2, 0));
        bodyAdder.add(new EmptyWidget(width / 2, 0));
        bodyAdder.add(presetList);
        bodyAdder.add(bodyGrid_R);

        bottomAdder.add(refreshButton);
        bottomAdder.add(bindingButton);
        bottomAdder.add(folderButton);
        bottomAdder.add(doneButton);
        bottomGrid.setColumnSpacing(bottom_button_spacing);

        layout.addHeader(Constant.PRESET_TITLE, textRenderer);
        layout.addBody(bodyGrid);
        layout.addFooter(bottomGrid);
        layout.refreshPositions();
        layout.forEachChild(this::addDrawableChild);


        // ================ 额外 ================
        validateEditBox(editBox.getText());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        // 根据是否选中条目调整按钮状态
        if (presetList.getSelectedOrNull() == null) {
            deleteButton.active = false;
            loadButton.active = false;
            coverButton.active = false;
        } else {
            deleteButton.active = true;
            loadButton.active = true;
            coverButton.active = true;
        }

        // 移开鼠标时清除按钮焦点
        if (!refreshButton.isHovered()) refreshButton.setFocused(false);
        if (!bindingButton.isHovered()) bindingButton.setFocused(false);
        if (!folderButton.isHovered()) folderButton.setFocused(false);
        if (!doneButton.isHovered()) doneButton.setFocused(false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean editBox_clicked = editBox.mouseClicked(mouseX, mouseY, button);
        if (!editBox_clicked) {
            editBox.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void validateEditBox(String text) {
        // 条件：（文本为空）&&（编辑框聚焦）
        if (text.isBlank() && editBox.isFocused()) {
            tooltip_1.setMessage(Constant.ENTER_TOOLTIP);
            tooltip_1.setTextColor(0xffffff);
            editBox.setEditableColor(0xffffff);
            saveButton.active = false;
            tooltip_2.visible = false;
        }
        // 条件：（非法字符）
        else if (Constant.FORBIDDEN_CHARS.matcher(text).find()) {
            tooltip_1.setMessage(Constant.FORBID_TOOLTIP);
            tooltip_1.setTextColor(0xfb4b4b);
            editBox.setEditableColor(0xfb4b4b);
            saveButton.active = false;
            tooltip_2.visible = true;
        }
        // 条件：（存在同名文件）
        else if (isExist(text)) {
            tooltip_1.setMessage(Constant.EXIST_TOOLTIP);
            tooltip_1.setTextColor(0xfb4b4b);
            editBox.setEditableColor(0xfb4b4b);
            saveButton.active = false;
            tooltip_2.visible = false;
        }
        // 条件：（文本非空）
        else if (!text.isBlank()) {
            tooltip_1.setMessage(Constant.VALID_TOOLTIP);
            tooltip_1.setTextColor(0x37fd3e);
            editBox.setEditableColor(0xffffff);
            saveButton.active = true;
            tooltip_2.visible = false;
        }
        // 默认
        else {
            tooltip_1.setMessage(Constant.ENTER_TOOLTIP);
            tooltip_1.setTextColor(0xffffff);
            tooltip_2.visible = false;
        }
    }

    private boolean isExist(String text) {
        for (String fileName : PresetManager.getPresets()) {
            if (fileName.equalsIgnoreCase(text)) return true;
        }
        return false;
    }
}