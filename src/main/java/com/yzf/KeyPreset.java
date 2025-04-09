package com.yzf;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.regex.Pattern;

@Mod(KeyPreset.MOD_ID)
public class KeyPreset {
    public static final String MOD_ID = "keypreset"; // 模组ID
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID); // 日志记录器
    public static final Path PRESET_FOLDER = FMLPaths.CONFIGDIR.get().resolve("KeyPreset"); // 模组预设文件夹
    public static final Pattern FORBIDDEN_CHARS = Pattern.compile("[\\\\/:*?\"<>|]"); // 非法字符正则表达式
    public static final MutableComponent ENTRY = Component.translatable("keypreset.entry"); // 入口按钮
    public static final MutableComponent TITLE = Component.translatable("keypreset.title"); // 界面标题
    public static final MutableComponent SAVE = Component.translatable("keypreset.save"); // 保存按钮
    public static final MutableComponent LOAD = Component.translatable("keypreset.load"); // 加载按钮
    public static final MutableComponent DONE = Component.translatable("keypreset.done"); // 完成按钮
    public static final MutableComponent DELETE = Component.translatable("keypreset.delete"); // 删除按钮
    public static final MutableComponent OPEN_FOLDER_TOOLTIP = Component.translatable("keypreset.open.folder.tooltip"); // 打开文件夹按钮
    public static final MutableComponent ENTER_TEXT = Component.translatable("keypreset.screen.enter.text"); // 提示文本：输入文本
    public static final MutableComponent BLANK_TEXT = Component.translatable("keypreset.screen.blank.text"); // 提示文本：空文本
    public static final MutableComponent VALID_TEXT = Component.translatable("keypreset.screen.valid.text"); // 提示文本：有效文本
    public static final MutableComponent EXIST_TEXT = Component.translatable("keypreset.screen.exist.text"); // 提示文本：已存在
    public static final MutableComponent FORBID_CHAR_TEXT = Component.translatable("keypreset.screen.forbid.char.text", "\\ / : * ? \" < > |"); // 提示文本：非法字符

    public KeyPreset() {
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus(); // 获取事件总线
        if (modEventBus != null) {
            modEventBus.addListener(this::createFolderEvent); // 订阅事件
        }
    }

    // 创建文件夹事件
    private void createFolderEvent(FMLClientSetupEvent event) {
        event.enqueueWork(KeyPresetManager::createPresetFolder); // 调用创建预设文件夹方法
    }
}