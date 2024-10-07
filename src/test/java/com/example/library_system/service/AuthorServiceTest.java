package com.example.library_system.service;

import com.example.library_system.entity.Author;
import com.example.library_system.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @InjectMocks
    private AuthorService authorService; // Inject service to test

    @Mock
    private AuthorRepository authorRepository; // Mock the repository

    private Author sampleAuthor;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create a sample author for testing
        sampleAuthor = new Author();
        sampleAuthor.setId(1L);
        sampleAuthor.setName("John Doe");
        sampleAuthor.setBio("Sample bio");
    }

    @Test
    void testCreateAuthor() {
        // Mock repository behavior
        when(authorRepository.save(any(Author.class))).thenReturn(sampleAuthor);

        // Call the service method
        Author createdAuthor = authorService.createAuthor(sampleAuthor);

        // Assert that the author was created and returned correctly
        assertNotNull(createdAuthor);
        assertEquals(sampleAuthor.getId(), createdAuthor.getId());
        assertEquals(sampleAuthor.getName(), createdAuthor.getName());

        // Verify that the save method was called
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testGetAuthorByIdSuccess() {
        // Mock repository behavior for a found author
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));

        // Call the service method
        Author foundAuthor = authorService.getAuthorById(1L);

        // Assert that the author was found and returned correctly
        assertNotNull(foundAuthor);
        assertEquals(sampleAuthor.getId(), foundAuthor.getId());
        assertEquals(sampleAuthor.getName(), foundAuthor.getName());

        // Verify that the findById method was called
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAuthorByIdNotFound() {
        // Mock repository behavior for an author not found
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authorService.getAuthorById(1L);
        });

        // Assert the exception message
        assertEquals("Author not found", exception.getMessage());

        // Verify that the findById method was called
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateAuthorSuccess() {
        // Mock repository behavior for a found author
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(sampleAuthor);

        // Create updated author details
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Name");
        updatedAuthor.setBio("Updated Bio");

        // Call the service method
        Author result = authorService.updateAuthor(1L, updatedAuthor);

        // Assert that the author was updated correctly
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Bio", result.getBio());

        // Verify repository interactions
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(sampleAuthor);
    }

    @Test
    void testUpdateAuthorNotFound() {
        // Mock repository behavior for an author not found
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authorService.updateAuthor(1L, new Author());
        });

        // Assert the exception message
        assertEquals("Author not found", exception.getMessage());

        // Verify that findById was called, but save was not
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void testDeleteAuthor() {
        // No need to mock repository behavior for delete, just verify the interaction

        // Call the service method
        authorService.deleteAuthor(1L);

        // Verify that the deleteById method was called
        verify(authorRepository, times(1)).deleteById(1L);
    }
}
