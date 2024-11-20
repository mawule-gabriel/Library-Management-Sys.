package Entity;

import java.sql.Date;
import java.time.LocalDate;

public class Patron {

    private int patronId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate membershipDate;

    // Constructor with all parameters
    public Patron(int patronId, String firstName, String lastName, String email, String phoneNumber, String address, LocalDate membershipDate) {
        this.patronId = patronId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.membershipDate = membershipDate;
    }

    // Getters and Setters
    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    // Here, we use `Date.valueOf()` to convert LocalDate to java.sql.Date for database insertion
    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    @Override
    public String toString() {
        return "Patron{" +
                "patronId=" + patronId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", membershipDate=" + membershipDate +
                '}';
    }
}
