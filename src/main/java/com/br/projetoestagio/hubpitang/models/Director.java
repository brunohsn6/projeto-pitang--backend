package com.br.projetoestagio.hubpitang.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToMany(targetEntity = Program.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "tb_program_directors",
            joinColumns = {@JoinColumn(name = "dir_cl_id")},
            inverseJoinColumns = {@JoinColumn(name = "prog_cl_id")})
    private List<Program> programs;


    public Director() {
    }
}
