package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @Mock
    PetRepository petRepository;

    @InjectMocks
    ClinicServiceImpl service;

    @Test
    void findPetTypes() {

        List<PetType> petTypes = new ArrayList<PetType>();

        PetType petType = new PetType();

        petTypes.add(petType);

        when(petRepository.findPetTypes()).thenReturn(petTypes);

        Collection<PetType> result = service.findPetTypes();

        assertThat(result.size()).isEqualTo(1);

        verify(petRepository).findPetTypes();

    }

}