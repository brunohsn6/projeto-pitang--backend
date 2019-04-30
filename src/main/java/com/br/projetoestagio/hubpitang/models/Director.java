package com.br.projetoestagio.hubpitang.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_director")
@Getter
@Setter
public class Director extends Person{

    @JsonIgnore
    @ManyToMany(targetEntity = Program.class, mappedBy = "directors")
    private List<Program> programs;


    public Director() {
    }
}
