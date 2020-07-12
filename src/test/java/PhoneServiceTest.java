import controller.PhoneService;
import entities.Contact;
import entities.ContactType;
import entities.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import utils.HibernateUtil;

public class PhoneServiceTest {

    private static PhoneService ps;
    private Contact contact1;
    private Contact contact2;
    private Contact contact3;

    @BeforeAll
    public static void createPhoneService(){
        ps = new PhoneService();
    }

    @BeforeEach
    public void createContacts(){

        contact1 = new Contact(new Person("John", "Doe", "Johnny", "test"),
                new ContactType("dummy"), "11111111111");

        contact2 = new Contact(new Person("Arthur", "Morgan", "Cowpoke", "test"),
                new ContactType("dummy"), "22222222222");

        contact3 = new Contact(new Person("Rick", "Sanchez", "C-137", "test"),
                new ContactType("dummy"), "33333333333");

        ps.createContact(contact1);
        ps.createContact(contact2);
        ps.createContact(contact3);

    }

    @AfterAll
    public static void deleteTestContacts(){
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.createQuery("delete from entities.Contact where number " +
                        "like '' or number like '11111111111' or number like '22222222222'" +
                        "or number like '33333333333'").executeUpdate();

                session.createQuery("delete from entities.ContactType where type like 'dummy'").executeUpdate();

                session.createQuery("delete from entities.Person where position " +
                        "like 'test'").executeUpdate();
                session.flush();
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }


    @Test
    public void testCreate(){
        Contact contact11 = null;
        Contact contact22 = null;
        Contact contact33 = null;

        //load created contacts from db
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            contact11 = session.get(Contact.class, contact1.getId());
            contact22 = session.get(Contact.class, contact2.getId());
            contact33 = session.get(Contact.class, contact3.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //check if values are the same
        assertEquals(contact1.getNumber(), contact11.getNumber());
        assertEquals(contact2.getNumber(), contact22.getNumber());
        assertEquals(contact3.getNumber(), contact33.getNumber());

        assertEquals(contact1.getPerson().getFirstName(), contact11.getPerson().getFirstName());
        assertEquals(contact2.getPerson().getFirstName(), contact22.getPerson().getFirstName());
        assertEquals(contact3.getPerson().getFirstName(), contact33.getPerson().getFirstName());

        assertEquals(contact1.getContactType().getType(), contact11.getContactType().getType());
        assertEquals(contact2.getContactType().getType(), contact22.getContactType().getType());
        assertEquals(contact3.getContactType().getType(), contact33.getContactType().getType());
    }

    @Test
    public void testGetContacts(){

        Contact[] contacts = ps.getContacts();
        boolean contact1InArray = false;
        boolean contact2InArray = false;
        boolean contact3InArray = false;

        for(Contact c : contacts){
            if(c.getNumber().equals(contact1.getNumber())) contact1InArray = true;
            if(c.getNumber().equals(contact2.getNumber())) contact2InArray = true;
            if(c.getNumber().equals(contact3.getNumber())) contact3InArray = true;
        }

        assertTrue(contact1InArray);
        assertTrue(contact2InArray);
        assertTrue(contact3InArray);
    }

    @Test
    public void testModifyContact(){

        //modify a contact
        contact1.setNumber("44444444444");
        contact1.getPerson().setFirstName("Barack");
        contact1.getPerson().setLastName("Obama");
        System.out.println(contact1.getId() + contact2.getId() + contact3.getId());
        ps.modifyContact(contact1);

        //get modified contact from database
        Contact modifiedContact = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            modifiedContact = session.get(Contact.class, contact1.getId());
            Transaction transaction = session.beginTransaction();
            session.delete(modifiedContact);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //make sure the contact was modified correctly
        assertEquals(contact1.getNumber(), modifiedContact.getNumber());
        assertEquals(contact1.getPerson().getFirstName(), modifiedContact.getPerson().getFirstName());
        assertEquals(contact1.getPerson().getLastName(), modifiedContact.getPerson().getLastName());
    }

    @Test
    public void testDeleteContact(){

        ps.deleteContact(contact1.getId());
        ps.deleteContact(contact2.getId());
        ps.deleteContact(contact3.getId());

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            assertTrue(session.createQuery("from entities.Contact where " +
                    "number like '11111111111' or number like '22222222222' or number like '33333333333'")
                    .list().isEmpty());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
