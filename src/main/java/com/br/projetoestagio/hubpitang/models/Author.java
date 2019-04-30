package com.br.projetoestagio.hubpitang.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "tb_author")
@Getter @Setter
public class Author extends Person {

    @JsonIgnore
    @ManyToMany(targetEntity = Program.class, mappedBy = "authors")
    private List<Program> programs;

    public Author() {
    }

}
