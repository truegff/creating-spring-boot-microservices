package com.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.tourservice.TravelAgentService;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringBeans.class);
        TravelAgentService agentService = context.getBean(TravelAgentService.class);

        System.out.println("\n******Explore California Tour Catalogue******");
        agentService.displayTours();

        System.out.println("\n******Explore California Tour Kid Friendly Tours******");
        agentService.displayToursBy(true);
    }
}