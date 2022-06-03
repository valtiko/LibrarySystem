package Database.Repository;

import Database.DatabaseAccess;
import models.entities.Author;


import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {
    DatabaseAccess databaseAccess;
    private static AuthorRepository instance;

    public static AuthorRepository getInstance() {
        if (instance == null) {
            instance = new AuthorRepository();
        }
        return instance;
    }

    public AuthorRepository() {
        databaseAccess = DatabaseAccess.getInstance();
    }

    public List<Author> findByName(String name) {
        String query = String.format("from Author c where c.authorName = '%s'", name);
        return authorList(databaseAccess.getQuery(query));
    }

    public Author findByNameAndLastName(String name, String lastname) {
        String query = String.format("from Author c where c.authorName = '%s' and c.lastName='%s'", name, lastname);
        return getOneAuthor(databaseAccess.getQuery(query));
    }

    public List<Author> getAllAuthors() {
        String query = String.format("from Author");
        List<Author> authorList = authorList(databaseAccess.getQuery(query));
        return authorList;
    }

    private List<Author> authorList(List<Object> objects) {
        List<Author> authors = new ArrayList<>();
        for (Object o : objects) {
            if (o instanceof Author) {
                authors.add((Author) o);
            }
        }
        return authors;
    }

    private Author getOneAuthor(List<Object> authorList) {
        if (!authorList.isEmpty()) return (Author) authorList.get(0);
        return null;
    }

    public Author createAuthor(Author author) {
        databaseAccess.save(author);
        return author;
    }


}

