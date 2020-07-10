package entities;

import javax.persistence.*;

@Entity
@Table(name = "Contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Person person;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private ContactType contactType;

    @Column(name = "number")
    private String number;

    public Contact() {
    }

    public Contact(Person person, ContactType contactType, String number) {
        this.person = person;
        this.contactType = contactType;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("%d.%s %s %s/ %s/%s : %s",
                id, person.getFirstName(), person.getMiddleName(),
                person.getLastName(), contactType.getType(), person.getPosition(), number);
    }
}
