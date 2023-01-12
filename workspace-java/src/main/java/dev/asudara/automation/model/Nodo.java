package dev.asudara.automation.model;
import dev.asudara.automation.bbdd.CredentialsModelo;

public class Nodo {
    private String host;
    private CredentialsModelo creds;
    private String credname = "";
    
    public String getCredname() {
        return credname;
    }
    public void setCredname(String credname) {
        this.credname = credname;
    }
    public Nodo(){
    }
    public String getHost() {
        return host;
    }
 
    public void setHost(String host) {
        this.host = host;
    }

    public CredentialsModelo getCreds() {
        return creds;
    }

    public void setCreds(CredentialsModelo creds) {
        this.creds = creds;
    }


}
