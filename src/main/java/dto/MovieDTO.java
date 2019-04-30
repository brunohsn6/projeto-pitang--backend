package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class MovieDTO {
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private ArrayList<Integer> genre_ids;
    private Integer id;
    private String original_title;
    private String title;
    private String backdrop_path;
    private Double popularity;
    private Integer vote_count;
    private boolean video;
    private Double vote_average;
    private String original_language;





}
