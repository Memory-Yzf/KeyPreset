package com.yzf.keypreset.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.regex.Pattern;

public class Constant {
    // 模组ID
    public static final String MOD_ID = "keypreset";
    // 日志器
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // GSON 美化构建对象
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create(); // Json美化构建对象
    // 路径
    public static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("keypreset.json"); // 配置文件
    public static final Path CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir(); // 配置文件
    public static final Path PRESET_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("KeyPreset"); // 预设文件夹
    // 正则表达式
    public static final Pattern FORBIDDEN_CHARS = Pattern.compile("[\\\\/:*?\"<>|]"); // 非法字符正则表达式
    // 翻译键
    public static final Text PRESET_TITLE = Text.translatable("keypreset.screen.title_preset");
    public static final Text BINDING_TITLE = Text.translatable("keypreset.screen.title_binding");
    public static final Text CONFIRM_TITLE = Text.translatable("keypreset.screen.title_confirm");
    public static final Text SAVE = Text.translatable("keypreset.screen.button_save");
    public static final Text DELETE = Text.translatable("keypreset.screen.button_delete");
    public static final Text LOAD = Text.translatable("keypreset.screen.button_load");
    public static final Text COVER = Text.translatable("keypreset.screen.button_cover");
    public static final Text REFRESH = Text.translatable("keypreset.screen.button_refresh");
    public static final Text OPEN_FOLDER = Text.translatable("keypreset.screen.button_preset_folder");
    public static final Text OPEN_CONFIG = Text.translatable("keypreset.screen.button_config_folder");
    public static final Text DONE = Text.translatable("keypreset.screen.button_done");
    public static final Text CONFIRM = Text.translatable("keypreset.screen.button_confirm");
    public static final Text CANCEL = Text.translatable("keypreset.screen.button_cancel");
    public static final Text RESET = Text.translatable("keypreset.screen.button_reset");
    public static final Text COVER_TOOLTIP = Text.translatable("keypreset.screen.message_cover_file");
    public static final Text DELETE_TOOLTIP = Text.translatable("keypreset.screen.message_delete_file");
    public static final Text ENTER_TOOLTIP = Text.translatable("keypreset.screen.message_enter_name");
    public static final Text VALID_TOOLTIP = Text.translatable("keypreset.screen.message_valid_name");
    public static final Text EXIST_TOOLTIP = Text.translatable("keypreset.screen.message_exist_file");
    public static final Text FORBID_TOOLTIP = Text.translatable("keypreset.screen.message_forbid_char");
    public static final Text SWITCH_TOOLTIP = Text.translatable("keypreset.ingame_chat_switch_tooltip");
}