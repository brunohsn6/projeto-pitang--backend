package com.br.projetoestagio.hubpitang.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_actor")
@Getter @Setter
public class Actor extends Person{

    @JsonIgnore
    @ManyToMany(targetEntity = Program.class, mappedBy = "actors",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Program> programs;


    public Actor() {
    }
}
