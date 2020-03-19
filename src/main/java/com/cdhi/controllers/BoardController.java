package com.cdhi.controllers;

import com.cdhi.domain.Board;
import com.cdhi.dtos.BoardDTO;
import com.cdhi.dtos.NewBoardDTO;
import com.cdhi.services.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Api(value = "Board Controller")
@RestController
@RequestMapping(value = "boards")
public class BoardController {

    @Autowired
    BoardService service;

    @ApiOperation(value = "Get Board by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<BoardDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findOne(id));
    }

    @ApiOperation(value = "User creates new board")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewBoardDTO newBoardDTO) {
        Board b = service.create(newBoardDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(b.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @ApiOperation(value = "Delete Board")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Board Id: " + id + " deleted successfully!");
    }

    @ApiOperation(value = "Add user to Board")
    @PostMapping(value = "{boardId}/{userId}")
    public ResponseEntity<?> addUserIntoBoard(@PathVariable("boardId") Integer boardId, @PathVariable("userId") Integer userId) {
        service.addUserInBoard(boardId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("User Id: " + userId + " added to Board " + boardId + " successfully!");
    }

    @ApiOperation(value = "Remove user from Board")
    @DeleteMapping(value = "{boardId}/{userId}")
    public ResponseEntity<?> removeUserFromBoard(@PathVariable("boardId") Integer boardId, @PathVariable("userId") Integer userId) {
        service.removeUserFromBoard(boardId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("User Id: " + userId + " removed from Board " + boardId + " successfully!");
    }

    @ApiOperation(value = "Leave Board")
    @PutMapping(value = "{boardId}/leave")
    public ResponseEntity<?> leaveBoard(@PathVariable("boardId") Integer boardId) {
        service.leave(boardId);
        return ResponseEntity.status(HttpStatus.OK).body("You left Board " + boardId + " successfully!");
    }

    @ApiOperation(value = "Edit Board")
    @PutMapping(value = "{boardId}")
    public ResponseEntity<?> updateBoard(@RequestBody @Valid NewBoardDTO newBoardDTO, @PathVariable("boardId") Integer boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.save(boardId, newBoardDTO));
    }
}
