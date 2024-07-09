package com.dubois.playlist_service.services;


import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dubois.playlist_service.converteres.PlaylistConverter;
import com.dubois.playlist_service.dtos.PlaylistDTO;
import com.dubois.playlist_service.dtos.PlaylistDTOWrapper;
import com.dubois.playlist_service.models.Playlist;
import com.dubois.playlist_service.repositories.PlaylistRepository;

import jakarta.persistence.EntityNotFoundException;


@Transactional
@Service
public class PlaylistService {

    @Autowired
    PlaylistRepository playlistRepository;
    @Autowired
    PlaylistConverter playlistConverter;

    public void createPlaylist(PlaylistDTO playlistDTO) throws BadRequestException {
        validatePlaylist(playlistDTO);
        this.playlistRepository.save(this.playlistConverter.toEntity(playlistDTO));
    }

    @Transactional(readOnly = true)
    public PlaylistDTOWrapper findAll() {
        List<Playlist> playlists = this.playlistRepository.findAll();
        List<PlaylistDTO> dtos = playlists.stream() //
                .map(this.playlistConverter::toDTO) //
                .collect(Collectors.toList());
        return new PlaylistDTOWrapper(dtos);
    }

    @Transactional(readOnly = true)
    public PlaylistDTO findByNome(String nome) {
        Playlist playlist =  this.playlistRepository.findByNome(nome).orElse(null);
        return this.playlistConverter.toDTO(playlist);
    }

    public void delete(String nome) {
        Playlist playlist = this.playlistRepository.findByNome(nome).orElseThrow(
                () -> new EntityNotFoundException("Playlist not found"));

        this.playlistRepository.delete(playlist);
    }

    private void validatePlaylist(PlaylistDTO playlistDTO) throws BadRequestException {
        if (playlistDTO == null) {
            throw new BadRequestException("It is necessary to provide a body for this request");
        }

        if (playlistDTO.nome() == null) {
            throw new BadRequestException("It is necessary to provide a name for the playlist");
        }

        if (this.findByNome(playlistDTO.nome()) != null) {
            throw new BadRequestException("The name of this playlist has already been used");
        }
    }
}
