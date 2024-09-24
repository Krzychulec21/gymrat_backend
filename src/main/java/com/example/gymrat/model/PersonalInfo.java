package com.example.gymrat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "avatar", columnDefinition = "BLOB")
    private byte[] avatar;

    private LocalDate dateOfBirth;
    private Double weight;
    private Double height;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 500)
    private String bio;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
