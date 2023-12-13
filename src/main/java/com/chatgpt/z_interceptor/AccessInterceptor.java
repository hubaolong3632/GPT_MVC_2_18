package com.chatgpt.z_interceptor;



import com.alibaba.fastjson.JSON;
import com.chatgpt.utio.UtioCode.Result;
import com.chatgpt.utio.UtioCode.ResultCode;
import com.chatgpt.utio.UtioY;
import com.chatgpt.utio.model.JWTDatasModel;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        System.out.println("步入拦截");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // 如果是OPTIONS请求，则返回false
            System.out.println("跨域请求判断");
            return true;
        }




//        需要用到redis判断一万次请求自动拦截掉
//
//         在请求处理之前进行拦截处理
//         获取请求的 URL
//        String Authorization = request.getHeader("Authorization"); //获取请求密钥
//        System.out.println(Authorization);
//        // 判断是否已登录
//        JWTDatasModel jwtDatasModel = UtioY.JWT_PAnalysis(Authorization);
//        System.out.println(jwtDatasModel);
//        if (jwtDatasModel == null) { //如果密钥错误或者过期等等
//            System.out.println("已经拦截 密钥错误");
//            response.setContentType("text/json; charset=utf-8"); //设置编码格式和数据类型
//            response.getWriter().println(JSON.toJSONString(Result.makeOnesOwnChoice("密钥失效", ResultCode.LoginNO)));
//
//            return false;
//        }
//        System.out.println("密钥正确");

        // 如果已登录，则继续进行请求处理
        return true;
    }



    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        System.out.println("------请求处理之后---------");
        // 在请求处理之后进行拦截处理，但是在视图被渲染之前

//        logFileService.addLogFile("日志保存",request); //日志保存





    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
//        System.out.println("------请求拦截 视图渲染之后---------");

        // 在请求处理之后进行拦截处理，且在视图被渲染之后
    }
}

//             如果未登录，重定向到登录页
//            response.sendRedirect("/no");
