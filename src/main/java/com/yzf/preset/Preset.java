package com.yzf.preset;

import net.minecraft.client.MinecraftClient;

import java.util.HashMap;

public class Preset {
    public HashMap<String, KeyBind> keybinds = new HashMap<>();

    public Preset() {
        /*
         * 遍历所有按键
         * key.getTranslationKey() 返回按键的翻译键
         * key.getBoundKeyTranslationKey() 返回绑定按键的翻译键的值
         * 将得到的翻译键和对应的 KeyBind 对象存入 keybinds 哈希表中
         */
        for (var key : MinecraftClient.getInstance().options.allKeys) {
            keybinds.put(key.getTranslationKey(), new KeyBind(key.getBoundKeyTranslationKey()));
        }
    }

    //用于将来扩展预设文件格式的对象
    public static class KeyBind {
        public String key;

        public KeyBind(String key) {
            this.key = key;
        }
    }
}
