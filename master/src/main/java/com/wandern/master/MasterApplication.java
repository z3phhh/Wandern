package com.wandern.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;


@SpringBootApplication
public class MasterApplication {
    public static void main(String[] args) {
            ApplicationContext ctx = SpringApplication.run(MasterApplication.class, args);

//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            System.out.println("Total number of beans: " + beanNames.length);
//
//            for (int i = 0; i < beanNames.length; i++) {
//                System.out.println((i + 1) + ". " + beanNames[i]);
//            }
    }
}

//todo: Run in this order: 1) master 2) agent 3) arm 4) manager