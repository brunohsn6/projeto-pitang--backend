package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IActorRepository extends JpaRepository<Actor, Long> {
    public Actor findActorByApiID(Long apiID);
    public Actor findActorById(Long id);

    @Query(value = "delete from PROGRAM_ACTORS where ACTORS_CL_ID = :id", nativeQuery = true)
    public void deleteActorsOcurrencies(@Param("id") Long id);

//    @Query("delete from TB_PROGRAM_ACTORS where act_cl_id = :id")
//    public void deleteRelations(Long id);
}
