package com.yzf.keypreset.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.yzf.keypreset.client.gui.screen.KeyPresetScreen;
import com.yzf.keypreset.util.Constant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsScreen.class)
public abstract class KeyBindsScreenMixin {
    @Shadow
    private Button resetButton;

    @Inject(method = "addFooter", at = @At(value = "TAIL"))
    private void modifyResetButton(CallbackInfo ci) {
        resetButton.setWidth(100);
    }

    @Inject(method = "addFooter", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
    private void addKeyPresetButton(CallbackInfo ci, @Local LinearLayout layout) {
        layout.addChild(Button.builder(
                Constant.TITLE,
                button -> Minecraft.getInstance().setScreen(new KeyPresetScreen(Minecraft.getInstance().screen))).width(100).build()
        );
    }

    @Redirect(method = "addFooter", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/components/Button$Builder;build()Lnet/minecraft/client/gui/components/Button;"))
    private Button redirectDoneButtonBuild(Button.Builder button) {
        return button.width(100).build();
    }
}