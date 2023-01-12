package dev.asudara.automation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.asudara.automation.bbdd.NodosModelo;
@Repository
public interface NodoRepo extends CrudRepository<NodosModelo,Long> {

    @Query(value =" SELECT * FROM  autokeitsdev.nodos WHERE hostname = :hostname AND configname = :configname AND projectname = :projectname", nativeQuery = true)
    public List<NodosModelo> findByHostnameAndConfignameAndProjectname(@Param("hostname") String hostname, @Param("configname") String configname,@Param("projectname") String projectname);
}
