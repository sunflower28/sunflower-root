package com.sunflower.framework;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.SqlExplainInterceptor;
import com.sunflower.framework.tx.SqlCommandTypeInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.sunflower.*.mapper"})
public class MybatisInterceptorConfiguration {
    public MybatisInterceptorConfiguration() {
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public SqlCommandTypeInterceptor sqlCommandTypeInterceptor() {
        return new SqlCommandTypeInterceptor();
    }

    @Bean
    public SqlExplainInterceptor sqlExplainInterceptor() {
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
        sqlExplainInterceptor.setStopProceed(true);
        return sqlExplainInterceptor;
    }
}

