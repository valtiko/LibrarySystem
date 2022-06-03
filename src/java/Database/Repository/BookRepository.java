package Database.Repository;

import Database.DatabaseAccess;
import models.entities.Book;
import models.tables.BookTable;
import models.tables.DestroyedBookTable;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class BookRepository {

    DatabaseAccess databaseAccess;
    private static BookRepository instance;

    public static BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    public BookRepository() {
        databaseAccess = DatabaseAccess.getInstance();
    }

    public List<Book> findByCategory(String category) {
        String query = String.format("from Book c where c.category = '%s'", category);
        return bookList(databaseAccess.getQuery(query));
    }

    public List<Book> getAllBooks() {
        String query = String.format("from Book");
        List<Book> bookList = bookList(databaseAccess.getQuery(query));
        return bookList;
    }

    public Book findByISBN(String isbn) {
        String query = String.format("from Book c where c.ISBN='%s'", isbn);
        return getOneBook(databaseAccess.getQuery(query));
    }

    public List<BookTable> getBookTables() {
        List<BookTable> bookTables = new ArrayList<>();
        String query = "select b.bookName,b.author,b.category,b.ISBN, b.availability from Book b order by b.availability, b.bookName";
        List<Object[]> books = databaseAccess.getQueryAsTableOfObjects(query);
        for (Object[] o : books) {
            BookTable book = new BookTable();
            book.setBookName(o[0].toString());
            book.setAuthor(o[1].toString());
            book.setCategory(o[2].toString());
            book.setISBN(o[3].toString());
            book.setAvailability(o[4].toString());
            bookTables.add(book);
        }
        return bookTables;

    }

    private List<Book> bookList(List<Object> objects) {
        List<Book> books = new ArrayList<>();
        for (Object o : objects) {
            if (o instanceof Book) {
                books.add((Book) o);
            }
        }
        return books;
    }

    private Book getOneBook(List<Object> bookList) {
        if (!bookList.isEmpty()) return (Book) bookList.get(0);
        return null;
    }

    public Book findBookByISBN(String ISBN) {
        String query = "from Book b where b.ISBN = '" + ISBN + "'";
        return (Book) databaseAccess.getQuery(query).get(0);
    }

    public void createISBN(Book book) {
        List<Book> books = getAllBooks();
        Random random = new Random();
        StringBuilder ISBN = new StringBuilder();
        ISBN.append(book.getAuthor().getAuthorName().charAt(0));
        ISBN.append(book.getAuthor().getLastName().charAt(0));
        ISBN.append(book.getBookName().charAt(0));
        for (int i = 0; i <= 4; i++) ISBN.append(random.nextInt(10));
        for (Book b : books) {
            if (b.getISBN().contentEquals(ISBN))
                createISBN(book);
        }
        book.setISBN(ISBN.toString());
    }

    public void createBook(Book book) {
        book.getAuthor().getBooks().add(book);
        databaseAccess.save(book);
    }

    public void deleteBook(Book book) {
        databaseAccess.delete(book);
    }

    public void changeAvailabilityOfBookToBorrowed(Book book) {
        book.setAvailability(Book.Availability.BORROWED.toString());
        databaseAccess.update(book);
    }

    private BookTable getOneBookTable(List<Object> bookList) {
        if (!bookList.isEmpty()) return (BookTable) bookList.get(0);
        return null;
    }

    public Boolean isBookAvailable(String title) {
        String query = "select b.ISBN from Book b where b.bookName='" + title + "' and b.availability='AVAILABLE' and b.status='NEW'";
        return !databaseAccess.getQuery(query).isEmpty();
    }

    public List<Object> getISBNListByTitle(String title) {
        String query = "select b.ISBN from Book b where b.bookName='"+title+"' and b.availability='AVAILABLE' and b.status='NEW'";
        return databaseAccess.getQuery(query);
    }

    public List<Object> getBookISBN(){
        String query = "select distinct b.ISBN from Book b where b.status='NEW' and b.availability='AVAILABLE'";
        return databaseAccess.getQuery(query);
    }

    public List<DestroyedBookTable> getAllDestroyedBooks() {
        List<DestroyedBookTable> destroyedBooks = new ArrayList<>();
        String query = "select b.bookName,a.authorName,a.lastName,b.category,b.ISBN from Book b," +
                "Author a where a.id=b.author and b.status='" + Book.Status.DESTROYED + "'";
        List<Object[]> objects = databaseAccess.getQueryAsTableOfObjects(query);
        for (Object[] o : objects) {
            DestroyedBookTable destroyedBook = new DestroyedBookTable();
            destroyedBook.setTitle(o[0].toString());
            destroyedBook.setAuthor(o[1].toString() + " " + o[2].toString());
            destroyedBook.setCategory(o[3].toString());
            destroyedBook.setISBN(o[4].toString());
            destroyedBooks.add(destroyedBook);
        }
        return destroyedBooks;
    }

    public void updateBook(Book book) {
        databaseAccess.update(book);
    }
}
