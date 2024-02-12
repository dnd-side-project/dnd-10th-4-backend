package dnd.myOcean.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class HttpHeaderConfig {

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return resHeaders;
    }
}
