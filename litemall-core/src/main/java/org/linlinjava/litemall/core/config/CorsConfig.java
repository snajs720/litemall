package org.linlinjava.litemall.core.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    // 当前跨域请求最大有效时长。这里默认30天
    private long maxAge = 30 * 24 * 60 * 60;

    /**
     * 允许的前端来源，逗号分隔，例如 https://mall.example.com,https://admin.example.com。
     * 留空时保持开发期宽松放行（*）；生产环境必须配置明确白名单，不要沿用通配源。
     */
    @Value("${litemall.cors.allowed-origins:}")
    private String allowedOrigins;

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (origins.isEmpty()) {
            // 未配置白名单：开发期宽松放行；生产请通过 litemall.cors.allowed-origins 收敛来源
            corsConfiguration.addAllowedOrigin("*");
        } else {
            origins.forEach(corsConfiguration::addAllowedOrigin);
        }
        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(maxAge);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
