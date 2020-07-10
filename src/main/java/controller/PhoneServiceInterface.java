package controller;

import entities.Contact;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PhoneServiceInterface {

    public String createContact(Contact contact);

    public String getContacts();

    public String deleteContact(int id);

    public String modifyContact(Contact contact);
}
