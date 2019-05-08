package com.br.projetoestagio.hubpitang.controllers;

import com.br.projetoestagio.hubpitang.models.Director;
import com.br.projetoestagio.hubpitang.repositories.IActorRepository;
import com.br.projetoestagio.hubpitang.repositories.IDirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return new ResponseEntity<>(this.directorRepository.findById(id), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> delete (@RequestParam("id") Long id){
        if(this.directorRepository.existsById(id)){
            try{
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
