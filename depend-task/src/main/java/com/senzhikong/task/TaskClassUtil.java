package com.senzhikong.task;

import com.senzhikong.util.string.sign.Base64Util;
import org.springframework.context.ApplicationContext;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shu.zhou
 * @date 2019年4月17日上午10:40:05
 */
public class TaskClassUtil {

    private String jars;
    private final String targetDir;

    public TaskClassUtil(ApplicationContext context) {
        String jarDir = "WEB-INF" + File.separator + "lib";
        this.targetDir = System.getProperty("java.io.tmpdir") + File.separator + "javaDynamicClasses";
        this.jars = getJarFiles(jarDir);
    }


    public Class<?> getClassFromJavaFile(BaseTask task) throws Exception {
        boolean flag = new File(targetDir).mkdirs();
        if (!flag) {
            throw new RuntimeException("创建package文件夹失败");
        }
        String javaFile = task.getJavaFile();
        javaFile = Base64Util.decode(javaFile);
        String className = task.getTaskClass();
        className = className.substring(className.lastIndexOf(".") + 1);
        File file = new File(targetDir + File.separator + className + ".java");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(javaFile.getBytes());
        fos.close();
        boolean isSuccess = compiler(file);
        if (isSuccess) {
            System.out.println("编译成功");
        } else {
            System.out.println("编译失败");
        }
        URL newurl = new URL("file:/" + targetDir + File.separator);

        URLClassLoader classLoader = (URLClassLoader) TaskClassUtil.class.getClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        boolean isAccessible = method.canAccess(classLoader);
        Class<?> resClass = null;
        try {

            if (!isAccessible) {
                method.setAccessible(true);
            }
            method.invoke(classLoader, newurl);
            resClass = classLoader.loadClass(task.getTaskClass());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.setAccessible(isAccessible);
            classLoader.close();
        }
        return resClass;
    }

    public boolean compiler(File sourceFile) throws Exception {
        List<File> sourceFileList = new ArrayList<>();
        sourceFileList.add(sourceFile);
        return compiler(sourceFileList);
    }

    public boolean compiler(List<File> sourceFileList) throws Exception {
        // 获取编译器实例
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 获取标准文件管理器实例
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            // 获取要编译的编译单元
            Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromFiles(sourceFileList);
            // 编译选项，在编译java文件时，编译程序会自动的去寻找java文件引用的其他的java源文件或者class。
            // -sourcepath选项就是定义java源文件的查找目录，
            // -classpath选项就是定义class文件的查找目录。
            List<String> options = Arrays.asList("-encoding", "UTF-8", "-classpath", jars, "-d", targetDir);

            CompilationTask compilationTask =
                    compiler.getTask(null, fileManager, null, options, null, compilationUnits);
            // 运行编译任务
            return compilationTask.call();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static final String JAR = ".jar";

    /**
     * 查找该目录下的所有的jar文件
     */
    private String getJarFiles(String jarPath) {
        File sourceFile = new File(jarPath);
        File[] files = sourceFile.listFiles(f -> {
            if (f.isDirectory()) {
                return true;
            } else {
                return f.getName().endsWith(JAR);
            }
        });
        if (files == null) {
            return "";
        }
        jars = "";
        Arrays.stream(files).forEach((f) -> {
            jars = jars + f.getPath() + ";";
        });
        return jars;
    }
}
