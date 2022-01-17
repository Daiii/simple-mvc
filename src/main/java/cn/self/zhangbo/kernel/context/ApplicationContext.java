package cn.self.zhangbo.kernel.context;

import cn.self.zhangbo.kernel.annotation.Autowired;
import cn.self.zhangbo.kernel.annotation.Component;
import cn.self.zhangbo.kernel.annotation.Controller;
import cn.self.zhangbo.kernel.annotation.Service;
import cn.self.zhangbo.kernel.xml.XMLParser;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IoC容器
 */
public class ApplicationContext {

    /**
     * 文件路径
     */
    private String contextConfigLocation;

    /**
     * 权限类集合
     */
    private final List<String> classes = new ArrayList<>();

    /**
     * bean容器
     */
    public final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     * 早期bean
     */
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    /**
     * 初始化
     */
    public void onRefresh() {
        // 解析XML读取backPackage
        String xml = this.contextConfigLocation.split(":")[1];

        // 包扫描
        String basePackage = XMLParser.getBasePackage(xml);
        String[] packages = basePackage.split(",");
        for (String pack : packages) {
            executeScanPackage(pack);
        }

        // 实例化
        executeInstance();

        // 自动注入
        executeAutowired();
    }

    /**
     * 根据package扫描
     *
     * @param pack 路径
     */
    private void executeScanPackage(String pack) {
        URL url = this.getClass().getClassLoader().getResource("/" + pack.replaceAll("\\.", "/"));
        String path = url.getFile();
        File dir = new File(path);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                executeScanPackage(pack + "." + file.getName());
            } else {
                String className = pack + "." + file.getName().replaceAll(".class", "");
                classes.add(className);
            }
        }
    }

    /**
     * 实例化
     */
    private void executeInstance() {
        for (String className : classes) {
            try {
                Class<?> clz = Class.forName(className);
                if (clz.isAnnotationPresent(Controller.class)) {
                    String value = clz.getAnnotation(Controller.class).value();
                    String beanName = value;
                    if (value.equals("")) {
                        beanName = clz.getSimpleName().substring(0, 1).toLowerCase() + clz.getSimpleName().substring(1);
                    }
                    singletonObjects.put(beanName, clz.newInstance());
                }

                if (clz.isAnnotationPresent(Component.class)) {
                    String value = clz.getAnnotation(Component.class).value();
                    String beanName = value;
                    if (value.equals("")) {
                        beanName = clz.getSimpleName().substring(0, 1).toLowerCase() + clz.getSimpleName().substring(1);
                    }
                    singletonObjects.put(beanName, clz.newInstance());
                }

                if (clz.isAnnotationPresent(Service.class)) {
                    String value = clz.getAnnotation(Service.class).value();
                    String beanName = value;
                    if (value.equals("")) {
                        beanName = clz.getInterfaces()[0].getSimpleName().substring(0, 1).toLowerCase()
                                + clz.getInterfaces()[0].getSimpleName().substring(1);
                    }
                    singletonObjects.put(beanName, clz.newInstance());
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自动注入
     */
    private void executeAutowired() {
        for (Map.Entry<String, Object> entry : singletonObjects.entrySet()) {
            Object bean = entry.getValue();
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    String value = field.getAnnotation(Autowired.class).value();
                    String beanName = value;
                    if (value.equals("")) {
                        beanName = field.getName().substring(0, 1).toLowerCase() + field.getName().substring(1);
                    }
                    field.setAccessible(Boolean.TRUE);
                    try {
                        field.set(bean, singletonObjects.get(beanName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public ApplicationContext() {
    }

    public ApplicationContext(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApplicationContext{");
        sb.append("contextConfigLocation='").append(contextConfigLocation).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getContextConfigLocation() {
        return contextConfigLocation;
    }

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }
}
