package com.example.library_system.controller;

import com.example.library_system.entity.Author;
import com.example.library_system.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Author existingAuthor;

    @BeforeEach
    void setUp() {
        // Clear the repository and add a sample author to test CRUD operations
        authorRepository.deleteAll();

        existingAuthor = new Author();
        existingAuthor.setName("John Doe");
        existingAuthor.setBio("Sample bio");
        existingAuthor = authorRepository.save(existingAuthor); // Save to get an ID
    }

    @Test
    void testCreateAuthor() throws Exception {
        Author newAuthor = new Author();
        newAuthor.setName("Jane Doe");
        newAuthor.setBio("New author bio");

        String authorJson = objectMapper.writeValueAsString(newAuthor);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.bio").value("New author bio"));
    }

    @Test
    void testGetAuthorByIdSuccess() throws Exception {
        mockMvc.perform(get("/api/authors/{id}", existingAuthor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.bio").value("Sample bio"));
    }

    @Test
    void testGetAuthorByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/authors/{id}", 999L)) // ID that does not exist
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateAuthorSuccess() throws Exception {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Author");
        updatedAuthor.setBio("Updated bio");

        String updatedAuthorJson = objectMapper.writeValueAsString(updatedAuthor);

        mockMvc.perform(put("/api/authors/{id}", existingAuthor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAuthorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Author"))
                .andExpect(jsonPath("$.bio").value("Updated bio"));
    }

    @Test
    void testUpdateAuthorNotFound() throws Exception {
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Author");
        updatedAuthor.setBio("Updated bio");

        String updatedAuthorJson = objectMapper.writeValueAsString(updatedAuthor);

        mockMvc.perform(put("/api/authors/{id}", 999L) // ID that does not exist
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAuthorJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAuthorSuccess() throws Exception {
        mockMvc.perform(delete("/api/authors/{id}", existingAuthor.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAuthorNotFound() throws Exception {
        mockMvc.perform(delete("/api/authors/{id}", 999L)) // ID that does not exist
                .andExpect(status().isNotFound());
    }
}

