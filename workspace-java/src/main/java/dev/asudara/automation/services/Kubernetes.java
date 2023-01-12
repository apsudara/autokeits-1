package dev.asudara.automation.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dev.asudara.automation.model.Configuration;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Kubernetes {
    String name;
    Configuration configs;

    public Kubernetes(){
        
    }
    public Kubernetes(int id, String name) {
        this.name = name;
        this.configs = new Configuration(name,"", "", false);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Configuration getConfigs() {
        return configs;
    }

    public void setConfigs(Configuration configs) {
        this.configs = configs;
    }
    
    
}
