package dev.asudara.automation.bbdd;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class ProjectModelo {
    @Id
    private String name;


    public ProjectModelo() {
    }

    public ProjectModelo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
