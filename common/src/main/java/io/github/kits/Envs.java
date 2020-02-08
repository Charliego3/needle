package io.github.kits;

import io.github.kits.log.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;

/**
 * 全局工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Envs {

	private static boolean isNullEmptyBlack(boolean isBlack, Object... objects) {
		if (Objects.isNull(objects) || objects.length == 0) {
			return true;
		}
		for (Object obj : objects) {
			if (Objects.isNull(obj)) {
				return true;
			} else if (obj instanceof CharSequence) {
				if (isBlack) {
					return Strings.isBlack((String) obj);
				} else {
					return Strings.isNullOrEmpty((String) obj);
				}
			}
		}
		return false;
	}

	public static boolean isNullEmptyBlack(Object... objects) {
		return isNullEmptyBlack(true, objects);
	}

	public static boolean isNotNullEmptyBlack(Object... objects) {
		return !isNullEmptyBlack(false, objects);
	}

	/**
	 * 判断是否有的参数为空: 是-true | 否-false
	 *
	 * @param objects 需要校验的参数
	 * @return true | false
	 */
	public static boolean hasNullOrEmpty(Object... objects) {
		return isNullEmptyBlack(false, objects);
	}

	/**
	 * 判断是否不包含空值: 不包含-true | 包含-false
	 *
	 * @param args Object
	 * @return true | false
	 */
	public static boolean notHasNullOrEmpty(Object... args) {
		return !hasNullOrEmpty(args);
	}

	/**
	 * 判断是否是win系统
	 *
	 * @return 是(true)
	 */
	public static boolean isWin() {
		String osName = System.getProperty("os.name");
		return osName.toLowerCase()
					 .contains("win");
	}

	/**
	 * 判断是否不是win系统
	 *
	 * @return 不是(true)
	 */
	public static boolean isNotWin() {
		return !isWin();
	}

	/**
	 * 获取当前栈信息
	 *
	 * @param stackTraceElements 栈信息对象数组
	 * @return 当前栈信息
	 */
	public static String getStackElementsMessage(StackTraceElement[] stackTraceElements) {
		StringBuilder stackInfo = new StringBuilder();
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			stackInfo.append(stackTraceElement.toString());
			stackInfo.append(Files.getLineSeparator());
		}
		return stackInfo.toString()
						.trim();
	}

	/**
	 * 获取进程的主线程
	 *
	 * @return 进程的主线程
	 */
	public static Thread getMainThread() {
		for (Thread thread : getThreads()) {
			if (thread.getId() == 1) {
				return thread;
			}
		}
		return null;
	}

	/**
	 * 获取JVM中的所有线程
	 *
	 * @return 线程对象数组
	 */
	public static Thread[] getThreads() {
		ThreadGroup group = Thread.currentThread()
								  .getThreadGroup()
								  .getParent();
		if (Objects.isNull(group)) {
			return new Thread[0];
		}
		int      estimatedSize = group.activeCount() * 2;
		Thread[] slackList     = new Thread[estimatedSize];
		int      actualSize    = group.enumerate(slackList);
		Thread[] list          = new Thread[actualSize];
		System.arraycopy(slackList, 0, list, 0, actualSize);
		return list;
	}

	/**
	 * 线程休眠
	 *
	 * @param milliSeconds 毫秒数
	 */
	public static void sleep(long milliSeconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliSeconds);
		} catch (Exception ex) {
			Logger.errorf("The Thread sleep error, Thread: {}", Thread.currentThread().getName());
		}
	}

	/**
	 * 判读是否是基本类型(null, boolean, byte, char, double, float, int, long, short, string)
	 *
	 * @param clazz Class 对象
	 * @return true: 是基本类型, false:非基本类型
	 */
	public static <T> boolean isBasicType(Class<T> clazz) {
		return clazz == null || clazz.isPrimitive() || clazz.getName().startsWith("java.lang");
	}

	/**
	 * 将对象数组转换成,对象类型的数组
	 *
	 * @param objs 对象类型数组
	 * @return 类数组
	 */
	public static Class<?>[] getArrayClasses(Object[] objs) {
		if (objs == null) {
			return new Class<?>[0];
		}

		Class<?>[] parameterTypes = new Class<?>[objs.length];
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] == null) {
				parameterTypes[i] = Object.class;
			} else {
				parameterTypes[i] = objs[i].getClass();
			}
		}
		return parameterTypes;
	}

	/**
	 * 判读是否是 JDK 中定义的类(java包下的所有类)
	 *
	 * @param clazz Class 对象
	 * @return true: 是JDK 中定义的类, false:非JDK 中定义的类
	 */
	public static <T> boolean isSystemType(Class<T> clazz) {
		if (clazz.isPrimitive()) {
			return true;
		}
		//排除的包中的 class 不加载
		for (String systemPackage : systemPackages) {
			if (clazz.getCanonicalName().startsWith(systemPackage)) {
				return true;
			}
		}
		return false;
	}

	private static final List<String> systemPackages = Arrays.asList("java.", "jdk.", "sun.", "javax.", "com.sun", "com.oracle", "javassist");

	/**
	 * 判断某个类型是否实现了某个接口
	 * 包括判断其父接口
	 *
	 * @param type           被判断的类型
	 * @param interfaceClass 检查是否实现了次类的接口
	 * @return 是否实现某个接口
	 */
	public static boolean isImpByInterface(Class<?> type, Class<?> interfaceClass) {
		if (type == interfaceClass) {
			return true;
		}
		Class<?>[] interfaces = type.getInterfaces();
		for (Class<?> interfaceItem : interfaces) {
			if (interfaceItem == interfaceClass) {
				return true;
			} else {
				return isImpByInterface(interfaceItem, interfaceClass);
			}
		}
		return false;
	}

	/**
	 * 判断某个类型是否继承于某个类
	 * 包括判断其父类
	 *
	 * @param type         判断的类型
	 * @param extendsClass 用于判断的父类类型
	 * @return 是否继承于某个类
	 */
	public static boolean isExtendsByClass(Class<?> type, Class<?> extendsClass) {
		if (extendsClass == type) {
			return true;
		}
		Class<?> superClass = type;
		do {
			if (superClass == extendsClass) {
				return true;
			}
			superClass = superClass.getSuperclass();
		} while (superClass != null && Object.class != superClass);

		return false;
	}

	/**
	 * 获取堆栈信息
	 *
	 * @param index 索引
	 * @return 对应的堆栈
	 */
	public static StackTraceElement getStaceTraceE(int index) {
		Throwable           ex         = new Throwable();
		StackTraceElement[] stackTrace = ex.getStackTrace();
		return stackTrace.length > index ? stackTrace[index] : stackTrace[stackTrace.length - 1];
	}

	/**
	 * 获取当前进程 PID
	 *
	 * @return 当前进程 ID
	 */
	public static long getCurrentPID() {
		return Long.parseLong(ManagementFactory.getRuntimeMXBean()
											   .getName()
											   .split("@")[0]);
	}

	/**
	 * 反射
	 *
	 * @param className 类名称
	 * @return 加载的Class
	 */
	public static Class<?> forName(String className) throws ClassNotFoundException {
		try {
			return Class.forName(className);
		} catch (Exception ex) {
			throw new ClassNotFoundException("load and define class " + className + " failed");
		}
	}

	/**
	 * 类检查器
	 * 是否符合 filters 中的约束条件, 注解/类/接口等
	 *
	 * @param clazz   Class 对象
	 * @param filters 过滤器
	 * @return true: 符合约束, false: 不符合约束
	 */
	public static boolean classChecker(Class clazz, List<Class> filters) {
		int matchCount = 0;
		// 当且仅当这个类是匿名类此方法返回true
		if (clazz.isAnonymousClass()) {
			return false;
		}

		if (Lists.isNullOrEmpty(filters)) {
			return true;
		}

		for (Class filterClazz : filters) {
			if (clazz == filterClazz) {
				break;
			}

			// Class.isAnnotationPresent 如果一个注解指定类型是存在于此元素上此方法返回true，否则返回false
			if (filterClazz.isAnnotation() && clazz.isAnnotationPresent(filterClazz)) {
//				matchCount++;
				return true;
			} else if (filterClazz.isInterface() && isImpByInterface(clazz, filterClazz)) {
//				matchCount++;
				return true;
			} else if (isExtendsByClass(clazz, filterClazz)) {
//				matchCount++;
				return true;
			} else if (filterClazz.isAnnotation()) {
				Field[] declaredFields = clazz.getDeclaredFields();
				for (Field field : declaredFields) {
					Annotation annotation = field.getAnnotation(filterClazz);
					if (Objects.nonNull(annotation)) {
//						matchCount++;
						return true;
					}
				}
			}
		}
		return false;
//		return matchCount >= filters.size();
	}

	/**
	 * 查找classPath下的类
	 *
	 * @return 查找到的所有class
	 * @throws IOException IOException
	 */
	public static List<Class> findClass(Class... filters) throws IOException {
		return findClass(null, filters);
	}

	/**
	 * 查找指定路径下的类
	 *
	 * @param classPath dir
	 * @return classes
	 * @throws IOException IOException
	 */
	public static List<Class> findClass(String classPath, Class... filters) throws IOException {
		Set<Class>   classes = new HashSet<>();
		List<String> userClassPath;
		if (Strings.isNullOrEmpty(classPath)) {
			userClassPath = Files.getClassPaths();
		} else {
			userClassPath = Collections.singletonList(classPath);
		}
		for (String path : userClassPath) {
			File rootFile = new File(path);
			if (rootFile.exists() && rootFile.isDirectory()) {
				classes.addAll(getDirClass(rootFile, filters));
			} else if (rootFile.exists() && rootFile.isFile() && Files.isFrameJar(rootFile)) {
				classes.addAll(getJarClass(rootFile, filters));
			}
		}
		return new ArrayList<>(classes);
	}

	/**
	 * 根据类名判断是否是匿名内部类
	 *
	 * @param className clsaa全名
	 * @return 匿名内部类-true | false
	 */
	public static boolean isAnonymousInnerClass(String className) {
		return Strings.regexFind(className, "\\$\\d*\\.class$");
	}

	/**
	 * 获取目录下的class
	 *
	 * @param rootFile 根目录文件
	 * @return Classes
	 * @throws IOException IOException
	 */
	public static List<Class> getDirClass(File rootFile, Class... filters) throws IOException {
		List<File> files   = Files.scanFile(rootFile);
		Set<Class> classes = new HashSet<>();
		if (!files.isEmpty()) {
			files.forEach(LambdaExes.rethrowConsumer(file -> {
					 String fileName = file.getCanonicalPath();
					 if ("class".equals(Files.getFileExtension(fileName))) {
						 //如果是内部类则跳过
						 if (isAnonymousInnerClass(fileName)) {
							 return;
						 }
						 fileName = fileName.replace(Files.getClassPath() + File.separator, "");
						 toClassAndChecker(Arrays.asList(filters), classes, fileName);
					 }
				 }));
		}
		return new ArrayList<>(classes);
	}

	/**
	 * 获取jar文件中的class
	 *
	 * @param jarFile     jar文件
	 * @param incldeClass 需要找的类型
	 * @return classes
	 * @throws IOException IOException
	 */
	public static List<Class> getJarClass(File jarFile, Class... incldeClass) throws IOException {
		Set<Class>     classes    = new HashSet<>();
		List<JarEntry> jarEntries = Files.scanJar(jarFile);
		jarEntries.forEach(jarEntry -> {
					  String fileName = jarEntry.getName();
					  if ("class".equals(Files.getFileExtension(fileName))) {
						  //如果是内部类则跳过
						  if (isAnonymousInnerClass(fileName)) {
							  return;
						  }
						  toClassAndChecker(Arrays.asList(incldeClass), classes, fileName);
					  }
				  });
		return new ArrayList<>(classes);
	}

	/**
	 * 根据class的全名加载, 并且检查是否符合过滤器
	 * 如果符合条件添加到classes中
	 *
	 * @param incldeClass class过滤器
	 * @param classes     需要添加的集合
	 * @param fileName    class全名
	 */
	private static void toClassAndChecker(List<Class> incldeClass, Set<Class> classes, String fileName) {
		try {
			Class clazz = resourceToClass(fileName);
			if (classChecker(clazz, incldeClass)) {
				classes.add(clazz);
			}
		} catch (ClassNotFoundException e) {
			Logger.errorf("Try to load class[{}] failed", e, fileName);
		}
	}

	/**
	 * 将资源文件路径 转换成 Class
	 *
	 * @param resourcePath 资源资源文件路径
	 * @return Class对象
	 * @throws ClassNotFoundException 类未找到异常
	 */
	public static Class resourceToClass(String resourcePath) throws ClassNotFoundException {
		String className;

		if (resourcePath.startsWith(File.separator)) {
			resourcePath = resourcePath.substring(1);
		}

		className = Strings.replace(resourcePath, ".class", "\\$.*\\.class$");
		className = Strings.replace(className, "", ".class$");
		className = Strings.replace(className, ".", File.separator);

		return forName(className);
	}

	/**
	 * 从 InputStream 读取全部字节
	 *
	 * @param inputStream 输入流
	 * @return 字节数组
	 * @throws IOException IO 异常
	 */
	public static byte[] readAll(InputStream inputStream) throws IOException {
		if (Objects.nonNull(inputStream)) {
			ByteArrayOutputStream byteOutputStream    = new ByteArrayOutputStream();
			BufferedInputStream   bufferedInputStream = new BufferedInputStream(inputStream);
			while (true) {
				byte[] tempBytes = new byte[1024];
				int    readSize  = bufferedInputStream.read(tempBytes);
				if (readSize > 0) {
					byteOutputStream.write(tempBytes, 0, readSize);
				} else if (readSize == -1) {
					break;
				}
			}
			bufferedInputStream.close();
			return byteOutputStream.size() == 0 ? null : byteOutputStream.toByteArray();
		}
		return null;
	}

}
