package Database.Repository;

import Database.DatabaseAccess;
import models.entities.Book;
import models.entities.Person;
import models.entities.Reservation;
import models.tables.ReservationTable;

import javax.management.openmbean.SimpleType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationRepository {
    DatabaseAccess databaseAccess;
    private static ReservationRepository instance;

    public static ReservationRepository getInstance() {
        if (instance == null) {
            instance = new ReservationRepository();
        }
        return instance;
    }

    public ReservationRepository() {
        databaseAccess = DatabaseAccess.getInstance();
    }

    public List<ReservationTable> getTableReservations() {
        List<ReservationTable> reservations = new ArrayList<>();
        String query = String.format("select b.bookName,b.author,r.person,r.reservationDate,r.endOfReservationDate " +
                "from Reservation r,Book b where b.id=r.book order by r.endOfReservationDate,b.bookName,r.reservationDate");
        List<Object[]> objects = databaseAccess.getQueryAsTableOfObjects(query);
        for (Object[] o : objects) {
            ReservationTable reservation = new ReservationTable();
            reservation.setTittle(o[0].toString());
            reservation.setAuthor(o[1].toString());
            Person person = (Person) o[2];
            reservation.setClientName(person.getName() + " " + person.getLastName());
            reservation.setDate((Date) o[3]);
            reservation.setEndOfReservationDate((Date) o[4]);
            reservations.add(reservation);
        }
        return reservations;
    }

    public List<ReservationTable> getAllUserReservations(Long id) {
        List<ReservationTable> reservations = new ArrayList<>();
        String query = "select b.bookName,b.author,b.category,b.ISBN,r.reservationDate from Reservation r, " +
                "Book b where r.book = b.id AND r.person = " + id;
        List<Object[]> objects = databaseAccess.getQueryAsTableOfObjects(query);
        for (Object[] o : objects) {
            ReservationTable reservationTable = new ReservationTable();
            reservationTable.setTittle(o[0].toString());
            reservationTable.setAuthor(o[1].toString());
            reservationTable.setCategory(o[2].toString());
            reservationTable.setISBN(o[3].toString());
            reservationTable.setDate((Date) o[4]);
            reservations.add(reservationTable);
        }
        return reservations;
    }

    public List<Reservation> getFullReservations() {
        String query = String.format("from Reservation");
        List<Reservation> reservations = reservationList(databaseAccess.getQuery(query));
        System.out.println(reservations);
        return reservations;
    }

    public Boolean isBookAlreadyReserved(Long id, String title) {
        String query = "from Reservation r, Book b where r.person = "
                + id + "AND r.book=b.id AND b.bookName = '" + title + "'";
        return databaseAccess.getQuery(query).isEmpty();
    }

    public Reservation findReservationByBook(Book book) {
        String query = "from Reservation r where r.book = " + book.getId();
        return (Reservation) databaseAccess.getQuery(query).get(0);
    }

    private List<Reservation> reservationList(List<Object> objects) {
        List<Reservation> reservations = new ArrayList<>();
        for (Object o : objects) {
            if (o instanceof Reservation) {
                reservations.add((Reservation) o);
            }
        }
        return reservations;
    }

    public Reservation getReservation(String title,String author,String clientName,Date reservationDate){
        String [] splitAuthor = author.split(" ");
        String [] splitClient = clientName.split(" ");
        String query = "select r from Reservation r,Book b,Person p,Author a where r.book=b.id and r.person=p.id and " +
                "b.author=a.id and a.authorName='"+splitAuthor[0]+"' and a.lastName='"+splitAuthor[1]+"' and p.name='"+
                splitClient[0]+"' and p.lastName='"+splitClient[1]+"' and b.bookName='"+title+"' " +
                "and r.reservationDate='"+reservationDate+"'";
        return getOneReservation(databaseAccess.getQuery(query));
    }

    public void updateReservation(Reservation reservation) {
        databaseAccess.update(reservation);
    }

    private Reservation getOneReservation(List<Object> reservationList){
        if (!reservationList.isEmpty()) return (Reservation) reservationList.get(0);
        return null;
    }

    public List<Reservation> getReservationByEndDate(Date date){
        String query = "from Reservation where endOfReservationDate<='"+date+"'";
        return reservationList(databaseAccess.getQuery(query));
    }
    public void createReservation(Reservation reservation) {
        databaseAccess.save(reservation);
    }

    public void deleteReservation(Reservation reservation) {
        databaseAccess.delete(reservation);
    }

}
