package com.br.projetoestagio.hubpitang.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dto.MovieDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class Value {

    private Integer page;
    private ArrayList<MovieDTO> results;
    private Integer total_results;
    private Integer total_pages;

}
