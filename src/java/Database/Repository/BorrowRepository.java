package Database.Repository;

import Database.DatabaseAccess;

import models.entities.Book;
import models.entities.Borrow;
import models.entities.Person;
import models.tables.BorrowTableElement;
import models.tables.ExtendTable;


import java.util.*;
import java.util.stream.Collectors;

public class BorrowRepository {
    DatabaseAccess databaseAccess;
    PersonRepository personRepository = PersonRepository.getInstance();
    private static BorrowRepository instance;

    public static BorrowRepository getInstance() {
        if (instance == null) {
            instance = new BorrowRepository();
        }
        return instance;
    }

    public BorrowRepository() {
        databaseAccess = DatabaseAccess.getInstance();
    }

    public List<BorrowTableElement> getAllPersonBorrows(Person person) {
        List<BorrowTableElement> borrowTableElementElements = new ArrayList<>();
        List<Borrow> borrows = person.getBorrows();
        for (Borrow b : borrows) {
            BorrowTableElement borrowTableElement = new BorrowTableElement();
            Book book = b.getBook();
            borrowTableElement.setBook(book.getBookName());
            borrowTableElement.setBookISBN(book.getISBN());
            borrowTableElement.setStartOfBorrow(b.getBorrowDate());
            borrowTableElement.setEndOfBorrow(b.getEndOfBorrowDate());
            borrowTableElement.setWasExtended(b.getWasExtended());
            borrowTableElementElements.add(borrowTableElement);
        }
        return borrowTableElementElements;
    }

    public List<ExtendTable> getAllExtendRequest() {
        List<ExtendTable> extendTables = new ArrayList<>();
        String query = "select a.authorName,a.lastName,b.bookName,bw.endOfBorrowDate,bw.person,b.ISBN " +
                "from Borrow bw,Author a,Book b where bw.wasExtended = 'REQUESTED' " +
                "and bw.book=b.id and b.author=a.id order by b.bookName";
        List<Object[]> objects = databaseAccess.getQueryAsTableOfObjects(query);
        for (Object[] o : objects) {
            ExtendTable extendTable = new ExtendTable();
            extendTable.setAuthor(o[0].toString() + " " + o[1].toString());
            extendTable.setTittle(o[2].toString());
            extendTable.setEndOfBorrowDate((Date) o[3]);
            Person person = (Person) o[4];
            extendTable.setClient(person.getName() + " " + person.getLastName());
            extendTable.setISBN(o[5].toString());
            extendTables.add(extendTable);
        }
        return extendTables;
    }

    public void requestExtendBookBorrow(Borrow borrow) {
        borrow.setWasExtended(Borrow.WasExtended.REQUESTED.toString());
        databaseAccess.update(borrow);
    }

    public void updateExtensionStatus(Borrow borrow, String status) {
        borrow.setWasExtended(status);
        if (status.equals("EXTENDED")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(borrow.getEndOfBorrowDate());
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            borrow.setEndOfBorrowDate(calendar.getTime());
        }
        databaseAccess.update(borrow);
    }

    public Borrow getBorrowByBook(Book book) {
        String query = "select bo from Borrow bo,Book b where bo.book=" + book.getId() + " and b.availability='BORROWED'";
        return getOneBorrow(databaseAccess.getQuery(query));
    }

    public Boolean isBookBorrowed(String email, String title) {
        Person person = personRepository.findByEmail(email);
        List<Borrow> borrow = person.getBorrows().stream().filter(b -> b.getBook().getBookName().equals(title)).
                collect(Collectors.toList());
        if (borrow.isEmpty()) {
            return false;
        }
        return borrow.stream().filter(b -> !isBookReturned(b)).findFirst().orElse(null) != null;
    }

    private boolean isBookReturned(Borrow borrow) {
        return borrow.getAReturn() != null;
    }

    private Borrow getOneBorrow(List<Object> borrowList) {
        if (!borrowList.isEmpty()) return (Borrow) borrowList.get(0);
        return null;
    }

    public void deleteBorrow(Borrow borrow) {
        databaseAccess.delete(borrow);
    }

    public void createBorrow(Borrow borrow) {
        databaseAccess.save(borrow);
    }
}
