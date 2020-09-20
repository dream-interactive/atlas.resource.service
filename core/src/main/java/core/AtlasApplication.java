package core;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class AtlasApplication{
    public static void main(String[] args) {
        SpringApplication.run(AtlasApplication.class, args);
    }
}
