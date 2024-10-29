package com.ifortex.internship.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan("com.ifortex.internship")
@PropertySource(
    value = {"classpath:application-${envTarget:dev}.yml"},
    factory = YamlPropertySourceFactory.class)
public class ApplicationConfiguration {}
