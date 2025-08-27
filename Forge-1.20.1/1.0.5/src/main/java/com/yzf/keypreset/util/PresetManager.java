package com.yzf.keypreset.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
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
    static Gson gson = new GsonBuilder().setPrettyPrinting().create(); // 设置漂亮格式化
    private static final Type STRING_MAP_TYPE = TypeToken.getParameterized(LinkedHashMap.class, String.class, String.class).getType(); // 创建泛型类型标识（解决类型擦除问题）

    // 保存预设
    public static void savePreset(String name) {
        createPresetFolder(); // 创建预设文件夹
        LinkedHashMap<String, String> keyBindsMap = new LinkedHashMap<>(); // 按键绑定链表
        // 遍历按键绑定映射表
        for (var keyMappings : Minecraft.getInstance().options.keyMappings) {
            String key = keyMappings.getName(); // 获取按键名称
            KeyModifier modifier = keyMappings.getKeyModifier(); // 获取修饰符（ALT / SHIFT / CONTROL）
            String value = keyMappings.getKey().toString(); // 获取按键键位
            if (modifier != KeyModifier.NONE) {
                value = value + ":" + modifier.name(); // 若有修饰符，则在键位后添加 ":" + 修饰符名称
            }
            keyBindsMap.put(key, value); // 将名称和键位添加进链表
        }
        // 开启IO流，处理目标文件
        try (FileWriter writer = new FileWriter(Constant.PRESET_FOLDER.resolve(name + ".json").toString())) {
            gson.toJson(keyBindsMap, writer); // 序列化链表内容后写入目标文件
        } catch (IOException e) {
            Constant.LOGGER.error("保存预设文件失败: {}", e.getMessage());
        }
    }

    // 删除预设
    public static void deletePreset(String name) {
        createPresetFolder(); // 创建预设文件夹
        try {
            Files.delete(Constant.PRESET_FOLDER.resolve(name + ".json"));
        } catch (IOException e) {
            Constant.LOGGER.error("删除预设文件失败: {}", e.getMessage());
        }
    }

    // 加载预设
    public static void loadPreset(String name) {
        createPresetFolder(); // 创建预设文件夹
        // 开启IO流，读取指定路径的 Json 文件
        try (FileReader reader = new FileReader(Constant.PRESET_FOLDER.resolve(name + ".json").toFile())) {
            // 创建泛型类型标识（解决类型擦除问题）
            LinkedHashMap<String, String> keyBindsMap = gson.fromJson(new JsonReader(reader), STRING_MAP_TYPE); // 反序列化 Json 文件
            for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
                String key = keyMapping.getName(); // 获取按键名称
                // 判断链表内是否存在该按键名称
                if (keyBindsMap.containsKey(key)) {
                    String value = keyBindsMap.get(key); // 获取对应按键名称的键位
                    String[] parts = value.split(":"); // 分割主键和修饰符
                    InputConstants.Key mainKey = InputConstants.getKey(parts[0]); // 获取主键
                    KeyModifier modifier = KeyModifier.NONE; // 初始化修饰符为 NONE
                    if (parts.length > 1) {
                        try {
                            modifier = KeyModifier.valueOf(parts[1]); // 获取修饰符
                        } catch (IllegalArgumentException e) {
                            Constant.LOGGER.warn("未知修饰键: {}", parts[1]);
                        }
                    }
                    keyMapping.setKeyModifierAndCode(modifier, mainKey); // 设置按键的键位
                }
            }
            KeyMapping.resetMapping(); // 重置按键绑定映射表
            Minecraft.getInstance().options.save(); // 保存游戏选项文件
        } catch (Exception e) {
            Constant.LOGGER.error("加载预设文件失败: {}", e.getMessage());
        }
    }

    // 打开预设文件夹
    public static void openPresetFolder() {
        createPresetFolder(); // 创建预设文件夹
        String os = System.getProperty("os.name").toLowerCase(); // 获取系统名称
        ProcessBuilder pb = null;
        if (os.contains("windows")) {
            pb = new ProcessBuilder("explorer", Constant.PRESET_FOLDER.toAbsolutePath().toString());
        } else if (os.contains("mac")) {
            pb = new ProcessBuilder("open", Constant.PRESET_FOLDER.toAbsolutePath().toString());
        } else if (os.contains("linux")) {
            pb = new ProcessBuilder("xdg-open", Constant.PRESET_FOLDER.toAbsolutePath().toString());
        } else if (os.contains("unix")) {
            pb = new ProcessBuilder("xdg-open", Constant.PRESET_FOLDER.toAbsolutePath().toString());
        }
        try {
            if (pb != null) {
                pb.start();
            }
        } catch (IOException e) {
            Constant.LOGGER.error("打开预设文件夹失败: {}", e.getMessage());
        }
    }

    // 获取预设文件
    public static String[] getPresets() {
        createPresetFolder(); // 创建预设文件夹
        // 开启IO流，列表化指定目录的文件
        try (Stream<Path> stream = Files.list(Constant.PRESET_FOLDER)) {
            return stream
                    // 第一阶段过滤：确保是普通文件（排除目录/符号链接等非常规文件）
                    .filter(Files::isRegularFile)
                    // 第二阶段过滤：确保是 Json 文件
                    .filter(p -> p.getFileName().toString().endsWith(".json")) // 确认文件名后缀
                    .filter(p -> {
                        try (FileReader reader = new FileReader(p.toFile())) {
                            LinkedHashMap<String, String> data = gson.fromJson(new JsonReader(reader), STRING_MAP_TYPE);
                            return data != null && !data.isEmpty();
                        } catch (Exception e) {
                            Constant.LOGGER.warn("非法预设文件：{}，原因：{}", p.getFileName(), e.toString());
                            return false;
                        }
                    })
                    // 文件名转换处理
                    .map(p -> {
                        String fullName = p.getFileName().toString(); // 获取文件名字符串
                        return fullName.substring(0, fullName.lastIndexOf(".json")); // 截取后缀名除外的所有字符
                    })
                    // 将流转换为字符串数组
                    .toArray(String[]::new);
        } catch (IOException e) {
            Constant.LOGGER.error("获取预设文件失败: {}", e.getMessage());
            return new String[0]; // 没有文件时返回空数组，避免空指针异常
        }
    }

    // 创建预设文件夹
    public static void createPresetFolder() {
        try {
            Files.createDirectories(Constant.PRESET_FOLDER); // 创建预设文件夹
        } catch (IOException e) {
            Constant.LOGGER.error("创建预设文件夹失败: {}", e.getMessage());
        }
    }
}