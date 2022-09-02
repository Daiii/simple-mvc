package cn.self.zhangbo.kernel.util;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * 注解工具类
 *
 * @author zhangbo
 * @since 2020/09/01
 */
public class AnnotationUtil {

    public static boolean isCandidateClass(Class<?> clazz, Collection<Class<? extends Annotation>> annotationTypes) {
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (isCandidateClass(clazz, annotationType)) {
                return true;
            }
        }
        return Boolean.FALSE;
    }

    public static boolean isCandidateClass(Class<?> clazz, Class<? extends Annotation> annotationType) {
        for (Annotation annotation : clazz.getAnnotations()) {
            boolean condition = StringUtil.equals(annotation.annotationType().getName(), annotationType.getName());
            if (condition) return condition;
        }
        return Boolean.FALSE;
    }
}
