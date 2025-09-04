package com.yzf.keypreset.util;

import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

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

    public static void savePreset(String name) {
        Config.createPresetFolder(); // 创建预设文件夹
        LinkedHashMap<String, String> keyBindsMap = new LinkedHashMap<>(); // 按键绑定链表
        // 遍历按键绑定映射表
        for (var keyMappings : MinecraftClient.getInstance().options.allKeys) {
            String key = keyMappings.getTranslationKey(); // 获取按键名称
            String value = keyMappings.getBoundKeyTranslationKey(); // 获取按键键位
            keyBindsMap.put(key, value); // 将名称和键位添加进链表
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

    public static void loadPreset(String name) {
        Config.createPresetFolder(); // 创建预设文件夹
        // 开启IO流，读取指定路径的 Json 文件
        try (FileReader reader = new FileReader(Constant.PRESET_FOLDER.resolve(name + ".json").toFile())) {
            // 创建泛型类型标识（解决类型擦除问题）
            LinkedHashMap<String, String> keyBindsMap = Constant.GSON.fromJson(reader, STRING_MAP_TYPE); // 反序列化 Json 文件
            for (var keyMapping : MinecraftClient.getInstance().options.allKeys) {
                String key = keyMapping.getTranslationKey(); // 获取按键名称
                // 判断链表内是否存在该按键名称
                if (keyBindsMap.containsKey(key)) {
                    String value = keyBindsMap.get(key); // 获取对应按键名称的键位
                    InputUtil.Key newKey = InputUtil.fromTranslationKey(value); // 将键位转化为对应的按键代码
                    keyMapping.setBoundKey(newKey); // 设置按键键位
                }
            }
            KeyBinding.updateKeysByCode(); // 刷新键位映射
            MinecraftClient.getInstance().options.write(); // 保存游戏选项文件
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
                    // 第二阶段过滤：确保是 Json 文件
                    .filter(p -> p.getFileName().toString().endsWith(".json")) // 确认文件名后缀
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
}