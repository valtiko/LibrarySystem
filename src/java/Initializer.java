import Database.DatabaseAccess;
import Database.Repository.*;
import controllers.AccountantControllers.invoice.InvoiceRepository;
import controllers.AccountantControllers.invoice.InvoiceRepositoryImpl;
import controllers.security.BCrypt;
import javafx.fxml.Initializable;
import models.entities.*;


import java.net.URL;
import java.util.*;

public class Initializer implements Initializable {
    private static Initializer instance;
    private final BorrowRepository borrowRepository = BorrowRepository.getInstance();
    private final PersonRepository clientRepository = PersonRepository.getInstance();
    private final BookRepository bookRepository = BookRepository.getInstance();
    private final AuthorRepository authorRepository = AuthorRepository.getInstance();
    private final BookProposalRepository proposalRepository = BookProposalRepository.getInstance();
    private final ReservationRepository reservationRepository = ReservationRepository.getInstance();
    private final ReturnRepository returnRepository = ReturnRepository.getInstance();
    private final ReportRepository reportRepository = ReportRepository.getInstance();
    private final InvoiceRepository invoiceRepository = new InvoiceRepositoryImpl(DatabaseAccess.getEntityManager());

    public static Initializer getInstance() {
        if (instance == null) {
            instance = new Initializer();
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Person> people = initializePeople();
        List<Author> authors = initializeAuthors();
        List<Book> books = initializeBooks(authors);
        List<BookProposal> proposals = initializeProposals(people);
        List<Reservation> reservations = initializeReservations(books, people);
        List<Borrow> borrows = initializeBorrows(books, people);
        List<Return> returns = initializeReturns(borrows);
        List<Report> reports = initializeReport();
<<<<<<< HEAD
        initializeProposals(people);
        initializeInvoices();
    }

    private void initializeInvoices() {
        Invoice invoice = new Invoice();
        invoice.setSubjectName("A");
        invoice.setData(new byte[0]);
        invoice.setStatus(Invoice.Status.ISSUED);
        invoice.setAdditionalInfo("info");
        invoice.setNumber("");
        DatabaseAccess.getInstance().save(invoice);
=======
        removeExpiredReservations();
>>>>>>> b09d5cc32595c938f02118375d87ce0df1e72943
    }

    public List<Report> initializeReport() {
        List<Report> reports = new ArrayList<>();
        Calendar calendar = new GregorianCalendar(2022, Calendar.APRIL, 25);
        Report report = new Report(calendar.getTime(), Report.ReportType.List.toString(), "newBook(Wladca piesciena 1,J.R.R. Tolkien,3)\n" +
                "deleteBook(Wladca piescienia 2,J.R.R. Tolkien,SA123)\n" +
                "newBook(Piraci z karaibow klatwa czarnej perly, Gore Verbinski, 2)\n" +
                "deleteBook(Wladca Pierscieni Tom 3. Powrot krola,J.R.R. Tolkien,SZXA1)");
        reportRepository.createReport(report);
        reports.add(report);
        return reports;
    }

    public List<Person> initializePeople() {
        List<Person> people = new ArrayList<>();
        Person person1 = new Person("Arek", "Fluda", "arek@wp.pl",
                BCrypt.hashpw("123456", BCrypt.gensalt(12)), Person.Role.ADMIN);
        clientRepository.createClient(person1);
        people.add(person1);
        Person person2 = new Person("Sebastian", "Zarzycki", "zarzyk@wp.pl",
                BCrypt.hashpw("123456", BCrypt.gensalt(12)), Person.Role.LIBRARIAN);
        clientRepository.createClient(person2);
        people.add(person2);
        Person person3 = new Person("Piotr", "Kojder", "piter@gmail.com",
                BCrypt.hashpw("123456", BCrypt.gensalt(12)), Person.Role.ACCOUNTANT);
        clientRepository.createClient(person3);
        people.add(person3);
        Person person4 = new Person("Michał", "Puzon", "michu@wp.pl",
                BCrypt.hashpw("123456", BCrypt.gensalt(12)), Person.Role.USER);
        clientRepository.createClient(person4);
        people.add(person4);
        Person person5 = new Person("Krystian", "Prokopik", "krystek@wp.pl",
                BCrypt.hashpw("123456", BCrypt.gensalt(12)), Person.Role.USER);
        clientRepository.createClient(person5);
        people.add(person5);
        Person person6 = new Person("Rafał", "Michura", "rafi@gmail.com",
                BCrypt.hashpw("123456", BCrypt.gensalt(12)), Person.Role.USER);
        clientRepository.createClient(person6);
        people.add(person6);
        return people;
    }

    public List<Author> initializeAuthors() {
        List<Author> authors = new ArrayList<>();
        Author author1 = new Author("J.R.R", "Tolkien");
        authorRepository.createAuthor(author1);
        authors.add(author1);
        Author author2 = new Author("John", "Flanagan");
        authorRepository.createAuthor(author2);
        authors.add(author2);
        Author author3 = new Author("Piers", "Anthony");
        authorRepository.createAuthor(author3);
        authors.add(author3);
        return authors;
    }

    public List<Book> initializeBooks(List<Author> authors) {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book("Ruiny Gorlanu", authors.get(1), "Fantasy");
        bookRepository.createISBN(book1);
        bookRepository.createBook(book1);
        books.add(book1);
        Book book2 = new Book("Wyrzutki", authors.get(1), "Fantasy");
        bookRepository.createISBN(book2);
        bookRepository.createBook(book2);
        books.add(book2);
        Book book3 = new Book("Drużyna Pierścienia", authors.get(0), "Fantasy");
        bookRepository.createISBN(book3);
        bookRepository.createBook(book3);
        books.add(book3);
        Book book4 = new Book("Spryciarz z Londynu", authorRepository.createAuthor(
                new Author("Terry", "Pratchet")), "science-fiction");
        bookRepository.createISBN(book4);
        bookRepository.createBook(book4);
        books.add(book4);
        Book book5 = new Book("Zaklęcie Dla Kameleon", authors.get(2), "science-fiction");
        bookRepository.createISBN(book5);
        bookRepository.createBook(book5);
        books.add(book5);
        Book book6 = new Book("Najeźdźcy", authors.get(1), "Fantasy");
        bookRepository.createISBN(book6);
        bookRepository.createBook(book6);
        books.add(book6);
        Book book7 = new Book("Wyrzutki", authors.get(1), "Fantasy");
        bookRepository.createISBN(book7);
        bookRepository.createBook(book7);
        books.add(book7);
        return books;
    }

    public List<BookProposal> initializeProposals(List<Person> people) {
        List<BookProposal> proposals = new ArrayList<>();
        BookProposal bookProposal = new BookProposal("W pustynii i w puszczy", "Henryk Sienkiewicz", people.get(3));
        proposalRepository.createBookProposal(bookProposal);
        proposals.add(bookProposal);
        BookProposal bookProposal1 = new BookProposal("Szklane domy", "Stefan Żeromski", people.get(3));
        proposalRepository.createBookProposal(bookProposal1);
        proposals.add(bookProposal1);
        BookProposal bookProposal2 = new BookProposal("Pan Tadeusz", "Adam Mickiewicz", people.get(4));
        proposalRepository.createBookProposal(bookProposal2);
        proposals.add(bookProposal2);
        BookProposal bookProposal3 = new BookProposal("Romeo i Julia", "William Szekspir", people.get(5));
        proposalRepository.createBookProposal(bookProposal3);
        proposals.add(bookProposal3);
        return proposals;
    }

    public List<Reservation> initializeReservations(List<Book> books, List<Person> people) {
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = new Reservation(books.get(0), people.get(3));
        reservationRepository.createReservation(reservation);
        reservations.add(reservation);
        Reservation reservation1 = new Reservation(books.get(1), people.get(4));
        reservationRepository.createReservation(reservation1);
        reservations.add(reservation1);
        Reservation reservation2 = new Reservation(books.get(2), people.get(4));
        reservationRepository.createReservation(reservation2);
        reservations.add(reservation2);
        Reservation reservation3 = new Reservation(books.get(3), people.get(5));
        reservationRepository.createReservation(reservation3);
        reservations.add(reservation3);
        return reservations;
    }

    public List<Borrow> initializeBorrows(List<Book> books, List<Person> people) {
        List<Borrow> borrows = new ArrayList<>();
        Borrow borrow1 = new Borrow(people.get(4), books.get(0));
        bookRepository.changeAvailabilityOfBookToBorrowed(books.get(0));
        borrowRepository.createBorrow(borrow1);
        borrows.add(borrow1);
        Borrow borrow2 = new Borrow(people.get(3), books.get(1));
        bookRepository.changeAvailabilityOfBookToBorrowed(books.get(1));
        borrowRepository.createBorrow(borrow2);
        borrowRepository.requestExtendBookBorrow(borrow2);
        borrows.add(borrow2);
        Borrow borrow3 = new Borrow(people.get(5), books.get(2));
        bookRepository.changeAvailabilityOfBookToBorrowed(books.get(2));
        borrowRepository.createBorrow(borrow3);
        borrowRepository.requestExtendBookBorrow(borrow3);
        borrows.add(borrow3);
        Borrow borrow4 = new Borrow(people.get(4), books.get(3));
        bookRepository.changeAvailabilityOfBookToBorrowed(books.get(3));
        borrowRepository.createBorrow(borrow4);
        borrows.add(borrow4);
        return borrows;
    }

    public List<Return> initializeReturns(List<Borrow> borrows) {
        List<Return> returns = new ArrayList<>();
        Return ret = new Return(borrows.get(3));
        returnRepository.createReturn(ret);
        return returns;
    }

    public void removeExpiredReservations() {
        List<Reservation> reservations = reservationRepository.getReservationByEndDate(new Date());
        for (Reservation r : reservations) {
            reservationRepository.deleteReservation(r);
        }
    }

}
