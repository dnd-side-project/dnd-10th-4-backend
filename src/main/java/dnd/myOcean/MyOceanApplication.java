package dnd.myOcean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableAsync
public class MyOceanApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyOceanApplication.class, args);
    }
}
