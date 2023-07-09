package com.chatgpt.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class MvcWebApplnitizer  extends AbstractAnnotationConfigDispatcherServletInitializer {  //抽象注解配置分发Servlet初始化器

//    自己要用的配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
        System.out.println("-----------自定义配置类启动成功--------");


        return new Class[]{RootConfig.class,RedisConfig.class}; // redis 支持 标识自定义加载的类区域
//        return new Class[]{RootConfig.class}; //标识自定义加载的类区域
    }


//    需要跑的配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        System.out.println("-----------Web类启动成功--------");

        return new Class[]{ServletConfig.class};
    }




    //设置编码格式等等
    @Override
    protected String[] getServletMappings() {

          return new String[]{"/"};  //它指定将DispatcherServlet映射到哪些请求路径  在这种情况下，DispatcherServlet将处理所有的请求路

    }



//编码
    @Override
    protected Filter[] getServletFilters() {



        System.out.println("----------编码格式初始化启动成功------------");
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();//字符    编码  过滤器

//        characterEncodingFilter.setEncoding("UTF-8"); //设置编码格式
        characterEncodingFilter.setForceEncoding(true); //设置强制性转换成UTF-8不论提交过来的请求是什么编码格式


            return new Filter[]{characterEncodingFilter};
    }
}
