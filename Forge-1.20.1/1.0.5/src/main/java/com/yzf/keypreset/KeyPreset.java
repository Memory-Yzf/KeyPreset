package com.yzf.keypreset;

import com.yzf.keypreset.client.event.ClientSetup;
import com.yzf.keypreset.util.Constant;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@SuppressWarnings("removal")
@Mod(Constant.MOD_ID)
public class KeyPreset {
    public KeyPreset() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientSetup::clientSetup); // 客户端初始化事件
        bus.addListener(ClientSetup::registerKeyMappings); // 注册按键事件
    }
}
