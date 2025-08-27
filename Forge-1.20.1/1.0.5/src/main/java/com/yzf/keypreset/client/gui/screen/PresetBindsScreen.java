package com.yzf.keypreset.client.gui.screen;

import com.google.gson.JsonObject;
import com.yzf.keypreset.client.gui.widget.PresetList;
import com.yzf.keypreset.client.gui.widget.ScrollStringWidget;
import com.yzf.keypreset.util.BindingManager;
import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class PresetBindsScreen extends Screen {
    private Screen parent; // 父界面
    private PresetList presetList; // 预设列表
    private Button loadButton; // 加载按钮
    private Button refreshButton;  // 刷新按钮
    private Button keyPresetButton; // 键位预设按钮
    private Button openFolderButton; // 打开文件夹按钮
    private Button doneButton;  // 完成按钮
    private Button addButton_1; // 添加绑定按钮（1）
    private Button addButton_2; // 添加绑定按钮（2）
    private Button addButton_3; // 添加绑定按钮（3）
    private Button addButton_4; // 添加绑定按钮（4）
    private Button addButton_5; // 添加绑定按钮（5）
    private Button removeButton_1; // 删除绑定按钮（1）
    private Button removeButton_2; // 删除绑定按钮（2）
    private Button removeButton_3; // 删除绑定按钮（3）
    private Button removeButton_4; // 删除绑定按钮（4）
    private Button removeButton_5; // 删除绑定按钮（5）
    private ScrollStringWidget presetName_1; // 预设名称（1）
    private ScrollStringWidget presetName_2; // 预设名称（2）
    private ScrollStringWidget presetName_3; // 预设名称（3）
    private ScrollStringWidget presetName_4; // 预设名称（4）
    private ScrollStringWidget presetName_5; // 预设名称（5）
    private List<Button> addButtons = new ArrayList<>(); // 添加按钮列表
    private List<Button> removeButtons = new ArrayList<>(); // 删除按钮列表
    private List<ScrollStringWidget> presetNames = new ArrayList<>(); // 预设名按钮列表
    private List<Button> bottomButons = new ArrayList<>(); // 底部按钮列表

    protected PresetBindsScreen(Screen parent) {
        super(Constant.PRESET_BINGDING); // 界面标题
        this.parent = parent; // 父界面
    }

    @Override
    protected void init() {
        super.init();
        // 清空旧按钮（防止重复添加）
        addButtons.clear();
        removeButtons.clear();
        bottomButons.clear();

        // 构建列表
        presetList = new PresetList(
                Minecraft.getInstance(),
                (int) (width / 2),
                (int) (height * 0.7),
                (int) (height * 0.1),
                (int) (height * 0.8),
                20);
        addRenderableWidget(presetList);

        addButton_1 = Button.builder(Component.literal("+"), b -> {
            if (Constant.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                presetName_1.setText(presetList.getSelected().text);
                Config.savePresetName(1, presetList.getSelected().text);
            }
            presetList.reload();
        }).build();
        addButton_1.active = false;
        addButtons.add(addButton_1);

        addButton_2 = Button.builder(Component.literal("+"), b -> {
            if (Constant.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                presetName_2.setText(presetList.getSelected().text);
                Config.savePresetName(2, presetList.getSelected().text);
            }
            presetList.reload();
        }).build();
        addButton_2.active = false;
        addButtons.add(addButton_2);

        addButton_3 = Button.builder(Component.literal("+"), b -> {
            if (Constant.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                presetName_3.setText(presetList.getSelected().text);
                Config.savePresetName(3, presetList.getSelected().text);
            }
            presetList.reload();
        }).build();
        addButton_3.active = false;
        addButtons.add(addButton_3);

        addButton_4 = Button.builder(Component.literal("+"), b -> {
            if (Constant.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                presetName_4.setText(presetList.getSelected().text);
                Config.savePresetName(4, presetList.getSelected().text);
            }
            presetList.reload();
        }).build();
        addButton_4.active = false;
        addButtons.add(addButton_4);

        addButton_5 = Button.builder(Component.literal("+"), b -> {
            if (Constant.PRESET_FOLDER.resolve(presetList.getSelected().text + ".json").toFile().exists()) {
                presetName_5.setText(presetList.getSelected().text);
                Config.savePresetName(5, presetList.getSelected().text);
            }
            presetList.reload();
        }).build();
        addButton_5.active = false;
        addButtons.add(addButton_5);

        int add_buttonWidth = 30;
        int add_buttonHeight = 30;
        int add_buttonSpacing = 10;
        int add_totalHeight = (addButtons.size() * add_buttonHeight) + ((addButtons.size() - 1) * add_buttonSpacing);
        int add_startX = (int) (width * 0.5 + 10);
        int add_startY = (int) (((height * 0.9) / 2) - (add_totalHeight / 2));

        for (int i = 0; i < addButtons.size(); i++) {
            Button temp = addButtons.get(i);
            temp.setWidth(add_buttonWidth);
            temp.setHeight(add_buttonHeight);
            temp.setPosition(add_startX, add_startY + i * (add_buttonHeight + add_buttonSpacing)); // 设置按钮位置
            addRenderableWidget(temp); // 添加按钮
        }

        removeButton_1 = Button.builder(Component.literal("-"), b -> {
            presetName_1.setText("");
            Config.savePresetName(1, "");
        }).build();
        removeButton_1.active = false;
        removeButtons.add(removeButton_1);

        removeButton_2 = Button.builder(Component.literal("-"), b -> {
            presetName_2.setText("");
            Config.savePresetName(2, "");
        }).build();
        removeButton_2.active = false;
        removeButtons.add(removeButton_2);

        removeButton_3 = Button.builder(Component.literal("-"), b -> {
            presetName_3.setText("");
            Config.savePresetName(3, "");
        }).build();
        removeButton_3.active = false;
        removeButtons.add(removeButton_3);

        removeButton_4 = Button.builder(Component.literal("-"), b -> {
            presetName_4.setText("");
            Config.savePresetName(4, "");
        }).build();
        removeButton_4.active = false;
        removeButtons.add(removeButton_4);

        removeButton_5 = Button.builder(Component.literal("-"), b -> {
            presetName_5.setText("");
            Config.savePresetName(5, "");
        }).build();
        removeButton_5.active = false;
        removeButtons.add(removeButton_5);

        int remove_buttonWidth = 30;
        int remove_buttonHeight = 30;
        int remove_buttonSpacing = 10;
        int remove_totalHeight = (removeButtons.size() * remove_buttonHeight) + ((removeButtons.size() - 1) * remove_buttonSpacing);
        int remove_startX = (int) (width * 0.5 + add_buttonWidth + 15);
        int remove_startY = (int) (((height * 0.9) / 2) - (add_totalHeight / 2));

        for (int i = 0; i < removeButtons.size(); i++) {
            Button temp = removeButtons.get(i);
            temp.setWidth(add_buttonWidth);
            temp.setHeight(add_buttonHeight);
            temp.setPosition(remove_startX, remove_startY + i * (remove_buttonHeight + remove_buttonSpacing)); // 设置按钮位置
            addRenderableWidget(temp); // 添加按钮
        }

        JsonObject configText = Config.loadConfig();

        presetName_1 = new ScrollStringWidget(
                (int) (width * 0.5 + add_buttonWidth + remove_buttonWidth + 20),
                (int) ((height * 0.9) / 2 - 95),
                80,
                30,
                configText.has("preset_binding_1") ? configText.get("preset_binding_1").getAsString() : ""
        );
        presetNames.add(presetName_1);
        addRenderableWidget(presetName_1);

        presetName_2 = new ScrollStringWidget(
                (int) (width * 0.5 + add_buttonWidth + remove_buttonWidth + 20),
                (int) ((height * 0.9) / 2 - 55),
                80,
                30,
                configText.has("preset_binding_2") ? configText.get("preset_binding_2").getAsString() : ""
        );
        presetNames.add(presetName_2);
        addRenderableWidget(presetName_2);

        presetName_3 = new ScrollStringWidget(
                (int) (width * 0.5 + add_buttonWidth + remove_buttonWidth + 20),
                (int) ((height * 0.9) / 2 - 15),
                80,
                30,
                configText.has("preset_binding_3") ? configText.get("preset_binding_3").getAsString() : ""
        );
        presetNames.add(presetName_3);
        addRenderableWidget(presetName_3);

        presetName_4 = new ScrollStringWidget(
                (int) (width * 0.5 + add_buttonWidth + remove_buttonWidth + 20),
                (int) ((height * 0.9) / 2 + 25),
                80,
                30,
                configText.has("preset_binding_4") ? configText.get("preset_binding_4").getAsString() : ""
        );
        presetNames.add(presetName_4);
        addRenderableWidget(presetName_4);

        presetName_5 = new ScrollStringWidget(
                (int) (width * 0.5 + add_buttonWidth + remove_buttonWidth + 20),
                (int) ((height * 0.9) / 2 + 65),
                80,
                30,
                configText.has("preset_binding_5") ? configText.get("preset_binding_5").getAsString() : ""
        );
        presetNames.add(presetName_5);
        addRenderableWidget(presetName_5);

        // 构建刷新按钮
        refreshButton = Button.builder(Constant.REFRESH, b -> {
            presetList.reload();
        }).build();
        bottomButons.add(refreshButton);

        // 构建键位预设按钮
        keyPresetButton = Button.builder(Constant.TITLE, b -> {
            Minecraft.getInstance().setScreen(new KeyPresetScreen(parent));
        }).build();
        bottomButons.add(keyPresetButton);

        // 构建打开文件夹按钮
        openFolderButton = Button.builder(Constant.OPEN_FOLDER, b -> {
            BindingManager.openConfigFolder();
        }).build();
        bottomButons.add(openFolderButton);

        // 构建完成按钮
        doneButton = Button.builder(Constant.DONE, b -> {
            Minecraft.getInstance().setScreen(parent); // 返回上个界面
        }).build();
        bottomButons.add(doneButton);

        int bottom_buttonWidth = 80; // 按钮宽度
        int bottom_buttonHeight = 20; // 按钮高度
        int bottom_buttonSpacing = 10; // 按钮间距
        int bottom_totalWidth = (bottomButons.size() * bottom_buttonWidth) + ((bottomButons.size() - 1) * bottom_buttonSpacing);
        int bottom_startX = (width - bottom_totalWidth) / 2; // 按钮起始X坐标
        int bottom_startY = (int) (height * 0.85); // 按钮起始Y坐标

        // 设置按钮属性与添加按钮
        for (int i = 0; i < bottomButons.size(); i++) {
            Button temp = bottomButons.get(i); // 获取列表按钮
            temp.setWidth(bottom_buttonWidth);
            temp.setHeight(bottom_buttonHeight);
            temp.setPosition(bottom_startX + i * (bottom_buttonWidth + bottom_buttonSpacing), bottom_startY); // 设置按钮位置
            addRenderableWidget(temp); // 添加按钮
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // 绘制界面标题
        guiGraphics.drawString(
                font,
                Constant.PRESET_BINGDING,
                (width - font.width(Constant.TITLE)) / 2, (int) (height * 0.03),
                0xffffff, // 文本颜色
                true // 是否显示阴影
        );

        // 取消按钮焦点（美化体验）
        if (!openFolderButton.isHovered()) openFolderButton.setFocused(false);
        if (!refreshButton.isHovered()) refreshButton.setFocused(false);

        // 根据选择项控制“添加按钮”状态
        boolean hasSelection = presetList.getSelected() != null;
        for (Button btn : addButtons) {
            btn.active = hasSelection;
        }

        JsonObject config = Config.loadConfig();

        // 根据文本控制“删除按钮”状态
        for (int i = 0; i < removeButtons.size(); i++) {
            String key = "preset_binding_" + (i + 1);
            boolean hasValue = config.has(key) && !config.get(key).getAsString().isEmpty();

            removeButtons.get(i).active = hasValue;
        }
    }
}
