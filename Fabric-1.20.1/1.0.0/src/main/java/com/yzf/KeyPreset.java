package com.yzf;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class KeyPreset implements ClientModInitializer {
	public static final String MOD_ID = "KeyPreset";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Path FOLDER_PATH = FabricLoader.getInstance().getConfigDir().resolve("KeyPreset");

	public void onInitializeClient() {
		if (!FOLDER_PATH.toFile().exists()) {
			try {
				FOLDER_PATH.toFile().mkdir();
			} catch (Exception e) {
				LOGGER.error(String.valueOf(e));
			}
		}
	}

	//获取X轴中心
	public static int centerX() {
		return MinecraftClient.getInstance().getWindow().getScaledWidth() / 2;
	}

	//获取Y轴中心
	public static int centerY() {
		return MinecraftClient.getInstance().getWindow().getScaledHeight() / 2;
	}

	//获取窗口宽度
	public static int screenWidth() {
		return MinecraftClient.getInstance().getWindow().getScaledWidth();
	}

	//获取窗口高度
	public static int screenHeight() {
		return MinecraftClient.getInstance().getWindow().getScaledHeight();
	}
}