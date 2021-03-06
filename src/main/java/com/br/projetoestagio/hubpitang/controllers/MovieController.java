package com.br.projetoestagio.hubpitang.controllers;


import com.br.projetoestagio.hubpitang.error.ResourceNotFoundException;
import com.br.projetoestagio.hubpitang.models.Movie;
import com.br.projetoestagio.hubpitang.repositories.IGenreRepository;
import com.br.projetoestagio.hubpitang.repositories.IMovieRepository;
import com.br.projetoestagio.hubpitang.utils.Initialization;
import com.br.projetoestagio.hubpitang.utils.MovieSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private Initialization initialization;

    @GetMapping(path = "/")
    public ResponseEntity<?> getAll(){
        try{
            return new ResponseEntity<>(this.movieRepository.findAll(), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getById")
    public ResponseEntity<?> getById(@RequestParam("id") Long id){
        try{
            return new ResponseEntity<>(this.movieRepository.findMovieById(id), HttpStatus.OK);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            throw new ResourceNotFoundException("there is no movie with this id!");
        }
    }

    @GetMapping(path = "/getByFiltering")
    public ResponseEntity<?> filterMovies(@RequestParam(required = false) String title,
                                          @RequestParam(required = false) String year,
                                          @RequestParam(required = false) String language){
        try{
            List<Movie> filteredList= this.movieRepository.findAll(MovieSpecification.searchMovie(title, year, language));
            return new ResponseEntity<>(filteredList, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
            System.out.println(e.getLocalizedMessage());
            throw new ResourceNotFoundException("there is no movie with those specified params!");
        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<?> insert(@RequestBody Movie movie){
        try{
            this.movieRepository.save(movie);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            Logger.getLogger(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> update(@RequestBody Movie movie, @PathVariable Long id){
        try{
            Movie searchedDirector = this.movieRepository.findMovieById(id);
            movie.setId(searchedDirector.getId());
            this.movieRepository.save(movie);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }catch (Exception e){
            throw new ResourceNotFoundException("the object has an invalid bind!");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        if(this.movieRepository.existsById(id)){
            try{
                this.movieRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(path = "/deleteById")
    public boolean deleteActor(@RequestParam Long movieId, @RequestParam Long actorId){
        Movie m = this.movieRepository.findMovieById(movieId);
        if(m != null){
            for(int i = 0; i < m.getActors().size(); i++){
                if(m.getActors().get(i).getId() == actorId){
                    m.getActors().remove(i);
                    break;
                }
            }
            this.movieRepository.save(m);
            return true;
        }

        return false;
    }
}
