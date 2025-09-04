package com.yzf.keypreset.client.gui.widget;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class BindingButtonWidget extends Button {
    // 组合键列表
    private final List<InputConstants.Key> comboKey;
    // 等待输入状态
    private boolean isWaitingInput;
    // 鼠标按下状态
    private boolean isMousePressed;
    // 鼠标按下的按键
    private InputConstants.Key mousePressKey;
    // 单独鼠标状态
    private boolean isSoloMouse = false;
    // 绑定被修改时的回调事件
    private Consumer<BindingButtonWidget> onBindingChanged;


    public BindingButtonWidget() {
        super(0, 0, 0, 0, Component.empty(), b -> {
        }, DEFAULT_NARRATION);
        this.comboKey = new ArrayList<>(3);
        this.isWaitingInput = false;
    }

    @Override
    public void onPress() {
        isWaitingInput = true;
        isMousePressed = false;
        isSoloMouse = false;
        mousePressKey = InputConstants.UNKNOWN;
        comboKey.clear();
        this.setMessage(Component.literal("§n" + getMessage().getString()));
    }

    public boolean handleKeyPress(int keyCode, int scanCode) {
        if (!isWaitingInput) return false;
        // 条件：按键为ESC键
        if (keyCode == 256) {
            comboKey.clear();
            finishBinding();
            return true;
        }
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        // 条件：鼠标按下
        if (isMousePressed) isSoloMouse = false;
        // 条件：组合键不包含此按键 && 组合键小于3个
        if (!comboKey.contains(key) && comboKey.size() < 3) {
            comboKey.add(key);
            if (comboKey.size() >= 3) {
                finishBinding();
            }
        }
        return true;
    }

    public boolean handleKeyRelease() {
        if (!isWaitingInput) return false;
        finishBinding();
        return true;
    }

    public boolean handleMousePress(int button) {
        if (!isWaitingInput) return false;
        // 条件：未知按键
        if (button == -1) {
            comboKey.clear();
            finishBinding();
            return true;
        }
        InputConstants.Key mouseKey = InputConstants.Type.MOUSE.getOrCreate(button);
        // 条件：鼠标未按下
        if (!isMousePressed) {
            isMousePressed = true;
            mousePressKey = mouseKey;
            isSoloMouse = true;
        } else {
            isSoloMouse = false;
        }
        // 条件：组合键不包含此按键 && 组合键小于3个
        if (!comboKey.contains(mouseKey) && comboKey.size() < 3) {
            comboKey.add(mouseKey);
            if (comboKey.size() >= 3) {
                finishBinding();
            }
        }
        return true;
    }

    public boolean handleMouseRelease(int button) {
        if (!isWaitingInput) return false;
        InputConstants.Key released = InputConstants.Type.MOUSE.getOrCreate(button);
        // 条件：鼠标按下 && 松开的是按下的按键
        if (isMousePressed && released.equals(mousePressKey)) {
            isMousePressed = false;
            // 条件：单独鼠标按键 && 组合键为1 && 组合键第一个为松开的按键
            if (isSoloMouse && comboKey.size() == 1 && comboKey.getFirst().equals(released)) {
                finishBinding();
                return true;
            }
            finishBinding();
            return true;
        }
        return false;
    }

    private void finishBinding() {
        isWaitingInput = false;
        List<InputConstants.Key> modifiers = new ArrayList<>();
        List<InputConstants.Key> normals = new ArrayList<>();
        for (InputConstants.Key key : comboKey) {
            String name = key.getName().toLowerCase(Locale.ROOT);
            // 条件：按键类型为修饰符
            if (name.contains("control") || name.contains("shift") || name.contains("alt")) {
                modifiers.add(key);
            } else {
                normals.add(key);
            }
        }
        // 有序列表
        List<InputConstants.Key> ordered = new ArrayList<>();
        ordered.addAll(modifiers);
        ordered.addAll(normals);

        Component display = Component.empty();

        for (int i = 0; i < ordered.size(); i++) {
            InputConstants.Key key = ordered.get(i);
            // 条件：有序列表非空
            if (i > 0) display = display.copy().append(Component.literal(" + "));
            display = display.copy().append(key.getDisplayName());
        }

        setMessage(ordered.isEmpty() ? Component.literal("") : display);
        // 条件：绑定回调不为空
        if (onBindingChanged != null) onBindingChanged.accept(this);
    }

    public String getConfigValue() {
        List<InputConstants.Key> modifiers = new ArrayList<>();
        List<InputConstants.Key> normals = new ArrayList<>();

        for (InputConstants.Key key : comboKey) {
            String name = key.getName().toLowerCase(Locale.ROOT);
            // 条件：按键类型为修饰符
            if (name.contains("control") || name.contains("shift") || name.contains("alt")) {
                modifiers.add(key);
            } else {
                normals.add(key);
            }
        }

        List<InputConstants.Key> ordered = new ArrayList<>();
        ordered.addAll(modifiers);
        ordered.addAll(normals);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ordered.size(); i++) {
            if (i > 0) sb.append(":");
            sb.append(ordered.get(i).getName());
        }
        return sb.toString();
    }

    public void setOnBindingChanged(Consumer<BindingButtonWidget> onBindingChanged) {
        this.onBindingChanged = onBindingChanged;
    }
}