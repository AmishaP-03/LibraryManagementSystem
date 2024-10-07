package com.example.library_system.controller;

import com.example.library_system.entity.Author;
import com.example.library_system.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "Operations related to authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    // Get all authors
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    // Get author by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get an author by ID", description = "Retrieves author details by ID")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        try {
            Author author = authorService.getAuthorById(id);
            return ResponseEntity.ok(author); // If successful, return 200 OK with the  author
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // If not found, return 404
        }
    }

    // Create a new author
    @PostMapping
    @Operation(summary = "Create a new author", description = "Creates a new author in the system")
    public Author createAuthor(@RequestBody Author author) {
        return authorService.createAuthor(author);
    }

    // Update an existing author
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing author", description = "Updates author details")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        try {
            Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
            return ResponseEntity.ok(updatedAuthor); // If successful, return 200 OK with the updated author
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // If not found, return 404
        }

    }

    // Delete an author
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author by ID", description = "Deletes an author by ID")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        boolean isDeleted = authorService.deleteAuthor(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
