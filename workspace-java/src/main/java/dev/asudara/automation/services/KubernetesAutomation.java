package dev.asudara.automation.services;

import java.io.IOException;
import java.util.ArrayList;


import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import dev.asudara.automation.errors.AutomationException;
import dev.asudara.automation.model.Configuration;
import dev.asudara.automation.model.Master;
import dev.asudara.automation.model.Worker;

public class KubernetesAutomation {
    private ArrayList<Master> masters;
    private ArrayList<Worker> workers;
    private String cri;
    private String cni;
    private String join_certificate_key;
    private String join_certificate_key_hash;
    private String join_token;
    private String ssoo;

    private void existsCertificate() throws AutomationException {
        if (this.join_certificate_key == null | this.join_certificate_key.compareTo("") == 0) {
            throw new AutomationException("Clave Certificado vacio. ");
        }
    }

    public KubernetesAutomation() {
    }

    public void setConfiguration(Configuration configuration) {
        this.masters = configuration.masterHosts;
        this.workers = configuration.workerHosts;
        this.cri = configuration.getCri();
        this.cni = configuration.getCni();
        this.ssoo = configuration.getSsoo();
    }

    public void deployKubernete() throws JSchException, AutomationException, IOException, SftpException {
        String base_path_src = "";// ResourceUtils.getFile("classpath:static/").getPath();
        String base_path_dst = "/tmp/";
        Master main_control_plane = this.setupNodes(base_path_src + "linux/" + ssoo + "/", base_path_dst);
        this.initCluster(base_path_src + "linux/" + ssoo + "/", base_path_dst, main_control_plane);
        this.joinNodes(base_path_src + "linux/" + ssoo + "/", base_path_dst, main_control_plane.getHost());
    }

    public String getJoin_certificate_key() throws AutomationException { // para posterior guardado en bbdd
        this.existsCertificate();
        return join_certificate_key;
    }

    public String getJoin_token() throws AutomationException { // para posterior guardado en bbdd
        this.existsCertificate();
        return join_token;
    }

    private Master setupNodes(String base_src, String dest)
            throws JSchException, AutomationException, IOException, SftpException {
        ArrayList<String> nodos = this.getAllHost();
        Master main_control_plane = null;

        for (Master master : masters) {
            System.out.println("[DEBUG] MASTER: "+ master.getHost() + "**************");
            String user = master.getCreds().getUser();
            String password = master.getCreds().getPassword();
            String host = master.getHost();
            MasterOps masterOps = new MasterOps();
            masterOps.setSsoo(ssoo);
            masterOps.getHost().startSession(user, password, host);
            if (ssoo.compareTo("Ubuntu") == 0) {
                masterOps.cri(base_src, dest, this.cri);
                masterOps.installKubeAdmLetCtl(base_src, dest);
                masterOps.installServices(base_src, dest);
            }
            if (ssoo.compareTo("CentOS7") == 0) {
                masterOps.installServices(base_src, dest);
                masterOps.cri(base_src, dest, this.cri);
                masterOps.installKubeAdmLetCtl(base_src, dest);
            }
            masterOps.enableResolveHosts(base_src + "master/", dest, nodos);
            masterOps.hostsFirewall(base_src + "master/", dest, nodos);
            masterOps.firewallCni(base_src + "master/", dest, this.cni);

            masterOps.getHost().desconectSession();
            if (master.getIsCPEndpoint()) {
                main_control_plane = master;
            }
        }
        for (Worker worker : workers) {
            System.out.println("[DEBUG] WORKER: "+ worker.getHost() + "**************");
            String user = worker.getCreds().getUser();
            String password = worker.getCreds().getPassword();
            String host = worker.getHost();
            WorkerOps workerOps = new WorkerOps();
            workerOps.setSsoo(ssoo);
            workerOps.getHost().startSession(user, password, host);
            if (ssoo.compareTo("Ubuntu") == 0) {
            workerOps.cri(base_src, dest, this.cri);
            workerOps.installKubeAdmLetCtl(base_src, dest);
            workerOps.installServices(base_src, dest);
            }
            if (ssoo.compareTo("CentOS7") == 0) {
                workerOps.installServices(base_src, dest);
                workerOps.cri(base_src, dest, this.cri);
                workerOps.installKubeAdmLetCtl(base_src, dest);
            }
            workerOps.enableResolveHosts(base_src + "worker/", dest, nodos);
            workerOps.hostsFirewall(base_src + "worker/", dest, nodos);
            workerOps.firewallPorts(base_src + "worker/", dest);

            workerOps.getHost().desconectSession();
        }
        if (main_control_plane == null)
            throw new AutomationException("No existe Main Control Plane");
        return main_control_plane;
    }

