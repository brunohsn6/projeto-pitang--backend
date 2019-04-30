package com.br.projetoestagio.hubpitang.utils;

import dto.MovieDTO;
import com.br.projetoestagio.hubpitang.models.Genre;
import com.br.projetoestagio.hubpitang.models.Movie;
import com.br.projetoestagio.hubpitang.models.Value;
import com.br.projetoestagio.hubpitang.repositories.IGenreRepository;
import com.br.projetoestagio.hubpitang.repositories.IMovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.GenreValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;

@Component
public class Initialization {

    @Autowired
    private IMovieRepository movieRepository;
    @Autowired
    private IGenreRepository genreRepository;

    private Value valueMovies;

    private Value valueSeries;

    private Genre[] mappedGenres;

    private String apikey = "19d5e92e35aa51da053927bec781facf";

    private String language = "pt-BR";


    public Initialization() {
        try{

            RestTemplate restTemplate = new RestTemplate();

            String jsonMovies = restTemplate.getForObject("https://api.themoviedb.org/3/discover/movie?api_key="+apikey+"&language="+language, String.class);
            String jsonGenres = restTemplate.getForObject("https://api.themoviedb.org/3/genre/movie/list?api_key="+apikey+"&language="+language, String.class);
            String jsonTvshows = restTemplate.getForObject("https://api.themoviedb.org/3/discover/tv/?api_key="+apikey+"&language="+language, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            this.valueMovies = objectMapper.readValue(jsonMovies, Value.class);
            GenreValue genres = objectMapper.readValue(jsonGenres, GenreValue.class);
            this.mappedGenres = objectMapper.convertValue(genres.getGenres(), Genre[].class);
            this.valueSeries = objectMapper.readValue(jsonTvshows, Value.class);



        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    public void parseMovies() {
        for (MovieDTO movie : valueMovies.getResults()) {

            if(!this.movieRepository.existsById(new Long(movie.getId()))){
                Movie new_movie = new Movie();
                ArrayList<Genre> genres = new ArrayList<>();

                new_movie.setId(new Long(movie.getId()));
                new_movie.setTitle(movie.getOriginal_title());
                new_movie.setRelease_year(movie.getRelease_date());
                new_movie.setCountryOrigin("string");
                new_movie.setDescription(movie.getOverview());
                new_movie.setDuration("2hrs");
                new_movie.setLanguage(movie.getOriginal_language());
                for(Integer genre_id : movie.getGenre_ids()){
                    if(this.genreRepository.existsById(new Long(genre_id))){
                        genres.add(this.genreRepository.findGenreById(new Long(genre_id)));
                    }
                }
                new_movie.setGenres(new HashSet<>(genres));
                this.movieRepository.save(new_movie);
            }
        }
    }

    public void parseGenres(){
        for(Genre genre : this.mappedGenres){
            if(!this.genreRepository.existsById(genre.getId())){
                this.genreRepository.save(genre);
            }
        }

    }

    public void parseTvshows(){
        //implementar para as séries aqui!!
    }
}
