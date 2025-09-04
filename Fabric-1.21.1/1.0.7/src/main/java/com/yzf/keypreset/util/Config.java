package com.yzf.keypreset.util;

import com.google.gson.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Config {
    // ============= 创建默认配置文件 =============
    public static void createDefaultConfig() {
        try {
            if (Files.exists(Constant.CONFIG_FILE)) return;
            Files.createDirectories(Constant.CONFIG_FILE.getParent());
            JsonObject root = new JsonObject();
            for (int i = 1; i < 6; i++) {
                root.addProperty("preset_binding_" + i, "");
                root.addProperty("preset_binding_key_" + i, "");
            }

            try (Writer w = Files.newBufferedWriter(
                    Constant.CONFIG_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                Constant.GSON.toJson(root, w);
            }
        } catch (Exception e) {
            Constant.LOGGER.error("创建默认配置文件失败: {}", e.getMessage());
        }
    }

    // ============= 创建预设文件夹 =============
    public static void createPresetFolder() {
        try {
            if (Files.exists(Constant.PRESET_FOLDER)) return;
            Files.createDirectories(Constant.PRESET_FOLDER);
        } catch (Exception e) {
            Constant.LOGGER.error("创建预设文件夹失败: {}", e.getMessage());
        }
    }

    // ============= 读取配置文件 =============
    public static JsonObject loadConfig() {
        JsonObject root = new JsonObject();
        try {
            if (Files.exists(Constant.CONFIG_FILE)) {
                try (Reader reader = Files.newBufferedReader(Constant.CONFIG_FILE, StandardCharsets.UTF_8)) {
                    JsonElement element = JsonParser.parseReader(reader);
                    if (element != null && element.isJsonObject()) {
                        root = element.getAsJsonObject();
                    }
                }
            }

            boolean changed = false;

            for (int i = 1; i < 6; i++) {
                String name = "preset_binding_" + i;
                String key = "preset_binding_key_" + i;
                if (!root.has(name) || !root.get(name).isJsonPrimitive()) {
                    root.addProperty(name, "");
                    changed = true;
                }
                if (!root.has(key) || !root.get(key).isJsonPrimitive()) {
                    root.addProperty(key, "");
                    changed = true;
                }
            }

            if (changed) {
                try (Writer writer = Files.newBufferedWriter(
                        Constant.CONFIG_FILE,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE
                )) {
                    Constant.GSON.toJson(root, writer);
                }
            }
        } catch (Exception e) {
            Constant.LOGGER.error("读取配置文件失败: {}", e.getMessage());
        }
        return root;
    }

    // ============= 保存预设名 =============
    public static void savePresetName(int index, String value) {
        try {
            JsonObject root = loadConfig();
            root.addProperty("preset_binding_" + index, value);
            try (Writer w = Files.newBufferedWriter(
                    Constant.CONFIG_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                Constant.GSON.toJson(root, w);
            }
        } catch (Exception e) {
            Constant.LOGGER.error("保存预设名失败: {}", e.getMessage());
        }
    }

    // ============= 删除预设名 =============
    public static void deletePresetName(int index) {
        try {
            JsonObject root = loadConfig();
            root.addProperty("preset_binding_" + index, "");
            try (Writer w = Files.newBufferedWriter(
                    Constant.CONFIG_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                Constant.GSON.toJson(root, w);
            }
        } catch (Exception e) {
            Constant.LOGGER.error("删除预设名失败: {}", e.getMessage());
        }
    }

    // ============= 获取预设名 =============
    public static String getPresetName(int index) {
        try {
            if (Files.exists(Constant.CONFIG_FILE)) {
                try (Reader reader = Files.newBufferedReader(Constant.CONFIG_FILE, StandardCharsets.UTF_8)) {
                    JsonElement element = JsonParser.parseReader(reader);
                    if (element != null && element.isJsonObject()) {
                        JsonObject root = element.getAsJsonObject();
                        String key = "preset_binding_" + index;
                        if (root.has(key) && root.get(key).isJsonPrimitive()) {
                            return root.get(key).getAsString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Constant.LOGGER.error("读取预设名失败: {}", e.getMessage());
        }
        return "";
    }

    // ============= 保存键位 =============
    public static void saveKeyBinding(int index, String value) {
        try {
            JsonObject root = loadConfig();
            root.addProperty("preset_binding_key_" + index, value);
            try (Writer writer = Files.newBufferedWriter(
                    Constant.CONFIG_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                Constant.GSON.toJson(root, writer);
            }
        } catch (Exception e) {
            Constant.LOGGER.error("保存键位失败: {}", e.getMessage());
        }

    }

    // ============= 删除键位 =============
    public static void deleteKeyBinding(int index) {
        try {
            JsonObject root = loadConfig();
            root.addProperty("preset_binding_key_" + index, "");
            try (Writer writer = Files.newBufferedWriter(
                    Constant.CONFIG_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                Constant.GSON.toJson(root, writer);
            }
        } catch (Exception e) {
            Constant.LOGGER.error("删除键位失败: {}", e.getMessage());
        }
    }

    // ============= 获取键位 =============
    public static String getKeyBinding(int index) {
        try {
            JsonObject root = loadConfig();
            String key = "preset_binding_key_" + index;
            if (root.has(key) && root.get(key).isJsonPrimitive()) {
                return root.get(key).getAsString();
            }
        } catch (Exception e) {
            Constant.LOGGER.error("读取键位失败: {}", e.getMessage());
        }
        return "";
    }

    // ============= 获取键位显示名 =============
    public static Text getKeyBindingDisplayName(int index) {
        try {
            String raw = getKeyBinding(index);
            if (raw == null || raw.isEmpty()) {
                return Text.literal("");
            }

            String[] parts = raw.split(":");
            List<String> displayNames = new ArrayList<>();

            for (String part : parts) {
                InputUtil.Key key = InputUtil.fromTranslationKey(part);
                if (key != null) {
                    displayNames.add(key.getLocalizedText().getString());
                } else {
                    displayNames.add(part); // 兜底
                }
            }

            return Text.literal(String.join(" + ", displayNames));
        } catch (Exception e) {
            Constant.LOGGER.error("读取键位翻译名失败: {}", e.getMessage());
        }
        return Text.literal("");
    }
}