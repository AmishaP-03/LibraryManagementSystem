package com.example.library_system.controller;

import com.example.library_system.entity.Book;
import com.example.library_system.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Operations related to books in the library")
public class BookController {

    @Autowired
    private BookService bookService;

    // Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Get book by ID
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a book by ID", description = "Fetches a book's details using its unique ID")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new book
    @PostMapping
    @Operation(summary = "Create a new book", description = "Adds a new book to the library")
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    // Update an existing book
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book", description = "Updates the details of an existing book in the library")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        try {
            // Call service to update book
            Book updatedBookResponse = bookService.updateBook(id, updatedBook);
            return ResponseEntity.ok(updatedBookResponse); // If successful, return 200 OK with the updated book
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // If not found, return 404
        }
    }

    // Delete a book
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by ID", description = "Removes a book from the library by its unique ID")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Search books by title
    @GetMapping("/search/title")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        List<Book> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    // Search books by author's name
    @GetMapping("/search/author")
    public ResponseEntity<List<Book>> searchBooksByAuthor(@RequestParam String authorName) {
        List<Book> books = bookService.searchBooksByAuthorName(authorName);
        return ResponseEntity.ok(books);
    }
}
