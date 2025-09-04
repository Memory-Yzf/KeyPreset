package com.yzf.keypreset.util;

import java.io.IOException;
import java.nio.file.Path;

public class BindingManager {
    public static void openConfigFolder() {
        // 获取配置文件夹路径
        Path configFolder = Constant.CONFIG_FOLDER;
        // 获取系统名称
        String os = System.getProperty("os.name").toLowerCase();
        // 命令
        String command = null;
        // 条件：Windows系统
        if (os.contains("win")) {
            command = "explorer";
        }
        // 条件：macOS系统
        else if (os.contains("mac")) {
            command = "open";
        }
        // 条件：Linux系统
        else if (os.contains("linux") || os.contains("unix") || os.contains("nux") || os.contains("nix")) {
            command = "xdg-open";
        }

        // 条件：命令为空
        if (command == null) {
            Constant.LOGGER.warn("未知操作系统: {}，无法打开配置文件夹", os);
            return;
        }

        try {
            // 执行命令
            new ProcessBuilder(command, configFolder.toAbsolutePath().toString()).start();
        } catch (IOException e) {
            Constant.LOGGER.error("打开配置文件夹失败: {}", e.getMessage());
        }
    }
}