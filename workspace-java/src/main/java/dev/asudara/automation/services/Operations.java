package dev.asudara.automation.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.util.ResourceUtils;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class Operations {
    protected String ssoo;
    protected HostProvision host;
    public Operations(){
        this.host = new HostProvision();
    }
    protected void sendExecScript(String src,String dest, String scriptname) throws JSchException, IOException, SftpException{
        this.host.sendFile(src + scriptname , dest + scriptname); 
        this.host.execCommand("sh "+ dest + scriptname, "",scriptname);
    }

    public void installServices(String src,String dest) throws JSchException, IOException, SftpException{
        String scriptname = "0_0_install_services.sh";
        this.sendExecScript(src, dest, scriptname);
    }
    
    public void cri(String src,String dest,String cri) throws JSchException, SftpException, IOException{
        String scriptname = "";
        switch(cri){
            case "containerd":
                scriptname = "0_cri-containerd.sh";
                this.sendExecScript(src, dest, scriptname);
            ;break;
            case "cri-o":
                scriptname = "0_cri-crio.sh";
                this.sendExecScript(src, dest, scriptname);
            ;break;
            case "marantes":
                scriptname = "0_cri-marantes.sh";
                this.sendExecScript(src, dest, scriptname);
            ;break;
            default:
            break;
        }
    }

    public void installKubeAdmLetCtl(String src, String dest) throws JSchException, IOException, SftpException{
        String scriptname = "1_kubeadm_kubelet_kubectl.sh";
        this.sendExecScript(src, dest, scriptname);

    }
    public void enableResolveHosts(String src,String dest, ArrayList<String> hosts) throws JSchException, IOException, SftpException{
        String scriptname = "2_hosts.sh";
        String base_path = ResourceUtils.getFile("classpath:static/").getPath() + "/";
        File file = new File(base_path + src + scriptname );
        System.out.println("[DEBUG TEST A] path para enableResolveHosts: '" +base_path + src + scriptname  +"'" );
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("#!/bin/bash\n");
        fw.close();
        fw = new FileWriter(file, true);
        String script ="";
        switch(this.ssoo){
            case "CentOS7": 
            script = ""
            ;break;
            case "Ubuntu":
            script = "sudo ";
            break;
            default:
        }
        script = script + "sh -c ''' echo \"127.0.0.1   localhost  \n" +
                "::1         localhost  \n" +
                "127.0.0.1   ubuntu-xenial  \n";

        for (String ip : hosts){
            script =script + ip + "  " + ip + ".kubernetes.int  "+"\n"; 
        }

        script =script + " \" > /etc/hosts '''";
        fw.append(script);
        fw.close();
        String path_dest = "/tmp/" +scriptname;
        this.host.sendFile(src + scriptname, path_dest);
        this.host.execCommand("sh " + path_dest, "", scriptname);
    }

    public void hostsFirewall(String src,String dest, ArrayList<String> hosts) throws IOException, JSchException, SftpException{
        String scriptname = "4_firewalld_hosts.sh";
        String base_path = ResourceUtils.getFile("classpath:static/").getPath() + "/";
        
        File file = new File(base_path + src + scriptname );
        System.out.println("[DEBUG TEST B] path para hostsFirewall: '" +base_path + src + scriptname  +"'" );
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.write("#!/bin/bash\n");
        fw.close();

        fw = new FileWriter(file, true);
        String script ="";
        switch(this.ssoo){
            case "CentOS7": 
            script = ""
            ;break;
            case "Ubuntu":
            script = "sudo ";
            break;
            default:
        }
        for (String host : hosts) {

            fw.append(script + " firewall-cmd --add-rich-rule=\"rule family=ipv4 source address=" + host
                    + "/32 accept\" --permanent \n");
        }

        String k8s_firewalls = script +" firewall-cmd --add-port 6443/tcp --permanent\n" +
                script + " firewall-cmd --add-port 2379-2380/tcp --permanent \n" +
                script + " firewall-cmd --add-port 10250/tcp --permanent\n" +
                script + " firewall-cmd --add-port 10251/tcp --permanent\n" +
                script + " firewall-cmd --add-port 10252/tcp --permanent\n" +
                script + " firewall-cmd --add-port 10255/tcp --permanent\n";
        fw.append(k8s_firewalls);
        fw.append(script + " firewall-cmd --reload\n");
        fw.close();
        String path_dest = "/tmp/" +scriptname;
        this.host.sendFile(src + scriptname, path_dest);
        this.host.execCommand("sh " + path_dest, "", scriptname);
    }
    public String getSsoo() {
        return ssoo;
    }
    public void setSsoo(String ssoo) {
        this.ssoo = ssoo;
    }
    public HostProvision getHost() {
        return host;
    }
    public void setHost(HostProvision host) {
        this.host = host;
    }
    

}
