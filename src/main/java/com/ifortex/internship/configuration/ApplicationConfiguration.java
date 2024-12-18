package com.ifortex.internship.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.ifortex.internship")
@PropertySource(
    value = {"classpath:application-${envTarget:prod}.yml"},
    factory = YamlPropertySourceFactory.class)
@EnableTransactionManagement
@EnableScheduling
public class ApplicationConfiguration {}
