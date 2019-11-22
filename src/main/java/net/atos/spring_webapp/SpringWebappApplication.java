package net.atos.spring_webapp;

import net.atos.spring_webapp.model.Permission;
import net.atos.spring_webapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebappApplication.class, args);
    }

}
