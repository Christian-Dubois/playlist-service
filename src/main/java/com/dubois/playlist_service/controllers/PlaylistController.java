package com.dubois.playlist_service.controllers;


import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dubois.playlist_service.dtos.PlaylistDTO;
import com.dubois.playlist_service.dtos.PlaylistDTOWrapper;
import com.dubois.playlist_service.services.PlaylistService;

import jakarta.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    @Autowired
    PlaylistService playlistService;

    @PostMapping("/lists")
    public ResponseEntity<String> createPlaylist(@RequestBody PlaylistDTO playlistDTO) {
        try {
            this.playlistService.createPlaylist(playlistDTO);
            return ResponseEntity //
                    .status(HttpStatus.CREATED) //
                    .body("Playlist created successfully");
        } catch (BadRequestException e) {
            return ResponseEntity //
                    .status(HttpStatus.BAD_REQUEST) //
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity //
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) //
                    .body(e.getMessage());
        }
    }

    @GetMapping("/lists")
    public PlaylistDTOWrapper findAllPlaylist() {
        return this.playlistService.findAll();
    }

    @GetMapping("/lists/")
    public ResponseEntity<?> findPlaylistByName(@RequestParam("listName") String listName) {
        PlaylistDTO playlistDTO = this.playlistService.findByNome(listName);
        if (playlistDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Playlist not found");
        }
        return ResponseEntity.ok(playlistDTO);
    }

    @DeleteMapping("/lists/")
    public ResponseEntity<String> deletePlaylistByName(@RequestParam("listName") String listName) {
        try {
            this.playlistService.deleteByNome(listName);
            return ResponseEntity //
                    .status(HttpStatus.NO_CONTENT) //
                    .body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity //
                    .status(HttpStatus.NOT_FOUND) //
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity //
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) //
                    .body(e.getMessage());
        }
    }

}
