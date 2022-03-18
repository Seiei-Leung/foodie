package top.seiei.controller;

import java.io.File;

public class BaseController {

    public static final int PAGE_SIZE = 10;

    // 用户头像存储路径，File.separator 表示系统文件路径的分隔符
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "workspaces" +
                                                            File.separator + "images" +
                                                            File.separator + "foodie" +
                                                            File.separator + "faces";
}
