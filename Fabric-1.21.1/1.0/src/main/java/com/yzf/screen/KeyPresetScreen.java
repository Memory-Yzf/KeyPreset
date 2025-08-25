package com.yzf.screen;

import com.yzf.KeyPreset;
import com.yzf.KeyPresetManager;
import com.yzf.mixin.KeyBindsScreenAccessor;
import com.yzf.screen.component.PresetList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.Objects;

@SuppressWarnings("all")
public class KeyPresetScreen extends Screen {
    private Screen parent; // 父界面
    private PresetList presetList; // 预设列表
    private Text editBoxText; // 编辑框提示文本
    private int editBoxTextColor; // 编辑框提示文本颜色
    private TextFieldWidget editBox; // 编辑框
    private ButtonWidget loadButton;
    private ButtonWidget deleteButton; // 删除按钮
    private ButtonWidget saveButton;  // 保存按钮
    private ButtonWidget openFolderButton; // 打开文件夹按钮
    private ButtonWidget doneButton;  // 完成按钮

    public KeyPresetScreen(Screen parent) {
        super(KeyPreset.TITLE);
        this.parent = parent;
        this.editBoxText = KeyPreset.ENTER_TEXT; // 编辑框默认提示文本
        this.editBoxTextColor = 0xffffff; // 编辑框默认提示文本颜色
    }

    @Override
    protected void init() {
        super.init();
        initElement();
    }

    private void initElement() {
        // 构建列表
        presetList = new PresetList(
                MinecraftClient.getInstance(),
                (int) (width * 0.5),
                (int) (height * 0.7),
                (int) (height * 0.1),
                20);
        // 构建编辑框
        editBox = new TextFieldWidget(
                textRenderer,
                (int) ((width * 0.75) - (width * 0.2)),
                (int) ((height * 0.5) - (height * 0.025)),
                (int) (width * 0.4),
                (int) (height * 0.05),
                Text.literal(""));

        // 设置编辑框文本改变事件
        editBox.setChangedListener(text -> {
            // 判断文本是否为空
            if (text.isBlank()) {
                editBoxText = KeyPreset.BLANK_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setEditableColor(0xffffff);
                saveButton.active = false;
            }
            // 判断文本是否存在非法字符
            else if (KeyPreset.FORBIDDEN_CHARS.matcher(text).find()) {
                editBoxText = KeyPreset.FORBID_CHAR_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setEditableColor(editBoxTextColor);
                saveButton.active = false;
            }
            // 判断是否存在相同名称的文件
            else if (isExist(text)) {
                editBoxText = KeyPreset.EXIST_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setEditableColor(editBoxTextColor);
                saveButton.active = false;
            } else {
                editBoxText = KeyPreset.VALID_TEXT;
                editBoxTextColor = 0x37fd3e;
                editBox.setEditableColor(0xffffff);
                saveButton.active = true;
            }
        });

        // 构建加载按钮
        loadButton = ButtonWidget.builder(
                KeyPreset.LOAD,
                button -> {
                    if (!KeyPreset.PRESET_FOLDER.resolve(Objects.requireNonNull(presetList.getSelectedOrNull()).text + ".json").toFile().exists()) {
                        presetList.reload();
                        return;
                    }
                    KeyPresetManager.loadPreset(presetList.getSelectedOrNull().text);
                    MinecraftClient.getInstance().setScreen(parent);
                    if (MinecraftClient.getInstance().currentScreen instanceof KeybindsScreen) {
                        ((KeyBindsScreenAccessor) MinecraftClient.getInstance().currentScreen).getControlsListWidget().update();
                    }
                }).size((int) (width * 0.2), (int) (height * 0.05)).position((int) ((width * 0.375) - (width * 0.1)), (int) (height * 0.85)).build();
        loadButton.active = false; // 初始禁用加载按钮

        // 构建删除按钮
        deleteButton = ButtonWidget.builder(
                KeyPreset.DELETE,
                button -> {
                    if (!KeyPreset.PRESET_FOLDER.resolve(Objects.requireNonNull(presetList.getSelectedOrNull()).text + ".json").toFile().exists()) {
                        presetList.reload();
                        return;
                    }
                    KeyPresetManager.deletePreset(presetList.getSelectedOrNull().text);
                    presetList.reload();
                }).size((int) (width * 0.2), (int) (height * 0.05)).position((int) (((width * 0.125)) - (width * 0.1)), (int) (height * 0.85)).build();
        deleteButton.active = false;  // 初始禁用删除按钮

        // 构建保存按钮
        saveButton = ButtonWidget.builder(
                KeyPreset.SAVE,
                button -> {
                    KeyPresetManager.savePreset(editBox.getText()); // 将编辑框的内容作为参数传递
                    saveButton.active = false;
                    editBox.setText(""); // 将编辑框的内容清空
                    editBoxText = KeyPreset.ENTER_TEXT; // 设置提示文本
                    editBoxTextColor = 0xffffff; // 设置提示文本颜色
                    presetList.reload(); // 重载列表
                }).size((int) (width * 0.15), (int) (height * 0.05)).position((int) ((width * 0.75) - (width * 0.075)), (int) ((height * 0.5) + (height * 0.05))).build();
        saveButton.active = false;  // 初始禁用保存按钮

        // 构建打开文件夹按钮
        openFolderButton = ButtonWidget.builder(
                Text.literal("..."),
                button -> {
                    KeyPresetManager.openPresetFolder(); // 打开预设文件夹
                }).size((int) (width * 0.05), (int) (height * 0.05)).position((int) ((width * 0.5)), (int) (height * 0.85)).tooltip(KeyPreset.OPEN_FOLDER_TOOLTIP).build();

        // 构建完成按钮
        doneButton = ButtonWidget.builder(
                KeyPreset.DONE,
                button -> {
                    MinecraftClient.getInstance().setScreen(parent); // 返回上个界面
                }).size((int) (width * 0.2), (int) (height * 0.05)).position((int) ((width * 0.75) - (width * 0.1)), (int) (height * 0.85)).build();

        addDrawableChild(presetList); // 界面：添加列表控件
        addDrawableChild(editBox); // 界面：添加编辑框
        addDrawableChild(saveButton); // 界面：添加保存按钮
        addDrawableChild(loadButton); // 界面：添加加载按钮
        addDrawableChild(deleteButton); // 界面：添加删除按钮
        addDrawableChild(openFolderButton); // 界面：添加打开文件夹按钮
        addDrawableChild(doneButton); // 界面：添加完成按钮
    }

