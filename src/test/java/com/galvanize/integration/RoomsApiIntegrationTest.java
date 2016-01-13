package com.galvanize.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.Application;
import com.galvanize.models.Room;
import com.galvanize.repositories.RoomsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest("server.port:9000")
public class RoomsApiIntegrationTest {

  @Autowired
  RoomsRepository roomsRepository;

  RestTemplate restTemplate = new TestRestTemplate();

  final String BASE_URL = "http://localhost:9000/rooms";

  @Before
  public void cleanDatabase() {
    roomsRepository.deleteAll();

  }

  @After
  public void cleanDatabaseAfter() {
    roomsRepository.deleteAll();
  }


  @Test
  public void postRespondsWith201OnSuccessfulCreate() {
    final String name = "Ruby";
    final String campusName = "Boulder";
    final int capacity = 12;
    final boolean vc = false;

    Room room = new Room(name, campusName, capacity, vc);
    ResponseEntity<Room> response = restTemplate.postForEntity(BASE_URL, room, Room.class, Collections.EMPTY_MAP);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
  }

  @Test
  public void postRespondsWithFieldsForTheInstance() {
    final String name = "Ruby";
    final String campusName = "Boulder";
    final int capacity = 12;
    final boolean vc = false;

    Room room = new Room(name, campusName, capacity, vc);

    ResponseEntity<Room> response = restTemplate.postForEntity(BASE_URL, room, Room.class, Collections.EMPTY_MAP);

    Room newRoom = response.getBody();
    assertThat(newRoom.getId(), notNullValue());
    assertThat(newRoom.getName(), equalTo(name));
    assertThat(newRoom.getCampusName(), equalTo(campusName));
    assertThat(newRoom.getCapacity(), equalTo(capacity));
    assertThat(newRoom.isVc(), equalTo(vc));
  }

  @Test
  public void addsTheInstanceToTheDatabase() {
    final String name = "Ruby";
    final String campusName = "Boulder";
    final int capacity = 12;
    final boolean vc = false;

    Room room = new Room(name, campusName, capacity, vc);

    ResponseEntity<Room> response = restTemplate.postForEntity(BASE_URL, room, Room.class, Collections.EMPTY_MAP);

    assertThat(roomsRepository.count(), equalTo(1L));
  }

  @Test
  public void correctlyHandlesMultipleRequests() {
    Room room = new Room("Rails", "Boulder", 10, false);
    restTemplate.postForEntity(BASE_URL, room, Room.class, Collections.EMPTY_MAP);

    Room secondRoom = new Room("Go", "Boulder", 4, false);
    restTemplate.postForEntity(BASE_URL, secondRoom, Room.class, Collections.EMPTY_MAP);

    assertThat(roomsRepository.count(), equalTo(2L));
  }

  @Test
  public void givenIamusingTheApi_WhenIPostAnInvalidRequest_ThenTheResponseIsUnprocessableEntity() {
    Room room = new Room("", "Boulder", 12, false);

    ResponseEntity<Room> response = restTemplate.postForEntity(BASE_URL, room, Room.class, Collections.EMPTY_MAP);

    assertThat(response.getStatusCode(), equalTo(HttpStatus.UNPROCESSABLE_ENTITY));
  }

  @Test
  public void postRespondsWithDetailsOfValidationError() {
    Room room = new Room("", "Boulder", 12, false);

    ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, room, String.class, Collections.EMPTY_MAP);

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> jsonMap = objectMapper.readValue(response.getBody(), Map.class);

      assertThat(422, equalTo(jsonMap.get("status")));
      assertThat("Unprocessable Entity", equalTo(jsonMap.get("error")));

      ArrayList<LinkedHashMap<String, String>> errors = (ArrayList<LinkedHashMap<String, String>>) jsonMap.get("errors");
      assertThat("Room name must be more than 1 character", equalTo(errors.get(0).get("defaultMessage")));
    } catch(IOException e) {
      // no op, test will fail
    }
  }
}
