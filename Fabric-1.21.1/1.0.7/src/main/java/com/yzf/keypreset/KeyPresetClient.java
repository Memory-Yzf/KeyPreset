package com.yzf.keypreset;

import com.yzf.keypreset.client.event.KeyHandlerEvent;
import com.yzf.keypreset.util.Config;
import net.fabricmc.api.ClientModInitializer;

public class KeyPresetClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 创建默认配置文件
        Config.createDefaultConfig();
        // 创建预设文件夹
        Config.createPresetFolder();
        // 注册按键事件
        KeyHandlerEvent.register();
    }
}