    // 判断是否存在同名预设文件
    public boolean isExist(String text) {
        for (String fileName : KeyPresetManager.getPresets()) {
            // 判断文件名是否相同（不分区大小写）
            if (fileName.equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // 绘制界面标题
        context.drawText(
                textRenderer,
                KeyPreset.TITLE,
                (width - textRenderer.getWidth(KeyPreset.TITLE.getString())) / 2, (int) (height * 0.03),
                0xffffff, // 文本颜色
                true // 是否显示阴影
        );

        // 绘制编辑框提示文本
        context.drawText(
                textRenderer,
                editBoxText,
                (int) ((width * 0.75) - (textRenderer.getWidth(editBoxText.getString()) / 2.0)), (int) ((height * 0.5) - (height * 0.1)),
                editBoxTextColor, // 文本颜色
                true // 是否显示阴影
        );

        // 判断列表是否选中内容
        if (presetList.getSelectedOrNull() == null) {
            deleteButton.active = false; // 禁用删除按钮
            loadButton.active = false; // 禁用加载按钮
        } else {
            deleteButton.active = true; // 启用删除按钮
            loadButton.active = true; // 启用加载按钮
        }

        // 判断打开文件夹按钮是否处于鼠标悬停状态
        if (!openFolderButton.isHovered()) {
            openFolderButton.setFocused(false); // 关闭焦点
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clickedOnEditBox = editBox.mouseClicked(mouseX, mouseY, button);
        // 判断是否点击编辑框外并且编辑框处于焦点状态
        if (!clickedOnEditBox && editBox.isFocused()) {
            editBox.setFocused(false); // 移除编辑框焦点
            // 判断编辑框内容是否为空
            if (editBox.getText().isBlank()) {
                editBox.setText(""); // 清空编辑框文本
                editBoxText = KeyPreset.ENTER_TEXT; // 设置提示文本
                editBoxTextColor = 0xffffff; // 设置提示文本颜色
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}