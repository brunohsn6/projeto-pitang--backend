package com.br.projetoestagio.hubpitang.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_tvshow")
@Getter
@Setter
public class Tvshow extends Program {


    @Column(name = "cl_seasons")
    private int seasons;

    public Tvshow() {
    }
}


