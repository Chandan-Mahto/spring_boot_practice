package com.practice.practice.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;
    private String username;
    private String password;
}
