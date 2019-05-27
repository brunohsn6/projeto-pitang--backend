package com.br.projetoestagio.hubpitang;

import com.br.projetoestagio.hubpitang.models.Actor;
import com.br.projetoestagio.hubpitang.models.Tvshow;
import com.br.projetoestagio.hubpitang.repositories.IActorRepository;
import com.br.projetoestagio.hubpitang.utils.Gender;
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
public class ActorTests {

    @MockBean
    private IActorRepository actorRepository;

    @Autowired
    private MockMvc actorMockMvc;

    private Actor actor1;

    private Actor actor2;

    /*
    Before()
        on this method, all the variables and objects that will be used as data, to the mocks are inicializaded

    get_All_with_success()
        on this method, the get method is tested, that should return a list of actors, with the two objects that
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
        actor1 = new Actor();
        actor1.setId(new Long(20));
        actor1.setName("Robert");
        actor1.setBorn_city("phillipines");
        actor1.setGender(Gender.MEN);
        actor1.setHeight("1.74");


        actor2 = new Actor();
        actor2.setId(new Long(30));
        actor2.setName("Chris");

    }


    @Test
    public void get_all_with_success() throws Exception{
        List<Actor> actors = Arrays.asList(actor1, actor2);

        when(actorRepository.findAll()).thenReturn(actors);

        actorMockMvc.perform(get("/actors/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(actor1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(actor1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value((actor2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(actor2.getName()));
        verify(actorRepository, times(1)).findAll();
        verifyNoMoreInteractions(actorRepository);
    }

    @Test
    public void getWithAValidId() throws Exception{
        when(actorRepository.findActorById(actor1.getId())).thenReturn(actor1);

        actorMockMvc.perform(get("/actors/getById").param("id", actor1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(actor1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(actor1.getName()));
        verify(actorRepository, times(1)).findActorById(actor1.getId());
        verifyNoMoreInteractions(actorRepository);
    }

    @Test
    public void getWithAnInvalidId() throws Exception{
        when(actorRepository.findActorById(actor1.getId())).thenReturn(null);

        actorMockMvc.perform(get("/actors/getById").param("id", actor1.getId().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        verify(actorRepository, times(1)).findActorById(actor1.getId());
        verifyNoMoreInteractions(actorRepository);
    }

    @Test
    public void get_filtering_by_specification() throws Exception{
        List<Actor> actors = Arrays.asList(actor1, actor2);

        when(actorRepository.findAll(ArgumentMatchers.isA(Specification.class))).thenReturn(actors);

        actorMockMvc.perform(get("/actors/getByFiltering")
                .param("name", actor1.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(actor1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(actor1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value((actor2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(actor2.getName()));
        verify(actorRepository, times(1)).findAll(ArgumentMatchers.isA(Specification.class));
        verifyNoMoreInteractions(actorRepository);
    }

    @Test
    public void deleteAValidId() throws Exception{
        when(actorRepository.existsById(actor1.getId())).thenReturn(true);
        actorMockMvc.perform(delete("/actors/{id}", actor1.getId()))
                .andExpect(status().isOk())
                .andDo(print());
        verify(actorRepository, times(1)).deleteById(actor1.getId());
        verify(actorRepository, times(1)).existsById(actor1.getId());
    }

    @Test
    public void deleteAnInvalidId() throws Exception{
        when(actorRepository.existsById(actor1.getId())).thenReturn(false);
        actorMockMvc.perform(delete("/actors/{id}", actor1.getId()))
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(actorRepository, times(0)).deleteById(actor1.getId());
        verify(actorRepository, times(1)).existsById(actor1.getId());
    }



    @After
    public void after(){
        actor1 = null;
        actor2 = null;
    }

}