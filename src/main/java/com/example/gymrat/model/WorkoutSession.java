package com.example.gymrat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String note;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL)
    private List<ExerciseSession> exerciseSessions;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
