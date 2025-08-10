package com.yzf.screen;

import com.yzf.KeyPreset;
import com.yzf.mixin.KeybindsScreenAccessor;
import com.yzf.preset.PresetManager;
import com.yzf.screen.widget.ListWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.io.File;

import static com.yzf.key.TranslateKey.*;

public class LoadPresetScreen extends Screen {
    private final Screen parent;
    private ListWidget listWidget;
    private ButtonWidget deleteButton;
    private ButtonWidget loadButton;
    private ButtonWidget openFolderButton;
    private ButtonWidget doneButton;

    public LoadPresetScreen(Screen parent) {
        super(SAVE_PRESET);
        this.parent = parent;
    }

    //初始化组件状态
    public void initWidgetState() {
        //列表组件
        //实参解释（游戏实例，组件宽度，组件高度，组件上边距，条目选中时的高度）
        listWidget = new ListWidget(MinecraftClient.getInstance(), KeyPreset.screenWidth(), (int) (KeyPreset.screenHeight() * 0.75) - 25, 33, 20);

        //删除按钮
        deleteButton = ButtonWidget.builder(DELETE, clickEvent -> {
            if (listWidget.getSelectedOrNull() == null) return;
            PresetManager.deletePreset(listWidget.getSelectedOrNull().text);
            listWidget.reload();
        }).build();

        //删除按钮默认状态
        deleteButton.active = false;

        //加载按钮
        loadButton = ButtonWidget.builder(LOAD, clickEvent -> {
            if (listWidget.getSelectedOrNull() == null) return;
            if (PresetManager.loadPreset(listWidget.getSelectedOrNull().text) != 0) close();
            if (MinecraftClient.getInstance().currentScreen instanceof KeybindsScreen) {
                ((KeybindsScreenAccessor) MinecraftClient.getInstance().currentScreen).getControlsListWidget().update();
            }
        }).build();

        //加载按钮默认状态
        loadButton.active = false;

        //打开文件夹按钮
        openFolderButton = ButtonWidget.builder(Text.literal("…"), clickEvent -> {
            File folder = new File(FabricLoader.getInstance().getConfigDir() + "\\KeyPreset");
            if (folder.exists() || folder.isDirectory()) {
                String os = System.getProperty("os.name").toLowerCase();
                ProcessBuilder processBuilder;
                if (os.contains("win")) {
                    processBuilder = new ProcessBuilder("explorer.exe", folder.getAbsolutePath());
                } else if (os.contains("mac")) {
                    processBuilder = new ProcessBuilder("open", folder.getAbsolutePath());
                } else if (os.contains("nux")) {
                    processBuilder = new ProcessBuilder("xdg-open", folder.getAbsolutePath());
                } else {
                    return;
                }
                try {
                    processBuilder.start();
                } catch (Exception e) {
                    KeyPreset.LOGGER.error(String.valueOf(e));
                }
            }
        }).tooltip(OPEN_FOLDER_TOOLTIP).build();

        //完成按钮
        doneButton = ButtonWidget.builder(ScreenTexts.DONE, (clickEvent -> this.close())).build();

        initLayout();
    }

    //初始化组件布局
    public void initLayout() {
        deleteButton.setDimensions(KeyPreset.screenWidth() / 4, 20);
        deleteButton.setPosition(KeyPreset.centerX() - (KeyPreset.screenWidth() / 4) - 10, (int) (KeyPreset.screenHeight() * 0.8));
        loadButton.setDimensions(KeyPreset.screenWidth() / 4, 20);
        loadButton.setPosition(KeyPreset.centerX() + 10, (int) (KeyPreset.screenHeight() * 0.8));
        openFolderButton.setDimensions(20, 20);
        openFolderButton.setPosition(KeyPreset.centerX() + (KeyPreset.screenWidth() / 4 + 20), (int) (KeyPreset.screenHeight() * 0.8));
        doneButton.setDimensions(KeyPreset.screenWidth() / 4, 20);
        doneButton.setPosition(KeyPreset.centerX() - (KeyPreset.screenWidth() / 8), (int) (KeyPreset.screenHeight() * 0.9));
    }

    //返回上个界面
    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }

    //初始化方法
    @Override
    public void init() {
        initWidgetState();
        addDrawableChild(listWidget);
        addDrawableChild(deleteButton);
        addDrawableChild(loadButton);
        addDrawableChild(openFolderButton);
        addDrawableChild(doneButton);
    }

    //动态渲染
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, LOAD_PRESET, KeyPreset.centerX(), (int) (KeyPreset.screenHeight() * 0.045), 0xffffff);
        deleteButton.active = listWidget.getSelectedOrNull() != null;
        loadButton.active = listWidget.getSelectedOrNull() != null;
        initLayout();
    }

    //获取当前类对象
    public static ButtonWidget getButton() {
        return ButtonWidget.builder(LOAD_PRESET, clickEvent -> MinecraftClient
                .getInstance()
                .setScreen(new LoadPresetScreen(MinecraftClient.getInstance().currentScreen))).width(74).build();
    }
}