package dev.asudara.automation.model;

import dev.asudara.automation.bbdd.CredentialsModelo;

public class Master  extends Nodo{


    private Boolean isCPEndpoint ;
    


    public Master(){
        isCPEndpoint = false;
    }
    public Master(String host) {
        this.setHost(host);
        isCPEndpoint = false;
    } 

    public Master(String host, CredentialsModelo creds) {
        this.setHost(host);
        this.setCreds(creds); 
        isCPEndpoint = false;
    }

    public Boolean getIsCPEndpoint() {
        return isCPEndpoint;
    }

    public void setIsCPEndpoint(Boolean isCPEndpoint) {
        this.isCPEndpoint = isCPEndpoint;
    }
}
