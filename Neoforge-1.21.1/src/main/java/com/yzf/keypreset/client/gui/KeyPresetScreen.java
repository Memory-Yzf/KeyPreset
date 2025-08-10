package com.yzf.keypreset.client.gui;

import com.yzf.keypreset.KeyPreset;
import com.yzf.keypreset.KeyPresetManager;
import com.yzf.keypreset.mixin.KeyBindsScreenAccessor;
import com.yzf.keypreset.client.gui.widget.PresetList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
public class KeyPresetScreen extends Screen {
    private Screen parent; // 父界面
    private PresetList presetList; // 预设列表
    private Component editBoxText; // 编辑框提示文本
    private int editBoxTextColor; // 编辑框提示文本颜色
    private EditBox editBox; // 编辑框
    private Button saveButton;  // 保存按钮
    private Button deleteButton; // 删除按钮
    private Button loadButton; // 加载按钮
    private Button refreshButton;  // 刷新按钮
    private Button openFolderButton; // 打开文件夹按钮
    private Button doneButton;  // 完成按钮

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

    // 初始化元素方法
    private void initElement() {
        // 构建列表
        presetList = new PresetList(
                Minecraft.getInstance(),
                width / 2,
                (int) (height * 0.7),
                (int) (height * 0.1),
                20);

        // 构建编辑框
        editBox = new EditBox(
                font,
                (int) ((width * 0.75) - (width * 0.2)),
                height / 2 - 36,
                (int) (width * 0.4),
                16,
                Component.literal(""));

        // 设置编辑框文本改变事件
        editBox.setResponder(text -> {
            // 判断文本是否为空
            if (text.isBlank()) {
                editBoxText = KeyPreset.BLANK_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setTextColor(0xffffff);
                saveButton.active = false;
            }
            // 判断文本是否存在非法字符
            else if (KeyPreset.FORBIDDEN_CHARS.matcher(text).find()) {
                editBoxText = KeyPreset.FORBID_CHAR_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setTextColor(editBoxTextColor);
                saveButton.active = false;
            }
            // 判断是否存在相同名称的文件
            else if (isExist(text)) {
                editBoxText = KeyPreset.EXIST_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setTextColor(editBoxTextColor);
                saveButton.active = false;
            } else {
                editBoxText = KeyPreset.VALID_TEXT;
                editBoxTextColor = 0x37fd3e;
                editBox.setTextColor(0xffffff);
                saveButton.active = true;
            }
        });


        // 构建保存按钮
        saveButton = Button.builder(
                KeyPreset.SAVE,
                button -> {
                    KeyPresetManager.savePreset(editBox.getValue()); // 将编辑框的内容作为参数传递
                    saveButton.active = false;
                    editBox.setValue(""); // 将编辑框的内容清空
                    editBoxText = KeyPreset.ENTER_TEXT; // 设置提示文本
                    editBoxTextColor = 0xffffff; // 设置提示文本颜色
                    presetList.reload(); // 重载列表
                }).size(80, 20).pos((int) (width * 0.75 - 40), height / 2 - 5).build();
        saveButton.active = false;  // 初始禁用保存按钮

        // 构建删除按钮
        deleteButton = Button.builder(
                KeyPreset.DELETE,
                button -> {
                    if (!KeyPreset.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                        presetList.reload();
                        return;
                    }
                    KeyPresetManager.deletePreset(presetList.getSelected().text);
                    presetList.reload();
                }).size(80, 20).pos((int) (width * 0.75 - 40), height / 2 + 20).build();
        deleteButton.active = false;  // 初始禁用删除按钮

        // 构建加载按钮
        loadButton = Button.builder(
                KeyPreset.LOAD,
                button -> {
                    if (!KeyPreset.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                        presetList.reload();
                        return;
                    }
                    KeyPresetManager.loadPreset(presetList.getSelected().text);
                    Minecraft.getInstance().setScreen(parent);
                    if (Minecraft.getInstance().screen instanceof KeyBindsScreen) {
                        ((KeyBindsScreenAccessor) Minecraft.getInstance().screen).getKeyBindsList().refreshEntries();
                    }
                }).size(80, 20).pos(width / 2 - 175, (int) (height * 0.85)).build();
        loadButton.active = false; // 初始禁用加载按钮

        // 构建刷新按钮
        refreshButton = Button.builder(
                KeyPreset.REFRESH,
                button -> {
                    presetList.reload(); // 重载列表
                }).size(80, 20).pos(width / 2 - 85, (int) (height * 0.85)).build();

        // 构建打开文件夹按钮
        openFolderButton = Button.builder(
                KeyPreset.OPEN_FOLDER,
                button -> {
                    KeyPresetManager.openPresetFolder(); // 打开预设文件夹
                }).size(80, 20).pos(width / 2 + 5, (int) (height * 0.85)).build();

        // 构建完成按钮
        doneButton = Button.builder(
                KeyPreset.DONE,
                button -> {
                    Minecraft.getInstance().setScreen(parent); // 返回上个界面
                }).size(80, 20).pos(width / 2 + 95, (int) (height * 0.85)).build();

        addRenderableWidget(presetList); // 界面：添加列表控件
        addRenderableWidget(editBox); // 界面：添加编辑框
        addRenderableWidget(saveButton); // 界面：添加保存按钮
        addRenderableWidget(refreshButton); // 界面：添加刷新按钮
        addRenderableWidget(loadButton); // 界面：添加加载按钮
        addRenderableWidget(deleteButton); // 界面：添加删除按钮
        addRenderableWidget(openFolderButton); // 界面：添加打开文件夹按钮
        addRenderableWidget(doneButton); // 界面：添加完成按钮
    }

    // 判断是否存在同名预设文件
    public boolean isExist(String text) {
        for (String fileName : KeyPresetManager.getPresets()) {
            // 判断文件名是否相同（忽略大小写）
            if (fileName.equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    @Override
    // 重写界面动态渲染方法
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);

        // 绘制界面标题
        guiGraphics.drawString(
                font,
                KeyPreset.TITLE,
                (width - font.width(KeyPreset.TITLE)) / 2, (int) (height * 0.03),
                0xffffff, // 文本颜色
                true // 是否显示阴影
        );

        // 绘制编辑框提示文本
        guiGraphics.drawString(
                font,
                editBoxText,
                (int) ((width * 0.75) - (font.width(editBoxText) / 2.0)), height / 2 - 60,
                editBoxTextColor, // 文本颜色
                true // 是否显示阴影
        );

        // 判断列表是否选中内容
        if (presetList.getSelected() == null) {
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
    // 重写监听鼠标点击方法
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clickedOnEditBox = editBox.mouseClicked(mouseX, mouseY, button);
        // 判断是否点击编辑框外并且编辑框处于焦点状态
        if (!clickedOnEditBox && editBox.isFocused()) {
            editBox.setFocused(false); // 移除编辑框焦点
            // 判断编辑框内容是否为空
            if (editBox.getValue().isBlank()) {
                editBox.setValue(""); // 清空编辑框文本
                editBoxText = KeyPreset.ENTER_TEXT; // 设置提示文本
                editBoxTextColor = 0xffffff; // 设置提示文本颜色
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}