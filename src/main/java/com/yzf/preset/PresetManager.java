package com.yzf.preset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yzf.KeyPreset;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class PresetManager {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    //保存预设
    public static int savePreset(String name) {
        try {
            Preset preset = new Preset();
            Path presetFile = KeyPreset.FOLDER_PATH.resolve(name + ".json");
            FileWriter writer = new FileWriter(presetFile.toFile());
            writer.write(GSON.toJson(preset));
            writer.close();
        } catch (Exception e) {
            KeyPreset.LOGGER.error(String.valueOf(e));
            return 0;
        }
        return 1;
    }

    //加载预设
    public static int loadPreset(String name) {
        try {
            Path presetFile = KeyPreset.FOLDER_PATH.resolve(name + ".json");
            String content = Files.readString(presetFile);
            Preset preset = GSON.fromJson(content, Preset.class);
            for (var keybinding : MinecraftClient.getInstance().options.allKeys) {
                var presetBinding = preset.keybinds.get(keybinding.getTranslationKey());
                if (presetBinding != null) {
                    keybinding.setBoundKey(InputUtil.fromTranslationKey(presetBinding.key));
                }
            }
        } catch (Exception e) {
            KeyPreset.LOGGER.error(String.valueOf(e));
            return 0;
        }
        return 1;
    }

    //删除预设
    public static void deletePreset(String name) {
        try {
            Path presetFile = KeyPreset.FOLDER_PATH.resolve(name + ".json");
            Files.delete(presetFile);
        } catch (Exception e) {
            KeyPreset.LOGGER.error(String.valueOf(e));
        }
    }

    //获取预设
    public static String[] getPresets() {
        return KeyPreset.FOLDER_PATH.toFile().list((dir, name) -> name.endsWith(".json"));
    }
}
