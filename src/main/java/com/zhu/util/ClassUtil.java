package com.zhu.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class ClassUtil {
    private static final Logger log=  LoggerFactory.getLogger(ClassUtil.class);
    /**
     * 获取类加载器,返回当前线程类加载器即可
     */

    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * @param className
     * @param isInitialized 是否执行类的静态代码块
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInitialized){
        Class<?> cls;
        try {
            cls = Class.forName(className,isInitialized,getClassLoader());
        } catch (ClassNotFoundException e) {
            log.error("load class failure",e);
            throw new RuntimeException(e);
        }
        return cls;
    }
    /**
     * 获取指定包名下的所有类
     * 根据包名转化为路径，读取class获取jar包，获取所有class 类名加载类
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<>();
        try {
            //将包名转换成路径
            String packagePaths = packageName.replaceAll("\\.","/");
            Enumeration<URL> urls = getClassLoader().getResources(packagePaths);
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null){
                    String protocol = url.getProtocol();
                    //如果是文件
                    if(protocol.equals("file")){
                        //替换URL中的%20成空格
                        String packagePath = url.getPath().replaceAll("%20"," ");
                        addClass(classSet,packagePath,packageName);
                    }else if(protocol.equals("jar")){
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        url.openConnection();
                        if(jarURLConnection != null){
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if(jarFile!=null){
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while(jarEntries.hasMoreElements()){
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarName = jarEntry.getName();
                                    if(jarName.equals(".class")){
                                        String className = jarName.substring(0,jarName.lastIndexOf("."));
                                        classSet.add(loadClass(
                                                className.replaceAll("/","."),
                                                        false));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("get class set failure",e);
            throw  new RuntimeException(e);
        }

        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){

        //提取目录和.class文件
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".class")
                        || pathname.isDirectory());
            }
        });
        if(files.length>0){
            for(File file:files){
                String fileName = file.getName();
                //.class文件
                if(file.isFile()){
                    //获取文件名(不含后缀)即类名
                    String className = fileName.substring(0,fileName.lastIndexOf("."));
                    //如果有上级包名，将包名和类名拼接成完全类限定名
                    if(StringUtils.isNotEmpty(packageName)){
                        className = packageName+"."+className;
                    }

                    classSet.add(loadClass(className,false));
                }else{
                    //如果是目录

                    String newPackagePath = packagePath;
                    //拼接新的包路径
                    if(StringUtils.isNotEmpty(packagePath)){
                        newPackagePath += ("/"+fileName);

                    }
                    String newPackageName = packageName;
                    //拼接新的包名
                    if(StringUtils.isNotEmpty(packageName)){
                        newPackageName += ("."+fileName);
                    }
                    addClass(classSet,newPackagePath,newPackageName);
                }

            }
        }
    }

    public static void main(String[] args) {
        Set<Class<?>> classSet = ClassUtil.getClassSet("com.zhu");
        for(Class<?> clazz: classSet){
            System.out.println(clazz.getName());
        }
    }
}
