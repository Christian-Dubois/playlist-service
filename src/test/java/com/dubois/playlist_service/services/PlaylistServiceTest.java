package com.dubois.playlist_service.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.dubois.playlist_service.dtos.MusicDTO;
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
        List<MusicDTO> musics = Arrays.asList(
                new MusicDTO("Titulo 1", "Artista 1", "Album 1", "Ano 1", "Genero 1"),
                new MusicDTO("Titulo 2", "Artista 2", "Album 2", "Ano 2", "Genero 2")
        );
        PlaylistDTO playlist = new PlaylistDTO("Playlist 1", "Description", musics);

        // when:
        try {
            service.createPlaylist(playlist);
        } catch (Exception e) {
            Assertions.fail("Unexpected Exception: " + e.getMessage());
        }

        // then:
        PlaylistDTOWrapper playlistsPersisted = service.findAll();
        assertEquals(1, playlistsPersisted.playlists().size(),
                "The number of playlists in the bank is not as expected.");

        // and:
        PlaylistDTO playlistPersisted = playlistsPersisted.playlists().get(0);
        assertEquals("Playlist 1", playlistPersisted.nome(),
                "The name of the playlist created is not what was expected.");

        // and:
        assertEquals("Description", playlistPersisted.descricao(),
                "The description of the playlist created is not what was expected.");

        // and:
        assertEquals(musics.size(), playlistPersisted.musicas().size(),
                "The number of musics in the playlist created is not what was expected.");

        // and:
        assertTrue(playlistPersisted.musicas().containsAll(musics) && musics.containsAll(playlistPersisted.musicas()),
                "The musics of the playlist created are not what was expected.");
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
        PlaylistDTO playlist1 = new PlaylistDTO("Playlist 1", null, null);
        // and:
        PlaylistDTO playlist2 = new PlaylistDTO("Playlist 2", null, null);
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
        PlaylistDTO playlist1 = new PlaylistDTO("Playlist 1", null, null);
        // and:
        PlaylistDTO playlist2 = new PlaylistDTO("Playlist 2", null, null);
        // and:
        PlaylistDTO playlist3 = new PlaylistDTO("Playlist 3", null, null);
        // and:
        try {
            service.createPlaylist(playlist1);
            service.createPlaylist(playlist2);
            service.createPlaylist(playlist3);
        } catch (Exception e) {
            Assertions.fail("Failed to create playlist: " + e.getMessage());
        }

        // when:
        PlaylistDTOWrapper playlistsPersisted = service.findAll();

        // then:
        assertEquals(3, playlistsPersisted.playlists().size(),
                "The number of playlists in the bank is not as expected.");
    }

    @Test
    void delete() {
        // given:
        PlaylistDTO playlist1 = new PlaylistDTO("Playlist 1", null, null);
        // and:
        PlaylistDTO playlist2 = new PlaylistDTO("Playlist 2", null, null);
        // and:
        PlaylistDTO playlist3 = new PlaylistDTO("Playlist 3", null, null);
        // and:
        try {
            service.createPlaylist(playlist1);
            service.createPlaylist(playlist2);
            service.createPlaylist(playlist3);
        } catch (Exception e) {
            Assertions.fail("Failed to create playlist: " + e.getMessage());
        }

        // when:
        service.deleteByNome(playlist2.nome());

        // then:
        PlaylistDTOWrapper playlistsPersisted = service.findAll();
        assertEquals(2, playlistsPersisted.playlists().size(),
                "The number of playlists in the bank is not as expected.");
        // and:
        assertTrue(playlistsPersisted.playlists().contains(playlist1),
                "Playlist 1 not found after deletion.");
        // and:
        assertTrue(playlistsPersisted.playlists().contains(playlist3),
                "Playlist 3 not found after deletion.");
    }

    @Test
    void delete$ExceptionWhenPlaylistDoesNotExist() {
        // given:
        String NonExistingPlaylistName = "Playlist 1";
        String message = null;

        // when:
        try {
            service.deleteByNome(NonExistingPlaylistName);
        } catch (EntityNotFoundException e) {
            message = e.getMessage();
        } catch (Exception e) {
            Assertions.fail("Unexpected Exception: " + e.getMessage());
        }

        // then:
        assertEquals("Playlist not found", message);
    }

}
