package controller;

import entities.Contact;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PhoneServiceInterface {

    public Contact createContact(Contact contact);

    public Contact[] getContacts();

    public Contact deleteContact(int id);

    public Contact modifyContact(Contact contact);
}
