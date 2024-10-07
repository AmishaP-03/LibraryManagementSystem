package com.example.library_system.service;

import com.example.library_system.entity.Book;
import com.example.library_system.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService; // The service you want to test

    @Mock
    private BookRepository bookRepository; // Mock the repository dependency

    private Book existingBook;
    private Book updatedBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
        // Create a sample existing book (this simulates an entry in the database)
        existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");
        existingBook.setIsbn("1234567890");
        existingBook.setPublishedDate(LocalDate.of(2000, 1, 1));

        // Create a sample updated book (this simulates user-provided data for updating)
        updatedBook = new Book();
        updatedBook.setTitle("New Title");
        updatedBook.setIsbn("0987654321");
        updatedBook.setPublishedDate(LocalDate.of(2021, 10, 1));
    }

    @Test
    void testCreateBook() {
        Book book = new Book();
        book.setTitle("Test Book");

        // Mock the repository behavior
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book createdBook = bookService.createBook(book);

        // Verify the service behavior
        assertEquals("Test Book", createdBook.getTitle());
        verify(bookRepository, times(1)).save(book); // Verify that save() was called exactly once
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        // Mock the repository behavior
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = bookService.getBookById(1L);

        // Verify the service behavior
        assertEquals("Test Book", foundBook.get().getTitle());
        verify(bookRepository, times(1)).findById(1L); // Verify that findById() was called exactly once
    }

    @Test
    void testDeleteBook() {
        Long bookId = 1L;

        // Mock the repository behavior
        doNothing().when(bookRepository).deleteById(bookId);

        boolean isDeleted = bookService.deleteBook(bookId);

        // Verify the service behavior
        assertEquals(true, isDeleted);
        verify(bookRepository, times(1)).deleteById(bookId); // Verify that deleteById() was called exactly once
    }

    @Test
    void testUpdateBookSuccess() {
        // Mock the repository's findById() and save() behavior
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // Call the service method to update the book
        Book result = bookService.updateBook(1L, updatedBook);

        // Assert that the updated book has the new values
        assertEquals("New Title", result.getTitle());
        assertEquals("0987654321", result.getIsbn());
        assertEquals(LocalDate.of(2021, 10, 1), result.getPublishedDate());

        // Verify that findById() and save() methods were called exactly once
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void testUpdateBookNotFound() {
        // Mock the repository's findById() to return an empty Optional
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert that a RuntimeException is thrown when trying to update a non-existent book
        assertThrows(RuntimeException.class, () -> bookService.updateBook(1L, updatedBook));

        // Verify that save() was never called since the book wasn't found
        verify(bookRepository, times(0)).save(any(Book.class));
    }
}
