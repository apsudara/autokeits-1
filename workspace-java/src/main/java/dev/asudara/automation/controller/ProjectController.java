package dev.asudara.automation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.asudara.automation.bbdd.ProjectModelo;
import dev.asudara.automation.errors.Responses;
import dev.asudara.automation.repository.ProjectsRepo;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {
    @Autowired
    private ProjectsRepo projects;
    @GetMapping("/api/v1/projects")
    public ResponseEntity<Iterable<ProjectModelo>> getProjects() {
        return ResponseEntity.status(HttpStatus.OK).body(this.projects.findAll());
    }

    @PutMapping("/api/v1/projects/{projectName}")
    public ResponseEntity<Responses> createProject(@PathVariable String projectName) {
        Responses res = new Responses(true, "Mensaje por defecto!");
        ProjectModelo pm = new ProjectModelo();
        if(this.projects.findById(projectName).isPresent()){
            res.setIsError(true);
            res.setMsg("Ya existe el proyecto!");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(res);
        }
        pm.setName(projectName);
        this.projects.save(pm);
        res.setIsError(false);
        res.setMsg("Proyecto guardado creado!");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    @GetMapping("/api/v1/projects/{projectname}/exists")
    public ResponseEntity<Responses> existProject(@PathVariable String projectname){
        Responses res = new Responses(false, "Existe el proyecto: " + projectname);
        if (this.projects.findById(projectname).isPresent()) {
            res.setMsg("El proyecto existe");
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
        ;
        System.out.println("[DEBUG] Exist Project: '" + projectname + "'");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @DeleteMapping("/api/v1/projects/{projectname}")
    public ResponseEntity<Responses> deleteProject(@PathVariable String projectname){
        Responses res = new Responses(false, "Delete proyecto: " + projectname);
        if (!this.projects.findById(projectname).isPresent()) {
            res.setMsg("El proyecto no existe");
            res.setIsError(true);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }else{
            this.projects.deleteById(projectname);
        }
        System.out.println("[DEBUG] Delete Project: '" + projectname + "'");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
