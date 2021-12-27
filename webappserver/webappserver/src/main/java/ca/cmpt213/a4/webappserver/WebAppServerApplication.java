package ca.cmpt213.a4.webappserver;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class handles loading in items into the server
 * and starting the server by invoking SpringApplication.run
 */
@SpringBootApplication
public class WebAppServerApplication {

    public static void main(String[] args) {
        ConsumableManager.loadItems();
        SpringApplication.run(WebAppServerApplication.class, args);
    }

}
