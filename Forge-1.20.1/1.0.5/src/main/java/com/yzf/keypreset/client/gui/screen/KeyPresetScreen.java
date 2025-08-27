package com.yzf.keypreset.client.gui.screen;

import com.google.gson.JsonObject;
import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import com.yzf.keypreset.util.PresetManager;
import com.yzf.keypreset.mixin.KeyBindsScreenAccessor;
import com.yzf.keypreset.client.gui.widget.PresetList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    private Button preserBindingButton; // 预设绑定按钮
    private Button openFolderButton; // 打开文件夹按钮
    private Button doneButton;  // 完成按钮
    List<Button> bottomButons = new ArrayList<>(); // 底部按钮列表

    public KeyPresetScreen(Screen parent) {
        super(Constant.TITLE); // 界面标题
        this.parent = parent; // 父界面
        this.editBoxText = Constant.ENTER_TEXT; // 编辑框默认提示文本
        this.editBoxTextColor = 0xffffff; // 编辑框默认提示文本颜色
    }

    @Override
    protected void init() {
        super.init();
        bottomButons.clear(); // 清空按钮列表防止重复添加
        // 构建列表
        presetList = new PresetList(
                Minecraft.getInstance(),
                width / 2,
                (int) (height * 0.7),
                (int) (height * 0.1),
                (int) (height * 0.8),
                20);

        // 构建编辑框
        editBox = new EditBox(
                font,
                (int) ((width * 0.75) - (width * 0.2)),
                height / 2 - 36,
                (int) (width * 0.4),
                16,
                Component.empty());

        // 设置编辑框文本改变事件
        editBox.setResponder(text -> {
            // 判断文本是否为空
            if (text.isBlank()) {
                editBoxText = Constant.BLANK_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setTextColor(0xffffff);
                saveButton.active = false;
            }
            // 判断文本是否存在非法字符
            else if (Constant.FORBIDDEN_CHARS.matcher(text).find()) {
                editBoxText = Constant.FORBID_CHAR_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setTextColor(editBoxTextColor);
                saveButton.active = false;
            }
            // 判断是否存在相同名称的文件
            else if (isExist(text)) {
                editBoxText = Constant.EXIST_TEXT;
                editBoxTextColor = 0xfb4b4b;
                editBox.setTextColor(editBoxTextColor);
                saveButton.active = false;
            } else {
                editBoxText = Constant.VALID_TEXT;
                editBoxTextColor = 0x37fd3e;
                editBox.setTextColor(0xffffff);
                saveButton.active = true;
            }
        });

        // 构建保存按钮
        saveButton = Button.builder(
                Constant.SAVE,
                button -> {
                    PresetManager.savePreset(editBox.getValue()); // 将编辑框的内容作为参数传递
                    saveButton.active = false;
                    editBox.setValue(""); // 将编辑框的内容清空
                    editBoxText = Constant.ENTER_TEXT; // 设置提示文本
                    editBoxTextColor = 0xffffff; // 设置提示文本颜色
                    presetList.reload(); // 重载列表
                }).size(80, 20).pos((int) (width * 0.75 - 40), height / 2 - 5).build();
        saveButton.active = false;  // 初始禁用保存按钮

        // 构建加载按钮
        loadButton = Button.builder(
                Constant.LOAD,
                button -> {
                    if (!Constant.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                        presetList.reload();
                        return;
                    }
                    PresetManager.loadPreset(presetList.getSelected().text);
                    Minecraft.getInstance().setScreen(parent);
                    if (Minecraft.getInstance().screen instanceof KeyBindsScreen) {
                        ((KeyBindsScreenAccessor) Minecraft.getInstance().screen).getKeyBindsList().refreshEntries();
                    }
                }).size(80, 20).pos((int) (width * 0.75 - 40), height / 2 + 20).build();
        loadButton.active = false; // 初始禁用加载按钮

        // 构建删除按钮
        deleteButton = Button.builder(
                Constant.DELETE,
                button -> {
                    String selected = presetList.getSelected().text;

                    if (!Constant.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                        presetList.reload();
                        return;
                    }

                    PresetManager.deletePreset(selected);

                    JsonObject config = Config.loadConfig();
                    for (int i = 1; i < 6; i++) {
                        String key = "preset_binding_" + i;
                        if (config.has(key) && config.get(key).getAsString().equals(selected)) {
                            Config.savePresetName(i, "");
                        }
                    }

                    presetList.reload();
                }).size(80, 20).pos((int) (width * 0.75 - 40), height / 2 + 45).build();
        deleteButton.active = false;  // 初始禁用删除按钮

        // 构建刷新按钮
        refreshButton = Button.builder(
                Constant.REFRESH,
                button -> {
                    presetList.reload(); // 重载列表
                }).build();

        // 构建预设绑定按钮
        preserBindingButton = Button.builder(
                Constant.PRESET_BINGDING,
                button -> {
                    Minecraft.getInstance().setScreen(new PresetBindsScreen(parent));
                }).build();

        // 构建打开文件夹按钮
        openFolderButton = Button.builder(
                Constant.OPEN_FOLDER,
                button -> {
                    PresetManager.openPresetFolder(); // 打开预设文件夹
                }).build();

        // 构建完成按钮
        doneButton = Button.builder(
                Constant.DONE,
                button -> {
                    Minecraft.getInstance().setScreen(parent); // 返回上个界面
                }).build();

        bottomButons.add(refreshButton); // 列表：添加刷新按钮
        bottomButons.add(preserBindingButton); // 列表：添加预设绑定按钮
        bottomButons.add(openFolderButton); // 列表：添加打开文件夹按钮
        bottomButons.add(doneButton); // 列表：添加完成按钮

        int buttonWidth = 80; // 按钮宽度
        int buttonHeight = 20; // 按钮高度
        int buttonSpacing = 10; // 按钮间距
        int totalWidth = (bottomButons.size() * buttonWidth) + ((bottomButons.size() - 1) * buttonSpacing);
        int startX = (width - totalWidth) / 2; // 按钮起始X坐标
        int startY = (int) (height * 0.85); // 按钮起始Y坐标

        // 设置按钮属性与添加按钮
        for (int i = 0; i < bottomButons.size(); i++) {
            Button temp = bottomButons.get(i); // 获取列表按钮
            temp.setWidth(buttonWidth);
            temp.setHeight(buttonHeight);
            temp.setPosition(startX + i * (buttonWidth + buttonSpacing), startY); // 设置按钮位置
            addRenderableWidget(temp); // 界面：添加按钮
        }

        addRenderableWidget(presetList); // 界面：添加预设列表
        addRenderableWidget(editBox); // 界面：添加编辑框
        addRenderableWidget(saveButton); // 界面：添加保存按钮
        addRenderableWidget(loadButton); // 界面：添加加载按钮
        addRenderableWidget(deleteButton); // 界面：添加删除按钮
    }

    // 判断是否存在同名预设文件
    public boolean isExist(String text) {
        for (String fileName : PresetManager.getPresets()) {
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
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);

        // 绘制界面标题
        guiGraphics.drawString(
                font,
                Constant.TITLE,
                (width - font.width(Constant.TITLE)) / 2, (int) (height * 0.03),
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

        // 判断刷新按钮是否处于鼠标悬停状态
        if (!refreshButton.isHovered()) {
            refreshButton.setFocused(false); // 关闭焦点
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
                editBoxText = Constant.ENTER_TEXT; // 设置提示文本
                editBoxTextColor = 0xffffff; // 设置提示文本颜色
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}