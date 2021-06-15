package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations ={"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    ClinicService clinicService;

    @Autowired
    OwnerController ownerController;

    MockMvc mockMvc;



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();

    }

    @Test
    void processUpdateOwnerFormNotValid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit",1)
                .param("firstName","Juan")
                .param("lastName","Sesan")
                .param("city","Paso del rey"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner","address"))
                .andExpect(model().attributeHasFieldErrors("owner","telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void processUpdateOwnerFormValid() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit",1)
                .param("firstName","Juan")
                .param("lastName","Sesan")
                .param("address","Conj Pellico 1056")
                .param("city","Paso del rey")
                .param("telephone","1154871831"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"));

        verify(clinicService).saveOwner(any());

    }

    @Test
    void testNewOwnerPostValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("firstName","Juan")
                    .param("lastName","Sesan")
                    .param("address","Conj Pellico 1056")
                    .param("city","Paso del rey")
                    .param("telephone","1154871831"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testNewOwnerPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("firstName","Juan")
                    .param("lastName","Sesan")
                    .param("city","Paso del rey"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner","address"))
                .andExpect(model().attributeHasFieldErrors("owner","telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testWithOwners() throws Exception {

        List<Owner> ownersList = new ArrayList<>();

        Owner owner1 = new Owner();
        owner1.setLastName("Sesan");
        owner1.setId(1);
        ownersList.add(owner1);

        when(clinicService.findOwnerByLastName(anyString())).thenReturn(ownersList);
        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));

        Owner owner2 = new Owner();
        owner2.setLastName("Sesan");
        owner2.setId(2);
        ownersList.add(owner2);


        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("selections"))
                .andExpect(view().name("owners/ownersList"));


    }

    @Test
    void noLastName() throws Exception {
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                .param("lastName", "Dont Find Me"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void initCreationForm() throws Exception {
        String result = "owners/createOrUpdateOwnerForm";

        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name(result));

    }

}