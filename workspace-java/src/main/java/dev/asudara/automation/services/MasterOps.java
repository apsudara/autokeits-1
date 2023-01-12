package dev.asudara.automation.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.util.ResourceUtils;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class MasterOps extends Operations {

    private String ca;
    private String token;
    private String certificateKeyHash;


    public MasterOps() {
    }

    public String generateCertificateKeyHash(String src,String dest) throws JSchException, SftpException, IOException{
        String script_name = "5_0_2_get_certificate_key_hash.sh";
        this.host.sendFile(src + script_name, dest + script_name);
        String certificate_key_hash_value = this.host.execCommand("sh " + dest + script_name, "", script_name);
        this.certificateKeyHash = certificate_key_hash_value.replace("\n", "");
        System.out.println("[DEBUG] get Certificate key HASH: '" + this.certificateKeyHash + "'");

        return this.certificateKeyHash;
    }
    public String generateCertificateKey(String src, String dest) throws JSchException, SftpException, IOException {
        String script_name = "5_0_1_certificate_key.sh";
        this.host.sendFile(src + script_name, dest + script_name);
        String certificate_key_value = this.host.execCommand("sh " + dest + script_name, "", script_name);
        this.ca = certificate_key_value.replace("\n", "");
        System.out.println("[DEBUG] get Certificate key: '" + this.ca + "'");

        return this.ca;
    }

    public void firewallCni(String src, String dest, String cni) throws IOException, JSchException, SftpException {
        String scriptname = "";
        switch (cni) {
            case "Calico":
                scriptname = "3_firewalld_cni_calico.sh";
                ;
                break;
            default:
                scriptname = "3_firewalld_cni.sh";
        }
        this.sendExecScript(src, dest, scriptname);
    }

    public void initKubeadm(String src, String dest, String type_cni, String master)
            throws IOException, JSchException, SftpException {
        String scriptname = "5_1_kubeadmin_init.sh";
        String base_path = ResourceUtils.getFile("classpath:static/").getPath() + "/";

        File file = new File(base_path + src + scriptname);
        System.out.println("[DEBUG TEST C] path para initKubeadm: '" + base_path + src + scriptname + "'");
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("#!/bin/bash\n");
        fw.close();
        String command = "";
        fw = new FileWriter(file, true);

        switch(this.ssoo){
            case "CentOS7": 
                command = ""
            ;break;
            case "Ubuntu":
                command = "sudo ";
            break;
            default:
        }
        switch (type_cni) {
            case "Calico":
                 String cni = "192.168.0.0";
                //String cni = "10.244.0.0";
                command = command + " kubeadm init --node-name " + master + ".kubernetes.int  " + " --control-plane-endpoint="
                        + master + ".kubernetes.int"
                        + " --apiserver-advertise-address=" + master + " --pod-network-cidr " + cni
                        + "/16  --upload-certs --certificate-key " + this.ca + " \n";
                ;
                break;
            default:
                // Nothing to do;
        }
        fw.append(command);
        fw.close();
        String path_dest = "/tmp/" + scriptname;
        this.host.sendFile(src + scriptname, path_dest);
        this.host.execCommand("sh " + path_dest, "", scriptname);

    }

    public String getToken(String src, String dest) throws JSchException, SftpException, IOException {
        String script_name = "5_3_kube_token.sh";
        this.host.sendFile(src + script_name, dest + script_name);
        String token_value = this.host.execCommand("sh " + dest + script_name, "", script_name);
        System.out.println("[DEBUG] get token: '" + token_value + "'");
        return token_value;

    }

    public void setupHostControlPlane(String src, String dest) throws JSchException, IOException, SftpException {
        String scriptname = "5_1_2_setupControlPlane.sh";
        this.sendExecScript(src, dest, scriptname);
    }

    public void deployCniCluster(String src, String dest, String cni) throws JSchException, IOException, SftpException {
        String scriptname = "";
        switch (cni) {
            case "Calico":
                scriptname = "5_2_kube_cni_calico.sh";
                break;
            default:
        }
        this.sendExecScript(src, dest, scriptname);
    }

    public void join(String src, String dest, String main_control_plane_endpoint,String master) throws IOException, JSchException, SftpException {
        String scriptname = "6_join_control_plane.sh";
        String base_path = ResourceUtils.getFile("classpath:static/").getPath() + "/";

        File file = new File(base_path + src + scriptname);
        System.out.println("[DEBUG TEST D] path para join master: '" + base_path + src + scriptname + "'");
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
        command = command + " kubeadm join " + main_control_plane_endpoint + ".kubernetes.int:6443"  
         + " --token " + this.token 
         + " --discovery-token-ca-cert-hash sha256:" + this.certificateKeyHash 
         + " --certificate-key " + this.ca
         + " --node-name " + master + ".kubernetes.int "
         + " --control-plane";
        fw.append(command);
        fw.close();
        String path_dest = dest + scriptname;
        this.host.sendFile(src + scriptname, path_dest);
        this.host.execCommand("sh " + path_dest, "", scriptname);
    }

    public HostProvision getHost() {
        return host;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getCertificateKeyHash() {
        return certificateKeyHash;
    }


    public void setCertificateKeyHash(String certificateKeyHash) {
        this.certificateKeyHash = certificateKeyHash;
    }


}
