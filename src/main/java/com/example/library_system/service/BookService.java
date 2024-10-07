package com.example.library_system.service;
import com.example.library_system.entity.Book;
import com.example.library_system.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Create a new book
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    // Update an existing book
    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> existingBookOptional = bookRepository.findById(id);

        // Check if the book exists
        if (existingBookOptional.isPresent()) {
            Book existingBook = existingBookOptional.get(); // Extract the Book object from Optional
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setIsbn(updatedBook.getIsbn());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPublishedDate(updatedBook.getPublishedDate());

            // Save and return the updated book
            return bookRepository.save(existingBook);
        } else {
            throw new RuntimeException("Book not found");
        }
    }


    // Delete a book by ID
    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Search for books by title
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    // Search for books by author's name
    public List<Book> searchBooksByAuthorName(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }
}

