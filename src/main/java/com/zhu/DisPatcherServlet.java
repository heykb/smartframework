package com.zhu;

import com.zhu.common.*;
import com.zhu.helper.BeanHelper;
import com.zhu.helper.ConfigHelper;
import com.zhu.helper.ControllerHelper;
import com.zhu.util.CodecUtil;
import com.zhu.util.JsonUtil;
import com.zhu.util.ReflectionUtil;
import com.zhu.util.StreamUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.IOP.Codec;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DisPatcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //初始化相关helper类
        HelperLoader.init();
        //获取servletContext，注册servet
        ServletContext context = servletConfig.getServletContext();
        //注册处理jsp的servlet
        ServletRegistration jspServlet = context.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");

        //注册处理静态资源的servlet
        ServletRegistration defaultServlet = context.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getJdbcAppAssetPath()+"*");

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //获取请求方法和路径
        String requestMethod = req.getMethod();
        RequestMethod requestMethod1 = RequestMethod.get(requestMethod);
        String requestPath = req.getPathInfo();
        //根据请求方法和路径得到handler
        Handler handler = ControllerHelper.getHandler(requestMethod1,requestPath);
        if(handler!=null){
            //获取对应的Controller对象
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);
            //封装请求参数
            Map<String,Object> paramMap = new HashMap<>();
            Enumeration<String> keys = req.getParameterNames();
            while(keys.hasMoreElements()){
                String key = keys.nextElement();
                paramMap.put(key,req.getParameter(key));
            }
            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtils.isNotEmpty(body)){
                String[] params = StringUtils.split(body,"&");
                if(ArrayUtils.isNotEmpty(params)){
                    for(String param:params){
                        String[] paramArr = StringUtils.split(param,"=");
                        if(ArrayUtils.isNotEmpty(paramArr)&&paramArr.length==2){
                            String key = paramArr[0];
                            String value = paramArr[1];
                            paramMap.put(key,value);
                        }
                    }
                }
            }
            Param param = new Param(paramMap);

            //执行Action方法
            Object result = ReflectionUtil.invokeMethod(controllerBean,handler.getActionMethod(),param);

            if(result instanceof View){
                View view = (View)result;
                String path = view.getPath();
                if(StringUtils.isNotEmpty(path)){
                    if(path.startsWith("/")){
                        //重定向
                        resp.sendRedirect(req.getContextPath()+path);
                    }else{
                        Map<String,Object> model = view.getModel();
                        //将model数据添加到请求参数
                        for(Map.Entry<String,Object> entry:model.entrySet()){
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        //转发请求到jsp
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req,resp);
                    }
                }
            }else if(result instanceof Data){
                //返回json
                Data data = (Data)result;
                Object model = data.getModel();
                if(model!=null){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("utf-8");
                    PrintWriter writer = resp.getWriter();
                    writer.write(JsonUtil.toJson(model));
                    writer.flush();
                    writer.close();
                }

            }
        }
    }
}
