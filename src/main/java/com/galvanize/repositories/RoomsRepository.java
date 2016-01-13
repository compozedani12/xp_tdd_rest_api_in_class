package com.galvanize.repositories;

import com.galvanize.models.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomsRepository extends MongoRepository<Room, String>{
}
