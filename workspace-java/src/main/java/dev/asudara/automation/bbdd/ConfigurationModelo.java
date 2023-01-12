package dev.asudara.automation.bbdd;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class ConfigurationModelo {
    @Id
    private String name;

    
    public ConfigurationModelo() {
    }

    public ConfigurationModelo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
