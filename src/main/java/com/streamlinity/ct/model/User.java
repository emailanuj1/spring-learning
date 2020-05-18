package com.streamlinity.ct.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Past;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue
    Integer id;

    String firstName;

    String lastName;

    @Past

    Date dob;
}
