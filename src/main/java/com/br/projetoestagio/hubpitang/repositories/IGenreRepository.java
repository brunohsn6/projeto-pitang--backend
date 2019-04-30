package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGenreRepository extends JpaRepository<Genre, Long> {

    public Genre findGenreById(Long id);
}
