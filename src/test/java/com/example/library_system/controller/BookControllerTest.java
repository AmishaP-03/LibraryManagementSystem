package com.example.library_system.controller;

import com.example.library_system.entity.Author;
import com.example.library_system.entity.Book;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper; // For converting objects to JSON

    private Book book;

    private Book existingBook;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        Author author = new Author();
        author.setName("Original Author");
        author.setBio("Original Author Bio");

        existingBook = new Book();
        existingBook.setTitle("Original Title");
        existingBook.setIsbn("1234567890");
        existingBook.setAuthor(author);
        existingBook.setPublishedDate(LocalDate.of(2000, 1, 1));

        // Save this book into the repository so we can update it in the test
        bookRepository.save(existingBook);
    }

    @Test
    void testCreateBook() throws Exception {
        when(bookService.createBook(any(Book.class))).thenReturn(book);

        // Perform the POST request to create a book
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book))) // Convert book object to JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book")); // Check the JSON response
    }

    @Test
    void testGetBookById() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        // Perform the GET request to retrieve the book
        mockMvc.perform(get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book")); // Check the JSON response
    }

    @Test
    void testDeleteBook() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(true);

        // Perform the DELETE request to delete the book
        mockMvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Check the HTTP status
    }

    @Test
    void testUpdateBookSuccess() throws Exception {
        // Prepare updated book data
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Author");
        updatedAuthor.setBio("Updated Author Bio");

        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setIsbn("0987654321");
        updatedBook.setAuthor(updatedAuthor);
        updatedBook.setPublishedDate(LocalDate.of(2021, 10, 1));

        // Convert the updated book to JSON for the request body
        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        // Perform the PUT request to update the book
        mockMvc.perform(put("/api/books/{id}", existingBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isOk()) // Expect 200 OK status
                .andExpect(content().json(updatedBookJson)); // Expect the response body to match the updated book data
    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        // Prepare updated book data
        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Author");
        updatedAuthor.setBio("Updated Author Bio");

        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setIsbn("0987654321");
        updatedBook.setAuthor(updatedAuthor);
        updatedBook.setPublishedDate(LocalDate.of(2021, 10, 1));

        // Convert the updated book to JSON for the request body
        String updatedBookJson = objectMapper.writeValueAsString(updatedBook);

        // Perform the PUT request to update a non-existent book (id = 999)
        mockMvc.perform(put("/api/books/{id}", 999L) // ID 999 does not exist
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isNotFound()); // Expect 404 Not Found status
    }
}
