package com.yzf.keypreset.util;

import com.google.gson.*;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Config {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    // 创建默认配置文件
    public static void createDefaultConfig() {
        try {
            // 存在则不覆盖
            if (Files.exists(Constant.CONFIG_FILE)) return;

            Files.createDirectories(Constant.CONFIG_FILE.getParent());

            JsonObject root = new JsonObject();
            for (int i = 1; i < 6; i++) {
                root.addProperty("preset_binding_" + i, "");
            }

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
            Constant.LOGGER.error("创建默认配置文件失败: {}", e.getMessage());
        }
    }

    public static JsonObject loadConfig() {
        JsonObject root = new JsonObject();
        try {
            if (Files.exists(Constant.CONFIG_FILE)) {
                try (Reader reader = Files.newBufferedReader(Constant.CONFIG_FILE, StandardCharsets.UTF_8)) {
                    JsonElement el = JsonParser.parseReader(reader);
                    if (el != null && el.isJsonObject()) {
                        root = el.getAsJsonObject();
                    }
                }
            }

            boolean changed = false;

            // 检查每个 slot 是否存在，如果不存在就补齐默认值
            for (int i = 1; i < 6; i++) {
                String key = "preset_binding_" + i;
                if (!root.has(key) || !root.get(key).isJsonPrimitive()) {
                    root.addProperty(key, "");
                    changed = true;
                }
            }

            // 如果有补齐，则写回文件
            if (changed) {
                try (Writer w = Files.newBufferedWriter(Constant.CONFIG_FILE, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE)) {
                    GSON.toJson(root, w);
                }
            }
        } catch (Exception e) {
            Constant.LOGGER.error("加载配置文件失败: {}", e.getMessage());
        }
        return root;
    }

    public static void savePresetName(int index, String value) {
        try {
            // 加载配置
            JsonObject root = loadConfig();

            // 更新指定键的值
            root.addProperty("preset_binding_" + index, value);

            // 写回文件
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
            Constant.LOGGER.error("保存预设失败: {}", e.getMessage());
        }
    }
}
