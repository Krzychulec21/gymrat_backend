package com.example.gymrat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "exercise_description", joinColumns = @JoinColumn(name = "exercise_info_id"))
    @Column(name = "step")
    private List<String> description = new ArrayList<>();

    private String videoId;

    @OneToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private Integer difficultyLevel;

    public void setDifficultyLevel(Integer difficultyLevel) {
        if (difficultyLevel < 1 || difficultyLevel > 5){
            throw new IllegalArgumentException("Difficulty level must be between 1 and 5");
        }
        this.difficultyLevel = difficultyLevel;
    }
}
