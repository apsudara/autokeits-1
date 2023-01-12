package dev.asudara.automation.repository;

import org.springframework.data.repository.CrudRepository;

import dev.asudara.automation.bbdd.ProjectModelo;

public interface ProjectsRepo extends CrudRepository<ProjectModelo,String> {
   
}
