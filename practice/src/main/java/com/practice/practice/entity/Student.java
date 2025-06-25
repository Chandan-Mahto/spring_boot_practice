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
    private Integer age;
    @Column(unique = true)
    private String username;
    private String password;
}
