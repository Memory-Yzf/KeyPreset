package com.yzf;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class KeyPreset implements ClientModInitializer {
    public static final String MOD_ID = "keypreset";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path PRESET_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("KeyPreset");
    public static final Pattern FORBIDDEN_CHARS = Pattern.compile("[\\\\/:*?\"<>|]"); // 非法字符正则表达式
    public static final Text ENTRY = Text.translatable("keypreset.entry");
    public static final Text TITLE = Text.translatable("keypreset.title");
    public static final Text SAVE = Text.translatable("keypreset.save");
    public static final Text LOAD = Text.translatable("keypreset.load");
    public static final Text DONE = Text.translatable("keypreset.done");
    public static final Text DELETE = Text.translatable("keypreset.delete");
    public static final Tooltip OPEN_FOLDER_TOOLTIP = Tooltip.of(Text.translatable("keypreset.open.folder.tooltip"));
    public static final Text ENTER_TEXT = Text.translatable("keypreset.screen.enter.text");
    public static final Text BLANK_TEXT = Text.translatable("keypreset.screen.blank.text");
    public static final Text VALID_TEXT = Text.translatable("keypreset.screen.valid.text");
    public static final Text EXIST_TEXT = Text.translatable("keypreset.screen.exist.text");
    public static final Text FORBID_CHAR_TEXT = Text.translatable("keypreset.screen.forbid.char.text", "\\ / : * ? \" < > |");

    public void onInitializeClient() {
        KeyPresetManager.createPresetFolder();
    }
}