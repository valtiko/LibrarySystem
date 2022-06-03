package AdminTests;

import Database.Repository.PersonRepository;
import controllers.RegisterController;
import models.entities.Person;
import org.junit.Assert;
import org.junit.Test;


public class AdminUserPanelControllerTest {
    PersonRepository personRepository = PersonRepository.getInstance();
    RegisterController registerController = new RegisterController();


    @Test
    public void createUser() {
        Person person1 = new Person("Testowy", "Testowy", "Testowy@gmail.com", registerController.hashPassword("123"), Person.Role.USER);
        personRepository.createClient(person1);
        Person person = personRepository.findByEmail("Testowy@gmail.com");
        Assert.assertEquals("Test poprawnie wykonany", person, person1);
    }

    @Test
    public void deleteUser() {
        Person person1 = new Person("Testowy", "Testowy", "Testowy@gmai111l.com", registerController.hashPassword("123"), Person.Role.USER);
        personRepository.createClient(person1);
        Person person2 = personRepository.findByEmail("Testowy@gmai111l.com");
        Assert.assertEquals(person1,person2);
        personRepository.deleteClient(person1);
        Person person = personRepository.findByEmail("Testowy@gmai111l.com");
        Assert.assertNull("Test poprawnie wykonany", person);

    }




}
