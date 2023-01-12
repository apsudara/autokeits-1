package dev.asudara.automation.repository;

import org.springframework.data.repository.CrudRepository;

import dev.asudara.automation.bbdd.CredentialsModelo;

public interface CredentialsRepo extends CrudRepository<CredentialsModelo,String> {
    
}
