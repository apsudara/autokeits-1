package dev.asudara.automation.model;

import java.util.ArrayList;


import com.fasterxml.jackson.annotation.JsonProperty;

import dev.asudara.automation.bbdd.CredentialsModelo;

public class Configuration {
    String projectname;
    String name;
    String ssoo;
    String cni;
    String cri;
    @JsonProperty()
    public
    ArrayList<Master> masterHosts;
    
    @JsonProperty()
    public
    ArrayList<Worker> workerHosts;
    boolean dashboard;
 


    public Configuration() {
    }

    
    public Configuration(String name, String ssoo, String cni, String cri, ArrayList<Master> masterHosts,
            ArrayList<Worker> workerHosts, boolean dashboard) {
        this.name = name;
        this.ssoo = ssoo;
        this.cni = cni;
        this.cri = cri;
        this.masterHosts = masterHosts;
        this.workerHosts = workerHosts;
        this.dashboard = dashboard;
    }


    public Configuration(String name, String ssoo, String storage, boolean dashboard) {
        this.name = name;
        this.ssoo = ssoo;
        this.dashboard = dashboard;
        this.masterHosts = new ArrayList<Master>();
        this.workerHosts = new ArrayList<Worker>();
    }
    
    public String getCni() {
        return cni;
    }


    public void setCni(String cni) {
        this.cni = cni;
    }


    public String getCri() {
        return cri;
    }


    public void setCri(String cri) {
        this.cri = cri;
    }


    public void setMasterHosts(ArrayList<Master> masterHosts) {
        this.masterHosts = masterHosts;
    }


    public void setWorkerHosts(ArrayList<Worker> workerHosts) {
        this.workerHosts = workerHosts;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSsoo() {
        return ssoo;
    }
    public void setSsoo(String ssoo) {
        this.ssoo = ssoo;
    }

    public boolean isDashboard() {
        return dashboard;
    }
    public void setDashboard(boolean dashboard) {
        this.dashboard = dashboard;
    }
    public void addMaster(String host,CredentialsModelo creds){
        Master m1 = new Master(host);
        m1.setCreds(creds);
        this.masterHosts.add(m1);

        
    }
    public void addMaster(Master m){
        Master m1 = new Master(m.getHost());
        m1.setCreds(m.getCreds());
        this.masterHosts.add(m1);

    }
    public void addWorker(String host,CredentialsModelo creds){
        Worker w1 = new Worker();
        w1.setHost(host);
        w1.setCreds(creds);
        this.workerHosts.add(w1);
    }
    public ArrayList<Master> getMasterHosts() {
        return masterHosts;
    }

    public ArrayList<Worker> getWorkerHosts() {
        return workerHosts;
    }
    public String getProjectname() {
        return projectname;
    }


    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }
}
