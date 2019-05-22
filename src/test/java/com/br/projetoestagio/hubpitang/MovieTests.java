package com.br.projetoestagio.hubpitang;

import com.br.projetoestagio.hubpitang.models.Movie;
import com.br.projetoestagio.hubpitang.repositories.IMovieRepository;
import com.br.projetoestagio.hubpitang.utils.MovieSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MovieTests {

    @MockBean
    private IMovieRepository movieRepository;

    @Autowired
    private MockMvc movieMockMvc;

    private Movie movie1;

    private Movie movie2;

    /*
    Before()
        on this method, all the variables and objects that will be used as data, to the mocks are inicializaded

    get_All_with_success()
        on this method, the get method is tested, that should return a list of movies, with the two objects that
        were created on the before method.
        note** the verify method is defined to guarantee that the program will only execute that quantity of time
        **
        note** the when method is defined to pass through the return of the method and answer the way the test
        expects
        **
    get_by_id_test()
        on this method, the program should return the object specified before, just like the test before, with a
        spefified object, but in that case, it should return only an object instead of a collection of it
    get_filtering_by_specification()

    delete_bt_id_test()
        on this method, is tested the delete operation. firstly, with the when method, we bypass the return of
        this method, so the fake object can be valid, and then the delete method is executed. Note that the delete
        doesn't have a defined return because it should be a void funcion and it must not return anything instead of
        the status of the operation.
    * */

    @Before
    public void before(){
        movie1 = new Movie();
        movie1.setId(new Long(20));
        movie1.setTitle("Aladdin");

        movie2 = new Movie();
        movie2.setId(new Long(30));
        movie2.setTitle("harry potter");

    }


    @Test
    public void get_all_with_success() throws Exception{
        List<Movie> movies = Arrays.asList(movie1, movie2);

        when(movieRepository.findAll()).thenReturn(movies);

        movieMockMvc.perform(get("/movies/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(movie1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(movie1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value((movie2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(movie2.getTitle()));
        verify(movieRepository, times(1)).findAll();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void get_by_id_test() throws Exception{
        when(movieRepository.findMovieById(movie1.getId())).thenReturn(movie1);

        movieMockMvc.perform(get("/movies/getById").param("id", movie1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(movie1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(movie1.getTitle()));
        verify(movieRepository, times(1)).findMovieById(movie1.getId());
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void get_filtering_by_specification() throws Exception{
        List<Movie> movies = Arrays.asList(movie1, movie2);

        when(movieRepository.findAll(ArgumentMatchers.isA(Specification.class))).thenReturn(movies);

        movieMockMvc.perform(get("/movies/getByFiltering")
                .param("title", movie1.getTitle())
                .param("year", movie1.getRelease_year())
                .param("language", movie1.getLanguage()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(movie1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(movie1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value((movie2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(movie2.getTitle()));
        verify(movieRepository, times(1)).findAll(ArgumentMatchers.isA(Specification.class));
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    public void delete_by_id_test() throws Exception{
        when(movieRepository.existsById(movie1.getId())).thenReturn(true);
        movieMockMvc.perform(delete("/movies/{id}", movie1.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        verify(movieRepository, times(1)).deleteById(movie1.getId());
        verify(movieRepository, times(1)).existsById(movie1.getId());
        verifyNoMoreInteractions(movieRepository);
    }



    @After
    public void after(){
        movie1 = null;
        movie2 = null;
    }

}
