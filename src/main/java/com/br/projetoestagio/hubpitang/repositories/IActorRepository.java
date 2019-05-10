package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Actor;
import com.br.projetoestagio.hubpitang.models.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IActorRepository extends JpaRepository<Actor, Long> {
    public Actor findActorByApiID(Long apiID);
    public Actor findActorById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from tb_program_actors pa where pa.act_cl_id = ?1", nativeQuery = true)
    public void deleteActorsOcurrencies(Long id);

    public List<Actor> findAll(Specification<Actor> predicate);


}
