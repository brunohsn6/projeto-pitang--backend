package com.br.projetoestagio.hubpitang.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "tb_author")
@Getter @Setter
public class Author extends Person {

    @JsonBackReference
    @ManyToMany(targetEntity = Program.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "tb_program_authors",
            joinColumns = {@JoinColumn(name = "aut_cl_id")},
            inverseJoinColumns = {@JoinColumn(name = "prog_cl_id")})
    private List<Program> programs;

    public Author() {
    }

}
