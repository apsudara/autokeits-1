package dev.asudara.automation.model;

public class RelacionProjectConfig {
    private String configname;
    private String projectname;
    private String host;
    
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public RelacionProjectConfig() {
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
    
}
