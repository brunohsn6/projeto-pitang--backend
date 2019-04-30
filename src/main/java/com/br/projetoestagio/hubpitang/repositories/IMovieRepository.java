package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, Long> {
    public Movie findMovieById(Long id);
    public boolean findMovieByTitle(String title);
}
