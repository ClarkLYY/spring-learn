package com.clarklyy.framework.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ClassScanUtil {
    private static final Set<Class<?>> CLASS_SET = new HashSet<Class<?>>();

    static{
        doClassScan();
    }

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    private static void doAddClass(String className) {
        try {
            Class<?> cls = Class.forName(className);
            CLASS_SET.add(cls);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("load class failure");
        }
    }


    public static void addClassByPath(String path, String packageName){
        File baseFile = new File(path);
        File[] fileList = baseFile.listFiles();
        for(File file:fileList){
            String fileName = file.getName();
            if(file.isFile()){
                //拼接包名和文件名
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                className = packageName+"."+className;
                doAddClass(className);
            }else{
                String subPath = path+"/"+fileName;
                String subName = packageName + "."+fileName;
                //递归获取文件
                addClassByPath(subPath, subName);
            }
        }
    }

    private static void doClassScan(){
        //从配置文件中读取包位置
        String packageName = PropertiesUtil.getAppBasePackage();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                String path = url.getPath();
                addClassByPath(path, packageName);
                System.out.println("=============类加载完毕，加载类个数: "+CLASS_SET.size()+"============");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("scan class failure");
        }
    }

    public static void main(String[] args) {
    }
}
