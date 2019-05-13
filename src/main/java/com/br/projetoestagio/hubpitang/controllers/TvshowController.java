package com.br.projetoestagio.hubpitang.controllers;


import com.br.projetoestagio.hubpitang.models.Tvshow;
import com.br.projetoestagio.hubpitang.repositories.ITvshowRepository;
import com.br.projetoestagio.hubpitang.utils.Initialization;
import com.br.projetoestagio.hubpitang.utils.TvshowSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/tvshows")
@CrossOrigin(origins = "http://localhost:4200")
public class TvshowController {

    @Autowired
    private ITvshowRepository iTvshowRepository;

    @Autowired
    private Initialization initialization;

    @GetMapping(path = "/")
    public ResponseEntity<?> getAll(){
        try{
            return new ResponseEntity<>(this.iTvshowRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@RequestParam Long id){
        try{
            return new ResponseEntity<>(this.iTvshowRepository.findById(id), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/getByFiltering")
    public ResponseEntity<?> filterTvshows(@RequestParam(required = false) String title,
                                          @RequestParam(required = false) String year,
                                          @RequestParam(required = false) String language){
        try{
            List<Tvshow> filteredList= this.iTvshowRepository.findAll(TvshowSpecification.searchTvshow(title, year, language));
            return new ResponseEntity<>(filteredList, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            System.out.println(e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<?> insert(@RequestBody Tvshow tvshow){
        try{
            return new ResponseEntity<>(this.iTvshowRepository.save(tvshow), HttpStatus.CREATED);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> update(@RequestBody Tvshow tvshow, @RequestParam Long id){
        try{
            Tvshow searchedDirector = this.iTvshowRepository.findTvshowById(id);
            tvshow.setId(searchedDirector.getId());

            return new ResponseEntity<>(this.iTvshowRepository.save(tvshow), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        if(this.iTvshowRepository.existsById(id)){
            try{
                this.iTvshowRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
