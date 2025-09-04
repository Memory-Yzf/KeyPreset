package com.yzf.keypreset.util;

import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.KeyModifier;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

public class PresetManager {
    private static final Type STRING_MAP_TYPE = TypeToken.getParameterized(LinkedHashMap.class, String.class, String.class).getType(); // 创建泛型类型标识（解决类型擦除问题）

    // 保存预设
    public static void savePreset(String name) {
        Config.createPresetFolder();
        // 按键绑定链表
        LinkedHashMap<String, String> keyBindsMap = new LinkedHashMap<>();
        // 遍历按键绑定映射表
        for (var keyMappings : Minecraft.getInstance().options.keyMappings) {
            // 获取按键名称
            String key = keyMappings.getName();
            // 获取修饰符（ALT / SHIFT / CONTROL）
            KeyModifier modifier = keyMappings.getKeyModifier();
            // 获取按键键位
            String value = keyMappings.getKey().toString();
            // 条件：修饰符不为空
            if (modifier != KeyModifier.NONE) {
                // 拼接键位和修饰符
                value = value + ":" + modifier.name();
            }
            // 将按键名称和按键键位加入链表
            keyBindsMap.put(key, value);
        }
        // 开启IO流，处理目标文件
        try (FileWriter writer = new FileWriter(Constant.PRESET_FOLDER.resolve(name + ".json").toString())) {
            Constant.GSON.toJson(keyBindsMap, writer); // 序列化链表内容后写入目标文件
        } catch (IOException e) {
            Constant.LOGGER.error("保存预设文件失败: {}", e.getMessage());
        }
    }

    public static void deletePreset(String name) {
        Config.createPresetFolder(); // 创建预设文件夹
        try {
            Files.delete(Constant.PRESET_FOLDER.resolve(name + ".json"));
        } catch (IOException e) {
            Constant.LOGGER.error("删除预设文件失败: {}", e.getMessage());
        }
    }

    // 加载预设
    public static void loadPreset(String name) {
        Config.createPresetFolder(); // 创建预设文件夹
        // 开启IO流，读取指定路径的 Json 文件
        try (FileReader reader = new FileReader(Constant.PRESET_FOLDER.resolve(name + ".json").toFile())) {
            // 创建泛型类型标识（解决类型擦除问题）
            LinkedHashMap<String, String> keyBindsMap = Constant.GSON.fromJson(reader, STRING_MAP_TYPE); // 反序列化 Json 文件
            for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
                // 获取按键名称
                String key = keyMapping.getName();
                // 条件：链表内存在该按键名称
                if (keyBindsMap.containsKey(key)) {
                    // 获取对应按键名称的按键键位
                    String value = keyBindsMap.get(key);
                    // 分割主键和修饰符
                    String[] parts = value.split(":");
                    // 获取主键
                    InputConstants.Key mainKey = InputConstants.getKey(parts[0]);
                    // 初始化修饰符为空
                    KeyModifier modifier = KeyModifier.NONE;
                    // 条件：存在修饰符
                    if (parts.length > 1) {
                        try {
                            // 获取修饰符
                            modifier = KeyModifier.valueOf(parts[1]);
                        } catch (IllegalArgumentException e) {
                            Constant.LOGGER.warn("未知修饰键: {}", parts[1]);
                        }
                    }
                    // 设置按键键位
                    keyMapping.setKeyModifierAndCode(modifier, mainKey);
                }
            }
            // 重置按键绑定映射表
            KeyMapping.resetMapping();
            // 保存游戏选项文件
            Minecraft.getInstance().options.save();
        } catch (Exception e) {
            Constant.LOGGER.error("加载预设文件失败: {}", e.getMessage());
        }
    }

    public static void openPresetFolder() {
        // 创建预设文件夹
        Config.createPresetFolder();
        // 获取配置文件夹路径
        Path configFolder = Constant.PRESET_FOLDER;
        // 获取系统名称
        String os = System.getProperty("os.name").toLowerCase();
        // 命令
        String command = null;
        // 条件：Windows系统
        if (os.contains("win")) {
            command = "explorer";
        }
        // 条件：macOS系统
        else if (os.contains("mac")) {
            command = "open";
        }
        // 条件：Linux系统
        else if (os.contains("linux") || os.contains("unix") || os.contains("nux") || os.contains("nix")) {
            command = "xdg-open";
        }
        // 条件：命令为空
        if (command == null) {
            Constant.LOGGER.warn("未知操作系统: {}，无法打开配置文件夹", os);
            return;
        }

        try {
            // 执行命令
            new ProcessBuilder(command, configFolder.toAbsolutePath().toString()).start();
        } catch (IOException e) {
            Constant.LOGGER.error("打开配置文件夹失败: {}", e.getMessage());
        }
    }

    public static String[] getPresets() {
        Config.createPresetFolder(); // 创建预设文件夹
        // 开启IO流，列表化指定目录的文件
        try (Stream<Path> stream = Files.list(Constant.PRESET_FOLDER)) {
            return stream
                    // 第一阶段过滤：确保是普通文件（排除目录/符号链接等非常规文件）
                    .filter(Files::isRegularFile)
                    // 第二阶段过滤：确保 Json 文件
                    .filter(p -> p.getFileName().toString().endsWith(".json"))
                    .filter(p -> {
                        try (FileReader reader = new FileReader(p.toFile())) {
                            LinkedHashMap<String, String> data = Constant.GSON.fromJson(reader, STRING_MAP_TYPE);
                            return data != null && !data.isEmpty();
                        } catch (Exception e) {
                            Constant.LOGGER.warn("非法预设文件：{}，原因：{}", p.getFileName(), e.toString());
                            return false;
                        }
                    })
                    // 文件名转换处理
                    .map(p -> {
                        // 获取文件名字符串
                        String fullName = p.getFileName().toString();
                        // 截取后缀名除外的所有字符
                        return fullName.substring(0, fullName.lastIndexOf(".json"));
                    })
                    // 将流转换为字符串数组
                    .toArray(String[]::new);
        } catch (IOException e) {
            Constant.LOGGER.error("获取预设文件失败: {}", e.getMessage());
            // 没有文件时返回空数组，避免空指针异常
            return new String[0];
        }
    }
}