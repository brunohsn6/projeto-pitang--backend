package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Author;
import com.br.projetoestagio.hubpitang.models.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Long> {

    public Author findAuthorByApiID(Long apiID);

    public Author findAuthorById(Long id);
    @Modifying
    @Transactional
    @Query(value = "delete from tb_program_authors pa where pa.aut_cl_id = ?1", nativeQuery = true)
    public void deleteAuthorsOcurrencies(Long id);
    public List<Author> findAll(Specification<Author> predicate);

}

