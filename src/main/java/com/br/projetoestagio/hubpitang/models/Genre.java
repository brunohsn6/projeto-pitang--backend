package com.br.projetoestagio.hubpitang.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_genre")
@Getter @Setter
public class Genre {

    @Id
    private Long id;

    private String name;

    @JsonBackReference
    @ManyToMany(targetEntity = Program.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "tb_program_genres", joinColumns = {@JoinColumn(name = "gen_cl_id")},
            inverseJoinColumns = @JoinColumn(name = "prog_cl_id"))
    private Set<Program> programs;

    public Genre() {

    }


}
