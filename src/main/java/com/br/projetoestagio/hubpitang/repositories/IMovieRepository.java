package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.Predicate;
import java.util.List;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, Long> {
    public Movie findMovieById(Long id);
    public boolean findMovieByTitle(String title);
    public List<Movie> findAll(Specification<Movie> predicate);
}
