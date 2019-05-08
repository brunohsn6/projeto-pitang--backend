package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IGenreRepository extends JpaRepository<Genre, Long> {
    public void deleteGenreById(Long id);
    public Genre findGenreById(Long id);

    @Query(value = "delete from program_genres where genres_id = :id", nativeQuery = true)
    public void deleteGenre(@Param("id") Long id);
}
