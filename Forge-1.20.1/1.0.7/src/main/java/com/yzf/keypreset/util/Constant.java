package com.yzf.keypreset.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLPaths;
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
    public static final Path CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("keypreset.json"); // 配置文件
    public static final Path CONFIG_FOLDER = FMLPaths.CONFIGDIR.get(); // 配置文件
    public static final Path PRESET_FOLDER = FMLPaths.CONFIGDIR.get().resolve("KeyPreset"); // 预设文件夹
    // 正则表达式
    public static final Pattern FORBIDDEN_CHARS = Pattern.compile("[\\\\/:*?\"<>|]"); // 非法字符正则表达式
    // 翻译键
    public static final Component PRESET_TITLE = Component.translatable("keypreset.screen.title_preset");
    public static final Component BINDING_TITLE = Component.translatable("keypreset.screen.title_binding");
    public static final Component CONFIRM_TITLE = Component.translatable("keypreset.screen.title_confirm");
    public static final Component SAVE = Component.translatable("keypreset.screen.button_save");
    public static final Component DELETE = Component.translatable("keypreset.screen.button_delete");
    public static final Component LOAD = Component.translatable("keypreset.screen.button_load");
    public static final Component COVER = Component.translatable("keypreset.screen.button_cover");
    public static final Component REFRESH = Component.translatable("keypreset.screen.button_refresh");
    public static final Component OPEN_FOLDER = Component.translatable("keypreset.screen.button_preset_folder");
    public static final Component OPEN_CONFIG = Component.translatable("keypreset.screen.button_config_folder");
    public static final Component DONE = Component.translatable("keypreset.screen.button_done");
    public static final Component CONFIRM = Component.translatable("keypreset.screen.button_confirm");
    public static final Component CANCEL = Component.translatable("keypreset.screen.button_cancel");
    public static final Component RESET = Component.translatable("keypreset.screen.button_reset");
    public static final Component COVER_TOOLTIP = Component.translatable("keypreset.screen.message_cover_file");
    public static final Component DELETE_TOOLTIP = Component.translatable("keypreset.screen.message_delete_file");
    public static final Component ENTER_TOOLTIP = Component.translatable("keypreset.screen.message_enter_name");
    public static final Component VALID_TOOLTIP = Component.translatable("keypreset.screen.message_valid_name");
    public static final Component EXIST_TOOLTIP = Component.translatable("keypreset.screen.message_exist_file");
    public static final Component FORBID_TOOLTIP = Component.translatable("keypreset.screen.message_forbid_char");
    public static final Component SWITCH_TOOLTIP = Component.translatable("keypreset.ingame_chat_switch_tooltip");
}
