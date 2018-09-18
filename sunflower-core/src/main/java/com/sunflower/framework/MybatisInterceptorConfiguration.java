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

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 PageHelper 的支持
        paginationInterceptor.setLocalPage(true);
        return paginationInterceptor;
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

