package com.galvanize.controllers;

import com.galvanize.models.Room;
import com.galvanize.repositories.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RoomsController {
  @Autowired
  RoomsRepository roomsRepository;

  @RequestMapping(value = "/rooms", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public Room createRoom(@Valid @RequestBody Room room) {
    Room newRoom = new Room(room.getName(), room.getCampusName(), room.getCapacity(), room.isVc());
    roomsRepository.save(newRoom);
    return newRoom;
  }

  @ExceptionHandler
  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Invalid attributes provided for Room")
  public Model handleException(MethodArgumentNotValidException ex, Model model) {
    model.addAttribute("status", HttpStatus.UNPROCESSABLE_ENTITY);
    model.addAttribute("errors", ex.getBindingResult().getFieldErrors());

    return model;
  }
}
