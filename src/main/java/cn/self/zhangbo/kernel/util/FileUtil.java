package cn.self.zhangbo.kernel.util;

import java.io.File;
import java.util.Set;

public class FileUtil {

    /**
     * 读取路径下所有class文件
     * 
     * @param inputFile File路径
     * @param classes class集合
     */
    public static void getAllFiles(File inputFile, Set<String> classes) {
        File[] files = inputFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getAllFiles(file, classes);
            } else {
                if (file.getName().contains(".class")) {
                    String fileName = file.getPath();
                    classes.add(fileName.substring(fileName.indexOf("classes/") + 8).replaceAll("/", ".")
                        .replaceAll(".class", ""));
                }
            }
        }
    }
}
