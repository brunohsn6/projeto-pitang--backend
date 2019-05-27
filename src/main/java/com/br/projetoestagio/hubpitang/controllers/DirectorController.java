package com.br.projetoestagio.hubpitang.controllers;

import com.br.projetoestagio.hubpitang.error.ResourceNotFoundException;
import com.br.projetoestagio.hubpitang.models.Actor;
import com.br.projetoestagio.hubpitang.models.Director;
import com.br.projetoestagio.hubpitang.repositories.IActorRepository;
import com.br.projetoestagio.hubpitang.repositories.IDirectorRepository;
import com.br.projetoestagio.hubpitang.utils.ActorSpecification;
import com.br.projetoestagio.hubpitang.utils.DirectorSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("directors")
@CrossOrigin(origins = "http://localhost:4200")
public class DirectorController {
    @Autowired
    private IDirectorRepository directorRepository;

    @GetMapping(path = "/")
    public ResponseEntity<?> getAll(){
        try{
            return new ResponseEntity<>(this.directorRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@RequestParam Long id){
        try{
            return new ResponseEntity<>(this.directorRepository.findDirectorById(id), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            throw new ResourceNotFoundException("there is no director with this id!");        }
    }

    @GetMapping(path = "/getByFiltering")
    public ResponseEntity<?> filterDirectors(@RequestParam(required = false) String name){
        try{
            List<Director> filteredList= this.directorRepository.findAll(DirectorSpecification.searchDirector(name));
            return new ResponseEntity<>(filteredList, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            System.out.println(e.getLocalizedMessage());
            throw new ResourceNotFoundException("there is no director with this specified name!");
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<?> insert(@RequestBody Director director){
        try{
            return new ResponseEntity<>(this.directorRepository.save(director), HttpStatus.CREATED);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@RequestBody Director director, @RequestParam Long id){
        try{
            Director searchedDirector = this.directorRepository.findDirectorById(id);
            director.setId(searchedDirector.getId());

            return new ResponseEntity<>(this.directorRepository.save(director), HttpStatus.ACCEPTED);
        }catch (Exception e){
            throw new ResourceNotFoundException("This object has an invalid bind!");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        if(this.directorRepository.existsById(id)){
            try{
                this.directorRepository.deleteDirectorsOcurrencies(id);
                this.directorRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
