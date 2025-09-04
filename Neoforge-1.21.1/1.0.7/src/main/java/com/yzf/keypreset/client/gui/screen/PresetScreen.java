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
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class PresetScreen extends Screen {
    private final Screen parent;
    // 组件
    private StringWidget tooltipText;
    private StringWidget forbidChar;
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
        // 布局组件
        HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
        GridLayout bodyGrid = new GridLayout();
        GridLayout bodyGrid_R = new GridLayout();
        GridLayout bodyGrid_R_1 = new GridLayout();
        GridLayout bodyGrid_R_2 = new GridLayout();
        GridLayout bottomGrid = new GridLayout();
        // 网格布局
        GridLayout.RowHelper bodyAdder = bodyGrid.createRowHelper(2);
        GridLayout.RowHelper bodyAdder_R = bodyGrid_R.createRowHelper(1);
        GridLayout.RowHelper bodyAdder_R_1 = bodyGrid_R_1.createRowHelper(2);
        GridLayout.RowHelper bodyAdder_R_2 = bodyGrid_R_2.createRowHelper(2);
        GridLayout.RowHelper bottomAdder = bottomGrid.createRowHelper(4);

        // =============== 初始化 ===============
        if (bottomButton_control) {
            bottomButton_control = false;
            // 预设列表
            presetList = new PresetListWidget(Minecraft.getInstance(), 0, 20);
            // 提示文本
            tooltipText = new StringWidget(Constant.ENTER_TOOLTIP, font);
            // 禁止字符
            forbidChar = new StringWidget(Component.literal("\\ / : * ? \" < > |"), font);
            // 编辑框
            editBox = new EditBox(font, 0, 0, 0, 0, Component.empty());
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

        // =============== 中间区域组件属性 ===============
        // 基础属性
        int body_buttonWidth = (width / 2 - (width / 6)) / 2;
        int body_generalHeight = 20;
        // 组件尺寸属性
        presetList.setWidth(width / 2);
        presetList.setHeight(height - layout.getHeaderHeight() - layout.getFooterHeight());
        tooltipText.setWidth((int) ((double) width / 2 * 0.9));
        forbidChar.setWidth((int) ((double) width / 2 * 0.9));
        editBox.setWidth(width / 2 - (width / 8));
        editBox.setHeight(20);
        saveButton.setWidth(body_buttonWidth);
        saveButton.setHeight(body_generalHeight);
        deleteButton.setWidth(body_buttonWidth);
        deleteButton.setHeight(body_generalHeight);
        loadButton.setWidth(body_buttonWidth);
        loadButton.setHeight(body_generalHeight);
        coverButton.setWidth(body_buttonWidth);
        coverButton.setHeight(body_generalHeight);
        // 组件位置属性
        tooltipText.alignCenter();
        forbidChar.alignCenter();
        // 组件颜色属性
        forbidChar.setColor(0xfb4b4b);
        // 组件激活属性
        forbidChar.visible = false;
        saveButton.active = false;
        deleteButton.active = false;
        loadButton.active = false;
        coverButton.active = false;
        // 组件事件属性
        editBox.setResponder(this::validateEditBox);
        saveButton.setPressAction(b -> {
            saveButton.active = false;
            PresetManager.savePreset(editBox.getValue());
            editBox.setValue("");
            presetList.reload();
        });
        deleteButton.setPressAction(b -> Minecraft.getInstance().setScreen(new ConfirmScreen(
                this,
                Constant.DELETE_TOOLTIP,
                () -> {
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
                    if (Minecraft.getInstance().screen instanceof KeyBindsScreen) {
                        ((KeyBindsScreenAccessor) Minecraft.getInstance().screen).getKeyBindsList().refreshEntries();
                    }
                }
                presetList.reload();
            }
        });
        coverButton.setPressAction(b -> Minecraft.getInstance().setScreen(new ConfirmScreen(
                this,
                Constant.COVER_TOOLTIP,
                () -> {
                    if (presetList.getSelected() != null) {
                        PresetManager.savePreset(presetList.getSelected().text);
                        presetList.reload();
                    }
                }
        )));

        // =============== 底部区域组件属性 ===============
        // 基础属性
        int bottom_buttonWidth = (int) ((width * 0.9) / 4);
        int bottom_buttonHeight = 20;
        int bottom_buttonSpacing = 4;
        // 组件尺寸属性
        refreshButton.setWidth(bottom_buttonWidth);
        refreshButton.setHeight(bottom_buttonHeight);
        bindingButton.setWidth(bottom_buttonWidth);
        bindingButton.setHeight(bottom_buttonHeight);
        folderButton.setWidth(bottom_buttonWidth);
        folderButton.setHeight(bottom_buttonHeight);
        doneButton.setWidth(bottom_buttonWidth);
        doneButton.setHeight(bottom_buttonHeight);
        // 组件事件属性
        refreshButton.setPressAction(b -> presetList.reload());
        bindingButton.setPressAction(b -> Minecraft.getInstance().setScreen(new BindingScreen(parent)));
        folderButton.setPressAction(b -> PresetManager.openPresetFolder());
        doneButton.setPressAction(b -> Minecraft.getInstance().setScreen(parent));

        // ================ 应用布局 ================
        bodyAdder_R_1.addChild(saveButton);
        bodyAdder_R_1.addChild(coverButton);
        bodyAdder_R_1.getGrid().spacing(1);

        bodyAdder_R_2.addChild(loadButton);
        bodyAdder_R_2.addChild(deleteButton);
        bodyAdder_R_2.getGrid().spacing(1);

        bodyAdder_R.addChild(tooltipText, LayoutSettings.defaults().padding((width / 2 - tooltipText.getWidth()) / 2, (height - layout.getHeaderHeight() - layout.getFooterHeight()) / 2 - 45, 0, 8));
        bodyAdder_R.addChild(forbidChar, LayoutSettings.defaults().padding((width / 2 - forbidChar.getWidth()) / 2, 0, 0, 10));
        bodyAdder_R.addChild(editBox, LayoutSettings.defaults().padding((width / 2 - editBox.getWidth()) / 2, 0, 0, 15));
        bodyAdder_R.addChild(bodyGrid_R_1, LayoutSettings.defaults().padding((width / 2 - (body_buttonWidth * 2)) / 2, 0, 0, 1));
        bodyAdder_R.addChild(bodyGrid_R_2, LayoutSettings.defaults().padding((width / 2 - (body_buttonWidth * 2)) / 2, 0, 0, 0));

        bodyAdder.addChild(new SpacerElement(width / 2, 1));
        bodyAdder.addChild(new SpacerElement(width / 2, 1));
        bodyAdder.addChild(presetList);
        bodyAdder.addChild(bodyGrid_R);

        bottomAdder.addChild(refreshButton);
        bottomAdder.addChild(bindingButton);
        bottomAdder.addChild(folderButton);
        bottomAdder.addChild(doneButton);
        bottomGrid.spacing(bottom_buttonSpacing);

        layout.addTitleHeader(Constant.PRESET_TITLE, font);
        layout.addToContents(bodyGrid);
        layout.addToFooter(bottomGrid);
        layout.arrangeElements();
        layout.visitChildren(le -> le.visitWidgets(this::addRenderableWidget));

        // ================ 额外事件 ================
        validateEditBox(editBox.getMessage().getString());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
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
            tooltipText.setMessage(Constant.ENTER_TOOLTIP);
            tooltipText.setColor(0xffffff);
            editBox.setTextColor(0xffffff);
            saveButton.active = false;
            forbidChar.visible = false;
        }
        // 条件：（非法字符）
        else if (Constant.FORBIDDEN_CHARS.matcher(text).find()) {
            tooltipText.setMessage(Constant.FORBID_TOOLTIP);
            tooltipText.setColor(0xfb4b4b);
            editBox.setTextColor(0xfb4b4b);
            saveButton.active = false;
            forbidChar.visible = true;
        }
        // 条件：（存在同名文件）
        else if (isExist(text)) {
            tooltipText.setMessage(Constant.EXIST_TOOLTIP);
            tooltipText.setColor(0xfb4b4b);
            editBox.setTextColor(0xfb4b4b);
            saveButton.active = false;
            forbidChar.visible = false;
        }
        // 条件：（文本非空）
        else if (!text.isBlank()) {
            tooltipText.setMessage(Constant.VALID_TOOLTIP);
            tooltipText.setColor(0x37fd3e);
            editBox.setTextColor(0xffffff);
            saveButton.active = true;
            forbidChar.visible = false;
        }
        // 默认
        else {
            tooltipText.setMessage(Constant.ENTER_TOOLTIP);
            tooltipText.setColor(0xffffff);
            forbidChar.visible = false;
        }
    }

    private boolean isExist(String text) {
        for (String fileName : PresetManager.getPresets()) {
            if (fileName.equalsIgnoreCase(text)) return true;
        }
        return false;
    }
}