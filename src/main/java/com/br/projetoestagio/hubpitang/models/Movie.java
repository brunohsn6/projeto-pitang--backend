package com.br.projetoestagio.hubpitang.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_Movie")
@Getter @Setter
public class Movie extends Program{



    public Movie() {
    }
}
