package dev.asudara.automation.bbdd;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "nodos")
public class NodosModelo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String hostname;
    private String credsname;
    private String ssoo;
    private String configname;
    private String projectname;
    private String role;
    public NodosModelo() {
    }
    
    public NodosModelo(String hostname, String credsname, String ssoo, String configname, String projectname,
            String role) {
        this.hostname = hostname;
        this.credsname = credsname;
        this.ssoo = ssoo;
        this.configname = configname;
        this.projectname = projectname;
        this.role = role;
    }

    public String getHostname() {
        return hostname;
    }
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getCredsname() {
        return credsname;
    }
    public void setCredsname(String credsname) {
        this.credsname = credsname;
    }
    public String getSsoo() {
        return ssoo;
    }
    public void setSsoo(String ssoo) {
        this.ssoo = ssoo;
    }
    public String getConfigname() {
        return configname;
    }
    public void setConfigname(String configname) {
        this.configname = configname;
    }
    public String getProjectname() {
        return projectname;
    }
    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

}
