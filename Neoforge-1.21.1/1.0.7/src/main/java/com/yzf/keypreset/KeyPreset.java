package com.yzf.keypreset;

import com.yzf.keypreset.util.Constant;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constant.MOD_ID)
public class KeyPreset {
    public KeyPreset(IEventBus bus) {
        bus.addListener(KeyPresetClient::clientSetup); // 客户端初始化事件
    }
}