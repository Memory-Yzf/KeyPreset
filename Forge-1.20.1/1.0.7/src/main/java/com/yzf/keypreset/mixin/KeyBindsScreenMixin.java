package com.yzf.keypreset.mixin;

import com.yzf.keypreset.client.gui.screen.PresetScreen;
import com.yzf.keypreset.util.Constant;
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
public abstract class KeyBindsScreenMixin extends Screen {
    protected KeyBindsScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addKeyPreset(CallbackInfo ci) {
        KeyBindsScreen screen = (KeyBindsScreen) (Object) this;
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        this.addRenderableWidget(Button.builder(Constant.PRESET_TITLE, button ->
                        Minecraft.getInstance().setScreen(new PresetScreen(screen))
                ).bounds(screenWidth / 2 - 50, screenHeight - 29, 100, 20).build()
        );
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
}