package com.dubois.playlist_service.converteres;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import com.dubois.playlist_service.dtos.MusicDTO;
import com.dubois.playlist_service.dtos.PlaylistDTO;
import com.dubois.playlist_service.models.Music;
import com.dubois.playlist_service.models.Playlist;


public class PlaylistConverterTest {

    @Mock
    private MusicConverter musicConverter;

    @InjectMocks
    private PlaylistConverter playlistConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testToEntity() {
        // given:
        List<MusicDTO> musicDTOs = List.of(new MusicDTO("Titulo 1", "Artista 1", "Album 1", "Ano 1", "Genero 1"));
        PlaylistDTO dto = new PlaylistDTO("Playlist 1", "Description", musicDTOs);

        // and:
        when(musicConverter.toEntity(any(MusicDTO.class))).thenReturn(
                new Music(null, "Titulo 1", "Artista 1", "Album 1", "Ano 1", "Genero 1"));

        // when:
        Playlist entity = playlistConverter.toEntity(dto);

        // then:
        Assertions.assertNotNull(entity, "Converted entity should not be null");
        // and:
        Assertions.assertEquals(dto.nome(), entity.getNome(), "Nome should match");
        // and:
        Assertions.assertEquals(dto.descricao(), entity.getDescricao(), "Descricao should match");
        // and:
        List<Music> expectedMusicEntities =
                List.of(new Music(null, "Titulo 1", "Artista 1", "Album 1", "Ano 1", "Genero 1"));
        Assertions.assertEquals(expectedMusicEntities.size(), entity.getMusicas().size(),
                "Number of musicas in entity should match");
        Assertions.assertTrue(entity.getMusicas().containsAll(expectedMusicEntities), "Musicas in entity should match");
    }

    @Test
    void testToDTO() {
        // given:
        List<Music> musicEntities = List.of(new Music(null, "Titulo 1", "Artista 1", "Album 1", "Ano 1", "Genero 1"));
        Playlist entity = new Playlist(null, "Playlist 1", "Description", musicEntities);

        // and:
        when(musicConverter.toDTO(any(Music.class))).thenReturn(
                new MusicDTO("Titulo 1", "Artista 1", "Album 1", "Ano 1", "Genero 1"));

        // when:
        PlaylistDTO dto = playlistConverter.toDTO(entity);

        // then:
        Assertions.assertNotNull(dto, "Converted DTO should not be null");
        // and:
        Assertions.assertEquals(entity.getNome(), dto.nome(), "Nome should match");
        // and:
        Assertions.assertEquals(entity.getDescricao(), dto.descricao(), "Descricao should match");
        // and:
        List<MusicDTO> expectedMusicDTOs =
                List.of(new MusicDTO("Titulo 1", "Artista 1", "Album 1", "Ano 1", "Genero 1"));
        Assertions.assertEquals(expectedMusicDTOs.size(), dto.musicas().size(),
                "Number of musicas in DTO should match");
        Assertions.assertTrue(dto.musicas().containsAll(expectedMusicDTOs), "Musicas in DTO should match");
    }
}
