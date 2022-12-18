package com.ntnn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

import java.io.File;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        System.out.println(projectPath);
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.addListeners(new ApplicationPidFileWriter(projectPath  + "/tmp/app.pid"));
        springApplication.run(args);
    }
}