    private void initCluster(String base_src, String dest, Master main_control_plane)
            throws JSchException, SftpException, IOException {
        // INIT cluster - first control plane
        System.out.println(
                "[DEBUG] initCluster src: " + base_src + " main control_plane: " + main_control_plane.getHost());
        String user = main_control_plane.getCreds().getUser();
        String password = main_control_plane.getCreds().getPassword();
        String host = main_control_plane.getHost();
        MasterOps masterOps = new MasterOps();
        masterOps.setSsoo(ssoo);
        masterOps.getHost().startSession(user, password, host);

        join_certificate_key = masterOps.generateCertificateKey(base_src + "master/", dest);
        masterOps.initKubeadm(base_src, dest, this.cni, host);
        join_certificate_key_hash = masterOps.generateCertificateKeyHash(base_src + "master/", dest);
        join_token = masterOps.getToken(base_src + "master/", dest);
        masterOps.setupHostControlPlane(base_src + "master/", dest);
        masterOps.deployCniCluster(base_src + "master/", dest, this.cni);

        masterOps.getHost().desconectSession();
    }

    private void joinNodes(String src, String dest, String main_control_plane)
            throws AutomationException, JSchException, IOException, SftpException {
        for (Master master : masters) {
            if (!master.getIsCPEndpoint()) {
                this.joinMaster(src, dest, main_control_plane, master);
            }
        }
        for (Worker worker : workers) {
            this.joinWorker(src, dest, main_control_plane, worker);
        }

    }

    public void joinMaster(String src, String dest, String main_control_plane, Master master)
            throws AutomationException, JSchException, IOException, SftpException {
        this.existsCertificate();
        MasterOps masterOps = new MasterOps();
        String user = master.getCreds().getUser();
        String password = master.getCreds().getPassword();
        String host = master.getHost();
        masterOps.setSsoo(ssoo);
        masterOps.getHost().startSession(user, password, host);
        masterOps.setCa(this.join_certificate_key);
        masterOps.setCertificateKeyHash(this.join_certificate_key_hash);
        masterOps.setToken(this.join_token);
        masterOps.join(src + "master/", dest, main_control_plane, host);
        masterOps.getHost().desconectSession();

    }

    public void joinWorker(String src, String dest, String main_control_plane, Worker worker)
            throws AutomationException, JSchException, IOException, SftpException {
        if (this.join_certificate_key == null | this.join_certificate_key.compareTo("") == 0) {
            throw new AutomationException("Clave Certificado vacio. ");
        }
        WorkerOps workerOps = new WorkerOps();
        String user = worker.getCreds().getUser();
        String password = worker.getCreds().getPassword();
        String host = worker.getHost();
        workerOps.setSsoo(ssoo);
        workerOps.getHost().startSession(user, password, host);
        workerOps.setCertificateKeyHash(this.join_certificate_key_hash);
        workerOps.setToken(this.join_token);
        workerOps.join(src + "worker/", dest, main_control_plane, host);
        workerOps.getHost().desconectSession();
    }

    private ArrayList<String> getAllHost() {
        ArrayList<String> res = new ArrayList<String>();
        for (Master master : masters) {
            res.add(master.getHost());
        }
        for (Worker worker : workers) {
            res.add(worker.getHost());
        }
        return res;
    }
}
