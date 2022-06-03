package Database.Repository;

import Database.DatabaseAccess;
import models.entities.Person;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    private SessionFactory sessionFactory;
    private DatabaseAccess databaseAccess = DatabaseAccess.getInstance();
    private static PersonRepository instance;

    public static PersonRepository getInstance() {
        if (instance == null) {
            instance = new PersonRepository();
        }
        return instance;
    }

    public PersonRepository() {
        this.databaseAccess = DatabaseAccess.getInstance();
    }


    public Person findByEmail(String email) {
        String query = String.format("from Person p where p.email = '%s'", email);
        String query1 = String.format("SELECT person from Person as person where person.email= '%s'", email);
        return getOneClient(databaseAccess.getQuery(query1));
    }


    public Person findByNameLastNameEmailRole(String name, String lastName, String email, String role) {
        String query = String.format("from Person p where p.name='%s' and p.lastName='%s' and p.email='%s' and p.role='%s'", name, lastName, email, role);
        return getOneClient(databaseAccess.getQuery(query));
    }

    public List<Person> getAllClients() {
        String query = String.format("from Person");
        List<Person> personList = clientList(databaseAccess.getQuery(query));
        return personList;
    }


    private List<Person> clientList(List<Object> objects) {
        List<Person> people = new ArrayList<>();
        for (Object o : objects) {
            if (o instanceof Person) {
                people.add((Person) o);
            }
        }
        return people;
    }


    private Person getOneClient(List<Object> clientList) {
        if (!clientList.isEmpty()) return (Person) clientList.get(0);
        return null;
    }

    public void createClient(Person person) {
        databaseAccess.save(person);
    }

    public void deleteClient(Person person) {
        databaseAccess.delete(person);
    }

    public void updateClient(Person person) {
        databaseAccess.update(person);
    }
}
