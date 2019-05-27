package com.br.projetoestagio.hubpitang.controllers;

import com.br.projetoestagio.hubpitang.error.ResourceNotFoundException;
import com.br.projetoestagio.hubpitang.models.Actor;
import com.br.projetoestagio.hubpitang.models.Movie;
import com.br.projetoestagio.hubpitang.models.Person;
import com.br.projetoestagio.hubpitang.models.Program;
import com.br.projetoestagio.hubpitang.repositories.IActorRepository;
import com.br.projetoestagio.hubpitang.utils.ActorSpecification;
import com.br.projetoestagio.hubpitang.utils.Gender;
import com.br.projetoestagio.hubpitang.utils.MovieSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.sql.PreparedStatement;
import java.util.List;
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
            throw new ResourceNotFoundException("there is no actor with this id!");
        }
    }

    @GetMapping(path = "/getByFiltering")
    public ResponseEntity<?> filterActors(@RequestParam(required = false) String name){
        try{

            List<Actor> filteredList= this.actorRepository.findAll(ActorSpecification.searchActor(name));
            return new ResponseEntity<>(filteredList, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            System.out.println(e.getLocalizedMessage());
            throw new ResourceNotFoundException("there is no actor with specified name!");
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

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> update(@RequestBody Actor actor, @PathVariable("id") Long id){
        try{
            Actor searchedActor = this.actorRepository.findActorById(id);
            actor.setId(searchedActor.getId());

            return new ResponseEntity<>(this.actorRepository.save(actor), HttpStatus.ACCEPTED);
        }catch (Exception e){
            throw new ResourceNotFoundException("The object has an invalid bind!");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
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


}
