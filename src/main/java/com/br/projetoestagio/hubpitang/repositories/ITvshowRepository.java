package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Tvshow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITvshowRepository extends JpaRepository<Tvshow, Long> {
    public Tvshow findTvshowById(Long id);
}
