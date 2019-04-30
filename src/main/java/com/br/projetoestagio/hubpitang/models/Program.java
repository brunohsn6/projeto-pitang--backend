package com.br.projetoestagio.hubpitang.models;

import com.br.projetoestagio.hubpitang.utils.IObjectPersistent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Program implements IObjectPersistent<Long> {

    @Id
    @Column(name = "cl_id")
    private Long id;

    @Column(name = "cl_title")
    private String title;

    @Column(name = "cl_description", length = 1300)
    private String description;

    //it could even be an entity or an enum of predeterminated countries around the world
    @Column(name = "cl_country_origin")
    private String countryOrigin;

    @Column(name = "cl_language")
    private String language;

    @Column(name = "cl_release_year")
    private String release_year;

    @Column(name = "cl_duration")
    private String duration;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tb_program_genres", joinColumns = {@JoinColumn(name = "prog_cl_id")},
            inverseJoinColumns = @JoinColumn(name = "gen_cl_id"))
    private Set<Genre> genres;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tb_program_actors",
            joinColumns = {@JoinColumn(name = "prog_cl_id")},
            inverseJoinColumns = {@JoinColumn(name = "act_cl_id")})
    private List<Actor>actors;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tb_program_authors",
            joinColumns = {@JoinColumn(name = "prog_cl_id")},
            inverseJoinColumns = {@JoinColumn(name = "aut_cl_id")})
    private List<Author>authors;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tb_program_directors",
            joinColumns = {@JoinColumn(name = "prog_cl_id")},
            inverseJoinColumns = {@JoinColumn(name = "dir_cl_id")})
    private List<Director>directors;


    public Program() {
    }
}
