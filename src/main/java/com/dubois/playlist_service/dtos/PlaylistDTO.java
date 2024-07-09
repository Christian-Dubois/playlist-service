package com.dubois.playlist_service.dtos;


import java.util.List;


public record PlaylistDTO(String nome, String descricao, List<MusicDTO> musicas) {
}
