package com.chatgpt.config;

import com.chatgpt.z_interceptor.AccessInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration  //标识是一个配置文件
@EnableWebMvc //定义MVC的配置类
@ComponentScan("com.chatgpt.a_controller") //定义扫描路径
public class ServletConfig  implements WebMvcConfigurer { //设置开始的定义


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("进入拦截器------------");
        // 注册拦截器  除了login和注册不拦截 其他都拦截
        registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/testredis")
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/login1")  //登入不拦截
                ;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        System.out.println("-------设置文件上传------------");
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        //设置上传文件的最大值，单位为字节
        multipartResolver.setMaxUploadSize(5400000);
        //设置请求的编码格式，默认为iso-8859-1
        multipartResolver.setDefaultEncoding("UTF-8");
//        System.out.println("设置成功");

        return multipartResolver;
    }

    @Bean
    public ViewResolver viewResolver(){  //用于加载所有不带 @Controller 注解去的地方  这个是视图解析器
        System.out.println("-----视图解析器路径扫描加载---------");
//
        InternalResourceViewResolver viewresolver = new InternalResourceViewResolver(); //视图解析器
        viewresolver.setPrefix("WEB-INF/views/"); //视图解析地方
        viewresolver.setSuffix(".html");//解析器后缀

        viewresolver.setExposeContextBeansAsAttributes(true); //将上下文Bean属性公开为属性
        return viewresolver;
    }


    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){ //配置默认servlet处理程序
        System.out.println("--------------启动默认servlet支持--------------");
        configurer.enable(); //启动默认servlet支持
    }

}
