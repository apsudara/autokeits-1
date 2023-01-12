package dev.asudara.automation.controller;


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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.asudara.automation.bbdd.CredentialsModelo;
import dev.asudara.automation.errors.Responses;
import dev.asudara.automation.model.Credentials;
import dev.asudara.automation.repository.CredentialsRepo;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CredentialsController {
    @Autowired
    private CredentialsRepo creds;

    @GetMapping("/api/v1/credentials/info")
    public ResponseEntity<CredentialsModelo> getCredentialInfo(@RequestParam String name) {
        if(name == null || name.compareTo("") == 0 || this.creds.findById(name).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(this.creds.findById(name).get());
    }
    // @GetMapping("/api/v1/credentials")
    // public ResponseEntity<Iterable<CredentialsModelo>> getAllCredentials() {
    //     return ResponseEntity.status(HttpStatus.OK).body(this.creds.findAll());
    // }
    @GetMapping("/api/v1/credentials")
    public ResponseEntity<ArrayList<Credentials>> getAllCredentials() {
        ArrayList<Credentials> res = new ArrayList<Credentials>();
        for(CredentialsModelo cred1: this.creds.findAll()){
            Credentials cred2 = new Credentials();
            cred2.setName(cred1.getName());
            res.add(cred2);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @GetMapping("/api/v1/credentials/{credname}/exists")
    public ResponseEntity<Responses> existKubernete(@PathVariable String credname) {
        Responses res = new Responses(false, "Existe el cred: " + credname);
        if (this.creds.findById(credname).isPresent()) {
            res.setMsg("El cred existe");
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        ;
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PutMapping("/api/v1/credentials/{credsName}")
    public ResponseEntity<Responses> createCredentials(@PathVariable String credsName,
            @RequestBody CredentialsModelo creds) {
        Responses res = new Responses();
        if (this.creds.existsById(creds.getUser())) {
            res.setIsError(true);
            res.setMsg("No se ha creaado el credential!");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
        }
        this.creds.save(creds);
        res.setIsError(false);
        res.setMsg("Credential guardado.");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    @DeleteMapping("/api/v1/credentials/{credsName}")
    public ResponseEntity<Responses> deleteProject(@PathVariable String credsName){
        Responses res = new Responses(false, "Delete proyecto: " + credsName);
        if (!this.creds.findById(credsName).isPresent()) {
            res.setMsg("El proyecto no existe");
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }else{
            this.creds.deleteById(credsName);
        }
        System.out.println("[DEBUG] Delete Project: '" + credsName + "'");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
