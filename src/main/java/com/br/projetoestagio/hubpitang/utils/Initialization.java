package com.br.projetoestagio.hubpitang.utils;

import com.br.projetoestagio.hubpitang.models.*;
import com.br.projetoestagio.hubpitang.repositories.IActorRepository;
import com.br.projetoestagio.hubpitang.repositories.ITvshowRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import dto.MovieDTO;
import com.br.projetoestagio.hubpitang.repositories.IGenreRepository;
import com.br.projetoestagio.hubpitang.repositories.IMovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.GenreValue;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class Initialization {

    @Autowired
    private IMovieRepository movieRepository;
    @Autowired
    private IGenreRepository genreRepository;
    @Autowired
    private ITvshowRepository tvshowRepository;
    @Autowired
    private IActorRepository actorRepository;

    private Value valueMovies;

    private Value valueSeries;

    private ArrayList<Genre> mappedGenres;

    private String baseUrl = "https://api.themoviedb.org/3/";

    private String apikey = "19d5e92e35aa51da053927bec781facf";

    private String language = "pt-BR";

    private ArrayList<Tvshow> tvshows;

    JSONArray jsonArray;

    private String absolutePath = "/home/bruno/IdeaProjects/hub-pitang/src/main/resources/script/find-height-script.py ";

    private int qtd_of_requests = 0;


    public Initialization() {
        try{

            RestTemplate restTemplate = new RestTemplate();

            String jsonMovies = restTemplate.getForObject(baseUrl+"discover/movie?api_key="+apikey+"&language="+language, String.class);
            String jsonGenresMovies = restTemplate.getForObject(baseUrl+"genre/movie/list?api_key="+apikey+"&language="+language, String.class);
            String jsonGenresTvshows = restTemplate.getForObject(baseUrl+"genre/tv/list?api_key="+apikey+"&language="+language, String.class);
            String jsonTvshows = restTemplate.getForObject(baseUrl+"discover/tv?api_key="+apikey+"&language="+language, String.class);


            ObjectMapper objectMapper = new ObjectMapper();

            this.valueMovies = objectMapper.readValue(jsonMovies, Value.class);
            GenreValue genresMovies = objectMapper.readValue(jsonGenresMovies, GenreValue.class);

            this.mappedGenres = new ArrayList<>();
            this.mappedGenres = objectMapper.convertValue(genresMovies.getGenres(), new TypeReference<ArrayList<Genre>>() {});
            GenreValue genreTvshows = objectMapper.readValue(jsonGenresTvshows, GenreValue.class);
            this.mappedGenres.addAll(objectMapper.convertValue(genreTvshows.getGenres(), new TypeReference<ArrayList<Genre>>() {}));

            JSONObject jobject = new JSONObject(jsonTvshows);
            jsonArray = jobject.getJSONArray("results");
            Thread.sleep(1000);


        }catch (Exception e){
            System.out.println("An error ocurred at 'initialization constructor'");
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void parseMovies() {
        try{
            if(this.movieRepository.count() == 0){

                int id = 0, i = 0;
                ObjectMapper om = new ObjectMapper();
                RestTemplate rst = new RestTemplate();
                for (MovieDTO movie : valueMovies.getResults()) {

                    verify_requests();

                    if(!this.movieRepository.existsById(new Long(movie.getId()))){
                        Movie new_movie = new Movie();
                        ArrayList<Genre> genres = new ArrayList<>();

                        id = movie.getId();
                        String movieSerie = rst.getForObject(baseUrl+"movie/"+id+"?api_key="+apikey+"&language="+language, String.class);
                        JSONObject movieObject = new JSONObject(movieSerie);
                        System.out.println(movieSerie);
                        new_movie.setId(new Long(movie.getId()));
                        new_movie.setTitle(movie.getOriginal_title());
                        new_movie.setRelease_year(movie.getRelease_date());
                        new_movie.setDescription(movie.getOverview());
                        new_movie.setDuration(movieObject.getString("runtime"));

                        String origin_country = "";
                        if(!movieObject.getJSONArray("production_countries").isNull(0)){
                            origin_country = movieObject.getJSONArray("production_countries")
                                    .getJSONObject(0).getString("name");
                        }
                        else{
                            origin_country = movieObject.getJSONArray("production_companies")
                                    .getJSONObject(0).getString("origin_country");
                        }
                        new_movie.setCountryOrigin(origin_country);
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

        }catch (Exception e){
            System.out.println("An error ocurred at 'parseMovies'");
            System.out.println(e.getLocalizedMessage());
        }



    }

    public void parseGenres(){
        for(Genre genre : this.mappedGenres){
            if(!this.genreRepository.existsById(genre.getId())){
                this.genreRepository.save(genre);
            }
        }

    }

    public void parseTvshows() {
        try{

            ObjectMapper om = new ObjectMapper();
            RestTemplate rst = new RestTemplate();
            int id = 0;
            this.tvshows = new ArrayList<>();

            if(this.tvshowRepository.count() == 0){

                for(int i = 0; i < jsonArray.length(); i++){
                    verify_requests();
                    Tvshow tvshow = new Tvshow();
                    id = this.jsonArray.getJSONObject(i).getInt("id");
                    String jsonSerie = rst.getForObject(baseUrl+"tv/"+id+"?api_key="+apikey+"&language="+language, String.class);
                    JSONObject serieObject = new JSONObject(jsonSerie);
                    System.out.println(serieObject);

                    tvshow.setTitle(this.jsonArray.getJSONObject(i).getString("name"));
                    tvshow.setId(this.jsonArray.getJSONObject(i).getLong("id"));
                    tvshow.setDescription(this.jsonArray.getJSONObject(i).getString("overview"));
                    tvshow.setLanguage(this.jsonArray.getJSONObject(i).getString("original_language"));
                    tvshow.setRelease_year(this.jsonArray.getJSONObject(i).getString("first_air_date"));
                    String list = om.convertValue(this.jsonArray.getJSONObject(i)
                            .getJSONArray("origin_country").getString(0), String.class);
                    tvshow.setCountryOrigin(list);

                    ArrayList<Genre> genres = new ArrayList<>();
                    for(int j = 0; j < this.jsonArray.getJSONObject(i).getJSONArray("genre_ids").length(); j++){
                        if(this.genreRepository.existsById(this.jsonArray.getJSONObject(i).getJSONArray("genre_ids").getLong(j))){
                            genres.add(this.genreRepository.findGenreById(this.jsonArray.getJSONObject(i).getJSONArray("genre_ids").getLong(j)));
                        }

                    }
                    tvshow.setGenres(new HashSet<>(genres));

                    tvshow.setDuration(serieObject.getJSONArray("episode_run_time").getString(0));
                    tvshow.setSeasons(serieObject.getInt("number_of_seasons"));
                    System.out.println(jsonArray.getJSONObject(i));

                    this.tvshowRepository.save(tvshow);
                }
            }

        }catch (Exception e){
            System.out.println("An error ocurred at 'parseTvshow'");
            System.out.println(e.getLocalizedMessage());
        }

    }

    public void parsePersons(){
        try{
            Thread.sleep(1000);

            RestTemplate restTemplate = new RestTemplate();
            String jsonPersons;
            JSONObject personsObject;
            int id = 0;

            if(this.actorRepository.count() == 0){
                List<Movie> movies = this.movieRepository.findAll();

                for(Movie m : movies){
                    verify_requests();
                    String jsonPersonsMovies = restTemplate.getForObject(baseUrl+"/movie/"+m.getId()+"/credits?api_key="+apikey+"&language="+language, String.class);
                    ArrayList<Actor> actors = new ArrayList<>();
                    qtd_of_requests++;
                    JSONObject actorsObject = new JSONObject(jsonPersonsMovies);
                    JSONArray actorsArray = actorsObject.getJSONArray("cast");
                    System.out.println("********** "+m.getTitle()+" **********");

                    for(int i = 0; i < 5; i++){
                        id = actorsArray.getJSONObject(i).getInt("id");
                        verify_requests();
                        jsonPersons = restTemplate.getForObject(baseUrl+"/person/"+id+"?api_key="+apikey+"&language="+language, String.class);
                        qtd_of_requests++;
                        personsObject = new JSONObject(jsonPersons);
                        Actor actor;

                        actor = this.actorRepository.findActorByApiID(new Long(id));
                        if(actor == null){
                            actor = new Actor();

                            System.out.println(id);
                            actor.setApiID(new Long(id));

                            System.out.println(actorsArray.getJSONObject(i).getString("name"));
                            actor.setName(actorsArray.getJSONObject(i).getString("name"));

                            System.out.println(personsObject.getString("place_of_birth"));
                            actor.setBorn_city(personsObject.getString("place_of_birth"));

                            System.out.println(Gender.values()[personsObject.getInt("gender")]);
                            actor.setGender(Gender.values()[personsObject.getInt("gender")]);


                            String height = getHeight(actor.getName());
                            if(height.contains(",")){
                                actor.setHeight(height);
                            }

                            System.out.println(height);

                            System.out.println();
                        }

                        actors.add(actor);

                    }
                    Thread.sleep(1000);
                    m.setActors(actors);
                    this.movieRepository.save(m);

                }

            }



        }catch (Exception e){
            System.out.println("An error ocurred at 'parsePersons'");
            System.out.println(e.getLocalizedMessage());
        }

    }

    public void ParsePersonTvshows() throws InterruptedException, JSONException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        int id;
        String jsonPersons;
        JSONObject personsObject;

        for(Tvshow tvshow : this.tvshowRepository.findAll()){
            verify_requests();
            String jsonPersonsTvshows = restTemplate.getForObject(baseUrl+"/tv/"+tvshow.getId()+"/credits?api_key="+apikey+"&language="+language, String.class);
            ArrayList<Actor> actors = new ArrayList<>();
            qtd_of_requests++;
            JSONObject actorsObject = new JSONObject(jsonPersonsTvshows);
            JSONArray actorsArray = actorsObject.getJSONArray("cast");
            System.out.println("********** "+tvshow.getTitle()+" **********");
            int tam =5;
            if(actorsArray.length() < tam){
                tam = actorsArray.length();
            }
            for(int i = 0; i < tam; i++){
                id = actorsArray.getJSONObject(i).getInt("id");
                verify_requests();
                jsonPersons = restTemplate.getForObject(baseUrl+"/person/"+id+"?api_key="+apikey+"&language="+language, String.class);
                qtd_of_requests++;
                personsObject = new JSONObject(jsonPersons);
                Actor actor;

                actor = this.actorRepository.findActorByApiID(new Long(id));
                if(actor == null){
                    actor = new Actor();

                    System.out.println(id);
                    actor.setApiID(new Long(id));

                    System.out.println(actorsArray.getJSONObject(i).getString("name"));
                    actor.setName(actorsArray.getJSONObject(i).getString("name"));

                    System.out.println(personsObject.getString("place_of_birth"));
                    actor.setBorn_city(personsObject.getString("place_of_birth"));

                    System.out.println(Gender.values()[personsObject.getInt("gender")]);
                    actor.setGender(Gender.values()[personsObject.getInt("gender")]);

                    String height = getHeight(actor.getName());
                    if(height.contains(",")){
                        actor.setHeight(height);
                    }

                    System.out.println(height);


                    System.out.println();
                }

                actors.add(actor);

            }
            Thread.sleep(1000);
            tvshow.setActors(actors);
            this.tvshowRepository.save(tvshow);
        }
    }

    public void verify_requests() throws InterruptedException{
        if(qtd_of_requests == 4){
            qtd_of_requests = 0;
            Thread.sleep(500);
        }

    }

    public String getHeight(String name){

        try{
            String param;
            String command;
            param = name;
            command = "python "+ absolutePath + param;
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/bruno/IdeaProjects/hub-pitang/answer.txt"));
            String height = bufferedReader.readLine();
            bufferedReader.close();

            return height;
        }catch (Exception e){
            System.out.println("An error ocurred at 'getHeight'");
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();

        }
        return "";

    }
}

