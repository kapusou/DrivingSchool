package com.llb.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis-plus分页拦截器bean
 * @Author llb
 * Date on 2020/3/20
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /**
     * mybatis-plus分页拦截器bean
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor()
    {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }
}
