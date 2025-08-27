package com.yzf.keypreset.client.event;

import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import com.yzf.keypreset.util.PresetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constant.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    // 客户端运行时事件
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        for (int i = 0; i < ClientSetup.PRESET_KEYS.length; i++) {
            if (ClientSetup.PRESET_KEYS[i].consumeClick()) {
                String fileName = Config.loadConfig().get("preset_binding_" + (i + 1)).getAsString();

                if (!fileName.isEmpty()) {
                    PresetManager.loadPreset(fileName);
                    if (mc.player != null) {
                        mc.player.sendSystemMessage(Component.translatable("keypreset.ingame_switch_tooltip", fileName));
                    }
                } else {
                    Constant.LOGGER.warn("此按键尚未绑定预设文件");
                }
            }
        }
    }
}
