package com.yzf.keypreset;

import com.yzf.keypreset.util.Constant;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constant.MOD_ID)
public class KeyPreset {
    public KeyPreset(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(KeyPresetClient::clientSetup); // 客户端初始化事件
    }
}