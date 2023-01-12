package dev.asudara.automation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dev.asudara.automation.bbdd.NodosModelo;
import dev.asudara.automation.repository.NodoRepo;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class NodoController {
    @Autowired
    private NodoRepo nodos;

    @PutMapping("/api/v2/nodos/{nodoname}")
    public @ResponseBody Iterable<NodosModelo> putNodo(@PathVariable String nodoname, @RequestBody NodosModelo nodo) {
        nodos.save(nodo);
        return nodos.findAll();
    }

}
