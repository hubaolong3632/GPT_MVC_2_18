package com.chatgpt.config;

import com.chatgpt.model.gpt.mvcGpt.GPTConfigurationModel;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;

@Configuration
// Reposity  @service  @Bean
@MapperScan(basePackages="com.chatgpt.idao")

@ComponentScan(basePackages = "com.chatgpt" ,excludeFilters =
        {@ComponentScan.Filter(type = FilterType.ANNOTATION,value ={EnableWebMvc.class, Controller.class})})
//是除了web层的所有的Bean
@PropertySource({"classpath:jdbc.properties"})
@PropertySource("classpath:configuration.properties") //配置文件注入
public class RootConfig {

    //@Bean 不管是不是自己写的类都可以纳入到spring的机制里来
    @Value("${jdbc.driverClass}")
    private  String driver;
    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.username}")
    private String user;

    @Value("${jdbc.password}")
    private String password;

    @Bean(name="datasource")
    public DataSource getDataSource(){

        ComboPooledDataSource dataSource=new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driver);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }


        return dataSource;
    }


    @Bean(name = "jdbctemplate")
    public JdbcTemplate getJdbcTemplate(DataSource dataSource){
        JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    //mybatis的配置
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        System.out.println("进入Mybatis---------------------------");
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();//mybatis-plus插件类
        sqlSessionFactoryBean.setDataSource(getDataSource());//数据源
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("classpath:com/chatgpt/idao/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.chatgpt.model");//别名，让*Mpper.xml实体类映射可以不加上具体包名
        return sqlSessionFactoryBean;
    }

    //    生成配置参数的构造函数流输出
    @Bean
    public GPTConfigurationModel gptConfigStream(@Value("${gpt.model}")  String model, @Value("${gpt.temperature}") Double temperature, @Value("${gpt.stream}") Boolean stream){
        System.out.println("----------生成gpt配置模型");
        GPTConfigurationModel gpt_Config=new GPTConfigurationModel();
        gpt_Config.setModel(model); //模型类型
        gpt_Config.setTemperature(temperature); //温度
        gpt_Config.setStream(stream); //是否是流输出
        return gpt_Config;
    }

    //创建定时任务线程池
    @Bean
    public TaskScheduler taskScheduler() {
//        System.out.println("任务线程池");
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);//允许同时最多10个线程存在
        return taskScheduler;
    }


}
