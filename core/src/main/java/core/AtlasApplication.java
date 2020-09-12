package core;


//import api.dto.TestInnerApi;
import api.dto.TestOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AtlasApplication{
    public static void main(String[] args) {
        SpringApplication.run(AtlasApplication.class, args);
    }
}

@RestController

class Test{
    @GetMapping("open-api")
    public String openTest(){
     return new TestOpenApi().testFromOpenApi();
    }
  //  @GetMapping("inner-api")
   // public String innerTest(){
  //      return new TestInnerApi().testFromInnerApi();
  //  }
}