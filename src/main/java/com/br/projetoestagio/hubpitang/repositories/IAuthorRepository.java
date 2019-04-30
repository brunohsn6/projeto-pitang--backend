package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Long> {

    public Author findAuthorById(Long id);
}
