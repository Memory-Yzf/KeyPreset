package com.yzf.keypreset;

import com.yzf.keypreset.util.Config;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class KeyPresetClient {
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(Config::createDefaultConfig); // 创建默认配置文件
        event.enqueueWork(Config::createPresetFolder); // 创建预设文件夹
    }
}