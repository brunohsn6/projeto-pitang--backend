package com.br.projetoestagio.hubpitang.controllers;


import com.br.projetoestagio.hubpitang.models.Genre;
import com.br.projetoestagio.hubpitang.repositories.IGenreRepository;
import com.br.projetoestagio.hubpitang.utils.Initialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("genres")
@CrossOrigin(origins = "http://localhost:4200")
public class GenreController {

    @Autowired
    private IGenreRepository genreRepository;

    @Autowired
    private Initialization initialization;

    @GetMapping(path = "/")
    public ResponseEntity<?> getAll(){
        try{
            return new ResponseEntity<>(this.genreRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@RequestParam Long id){
        try{
            return new ResponseEntity<>(this.genreRepository.findById(id), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<?> insert(@RequestBody Genre genre){
        try{
            return new ResponseEntity<>(this.genreRepository.save(genre), HttpStatus.CREATED);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@RequestBody Genre genre, @RequestParam Long id){
        try{
            Genre searchedGenre = this.genreRepository.findGenreById(id);
            genre.setId(searchedGenre.getId());

            return new ResponseEntity<>(this.genreRepository.save(genre), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        if(this.genreRepository.existsById(id)){
            try{
                this.genreRepository.deleteGenre(id);
                this.genreRepository.deleteById(id);
//                this.genreRepository.deleteGenreById(id);

                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }




}
