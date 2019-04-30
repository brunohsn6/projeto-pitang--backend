package com.br.projetoestagio.hubpitang.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToMany(targetEntity = Program.class, mappedBy = "genres")
    private Set<Program> programs;

    public Genre() {

    }


}
