package com.example.todos.controller;

import com.example.todos.exception.ResourceNotFound;
import com.example.todos.model.Category;
import com.example.todos.model.Todo;
import com.example.todos.repository.CategoryRepository;
import com.example.todos.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<Todo> createTodo(@PathVariable("categoryId") Long categoryId, @RequestBody Todo todo) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFound("Not found Category with id = " + categoryId);
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            todo.setCategory(category);
            Todo createdTodo = todoRepository.save(todo);
            return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        if (!todoRepository.existsById(id)) {
            throw new ResourceNotFound("Not found Todo with id = " + id);
        }
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{categoryId}/category")
    public ResponseEntity<List<Todo>> getAllTodosByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFound("Not found Tutorial with id = " + categoryId);
        }

        List<Todo> todos = todoRepository.findByCategoryId(categoryId);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodoById(@PathVariable Long id, @RequestBody Todo todo) {
        Todo existingTodo = todoRepository.findById(id).orElse(null);
        if (existingTodo != null) {
            existingTodo.setName(todo.getName());
            existingTodo.setCompleted(todo.isCompleted());
            Todo updatedTodo = todoRepository.save(existingTodo);
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todoRepository.delete(todo);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
