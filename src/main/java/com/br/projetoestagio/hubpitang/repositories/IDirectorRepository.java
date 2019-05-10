package com.br.projetoestagio.hubpitang.repositories;

import com.br.projetoestagio.hubpitang.models.Director;
import com.br.projetoestagio.hubpitang.models.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IDirectorRepository extends JpaRepository<Director, Long> {
    public Director findDirectorByApiID(Long id);
    public Director findDirectorById(Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from tb_program_directors pa where pa.dir_cl_id = ?1", nativeQuery = true)
    public void deleteDirectorsOcurrencies(Long id);
    public List<Director> findAll(Specification<Director> predicate);

}
