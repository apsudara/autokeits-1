package dev.asudara.automation.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import dev.asudara.automation.bbdd.ConfigurationModelo;
import dev.asudara.automation.bbdd.CredentialsModelo;
import dev.asudara.automation.bbdd.NodosModelo;
import dev.asudara.automation.bbdd.ProjectModelo;
import dev.asudara.automation.errors.AutomationException;
import dev.asudara.automation.errors.Responses;
import dev.asudara.automation.model.Configuration;
import dev.asudara.automation.model.Master;
import dev.asudara.automation.model.RelacionProjectConfig;
import dev.asudara.automation.model.Worker;
import dev.asudara.automation.repository.ConfigurationRepo;
import dev.asudara.automation.repository.CredentialsRepo;
import dev.asudara.automation.repository.NodoRepo;
import dev.asudara.automation.repository.ProjectsRepo;
import dev.asudara.automation.services.KubernetesAutomation;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class kubernetesController {
    private KubernetesAutomation kubeAuto = new KubernetesAutomation();
    @Autowired
    private NodoRepo nodos;
    @Autowired
    private ConfigurationRepo configs;
    @Autowired
    private ProjectsRepo projects;
    @Autowired
    private CredentialsRepo creds;

    @GetMapping("/api/v1/kubernetes")
    public ResponseEntity<ArrayList<RelacionProjectConfig>> getListKubernetes() {
        ArrayList<RelacionProjectConfig> res = new ArrayList<RelacionProjectConfig>();
        for (NodosModelo node : this.nodos.findAll()) {
            RelacionProjectConfig aux_projconf = new RelacionProjectConfig();
            aux_projconf.setConfigname(node.getConfigname());
            aux_projconf.setProjectname(node.getProjectname());
            int res_size = res.size();
            boolean enc = false;
            for (int i = 0; i < res_size && !enc; i++) {
                enc = res.get(i).getConfigname().compareTo(aux_projconf.getConfigname())==0 && res.get(i).getProjectname().compareTo(aux_projconf.getProjectname())==0;
            }
            if (!enc) {
                res.add(aux_projconf);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/api/v1/kubernetes/hosts")
    public ResponseEntity<ArrayList<RelacionProjectConfig>> getListKubernetesHost() {
        ArrayList<RelacionProjectConfig> res = new ArrayList<RelacionProjectConfig>();
        for (NodosModelo node : this.nodos.findAll()) {
            RelacionProjectConfig aux_projconf = new RelacionProjectConfig();
            aux_projconf.setConfigname(node.getConfigname());
            aux_projconf.setProjectname(node.getProjectname());
            aux_projconf.setHost(node.getHostname());
                res.add(aux_projconf);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PutMapping("/api/v1/kubernetes/{configName}")
    public ResponseEntity<Responses> deployKubernete(@PathVariable String configName,
            @RequestBody Configuration configForm) {
        Responses res = new Responses();
        System.out.println("[DEBUG] PUT /api/v1/kubernetes/" + configName);
        if (this.configs.findById(configName).isPresent()) {
            res.setMsg("Existe el proyecto: " + configName);
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(res);
        }

        ArrayList<NodosModelo> nodos = new ArrayList<NodosModelo>();
        int len = configForm.getWorkerHosts().size();
        boolean enc = false;
        boolean enccreds = false;
        for (int i = 0; i < len && !enc; i++) {
            Worker w = configForm.getWorkerHosts().get(i);
            enc = w.getHost().compareTo("") == 0;
            NodosModelo nodo = new NodosModelo();
            nodo.setConfigname(configForm.getName());
            nodo.setHostname(w.getHost());
            nodo.setProjectname(configForm.getProjectname());
            nodo.setRole("worker");
            nodo.setSsoo(configForm.getSsoo());
            if (this.creds.findById(w.getCredname()).isEmpty()) {

                enccreds = true;
                enc = true;
            } else {

                CredentialsModelo wcreds_model = this.creds.findById(w.getCredname()).get();
                configForm.getWorkerHosts().get(i).setCreds(wcreds_model);
                nodo.setCredsname(configForm.getWorkerHosts().get(i).getCredname());

            }
            if (this.nodos.findByHostnameAndConfignameAndProjectname(nodo.getHostname(), nodo.getConfigname(),
                    nodo.getProjectname()).isEmpty()) {
                nodos.add(nodo);
            } else {
                enc = true;
            }
        }
        if (enccreds) {
            res.setMsg("No se encuantra credencial proporcionado del/los host/s. Config name: " + configName);
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(res);
        }
        if (enc) {
            res.setMsg("El nodo ya esxiste en el mismo dominio de project y configs" + configName);
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(res);
        }
        len = configForm.getMasterHosts().size();
        enc = false;
        enccreds = false;
        for (int i = 0; i < len && !enc; i++) {
            Master m = configForm.getMasterHosts().get(i);
            enc = m.getHost().compareTo("") == 0;
            NodosModelo nodo = new NodosModelo();
            nodo.setConfigname(configForm.getName());
            nodo.setHostname(m.getHost());
            nodo.setProjectname(configForm.getProjectname());
            nodo.setRole("master");
            nodo.setSsoo(configForm.getSsoo());
            if (this.creds.findById(m.getCredname()).isEmpty()) {
                enccreds = true;
                enc = true;
            } else {

                CredentialsModelo wcreds_model = this.creds.findById(m.getCredname()).get();
                configForm.getMasterHosts().get(i).setCreds(wcreds_model);
                nodo.setCredsname(configForm.getMasterHosts().get(i).getCredname());

            }
            System.out.println("[AQUI ----- ]" + m.getCredname());
            if (this.nodos.findByHostnameAndConfignameAndProjectname(nodo.getHostname(), nodo.getConfigname(),
                    nodo.getProjectname()).isEmpty()) {
                nodos.add(nodo);
            } else {
                enc = true;
            }
        }
        if (enccreds) {
            res.setMsg("No se encuantra credencial proporcionado del/los host/s. Config name: " + configName);
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(res);
        }
        if (enc) {
            res.setMsg("El nodo ya esxiste en el mismo dominio de project y configs" + configName);
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(res);
        }
        for (NodosModelo addNodo : nodos) {
            this.nodos.save(addNodo);
        }
        ConfigurationModelo confmd = new ConfigurationModelo();
        confmd.setName(configForm.getName());
        this.configs.save(confmd);
        ProjectModelo proj = new ProjectModelo();
        proj.setName("default");
        this.projects.save(proj);

        kubeAuto.setConfiguration(configForm);
        try {
            kubeAuto.deployKubernete();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        res.setIsError(false);
        res.setMsg("Se ha creado el nodo!");
        System.out.print("[] Construccion del cluster terminado!");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/api/v1/kubernetes/{configName}/exist")
    public ResponseEntity<Responses> existKubernete(@PathVariable String configName) {
        Responses res = new Responses(false, "Existe el proyecto: " + configName);
        if (this.configs.findById(configName).isPresent()) {
            res.setMsg("El proyecto existe");
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        ;
        System.out.println("[DEBUG] existKubernate: '" + configName + "'");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping("/api/v1/kubernetes/{configName}")
    public ResponseEntity<Responses> deleteKubernetes(@PathVariable String configName) {
        Responses res = new Responses(false, "Delete config: " + configName);
        for(NodosModelo nodo: this.nodos.findAll()){

            if(nodo.getConfigname().compareTo(configName)==0){
                this.nodos.delete(nodo);
            }
        }
        // if (!this.configs.findById(configName).isPresent()) {
        //     res.setMsg("El config no existe");
        //     res.setIsError(true);
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        // } else {
        //     this.configs.deleteById(configName);
        // }
        System.out.println("[DEBUG] Delete config: '" + configName + "'");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
