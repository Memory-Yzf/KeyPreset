package com.yzf.keypreset.util;

import com.google.gson.*;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("all")
public class BindingManager {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    // 保存预设文件名
    public static void savePresetName(int index, String name) {
        try {
            // 1. 读取现有配置文件，如果不存在则会生成默认配置
            JsonObject root = Config.loadConfig();

            // 2. 根据编号生成对应的键名
            String key = "preset_binding_" + index;

            // 3. 检查配置中是否已有该键，如果没有则创建一个新的对象
            JsonObject slot;
            if (root.has(key) && root.get(key).isJsonObject()) {
                slot = root.getAsJsonObject(key);
            } else {
                slot = new JsonObject();
                slot.addProperty("preset", "");
                root.add(key, slot);
            }

            // 4. 更新 preset 字段的值
            slot.addProperty("preset", name);

            // 5. 将更新后的 root 写回配置文件
            Files.createDirectories(Constant.CONFIG_FILE.getParent());
            try (Writer w = Files.newBufferedWriter(
                    Constant.CONFIG_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                GSON.toJson(root, w);
            }
        } catch (Exception e) {
            Constant.LOGGER.error("保存预设名失败: {}", e.getMessage());
        }
    }

    // 加载预设文件名
    public static String loadPresetName(int index) {
        try {
            // 1. 读取现有配置文件，如果不存在则会生成默认配置
            JsonObject root = Config.loadConfig();

            // 2. 根据编号生成对应的键名
            String key = "preset_binding_" + index;

            // 3. 检查配置中是否已有该键，如果没有则创建一个新的对象
            if (root.has(key) && root.get(key).isJsonObject()) {
                JsonObject slot = root.getAsJsonObject(key);

                // 4. 检查 slot 中是否存在 "preset" 字段
                if (slot.has("preset")) {
                    return slot.get("preset").getAsString();
                }
            }
        } catch (Exception e) {
            Constant.LOGGER.error("加载预设名失败: {}", e.getMessage());
        }

        // 5. 未找到或出错时返回空字符串
        return "";
    }

    // 删除预设绑定
    public static void removePresetBinding(int index) {
        try {
            // 1. 读取现有配置文件，如果不存在则会生成默认配置
            JsonObject root = Config.loadConfig();

            // 2. 根据编号生成对应的键名
            String key = "preset_binding_" + index;

            // 3. 检查配置中是否已有该键，如果没有则创建一个新的对象
            JsonObject slot;
            if (!root.has(key) || !root.get(key).isJsonObject()) {
                slot = new JsonObject();
                root.add(key, slot);
            } else {
                slot = root.getAsJsonObject(key);
            }

            // 4. 置空 preset 和 hotkey
            slot.addProperty("preset", "");

            // 5. 写回文件
            Files.createDirectories(Constant.CONFIG_FILE.getParent());
            try (Writer w = Files.newBufferedWriter(
                    Constant.CONFIG_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                GSON.toJson(root, w);
            }
        } catch (Exception e) {
            Constant.LOGGER.error("删除预设绑定失败: {}", e.getMessage());
        }
    }

    // 打开配置文件夹
    public static void openConfigFolder() {
        Path configFolder = FMLPaths.CONFIGDIR.get();
        String os = System.getProperty("os.name").toLowerCase(); // 获取系统名称
        ProcessBuilder pb = null;

        if (os.contains("windows")) {
            pb = new ProcessBuilder("explorer", configFolder.toAbsolutePath().toString());
        } else if (os.contains("mac")) {
            pb = new ProcessBuilder("open", configFolder.toAbsolutePath().toString());
        } else if (os.contains("linux")) {
            pb = new ProcessBuilder("xdg-open", configFolder.toAbsolutePath().toString());
        } else if (os.contains("unix")) {
            pb = new ProcessBuilder("xdg-open", configFolder.toAbsolutePath().toString());
        }

        try {
            if (pb != null) {
                pb.start();
            }
        } catch (IOException e) {
            Constant.LOGGER.error("打开配置文件夹失败: {}", e.getMessage());
        }
    }
}
