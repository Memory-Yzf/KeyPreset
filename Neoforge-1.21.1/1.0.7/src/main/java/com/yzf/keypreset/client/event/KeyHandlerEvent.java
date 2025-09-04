package com.yzf.keypreset.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import com.yzf.keypreset.util.PresetManager;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = Constant.MOD_ID, value = Dist.CLIENT)
public class KeyHandlerEvent {
    // 游戏实例
    private static final Minecraft MC = Minecraft.getInstance();
    // 触发状态
    private static final Map<Integer, Boolean> triggered = new HashMap<>();

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        // 条件：玩家为空 && 已打开界面
        if (MC.player == null || MC.screen != null) return;
        // 获取游戏窗口
        long window = MC.getWindow().getWindow();
        for (int i = 1; i < 6; i++) {
            // 获取按键绑定
            String keyBind = Config.getKeyBinding(i);
            // 条件：按键绑定为null && 按键绑定为空
            if (keyBind == null || keyBind.isEmpty()) continue;
            // 分割按键绑定
            String[] keys = keyBind.split(":");
            // 所有按键按下
            boolean allPressed = true;
            for (String keyName : keys) {
                InputConstants.Key key = InputConstants.getKey(keyName);
                // 条件：按键或鼠标未按下
                if (!isKeyOrMousePressed(window, key)) {
                    allPressed = false;
                    break;
                }
            }

            // 获取触发状态
            boolean wasTriggered = triggered.getOrDefault(i, false);

            // 条件：所有按键都被按下
            if (allPressed) {
                // 条件：未触发
                if (!wasTriggered) {
                    triggered.put(i, true);
                    String name = Config.getPresetName(i);
                    PresetManager.loadPreset(name);
                    // 条件：玩家不为空
                    if (MC.player != null) {
                        // 向玩家发送系统消息
                        MC.player.sendSystemMessage(Constant.SWITCH_TOOLTIP.copy().append(name));
                    }
                }
            } else {
                triggered.put(i, false);
            }
        }
    }

    private static boolean isKeyOrMousePressed(long window, InputConstants.Key key) {
        // 条件：按键类型为键盘按钮
        if (key.getType() == InputConstants.Type.KEYSYM) {
            return InputConstants.isKeyDown(window, key.getValue());
        }
        // 条件：按键类型为鼠标按钮
        else if (key.getType() == InputConstants.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(window, key.getValue()) == GLFW.GLFW_PRESS;
        }
        return false;
    }
}