package AdminTests;

import models.entities.Author;
import org.junit.Assert;
import org.junit.Test;
import Database.Repository.AuthorRepository;

public class AddBookPanelControllerTest {
    AuthorRepository authorRepository = AuthorRepository.getInstance();

    @Test
    public void createUser() {
        Author author = new Author("Testowy","Testowy");
        authorRepository.createAuthor(author);
        Author author1 = authorRepository.findByNameAndLastName("Testowy","Testowy");
        Assert.assertEquals("Test poprawnie wykonany", author, author1);
    }
}
