package com.yzf.keypreset.client.event;

import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import com.yzf.keypreset.util.PresetManager;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
    // 存储所有按键映射
    public static final KeyMapping[] PRESET_KEYS = new KeyMapping[5];

    // 初始化所有按键映射
    static {
        for (int i = 0; i < PRESET_KEYS.length; i++) {
            PRESET_KEYS[i] = new KeyMapping(
                    Constant.APPLY_PRESET_KEYS[i],
                    -1,
                    Constant.MOD_CATEGORIES.getString());
        }
    }

    // 客户端初始化事件
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(Config::createDefaultConfig); // 创建默认配置文件
        event.enqueueWork(PresetManager::createPresetFolder); // 创建预设文件夹
    }

    // 注册所有按键映射
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        for (KeyMapping key : PRESET_KEYS) {
            event.register(key);
        }
    }
}
