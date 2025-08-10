package com.yzf.mixin;

import com.yzf.KeyPreset;
import com.yzf.screen.KeyPresetScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsScreen.class)
public class KeyBindsScreenMixin extends Screen {

    protected KeyBindsScreenMixin(Component title) {
        super(title);
    }

    // 修改重置按钮位置和宽度
    @ModifyArg(method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;",
            ordinal = 0
    ), index = 0)
    private int modifyResetX(int x) {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 155;
    }

    @ModifyArg(method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;",
            ordinal = 0
    ), index = 2)
    private int modifyResetWidth(int width) {
        return 100;
    }

    // 修改完成按钮位置和宽度
    @ModifyArg(method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;",
            ordinal = 1
    ), index = 0)
    private int modifyDoneX(int x) {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 55;
    }

    @ModifyArg(method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;",
            ordinal = 1
    ), index = 2)
    private int modifyDoneWidth(int width) {
        return 100;
    }

    // 添加中间的“键位预设”按钮
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        KeyBindsScreen screen = (KeyBindsScreen) (Object) this;

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        this.addRenderableWidget(
                Button.builder(KeyPreset.TITLE, button ->
                        Minecraft.getInstance().setScreen(new KeyPresetScreen(screen))
                ).bounds(screenWidth / 2 - 50, screenHeight - 29, 100, 20).build()
        );
    }
}