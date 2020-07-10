package controller;

import entities.Contact;
import entities.ContactType;
import entities.Person;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import javax.jws.WebService;
import java.util.List;

@WebService(name = "PhoneService", endpointInterface = "controller.PhoneServiceInterface")
public class PhoneService implements PhoneServiceInterface{

    @Override
    public Contact createContact(Contact contact){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the object
            session.save(contact);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return contact;
    }

    //get all contacts from database
    @Override
    public Contact[] getContacts(){
        List<Contact> contacts = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            contacts = session.createQuery("from Contact", Contact.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //convert list to array
        Contact[] contactsArray = new Contact[contacts.size()];
        for(int i = 0; i < contactsArray.length; i++){
            contactsArray[i] = contacts.get(i);
        }

        return contactsArray;
    }

    //delete a contact by id
    @Override
    public Contact deleteContact(int id){
        Contact contact = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            contact = session.get(Contact.class, id);
            Transaction transaction = session.beginTransaction();
            session.delete(contact);
            session.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }

    @Override
    public Contact modifyContact(Contact contact){
        Contact contactToModify = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            //get the contact using the supplied id
            contactToModify = session.get(Contact.class, contact.getId());

            //modify all the values
            ContactType contactTypeToModify = contactToModify.getContactType();
            Person personToModify = contactToModify.getPerson();
            contactTypeToModify.setType(contact.getContactType().getType());
            personToModify.setFirstName(contact.getPerson().getFirstName());
            personToModify.setLastName(contact.getPerson().getLastName());
            personToModify.setMiddleName(contact.getPerson().getMiddleName());
            personToModify.setPosition(contact.getPerson().getPosition());
            contactToModify.setNumber(contact.getNumber());

            //save modifications
            session.update(contactToModify);
            session.flush();
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactToModify;
    }

}
