package com.dubois.playlist_service.services;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.dubois.playlist_service.dtos.PlaylistDTO;
import com.dubois.playlist_service.dtos.PlaylistDTOWrapper;

import jakarta.persistence.EntityNotFoundException;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class PlaylistServiceTest {

    @Autowired
    PlaylistService service;

    @Test
    void createPlaylist() {
        // given:
        PlaylistDTO playlist = new PlaylistDTO("teste", null, null);

        // when:
        try {
            service.createPlaylist(playlist);
        } catch (Exception e) {
            Assertions.fail("Falha ao criar playlist: " + e.getMessage());
        }

        // then:
        PlaylistDTOWrapper playlistsPersisted = service.findAll();
        assertEquals(1, playlistsPersisted.playlists().size(), "A quantidade de playlists no banco não é a esperada.");
        // and:
        PlaylistDTO playlistCriada = playlistsPersisted.playlists().get(0);
        assertEquals("teste", playlistCriada.nome(), "O nome da playlist criada não é o esperado.");
    }

    @Test
    void createPlaylist$ExceptionWhenPlaylistDTOIsNull() {
        // given:
        PlaylistDTO playlist = null;
        String message = null;

        // when:
        try {
            service.createPlaylist(playlist);
        } catch (BadRequestException e) {
            message = e.getMessage();
        } catch (Exception e) {
            Assertions.fail("Unexpected Exception: " + e.getMessage());
        }

        // then:
        assertEquals("It is necessary to provide a body for this request", message);
    }

    @Test
    void createPlaylist$ExceptionWhenNomeIsNull() {
        // given:
        PlaylistDTO playlist = new PlaylistDTO(null, null, null);
        String message = null;

        // when:
        try {
            service.createPlaylist(playlist);
        } catch (BadRequestException e) {
            message = e.getMessage();
        } catch (Exception e) {
            Assertions.fail("Unexpected Exception: " + e.getMessage());
        }

        // then:
        assertEquals("It is necessary to provide a name for the playlist", message);
    }

    @Test
    void createPlaylist$ExceptionWhenNomeIsRepeated() {
        // given:
        PlaylistDTO playlist1 = new PlaylistDTO("teste", null, null);
        // and:
        PlaylistDTO playlist2 = new PlaylistDTO("teste", null, null);
        String message = null;

        // when:
        try {
            service.createPlaylist(playlist1);
            service.createPlaylist(playlist2);
        } catch (BadRequestException e) {
            message = e.getMessage();
        } catch (Exception e) {
            Assertions.fail("Unexpected Exception: " + e.getMessage());
        }

        // then:
        assertEquals("The name of this playlist has already been used", message);
    }

    @Test
    void findAll() {
        // given:
        PlaylistDTO playlist1 = new PlaylistDTO("teste", null, null);
        // and:
        PlaylistDTO playlist2 = new PlaylistDTO("teste2", null, null);
        // and:
        PlaylistDTO playlist3 = new PlaylistDTO("teste3", null, null);
        // and:
        try {
            service.createPlaylist(playlist1);
            service.createPlaylist(playlist2);
            service.createPlaylist(playlist3);
        } catch (Exception e) {
            Assertions.fail("Falha ao criar playlist: " + e.getMessage());
        }

        // when:
        PlaylistDTOWrapper playlistsPersisted = service.findAll();

        // then:
        assertEquals(3, playlistsPersisted.playlists().size(), "A quantidade de playlists no banco não é a esperada.");
    }

    @Test
    void delete() {
        // given:
        PlaylistDTO playlist1 = new PlaylistDTO("teste", null, null);
        // and:
        PlaylistDTO playlist2 = new PlaylistDTO("teste2", null, null);
        // and:
        PlaylistDTO playlist3 = new PlaylistDTO("teste3", null, null);
        // and:
        try {
            service.createPlaylist(playlist1);
            service.createPlaylist(playlist2);
            service.createPlaylist(playlist3);
        } catch (Exception e) {
            Assertions.fail("Falha ao criar playlist: " + e.getMessage());
        }

        // when:
        service.delete(playlist2.nome());

        // then:
        PlaylistDTOWrapper playlistsPersisted = service.findAll();
        assertEquals(2, playlistsPersisted.playlists().size(), "A quantidade de playlists no banco não é a esperada.");
    }

    @Test
    void delete$ExceptionWhenPlaylistDoesNotExist() {
        // given:
        String NonExistingPlaylistName = "teste";
        String message = null;

        // when:
        try {
            service.delete(NonExistingPlaylistName);
        } catch (EntityNotFoundException e) {
            message = e.getMessage();
        } catch (Exception e) {
            Assertions.fail("Unexpected Exception: " + e.getMessage());
        }

        // then:
        assertEquals("Playlist not found", message);
    }

}
