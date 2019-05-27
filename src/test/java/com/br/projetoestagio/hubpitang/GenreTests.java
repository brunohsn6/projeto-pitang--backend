package com.br.projetoestagio.hubpitang;

import com.br.projetoestagio.hubpitang.models.Genre;
import com.br.projetoestagio.hubpitang.models.Tvshow;
import com.br.projetoestagio.hubpitang.repositories.IGenreRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GenreTests {

    @MockBean
    private IGenreRepository genreRepository;

    @Autowired
    private MockMvc genreMockMvc;

    private Genre genre1;

    private Genre genre2;

    /*
    Before()
        on this method, all the variables and objects that will be used as data, to the mocks are inicializaded

    get_All_with_success()
        on this method, the get method is tested, that should return a list of genres, with the two objects that
        were created on the before method.
        note** the verify method is defined to guarantee that the program will only execute that quantity of time
        **
        note** the when method is defined to pass through the return of the method and answer the way the test
        expects
        **
    getWithAValidId()
        on this method, the program should return the object specified before, just like the test before, with a
        spefified object, but in that case, it should return only an object instead of a collection of it.
        **note: The opposite should happend with the antagonist method getWithAnInvalidId()


    deleteAValidId()
        on this method, is tested the delete operation. firstly, with the when method, we bypass the return of
        this method, so the fake object that can be valid, and then the delete method is executed.
        Note that the delete doesn't have a defined return because it should be a void funcion and it must not return
        anything instead of the status of the operation.
        **note: The opposite, no delections to execute because of the invalid id, should happend with the
        antagonist method deleteAnInvalidId()

    * */

    @Before
    public void before(){
        genre1 = new Genre();
        genre1.setId(new Long(20));
        genre1.setName("Action");

        genre2 = new Genre();
        genre2.setId(new Long(30));
        genre2.setName("Horror");

    }


    @Test
    public void get_all_with_success() throws Exception{
        List<Genre> genres = Arrays.asList(genre1, genre2);

        when(genreRepository.findAll()).thenReturn(genres);

        genreMockMvc.perform(get("/genres/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(genre1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(genre1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value((genre2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(genre2.getName()));
        verify(genreRepository, times(1)).findAll();
        verifyNoMoreInteractions(genreRepository);
    }

    @Test
    public void getWithAValidId() throws Exception{
        when(genreRepository.findGenreById(genre1.getId())).thenReturn(genre1);

        genreMockMvc.perform(get("/genres/getById").param("id", genre1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(genre1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(genre1.getName()));
        verify(genreRepository, times(1)).findGenreById(genre1.getId());
        verifyNoMoreInteractions(genreRepository);
    }

    @Test
    public void getWithAnInvalidId() throws Exception{
        when(genreRepository.findGenreById(genre1.getId())).thenReturn(null);

        genreMockMvc.perform(get("/genres/getById").param("id", genre1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        verify(genreRepository, times(1)).findGenreById(genre1.getId());
        verifyNoMoreInteractions(genreRepository);
    }


    @Test
    public void deleteAValidId() throws Exception{
        when(genreRepository.existsById(genre1.getId())).thenReturn(true);
        genreMockMvc.perform(delete("/genres/{id}", genre1.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        verify(genreRepository, times(1)).deleteById(genre1.getId());
        verify(genreRepository, times(1)).existsById(genre1.getId());
    }

    @Test
    public void deleteAnInvalidId() throws Exception{
        when(genreRepository.existsById(genre1.getId())).thenReturn(false);
        genreMockMvc.perform(delete("/genres/{id}", genre1.getId()))
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(genreRepository, times(0)).deleteById(genre1.getId());
        verify(genreRepository, times(1)).existsById(genre1.getId());
    }



    @After
    public void after(){
        genre1 = null;
        genre2 = null;
    }

}
