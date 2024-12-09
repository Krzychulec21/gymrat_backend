package com.example.gymrat.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryName category;

    @OneToOne(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExerciseInfo exerciseInfo;

    public Exercise(Long id, String name, CategoryName category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public Exercise(Long id, String name, CategoryName category, ExerciseInfo exerciseInfo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.exerciseInfo = exerciseInfo;
        if (exerciseInfo != null) {
            exerciseInfo.setExercise(this);
        }
    }


}
