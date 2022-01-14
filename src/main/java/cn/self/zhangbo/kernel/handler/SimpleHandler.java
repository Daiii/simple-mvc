package cn.self.zhangbo.kernel.handler;

import java.lang.reflect.Method;

/**
 * 映射关系
 */
public class SimpleHandler {

    /**
     * 请求路径
     */
    private String url;

    /**
     * 对应的controller
     */
    private Object controller;

    /**
     * 对应的方法
     */
    private Method method;

    public SimpleHandler() {
    }

    public SimpleHandler(String url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SimpleHandler{");
        sb.append("url='").append(url).append('\'');
        sb.append(", controller=").append(controller);
        sb.append(", method=").append(method);
        sb.append('}');
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
