package com.dubois.playlist_service.converteres;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.dubois.playlist_service.dtos.MusicDTO;
import com.dubois.playlist_service.models.Music;


class MusicConverterTest {

    private final MusicConverter converter = new MusicConverter();

    @Test
    void testToEntity() {
        // given:
        MusicDTO dto = new MusicDTO("Titulo", "Artista", "Album", "Ano", "Genero");

        // when:
        Music entity = converter.toEntity(dto);

        // then:
        assertNotNull(entity, "Converted entity should not be null");
        assertEquals(dto.titulo(), entity.getTitulo(), "Titulo should match");
        assertEquals(dto.artista(), entity.getArtista(), "Artista should match");
        assertEquals(dto.album(), entity.getAlbum(), "Album should match");
        assertEquals(dto.ano(), entity.getAno(), "Ano should match");
        assertEquals(dto.genero(), entity.getGenero(), "Genero should match");
    }

    @Test
    void testToDTO() {
        // given:
        Music entity = new Music();
        entity.setTitulo("Titulo");
        entity.setArtista("Artista");
        entity.setAlbum("Album");
        entity.setAno("Ano");
        entity.setGenero("Genero");

        // when:
        MusicDTO dto = converter.toDTO(entity);

        // then:
        assertNotNull(dto, "Converted DTO should not be null");
        assertEquals(entity.getTitulo(), dto.titulo(), "Titulo should match");
        assertEquals(entity.getArtista(), dto.artista(), "Artista should match");
        assertEquals(entity.getAlbum(), dto.album(), "Album should match");
        assertEquals(entity.getAno(), dto.ano(), "Ano should match");
        assertEquals(entity.getGenero(), dto.genero(), "Genero should match");
    }

}
