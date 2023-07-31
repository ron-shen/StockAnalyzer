package webapp.configs;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class GeneralConfiguration {
    @Bean
    public ChromeDriver chromeDriver(){
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless=new");
        return new ChromeDriver(options);
    }
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
