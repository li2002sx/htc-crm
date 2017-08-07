package com.htche.crm;

import com.htche.crm.util.DateFormatter;
import com.htche.crm.util.SecurityInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by jankie on 16/5/1.
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.htche.crm.mapper")
//启注解事务管理
@EnableTransactionManagement
public class WebBootstrap extends WebMvcConfigurerAdapter {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebBootstrap.class, args);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter());
        super.addFormatters(registry);
    }

    /**
     * 配置拦截器
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor())
                .addPathPatterns("/**/**");
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:8080", "http://172.168.1.26:8080"
                        , "http://192.168.1.103:8080", "http://m.grtstar.cn")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*")
//                .exposedHeaders("X-Auth-Token", "Authorization")
                .allowedMethods("POST", "GET", "DELETE", "PUT", "OPTIONS");
    }
}
