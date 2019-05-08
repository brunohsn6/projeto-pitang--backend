package com.br.projetoestagio.hubpitang.models;

import com.br.projetoestagio.hubpitang.utils.IObjectPersistent;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "cl_backdrop_path")
    private String backdropPath;

    @JsonManagedReference
    @ManyToMany(mappedBy = "programs", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Set<Genre> genres;

    @JsonManagedReference
    @ManyToMany(mappedBy = "programs",cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Actor>actors;

    @JsonManagedReference
    @ManyToMany(mappedBy = "programs", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Author>authors;

    @JsonManagedReference
    @ManyToMany(mappedBy = "programs", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Director>directors;


    public Program() {
    }
}
