package com.dubois.playlist_service.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dubois.playlist_service.models.Playlist;


public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByNome(String nome);

}
