package com.example.todos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The name of the todo task is required.")
    private String name;

    @NotNull(message = "The completed status must be true or false.")
    private boolean completed;

    @NotNull(message = "A category ID is required.")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
