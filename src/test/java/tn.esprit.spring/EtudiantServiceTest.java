package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.dao.entities.Etudiant;
import tn.esprit.spring.dao.repositories.EtudiantRepository;
import tn.esprit.spring.services.etudiant.EtudiantService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EtudiantServiceTest {

    @InjectMocks
    private EtudiantService etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    private Etudiant etudiant;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("Med")
                .prenomEt("Med")
                .cin(12345678L)
                .ecole("Esprit")
                .dateNaissance(LocalDate.of(2000, 1, 4))
                .build();
    }

    @Test
    void testAddOrUpdateEtudiant() {

        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);

        assertNotNull(savedEtudiant);
        assertEquals("Med", savedEtudiant.getNomEt());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testFindAllEtudiants() {

        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.findAll();

        assertEquals(1, result.size());
        assertEquals("Med", result.get(0).getNomEt());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testFindEtudiantById() {

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant foundEtudiant = etudiantService.findById(1L);

        assertNotNull(foundEtudiant);
        assertEquals(1L, foundEtudiant.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void testFindEtudiantById_NotFound() {

        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> etudiantService.findById(1L));

        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteEtudiantById() {

        doNothing().when(etudiantRepository).deleteById(1L);

        etudiantService.deleteById(1L);

        verify(etudiantRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEtudiant() {

        doNothing().when(etudiantRepository).delete(etudiant);
        etudiantService.delete(etudiant);

        verify(etudiantRepository, times(1)).delete(etudiant);
    }
}
