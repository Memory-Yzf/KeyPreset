package com.yzf.keypreset.client.gui.screen;

import com.google.gson.JsonObject;
import com.yzf.keypreset.client.gui.widget.MutableButtonWidget;
import com.yzf.keypreset.client.gui.widget.PresetListWidget;
import com.yzf.keypreset.mixin.KeyBindsScreenAccessor;
import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import com.yzf.keypreset.util.PresetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class PresetScreen extends Screen {
    private final Screen parent;
    // 组件
    private StringWidget title;
    private StringWidget tooltip_1;
    private StringWidget tooltip_2;
    private PresetListWidget presetList;
    private EditBox editBox;
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
        // =============== 布局组件 ===============
        HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
        GridLayout bodyGrid = new GridLayout();
        GridLayout bottomGrid = new GridLayout();
        GridLayout.RowHelper bodyAdder = bodyGrid.createRowHelper(1);
        GridLayout.RowHelper bottomAdder = bottomGrid.createRowHelper(4);


        // =============== 初始化 ===============
        if (bottomButton_control) {
            bottomButton_control = false;
            title = new StringWidget(Constant.PRESET_TITLE, font);
            presetList = new PresetListWidget(Minecraft.getInstance(), 20);
            tooltip_1 = new StringWidget(Constant.ENTER_TOOLTIP, font);
            tooltip_2 = new StringWidget(Component.literal("\\ / : * ? \" < > |"), font);
            editBox = new EditBox(font, 0, 0, 0, 0, Component.empty());
            saveButton = new MutableButtonWidget(Constant.SAVE);
            deleteButton = new MutableButtonWidget(Constant.DELETE);
            loadButton = new MutableButtonWidget(Constant.LOAD);
            coverButton = new MutableButtonWidget(Constant.COVER);
            refreshButton = new MutableButtonWidget(Constant.REFRESH);
            bindingButton = new MutableButtonWidget(Constant.BINDING_TITLE);
            folderButton = new MutableButtonWidget(Constant.OPEN_FOLDER);
            doneButton = new MutableButtonWidget(Constant.DONE);
        }


        // =============== 中间区域 ===============
        // 基础变量
        int body_height = height - layout.getFooterHeight() - layout.getHeaderHeight();
        int body_list_width = width / 2;
        int body_list_geight = height - layout.getHeaderHeight() - layout.getFooterHeight();
        int body_toolTip_width = width / 2;
        int body_editBox_width = width / 10 * 4;
        int body_button_width = (width / 2 - (width / 6)) / 2;
        int body_general_height = 20;
        int body_general_centerX = width / 4 * 3;
        int body_general_centerY = height / 2;

        // 尺寸与位置属性
        presetList.setWidth(body_list_width);
        presetList.setHeight(body_list_geight);
        presetList.setPosition(0, layout.getHeaderHeight());
        tooltip_1.setWidth(body_toolTip_width);
        tooltip_1.setHeight(body_general_height);
        tooltip_1.setPosition(body_general_centerX - (tooltip_1.getWidth() / 2), body_general_centerY - 52);
        tooltip_2.setWidth(body_toolTip_width);
        tooltip_2.setHeight(body_general_height);
        tooltip_2.setPosition(body_general_centerX - (tooltip_2.getWidth() / 2), body_general_centerY - 36);
        editBox.setWidth(body_editBox_width);
        editBox.setHeight(body_general_height);
        editBox.setPosition(body_general_centerX - (editBox.getWidth() / 2), body_general_centerY - 10);
        saveButton.setWidth(body_button_width);
        saveButton.setHeight(body_general_height);
        saveButton.setPosition(body_general_centerX - saveButton.getWidth() - 1, body_general_centerY + 16);
        loadButton.setWidth(body_button_width);
        loadButton.setHeight(body_general_height);
        loadButton.setPosition(body_general_centerX - loadButton.getWidth() - 1, body_general_centerY + 38);
        coverButton.setWidth(body_button_width);
        coverButton.setHeight(body_general_height);
        coverButton.setPosition(body_general_centerX + 1, body_general_centerY + 16);
        deleteButton.setWidth(body_button_width);
        deleteButton.setHeight(body_general_height);
        deleteButton.setPosition(body_general_centerX + 1, body_general_centerY + 38);

        // 颜色属性
        tooltip_2.setColor(0xfb4b4b);

        // 激活属性
        tooltip_2.visible = false;
        saveButton.active = false;
        deleteButton.active = false;
        loadButton.active = false;
        coverButton.active = false;

        // 事件属性
        editBox.setResponder(this::validateEditBox);
        saveButton.setPressAction(b -> {
            saveButton.active = false;
            PresetManager.savePreset(editBox.getValue());
            editBox.setValue("");
            presetList.reload();
        });
        deleteButton.setPressAction(b -> Minecraft.getInstance().setScreen(new ConfirmScreen(this, Constant.DELETE_TOOLTIP, () -> {
            if (presetList.getSelected() != null) {
                String text = presetList.getSelected().text;
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
            if (presetList.getSelected() != null) {
                String text = presetList.getSelected().text;
                if (Constant.PRESET_FOLDER.resolve(text + ".json").toFile().exists()) {
                    PresetManager.loadPreset(text);
                    Minecraft.getInstance().setScreen(parent);
                    if (parent instanceof KeyBindsScreen) {
                        KeyBindsScreenAccessor accessor = (KeyBindsScreenAccessor) parent;
                        accessor.getKeyBindsList().refreshEntries();
                    }
                }
                presetList.reload();
            }
        });
        coverButton.setPressAction(b -> Minecraft.getInstance().setScreen(new ConfirmScreen(this, Constant.COVER_TOOLTIP, () -> {
            if (presetList.getSelected() != null) {
                PresetManager.savePreset(presetList.getSelected().text);
                presetList.reload();
            }
        }
        )));


        // =============== 底部区域===============
        // 基础变量
        int bottom_buttonWidth = (int) ((width * 0.9) / 4);
        int bottom_buttonHeight = 20;
        int bottom_buttonSpacing = 4;

        // 尺寸属性
        refreshButton.setWidth(bottom_buttonWidth);
        refreshButton.setHeight(bottom_buttonHeight);
        bindingButton.setWidth(bottom_buttonWidth);
        bindingButton.setHeight(bottom_buttonHeight);
        folderButton.setWidth(bottom_buttonWidth);
        folderButton.setHeight(bottom_buttonHeight);
        doneButton.setWidth(bottom_buttonWidth);
        doneButton.setHeight(bottom_buttonHeight);

        // 事件属性
        refreshButton.setPressAction(b -> presetList.reload());
        bindingButton.setPressAction(b -> Minecraft.getInstance().setScreen(new BindingScreen(parent)));
        folderButton.setPressAction(b -> PresetManager.openPresetFolder());
        doneButton.setPressAction(b -> Minecraft.getInstance().setScreen(parent));


        // ================ 应用布局 ================
        layout.addToHeader(title);

        bodyAdder.addChild(new SpacerElement(width, body_height));
        layout.addToContents(bodyGrid);

        addRenderableWidget(presetList);
        addRenderableWidget(tooltip_1);
        addRenderableWidget(tooltip_2);
        addRenderableWidget(editBox);
        addRenderableWidget(saveButton);
        addRenderableWidget(coverButton);
        addRenderableWidget(loadButton);
        addRenderableWidget(deleteButton);

        bottomAdder.addChild(refreshButton);
        bottomAdder.addChild(bindingButton);
        bottomAdder.addChild(folderButton);
        bottomAdder.addChild(doneButton);
        bottomGrid.spacing(bottom_buttonSpacing);
        layout.addToFooter(bottomGrid);

        layout.arrangeElements();
        layout.visitChildren(le -> le.visitWidgets(this::addRenderableWidget));


        // ================ 额外事件 ================
        validateEditBox(editBox.getMessage().getString());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        // 根据是否选中条目调整按钮状态
        if (presetList.getSelected() == null) {
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
            tooltip_1.setColor(0xffffff);
            editBox.setTextColor(0xffffff);
            saveButton.active = false;
            tooltip_2.visible = false;
        }
        // 条件：（非法字符）
        else if (Constant.FORBIDDEN_CHARS.matcher(text).find()) {
            tooltip_1.setMessage(Constant.FORBID_TOOLTIP);
            tooltip_1.setColor(0xfb4b4b);
            editBox.setTextColor(0xfb4b4b);
            saveButton.active = false;
            tooltip_2.visible = true;
        }
        // 条件：（存在同名文件）
        else if (isExist(text)) {
            tooltip_1.setMessage(Constant.EXIST_TOOLTIP);
            tooltip_1.setColor(0xfb4b4b);
            editBox.setTextColor(0xfb4b4b);
            saveButton.active = false;
            tooltip_2.visible = false;
        }
        // 条件：（文本非空）
        else if (!text.isBlank()) {
            tooltip_1.setMessage(Constant.VALID_TOOLTIP);
            tooltip_1.setColor(0x37fd3e);
            editBox.setTextColor(0xffffff);
            saveButton.active = true;
            tooltip_2.visible = false;
        }
        // 默认
        else {
            tooltip_1.setMessage(Constant.ENTER_TOOLTIP);
            tooltip_1.setColor(0xffffff);
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