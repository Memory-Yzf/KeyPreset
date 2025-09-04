package com.yzf.keypreset.client.event;

import com.yzf.keypreset.util.Config;
import com.yzf.keypreset.util.Constant;
import com.yzf.keypreset.util.PresetManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class KeyHandlerEvent {
    private static final MinecraftClient MC = MinecraftClient.getInstance();
    private static final Map<Integer, Boolean> triggered = new HashMap<>();

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> onClientTick());
    }

    public static void onClientTick() {
        if (MC.player == null || MC.currentScreen != null) return;
        long window = MC.getWindow().getHandle();

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
                InputUtil.Key key = InputUtil.fromTranslationKey(keyName);
                // 条件：按键或鼠标未按下
                if (key == null || !isKeyOrMousePressed(window, key)) {
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
                        MC.player.sendMessage(Constant.SWITCH_TOOLTIP.copy().append(name), false);
                    }
                }
            } else {
                triggered.put(i, false);
            }
        }
    }

    private static boolean isKeyOrMousePressed(long window, InputUtil.Key key) {
        // 条件：按键类型为键盘按钮
        if (key.getCategory() == InputUtil.Type.KEYSYM) {
            return GLFW.glfwGetKey(window, key.getCode()) == GLFW.GLFW_PRESS;
        }
        // 条件：按键类型为鼠标按钮
        else if (key.getCategory() == InputUtil.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(window, key.getCode()) == GLFW.GLFW_PRESS;
        }
        return false;
    }
}