package com.br.projetoestagio.hubpitang.controllers;

import com.br.projetoestagio.hubpitang.models.Author;
import com.br.projetoestagio.hubpitang.repositories.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private IAuthorRepository authorRepository;

    @GetMapping(path = "/")
    public ResponseEntity<?> getAll(){
        try{
            return new ResponseEntity<>(this.authorRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@RequestParam Long id){
        try{
            return new ResponseEntity<>(this.authorRepository.findById(id), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<?> insert(@RequestBody Author author){
        try{
            return new ResponseEntity<>(this.authorRepository.save(author), HttpStatus.CREATED);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@RequestBody Author author, @RequestParam Long id){
        try{
            Author searchedAuthor = this.authorRepository.findAuthorById(id);
            author.setId(searchedAuthor.getId());

            return new ResponseEntity<>(this.authorRepository.save(author), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> delete (@RequestParam("id") Long id){
        if(this.authorRepository.existsById(id)){
            try{
                this.authorRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
