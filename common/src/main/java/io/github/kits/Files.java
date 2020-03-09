package io.github.kits;

import io.github.kits.log.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件操作工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Files {

    private static final Class<?> thisClass = Files.class;

    /**
     * 判断文件是否为空
     *
     * @param file  文件
     * @return      是否为空
     */
    public static boolean isNullOrEmpty(File file) {
        return null == file || !file.exists() || file.length() <= 0;
    }

    /**
     * 判断文件是否不为空
     *
     * @param file      文件对象
     * @return          是否为空
     */
    public static boolean isNotNullOrEmpty(File file) {
        return !isNullOrEmpty(file);
    }

    /**
     * 读取加载文件到字节数组
     *
     * @param file  文件
     * @return      读取的字节数组
     */
    public static byte[] loadFile(File file) {
        if (Objects.isNull(file) || !file.exists()) {
            return null;
        }
        RandomAccessFile accessFile = null;
        FileChannel      channel    = null;
        try {
            accessFile = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[(int) accessFile.length()];
            channel = accessFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int        readLength;
            int        offset = 0;
            while ((readLength = channel.read(buffer)) != -1) {
                buffer.flip();
                System.arraycopy(buffer.array(), 0, bytes, offset, readLength);
                buffer.clear();
                offset += readLength;
            }
            return bytes;
        } catch (Exception e) {
            Logger.error("Load file error", e);
            return null;
        } finally {
            if (Objects.nonNull(channel)) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.nonNull(accessFile)) {
                try {
                    accessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载资源文件
     *
     * @param fileName 文件名称
     * @return 字节数组
     */
    public static byte[] loadResourceFile(String fileName) {
        String resourcePath = fileName;
        try {
            resourcePath = getResourcePath(fileName);
            if (Strings.isNotNullOrEmpty(resourcePath)) {
                resourcePath = URLDecoder.decode(resourcePath,"UTF-8");
                String jarFlag = "!" + File.separator;
                if (resourcePath.contains(jarFlag)) {
                    resourcePath = resourcePath.substring(resourcePath.indexOf(jarFlag) + 2);
                    InputStream inputStream = Files.class.getClassLoader().getResourceAsStream(resourcePath);
                    return Envs.readAll(inputStream);
                } else {
                    return loadFile(new File(resourcePath));
                }
            }
        } catch (IOException e) {
            Logger.errorf("Load resource {} failed", e, resourcePath);
        }
        return null;
    }

    /**
     * 从文件或文件相对路径获取绝对路径
     *
     * @param fileOrPath  文件或文件的相对路径
     * @return      绝对路径
     */
    public static String getResourcePath(Object fileOrPath) {
        String resourcePath = null;
        try {
            if (Objects.nonNull(fileOrPath)) {
                if (fileOrPath instanceof File) {
                    resourcePath = ((File) fileOrPath).getAbsolutePath();
                } else if (fileOrPath instanceof String) {
                    String decode = URLDecoder.decode((String) fileOrPath, "UTF-8");
                    URL url = thisClass.getClassLoader().getResource(decode);
                    if (Objects.nonNull(url)) {
                        resourcePath = url.getFile();
                    }
                }
            }
        } catch (UnsupportedEncodingException ignored) {
        }
        return resourcePath;
    }

    /**
     * 从文件或文件相对路径获取绝对路径
     *
     * @param filePath  文件或文件的相对路径
     * @return      绝对路径
     */
    public static File getResourceFile(Object filePath) {
        String resourcePath = getResourcePath(filePath);
        if (Strings.isNullOrEmpty(resourcePath)) {
            return null;
        }
        return new File(resourcePath);
    }

    /**
     * 判断文件是否存在
     *
     * @param fullPath  文件完整路径
     * @return          文件不存在(true)
     */
    public static boolean isNotExists(String fullPath){
        return !isExists(fullPath);
    }

    /**
     * 判断文件是否存在
     *
     * @param fullPath  文件完整路径
     * @return          文件存在(true)
     */
    public static boolean isExists(String fullPath){
        if(fullPath.contains("!")){
            fullPath = fullPath.substring(0, fullPath.indexOf("!"));
        }
        return new File(fullPath).exists();
    }

    /**
     * 获得应用的工作根目录路径
     *
     * @return          工作空间的根据路径
     */
    public static String getContentPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取ClassPath
     * @return classPath
     */
    public static String getClassPath() {
        return (new File(Files.class.getResource("/").getPath())).getPath();
    }

    /**
     * 获取系统换行符
     *
     * @return          系统换行符
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    /**
     * 获取文件名称
     *
     * @param path      文件路径
     * @return          文件名称
     */
    public static String getFileName(String path) {
        return path.substring(path.contains(File.separator) ? path.lastIndexOf(File.separator) + 1 : 0, path.contains(".") ? path.lastIndexOf(".") : path.length());
    }

    /**
     * 获取文件的扩展名
     *
     * @param filePath  文件的路径或者文件名
     * @return          文件的扩展名
     */
    public static String getFileExtension(String filePath){
        try {
            if (filePath.lastIndexOf(".") > 0) {
                return filePath.substring(filePath.lastIndexOf(".") + 1);
            } else {
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * 获取类JVM 的 Class Path
     * 因部分 ide 会自动增加全部的 jvm 的 classpath, 所以这里会自动剔除 classpath 中 jvm 的 classPath
     *
     * @return 获得用户的类加载路径
     */
    public static List<String> getClassPaths() {
        ArrayList<String> userClassPath = new ArrayList<>();
        String javaHome = System.getProperty("java.home");

        //去除 javahome 的最后一个路径节点,扩大搜索范文
        javaHome = javaHome.replaceAll("/[a-zA-z0-9_$]*$", "");

        String[] classPaths = System.getProperty("java.class.path").split(File.pathSeparator);
        for (String classPath : classPaths) {
            if (!classPath.startsWith(javaHome)) {
                userClassPath.add(classPath);
            }
        }
        return userClassPath;
    }

    /**
     * 根据根目录获取目录下的文件
     *
     * @param rootFile  根目录
     * @return          目录下的所有文件
     */
    private static List<File> getFiles(File rootFile) {
        Set<File> files = new HashSet<>();
        if (Objects.isNull(rootFile) || !rootFile.exists()) {
            return Collections.emptyList();
        }
        if (rootFile.isDirectory()) {
            File[] listFiles = rootFile.listFiles();
            if (Objects.isNull(listFiles) || listFiles.length == 0) {
                files.add(rootFile);
            } else {
                for (File file : listFiles) {
                    files.addAll(getFiles(file));
                }
            }
        } else {
            files.add(rootFile);
        }
        return new ArrayList<>(files);
    }

    /**
     * 遍历指定文件对象
     *
     * @param rootFile  特定的文件或文件目录
     * @return          目录中的所有文件
     */
    public static List<File> scanFile(File rootFile) {
        return getFiles(rootFile).stream()
                .filter(file -> !Strings.regexMatch(file.getAbsolutePath(), ".*\\.jar$"))
                .collect(Collectors.toList());
    }

    /**
     * 获取框架jar
     *
     * @return          jar列表
     */
    public static List<String> findAgentJar(String... excludeAgentJar) {
        List<String> files = getClassPaths();
        return files.stream()
                .filter(jarName -> isFrameJar(new File(jarName)))
                .filter(jarName -> {
                    if (Lists.isNotNullOrEmpty(Arrays.asList(excludeAgentJar))) {
                        return Stream.of(excludeAgentJar)
                                .parallel()
                                .map(jar -> jar.replace(".", "\\."))
                                .anyMatch(jar -> Strings.regexFind(jar, jarName));
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 判断是否是框架jar
     *
     * @param jarFile jar
     * @return true-是框架 | false-不是
     */
    public static boolean isFrameJar(File jarFile) {
        return Objects.nonNull(jarFile) &&
                jarFile.exists() &&
                jarFile.isFile() &&
                Strings.regexMatch(jarFile.getAbsolutePath(),
                        "needle-[a-zA-Z0-9_-]+-([0-9]+.[0-9]+.[0-9]+)-((RELEASE)|(SNAPSHOT))\\.jar$");
    }

    /**
     * 遍历指定jar文件对象
     * @param file          jar文件对象
     * @return              匹配到的文件对象集合
     * @throws IOException IO 异常
     */
    public static List<JarEntry> scanJar(File file) throws IOException {
        ArrayList<JarEntry> result = new ArrayList<>();
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry > jarEntrys = jarFile.entries();
        while(jarEntrys.hasMoreElements()){
            JarEntry jarEntry = jarEntrys.nextElement();
            result.add(jarEntry);
        }
        jarFile.close();
        return result;
    }

}
