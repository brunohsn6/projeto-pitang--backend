package dto;

import com.br.projetoestagio.hubpitang.models.Genre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
public class GenreDTO {

    private Integer id;
    private String name;
}
