package dev.asudara.automation.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jcraft.jsch.JSchException;

import dev.asudara.automation.services.HostProvision;
import dev.asudara.automation.bbdd.CredentialsModelo;
import dev.asudara.automation.bbdd.NodosModelo;
import dev.asudara.automation.errors.ResponseOutput;
import dev.asudara.automation.services.Console;
import dev.asudara.automation.errors.Responses;
import dev.asudara.automation.repository.CredentialsRepo;
import dev.asudara.automation.repository.NodoRepo;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ApplicationsController {
    
    @Autowired
    private NodoRepo nodos;
    @Autowired
    private CredentialsRepo creds;
    
    @PutMapping("/api/v1/kubernetes/{host}/{configName}/apps/helloworld")
    public ResponseEntity<Responses> deployHelloWorld(@PathVariable String configName, @PathVariable String host,@RequestBody Console cmd){
        
        ResponseOutput res = new ResponseOutput();
        res.setIsError(false);
        HostProvision h = new HostProvision();
        // buscar en bbdd el cofigname + host
        String user = "";
        String password = "";

        for(NodosModelo nodo: this.nodos.findAll()){
            
            //System.out.println("NAME: creds="+nodo.getCredsname()+ " host="+nodo.getHostname()+" condigname="+nodo.getConfigname() + " Is emprty "+ this.creds.findById(nodo.getCredsname()).isEmpty());

            if(nodo.getConfigname().compareTo(configName)==0 && nodo.getHostname().compareTo(host)==0){
                //System.out.println("NAME: "+nodo.getCredsname() + " Is emprty "+ this.creds.findById(nodo.getCredsname()).isEmpty());
                CredentialsModelo cm = this.creds.findById(nodo.getCredsname()).get();
                user = cm.getUser();
                password = cm.getPassword();
            }   
        }

        try {

            System.out.println("user=" + user + "  password="+ password + " host="+ host );
            if(user!=""){
                h.startSession(user, password, host);
                //send file

                // execute 
                res.setResultOutPut(h.executeCommand(cmd.getCmd()));
            }

        } catch (JSchException e) {
            e.printStackTrace();
        }

        h.desconectSession();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PutMapping("/api/v1/kubernetes/apps/autokeits")
    public ResponseEntity<Responses> deployAutokeits(@PathVariable String configName){
        return null;
    }
}
