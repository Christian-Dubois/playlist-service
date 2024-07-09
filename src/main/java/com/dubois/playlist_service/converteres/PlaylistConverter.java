package com.dubois.playlist_service.converteres;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dubois.playlist_service.dtos.MusicDTO;
import com.dubois.playlist_service.dtos.PlaylistDTO;
import com.dubois.playlist_service.models.Music;
import com.dubois.playlist_service.models.Playlist;


@Transactional(readOnly = true)
@Component
public class PlaylistConverter {

    @Autowired
    MusicConverter musicConverter;

    public Playlist toEntity(PlaylistDTO dto) {
        if (dto == null) {
            return null;
        }

        List<Music> musicas = new ArrayList<>();
        if (dto.musicas() != null) {
            musicas = dto.musicas().stream() //
                    .map(this.musicConverter::toEntity) //
                    .collect(Collectors.toList());
        }

        Playlist entity = new Playlist();
        entity.setNome(dto.nome());
        entity.setDescricao(dto.descricao());
        entity.setMusicas(musicas);

        return entity;
    }

    public PlaylistDTO toDTO(Playlist entity) {
        if (entity == null) {
            return null;
        }

        List<MusicDTO> musicas = new ArrayList<>();
        if (entity.getMusicas() != null) {
            musicas = entity.getMusicas().stream() //
                    .map(this.musicConverter::toDTO) //
                    .collect(Collectors.toList());
        }

        return new PlaylistDTO( //
                entity.getNome(), //
                entity.getDescricao(), //
                musicas);
    }

}
