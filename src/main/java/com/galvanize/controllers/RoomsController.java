package com.galvanize.controllers;

import com.galvanize.exception.RoomNotFoundException;
import com.galvanize.models.Room;
import com.galvanize.repositories.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    System.out.println("In first exception handler......");
    model.addAttribute("status", HttpStatus.UNPROCESSABLE_ENTITY);
    model.addAttribute("errors", ex.getBindingResult().getFieldErrors());

    return model;
  }

  @RequestMapping(value = "/rooms", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public List getAllRooms(){

    List roomList = new ArrayList<Room>();
    roomList=roomsRepository.findAll();
    return roomList;
  }

  @RequestMapping(value = "/rooms/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public Room getRoomById(@PathVariable String id) throws RoomNotFoundException{

    Room room = new Room();
    room = roomsRepository.findOne(id);
    if(room==null)  {
      //return new ResponseEntity<Room>(HttpStatus.NOT_FOUND);
//      throw new RoomNotFoundException("Room is not found in the database1");
      throw new RoomNotFoundException();
    }
    return room;
  }

//  @ExceptionHandler(RoomNotFoundException.class)
//  @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Room is not found in the database2")
//  public Model handleException(RoomNotFoundException ex,  Model model) {
//    System.out.println("exception handling in process " + ex);
//    model.addAttribute("status", HttpStatus.NOT_FOUND);
//    return model;
//  }

}
