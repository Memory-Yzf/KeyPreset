package com.yzf.screen;

import com.yzf.KeyPreset;
import com.yzf.preset.PresetManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.intellij.lang.annotations.Language;

import java.io.File;
import java.util.regex.Pattern;

import static com.yzf.key.TranslateKey.*;

public class SavePresetScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget nameWidget; //文本输入框
    private ButtonWidget saveWidget; //保存按钮
    private ButtonWidget doneButton; //完成按钮
    private Text currentMessage = ENTER_PRESET_NAME; //提示文本
    private int messageColor = 0xbbbbbb; //提示文本颜色

    @Language("RegExp")
    public final static String[] FORBIDDEN_CHARS = {"\\\\", "/", ":", "<", ">", "\"", "\\|", "\\?", "\\*"};
    public static final Pattern FORBIDDEN_CHARS_PATTERN = Pattern.compile(String.join("|", FORBIDDEN_CHARS));

    public SavePresetScreen(Screen parent) {
        super(SAVE_PRESET);
        this.parent = parent;
    }

    //初始化组件状态
    public void initWidgetState() {
        //文本输入框
        nameWidget = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, KeyPreset.screenWidth() / 2, 20, PRESET);

        //文本输入框监听器
        nameWidget.setChangedListener(text -> {
            if (text.isBlank()) {
                currentMessage = EMPTY_PRESET_NAME;
                messageColor = 0xfb4b4b;
            } else if (FORBIDDEN_CHARS_PATTERN.matcher(text).find()) {
                currentMessage = CHAR_PRESET_NAME;
                messageColor = 0xfb4b4b;
            } else if (isExist(text)) {
                currentMessage = EXIST_PRESET_NAME;
                messageColor = 0xfb4b4b;
            } else {
                currentMessage = VALID_PRESET_NAME;
                messageColor = 0x37fd3e;
            }
        });

        //保存按钮
        saveWidget = ButtonWidget.builder(SAVE, (clickEvent) -> {
            var text = nameWidget.getText();
            if (text.isBlank()) {
                System.out.println(FabricLoader.getInstance().getConfigDir());
                currentMessage = EMPTY_PRESET_NAME;
                messageColor = 0xfb4b4b;
                return;
            }
            if (FORBIDDEN_CHARS_PATTERN.matcher(text).find()) {
                return;
            }
            if (PresetManager.savePreset(text) != 0) {
                close();
            }
        }).build();

        //完成按钮
        doneButton = ButtonWidget.builder(ScreenTexts.DONE, (clickEvent -> this.close())).build();

        initLayout();
    }

    //初始化组件布局
    public void initLayout() {
        nameWidget.setDimensions(KeyPreset.screenWidth() / 2, 20);
        nameWidget.setPosition(KeyPreset.centerX() - (KeyPreset.screenWidth() / 4), (int) (KeyPreset.screenHeight() * 0.4));
        saveWidget.setDimensions(KeyPreset.screenWidth() / 8, 20);
        saveWidget.setPosition(KeyPreset.centerX() - (KeyPreset.screenWidth() / 16), (int) (KeyPreset.screenHeight() * 0.55));
        doneButton.setDimensions(KeyPreset.screenWidth() / 4, 20);
        doneButton.setPosition(KeyPreset.centerX() - (KeyPreset.screenWidth() / 8), (int) (KeyPreset.screenHeight() * 0.9));
    }

    //判断是否存在同名预设文件
    public boolean isExist(String text) {
        File folder = new File(FabricLoader.getInstance().getConfigDir() + "\\KeyPreset");
        if (!folder.isDirectory()) return false;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().equalsIgnoreCase(text + ".json")) {
                    return true;
                }
            }
        }
        return false;
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
        this.addDrawableChild(doneButton);
        this.addDrawableChild(nameWidget);
        this.addDrawableChild(saveWidget);
    }

    //动态渲染
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, SAVE_PRESET, KeyPreset.centerX(), (int) (KeyPreset.screenHeight() * 0.1), 0xffffff);
        context.drawCenteredTextWithShadow(textRenderer, currentMessage, KeyPreset.centerX(), (int) (KeyPreset.screenHeight() * 0.3), messageColor);
        initLayout();
    }

    //获取当前类对象
    public static ButtonWidget getButton() {
        return ButtonWidget.builder(SAVE_PRESET, clickEvent -> MinecraftClient
                .getInstance()
                .setScreen(new SavePresetScreen(MinecraftClient.getInstance().currentScreen))).width(74).build();
    }
}