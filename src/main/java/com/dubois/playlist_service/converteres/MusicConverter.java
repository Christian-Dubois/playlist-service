package com.dubois.playlist_service.converteres;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dubois.playlist_service.dtos.MusicDTO;
import com.dubois.playlist_service.models.Music;


@Transactional(readOnly = true)
@Component
public class MusicConverter {

    public Music toEntity(MusicDTO dto) {
        Music music = new Music();
        music.setTitulo(dto.titulo());
        music.setArtista(dto.artista());
        music.setAlbum(dto.album());
        music.setAno(dto.ano());
        music.setGenero(dto.genero());

        return music;
    }

    public MusicDTO toDTO(Music entity) {
        return new MusicDTO( //
                entity.getTitulo(), //
                entity.getArtista(), //
                entity.getAlbum(), //
                entity.getAno(), //
                entity.getGenero());
    }

}
