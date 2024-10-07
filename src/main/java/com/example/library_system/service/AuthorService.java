package com.example.library_system.service;
import com.example.library_system.entity.Author;
import com.example.library_system.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    // Get all authors
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }


    // Create a new author
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new RuntimeException("Author not found"));
    }

    public Author updateAuthor(Long id, Author updatedAuthor) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
        existingAuthor.setName(updatedAuthor.getName());
        existingAuthor.setBio(updatedAuthor.getBio());
        return authorRepository.save(existingAuthor);
    }

    // Delete an author by ID
    public boolean deleteAuthor(Long id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

