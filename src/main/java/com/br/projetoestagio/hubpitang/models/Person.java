package com.br.projetoestagio.hubpitang.models;

import com.br.projetoestagio.hubpitang.utils.Gender;
import com.br.projetoestagio.hubpitang.utils.IObjectPersistent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy =                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           InheritanceType.TABLE_PER_CLASS)
public abstract class Person implements IObjectPersistent<Long> {

    @Id
    @Column(name = "cl_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cl_apiID")
    private Long apiID;

    @Column(name = "cl_name")
    private String name;

    @Column(name = "cl_height")
    private String height;

    @Column(name = "cl_born_city")
    private String born_city;

    @Column(name = "cl_living_country")
    private String living_country;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "cl_profile_path")
    private String profile_path;


    public Person() {
    }


}
