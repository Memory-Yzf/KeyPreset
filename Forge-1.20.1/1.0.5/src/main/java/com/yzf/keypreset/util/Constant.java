package com.yzf.keypreset.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class Constant {
    public static final String MOD_ID = "keypreset"; // 模组ID
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID); // 日志记录器
    public static final Path CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("keypreset.json"); // 配置文件
    public static final Path PRESET_FOLDER = FMLPaths.CONFIGDIR.get().resolve("KeyPreset"); // 预设文件夹
    public static final Pattern FORBIDDEN_CHARS = Pattern.compile("[\\\\/:*?\"<>|]"); // 非法字符正则表达式
    public static final MutableComponent TITLE = Component.translatable("keypreset.title"); // 界面标题
    public static final MutableComponent SAVE = Component.translatable("keypreset.save"); // 保存按钮
    public static final MutableComponent DELETE = Component.translatable("keypreset.delete"); // 删除按钮
    public static final MutableComponent LOAD = Component.translatable("keypreset.load"); // 加载按钮
    public static final MutableComponent REFRESH = Component.translatable("keypreset.refresh"); // 刷新按钮
    public static final MutableComponent OPEN_FOLDER = Component.translatable("keypreset.open_folder"); // 打开文件夹按钮
    public static final MutableComponent PRESET_BINGDING = Component.translatable("keypreset.preset_bingding"); // 预设绑定按钮
    public static final MutableComponent DONE = Component.translatable("keypreset.done"); // 完成按钮
    public static final MutableComponent ENTER_TEXT = Component.translatable("keypreset.screen_enter_text"); // 提示文本：输入文本
    public static final MutableComponent BLANK_TEXT = Component.translatable("keypreset.screen_blank_text"); // 提示文本：空文本
    public static final MutableComponent VALID_TEXT = Component.translatable("keypreset.screen_valid_text"); // 提示文本：有效文本
    public static final MutableComponent EXIST_TEXT = Component.translatable("keypreset.screen_exist_text"); // 提示文本：已存在
    public static final MutableComponent FORBID_CHAR_TEXT = Component.translatable("keypreset.screen_forbid_char_text", "\\ / : * ? \" < > |"); // 提示文本：非法字符
    public static final MutableComponent MOD_CATEGORIES = Component.translatable("key.categories.keypreset"); // 键位预设类别
    public static final String[] APPLY_PRESET_KEYS = new String[]{
            "key.keypreset.apply_preset_1",
            "key.keypreset.apply_preset_2",
            "key.keypreset.apply_preset_3",
            "key.keypreset.apply_preset_4",
            "key.keypreset.apply_preset_5"
    }; // 应用预设按键列表
}
