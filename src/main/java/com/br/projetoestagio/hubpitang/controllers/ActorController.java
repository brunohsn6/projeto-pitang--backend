package com.br.projetoestagio.hubpitang.controllers;

import com.br.projetoestagio.hubpitang.Service.ActorService;
import com.br.projetoestagio.hubpitang.models.Actor;
import com.br.projetoestagio.hubpitang.models.Person;
import com.br.projetoestagio.hubpitang.models.Program;
import com.br.projetoestagio.hubpitang.repositories.IActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

@RestController
@RequestMapping("/actors")
@CrossOrigin(origins = "http://localhost:4200")
public class ActorController {

    @Autowired
    private IActorRepository actorRepository;


    @GetMapping(path = "/")
    public ResponseEntity<?> getAll(){
        try{
            System.out.println("antes do sucesso");
            return new ResponseEntity<>(this.actorRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            System.out.println("no insucesso!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@RequestParam Long id){
        try{
            return new ResponseEntity<>(this.actorRepository.findActorById(id), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>("there is no actor with this id!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<?> insert(@RequestBody Actor actor){
        try{
            this.actorRepository.save(actor);
            return new ResponseEntity<>(actor, HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(" Não foi possível inserir dado!", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@RequestBody Actor actor, @RequestParam Long id){
        try{
            Actor searchedActor = this.actorRepository.findActorById(id);
            actor.setId(searchedActor.getId());

            return new ResponseEntity<>(this.actorRepository.save(actor), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> delete (@RequestParam("id") Long id){
        if(this.actorRepository.existsById(id)){
            try{

                this.actorRepository.deleteActorsOcurrencies(id);
                this.actorRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/deleteGambiarra")
    public boolean deleteAlternativo(@RequestParam Long id){
        Actor a = this.actorRepository.findActorById(id);

        for(Program p : a.getPrograms()){
            a.getPrograms().remove(p);
        }
        this.actorRepository.save(a);
        this.actorRepository.deleteById(id);
        return true;

    }




}
