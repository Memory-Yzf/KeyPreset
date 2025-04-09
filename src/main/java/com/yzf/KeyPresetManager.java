package com.yzf;

import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.include.com.google.gson.Gson;
import org.spongepowered.include.com.google.gson.GsonBuilder;
import org.spongepowered.include.com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

public class KeyPresetManager {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create(); // 设置漂亮格式化

    // 保存预设
    public static void savePreset(String name) {
        createPresetFolder(); // 创建预设文件夹
        LinkedHashMap<String, String> keyBindsMap = new LinkedHashMap<>(); // 按键绑定链表
        // 遍历按键绑定映射表
        for (var keyMappings : Minecraft.getInstance().options.keyMappings) {
            String key = keyMappings.getName(); // 获取按键名称
            String value = keyMappings.getKey().toString(); // 获取按键键位
            keyBindsMap.put(key, value); // 将名称和键位添加进链表
        }
        // 开启IO流，处理目标文件
        try (FileWriter writer = new FileWriter(KeyPreset.PRESET_FOLDER.resolve(name + ".json").toString())) {
            gson.toJson(keyBindsMap, writer); // 序列化链表内容后写入目标文件
        } catch (IOException e) {
            KeyPreset.LOGGER.error(e.getMessage());
        }
    }

    // 删除预设
    public static void deletePreset(String name) {
        createPresetFolder(); // 创建预设文件夹
        try {
            Files.delete(KeyPreset.PRESET_FOLDER.resolve(name + ".json"));
        } catch (IOException e) {
            KeyPreset.LOGGER.error(e.getMessage());
        }
    }

    // 加载预设
    public static void loadPreset(String name) {
        createPresetFolder(); // 创建预设文件夹
        // 开启IO流，读取指定路径的 Json 文件
        try (FileReader reader = new FileReader(KeyPreset.PRESET_FOLDER.resolve(name + ".json").toFile())) {
            Type linkedHashMapType = new TypeToken<LinkedHashMap<String, String>>() {}.getType(); // 创建泛型类型标识（解决类型擦除问题）
            LinkedHashMap<String, String> keyBindsMap = gson.fromJson(new JsonReader(reader), linkedHashMapType); // 反序列化 Json 文件
            for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
                String key = keyMapping.getName(); // 获取按键名称
                // 判断链表内是否存在该按键名称
                if (keyBindsMap.containsKey(key)) {
                    String value = keyBindsMap.get(key); // 获取对应按键名称的键位
                    InputConstants.Key newKey = InputConstants.getKey(value); // 将键位转化为对应的按键代码
                    keyMapping.setKey(newKey); // 将旧键位改为新键位
                }
            }
            Minecraft.getInstance().options.save(); // 保存游戏选项文件
        } catch (Exception e) {
            KeyPreset.LOGGER.error(e.getMessage());
        }
    }

    // 打开预设文件夹
    public static void openPresetFolder() {
        createPresetFolder(); // 创建预设文件夹
        String os = System.getProperty("os.name").toLowerCase(); // 获取系统名称
        ProcessBuilder pb = null;
        if (os.contains("windows")) {
            pb = new ProcessBuilder("explorer", KeyPreset.PRESET_FOLDER.toAbsolutePath().toString());
        } else if (os.contains("mac")) {
            pb = new ProcessBuilder("open", KeyPreset.PRESET_FOLDER.toAbsolutePath().toString());
        } else if (os.contains("linux")) {
            pb = new ProcessBuilder("xdg-open", KeyPreset.PRESET_FOLDER.toAbsolutePath().toString());
        } else if (os.contains("unix")) {
            pb = new ProcessBuilder("xdg-open", KeyPreset.PRESET_FOLDER.toAbsolutePath().toString());
        }
        try {
            if (pb != null) {
                pb.start();
            }
        } catch (IOException e) {
            KeyPreset.LOGGER.error(e.getMessage());
        }
    }

    // 获取预设文件
    public static String[] getPresets() {
        createPresetFolder(); // 创建预设文件夹
        // 开启IO流，列表化指定目录的文件
        try (Stream<Path> stream = Files.list(KeyPreset.PRESET_FOLDER)) {
            return stream
                    // 第一阶段过滤：确保是普通文件（排除目录/符号链接等非常规文件）
                    .filter(Files::isRegularFile)
                    // 第二阶段过滤：确保是 Json 文件
                    .filter(p -> {
                        String fileName = p.getFileName().toString(); // 获取文件名字符串
                        return fileName.endsWith(".json"); // 确认文件名后缀
                    })
                    // 文件名转换处理
                    .map(p -> {
                        String fullName = p.getFileName().toString(); // 获取文件名字符串
                        return fullName.substring(0, fullName.lastIndexOf(".json")); // 截取后缀名除外的所有字符
                    })
                    // 将流转换为字符串数组
                    .toArray(String[]::new);
        } catch (IOException e) {
            KeyPreset.LOGGER.error(e.getMessage());
            return new String[0]; // 没有文件时返回空数组，避免空指针异常
        }
    }

    // 创建预设文件夹
    public static void createPresetFolder() {
        try {
            Files.createDirectories(KeyPreset.PRESET_FOLDER); // 创建预设文件夹
        } catch (IOException e) {
            KeyPreset.LOGGER.error(e.getMessage());
        }
    }
}