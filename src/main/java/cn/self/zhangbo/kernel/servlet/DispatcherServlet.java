package cn.self.zhangbo.kernel.servlet;

import cn.self.zhangbo.kernel.annotation.Controller;
import cn.self.zhangbo.kernel.annotation.RequestMapping;
import cn.self.zhangbo.kernel.annotation.RequestParam;
import cn.self.zhangbo.kernel.annotation.ResponseBody;
import cn.self.zhangbo.kernel.context.ApplicationContext;
import cn.self.zhangbo.kernel.handler.SimpleHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    /**
     * 上下文
     */
    private ApplicationContext applicationContext;

    /**
     * 请求关系映射
     */
    public List<SimpleHandler> handlerMapping = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 请求分发
        dispatcher(req, resp);
    }

    @Override
    public void init() throws ServletException {
        // 加载初始化参数
        String contextConfigLocation = this.getServletConfig().getInitParameter(CONTEXT_CONFIG_LOCATION);

        // 创建IoC容器
        this.applicationContext = new ApplicationContext(contextConfigLocation);

        // 初始化
        this.applicationContext.onRefresh();

        // 初始化映射关系
        this.initHandlerMapping();
    }

    /**
     * 初始化映射
     */
    private void initHandlerMapping() {
        for (Map.Entry<String, Object> entry : applicationContext.singletonObjects.entrySet()) {
            Class clz = entry.getValue().getClass();
            if (clz.isAnnotationPresent(Controller.class)) {
                Method[] declaredMethods = clz.getDeclaredMethods();
                for (Method method : declaredMethods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String url = requestMapping.value();
                        SimpleHandler handler = new SimpleHandler(url, entry.getValue(), method);
                        handlerMapping.add(handler);
                    }
                }
            }
        }
    }

    /**
     * 分发请求
     *
     * @param req  Request
     * @param resp Response
     */
    private void dispatcher(HttpServletRequest req, HttpServletResponse resp) {
        SimpleHandler handler = getHandler(req);
        try {
            if (handler == null) {
                resp.getWriter().println("<h1>404 Not Found</h1>");
            } else {
                invoke(handler, req, resp);
            }
        } catch (IllegalAccessException | InvocationTargetException | IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据请求获取handler
     *
     * @param req Request
     * @return Handler
     */
    private SimpleHandler getHandler(HttpServletRequest req) {
        String uri = req.getRequestURI();
        for (SimpleHandler handler : handlerMapping) {
            if (handler.getUrl().equals(uri)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * 处理请求
     *
     * @param handler 请求信息
     * @param req     request
     * @param resp    response
     * @throws IOException               异常
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException    异常
     */
    private void invoke(SimpleHandler handler, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, InvocationTargetException, IllegalAccessException, ServletException {
        Object result = null;

        // 获取方法参数
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
        Annotation[][] parameterAnnotations = handler.getMethod().getParameterAnnotations();
        Object[] paraValues = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestParam) {
                    String paraName = ((RequestParam) annotation).value();
                    paraValues[i] = req.getParameter(paraName);
                }
            }
        }

        if (paraValues != null && paraValues.length > 0) {
            result = handler.getMethod().invoke(handler.getController(), paraValues);
        } else {
            result = handler.getMethod().invoke(handler.getController());
        }

        if (handler.getMethod().isAnnotationPresent(ResponseBody.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(result);
            result = json;
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.println(result);
            writer.flush();
            writer.close();
        } else {
            String[] results = result.toString().split(":");
            if (results[0].equals("redirect")) {
                resp.sendRedirect(results[1] + ".jsp");
            } else if (results[0].equals("forward")) {
                req.getRequestDispatcher(results[1]).forward(req, resp);
            } else {
                resp.setContentType("text/html;charset=utf-8");
                PrintWriter writer = resp.getWriter();
                writer.println(result);
                writer.flush();
                writer.close();
            }
        }
    }
}
