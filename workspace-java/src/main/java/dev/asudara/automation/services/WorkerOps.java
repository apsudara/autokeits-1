package dev.asudara.automation.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.util.ResourceUtils;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class WorkerOps extends Operations {
    private String certificateKeyHash;
    public String getCertificateKeyHash() {
        return certificateKeyHash;
    }

    public void firewallPorts(String src, String dest) throws JSchException, IOException, SftpException{
        String script_name = "2_firewall_ports.sh";
        this.sendExecScript(src, dest, script_name);
    }
    public void setCertificateKeyHash(String certificateKeyHash) {
        this.certificateKeyHash = certificateKeyHash;
    }
    private String token;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public WorkerOps() {
    }
    public HostProvision getHost() {
        return host;
    } 

    public void join(String src, String dest, String main_control_plane_endpoint,String worker) throws IOException, JSchException, SftpException{

        String scriptname = "3_join_worker.sh";
        String base_path = ResourceUtils.getFile("classpath:static/").getPath() + "/";

        File file = new File(base_path + src + scriptname);
        System.out.println("[DEBUG TEST E] path para join worker: '" + base_path + src + scriptname + "'");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("#!/bin/bash\n");
        fw.close();
        fw = new FileWriter(file, true);
        String command ="";
        switch(this.ssoo){
            case "CentOS7": 
                command = ""
            ;break;
            case "Ubuntu":
                command = "sudo ";
            break;
            default:
        }
         command = command + " kubeadm join "  + main_control_plane_endpoint + ".kubernetes.int:6443" 
        +" --token " + this.token 
        + " --discovery-token-ca-cert-hash sha256:" + this.certificateKeyHash + ""
        + " --node-name " + worker + ".kubernetes.int ";
        fw.append(command);
        fw.close();
        String path_dest = dest + scriptname;
        this.host.sendFile(src + scriptname, path_dest);
        this.host.execCommand("sh " + path_dest, "", scriptname);}
}
