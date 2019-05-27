package com.br.projetoestagio.hubpitang;

import com.br.projetoestagio.hubpitang.models.Tvshow;
import com.br.projetoestagio.hubpitang.repositories.ITvshowRepository;
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
public class TvshowTests {

    @MockBean
    private ITvshowRepository tvshowRepository;

    @Autowired
    private MockMvc tvshowMockMvc;

    private Tvshow tvshow1;

    private Tvshow tvshow2;

    /*
    Before()
        on this method, all the variables and objects that will be used as data, to the mocks are inicializaded

    get_All_with_success()
        on this method, the get method is tested, that should return a list of tvshows, with the two objects that
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

    get_filtering_by_specification()
        on this method, the program should return a list of objects filtered by the specification strategy. its resolved
        using the specification construction and passing it through parameter to the findAll method.

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
        tvshow1 = new Tvshow();
        tvshow1.setId(new Long(20));
        tvshow1.setTitle("Friends");
        tvshow1.setDuration("100");
        tvshow1.setCountryOrigin("usa");
        tvshow1.setDescription("test");
        tvshow1.setActors(null);
        tvshow1.setAuthors(null);
        tvshow1.setDirectors(null);
        tvshow1.setGenres(null);

        tvshow2 = new Tvshow();
        tvshow2.setId(new Long(30));
        tvshow2.setTitle("How I Met Your Mother");

    }


    @Test
    public void get_all_with_success() throws Exception{
        List<Tvshow> tvshows = Arrays.asList(tvshow1, tvshow2);

        when(tvshowRepository.findAll()).thenReturn(tvshows);

        tvshowMockMvc.perform(get("/tvshows/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(tvshow1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(tvshow1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value((tvshow2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(tvshow2.getTitle()));
        verify(tvshowRepository, times(1)).findAll();
        verifyNoMoreInteractions(tvshowRepository);
    }

    @Test
    public void getWithAValidId() throws Exception{
        when(tvshowRepository.findTvshowById(tvshow1.getId())).thenReturn(tvshow1);

        tvshowMockMvc.perform(get("/tvshows/getById").param("id", tvshow1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(tvshow1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(tvshow1.getTitle()));
        verify(tvshowRepository, times(1)).findTvshowById(tvshow1.getId());
        verifyNoMoreInteractions(tvshowRepository);
    }

    @Test
    public void getWithAnInvalidId() throws Exception{
        when(tvshowRepository.findTvshowById(tvshow1.getId())).thenReturn(null);

        tvshowMockMvc.perform(get("/tvshows/getById").param("id", tvshow1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        verify(tvshowRepository, times(1)).findTvshowById(tvshow1.getId());
        verifyNoMoreInteractions(tvshowRepository);
    }

    @Test
    public void get_filtering_by_specification() throws Exception{
        List<Tvshow> tvshows = Arrays.asList(tvshow1, tvshow2);

        when(tvshowRepository.findAll(ArgumentMatchers.isA(Specification.class))).thenReturn(tvshows);

        tvshowMockMvc.perform(get("/tvshows/getByFiltering")
                .param("title", tvshow1.getTitle())
                .param("year", tvshow1.getRelease_year())
                .param("language", tvshow1.getLanguage()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(tvshow1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(tvshow1.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value((tvshow2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(tvshow2.getTitle()));
        verify(tvshowRepository, times(1)).findAll(ArgumentMatchers.isA(Specification.class));
        verifyNoMoreInteractions(tvshowRepository);
    }

    @Test
    public void deleteAValidId() throws Exception{
        when(tvshowRepository.existsById(tvshow1.getId())).thenReturn(true);
        tvshowMockMvc.perform(delete("/tvshows/{id}", tvshow1.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        verify(tvshowRepository, times(1)).deleteById(tvshow1.getId());
        verify(tvshowRepository, times(1)).existsById(tvshow1.getId());
        verifyNoMoreInteractions(tvshowRepository);
    }

    @Test
    public void deleteAnInvalidId() throws Exception{
        when(tvshowRepository.existsById(tvshow1.getId())).thenReturn(false);
        tvshowMockMvc.perform(delete("/tvshows/{id}", tvshow1.getId()))
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(tvshowRepository, times(0)).deleteById(tvshow1.getId());
        verify(tvshowRepository, times(1)).existsById(tvshow1.getId());
        verifyNoMoreInteractions(tvshowRepository);
    }



    @After
    public void after(){
        tvshow1 = null;
        tvshow2 = null;
    }

}
