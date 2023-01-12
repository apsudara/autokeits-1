package dev.asudara.automation.repository;

import org.springframework.data.repository.CrudRepository;

import dev.asudara.automation.bbdd.ConfigurationModelo;

public interface ConfigurationRepo extends CrudRepository<ConfigurationModelo,String> {
    
}
