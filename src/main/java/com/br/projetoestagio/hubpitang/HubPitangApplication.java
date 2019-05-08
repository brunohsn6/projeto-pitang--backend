package com.br.projetoestagio.hubpitang;

import com.br.projetoestagio.hubpitang.utils.Initialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HubPitangApplication implements CommandLineRunner {

    @Autowired
    private Initialization initialization;

    public static void main(String[] args) {

        SpringApplication.run(HubPitangApplication.class, args);

    }


    @Override
    public void run(String... args) throws Exception {
//        this.initialization.parseGenres();
//        this.initialization.parseTvshows();
//        this.initialization.parseMovies();
//        this.initialization.parseMoviePersons();
//        this.initialization.ParsePersonTvshows();
    }
}
