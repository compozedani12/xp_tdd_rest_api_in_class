package com.galvanize.models;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RoomTest {
  private Validator validator;

  @Before
  public void init() {
    ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
    this.validator = vf.getValidator();
  }

  @Test
  public void ensureNameIsNotNull() {
    Room room = new Room(null, "foo", 0, false);

    Set<ConstraintViolation<Room>> violations = validator.validate(room);

    assertEquals(1, violations.size());
  }

  @Test
  public void ensureNameIsNotBlank() {
    Room room = new Room("", "foo", 0, false);

    Set<ConstraintViolation<Room>> violations = validator.validate(room);

    assertEquals(1, violations.size());
  }
}
