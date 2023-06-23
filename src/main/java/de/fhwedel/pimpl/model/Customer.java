package de.fhwedel.pimpl.model;

import de.fhwedel.pimpl.Utility.GenerateUniqueNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Integer id;

	@NotNull(message = "Pflichtangabe")
	@Column(unique=true, updatable = false)
	private String customerNumber;

	@NotNull(message = "Pflichtangabe")
	@Size(min = 2, message = "Mindestens zwei Zeichen Länge")
	private String surname;

	@NotNull(message = "Pflichtangabe")
	@Size(min = 2, message = "Mindestens zwei Zeichen Länge")
	private String prename;

	@NotNull(message = "Pflichtangabe")
	@Size(min = 1, message = "Mindestens ein Zeichen Länge")
	private String street;

	@NotNull(message = "Pflichtangabe")
	@Size(min = 1, message = "Mindestens ein Zeichen Länge")
	private String zip;

	@NotNull(message = "Pflichtangabe")
	@Size(min = 1, message = "Mindestens ein Zeichen Länge")
	private String city;

	@NotNull(message = "Pflichtangabe")
	@Min(value = 0, message = "Mindest Discount")
	@Max(value = 100, message = "Max Discount")
	private Integer discount = 0;


	public Customer() {}

	public Customer(String surname, String prename, String street, String zip, String city, Integer discount) {
		this.customerNumber = GenerateUniqueNumber.createUniqueIdentifier();
		this.surname = surname;
		this.prename = prename;
		this.street = street;
		this.zip = zip;
		this.city = city;
		this.discount = discount;
	}

	public Integer getId() {
		return id;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPrename() {
		return prename;
	}

	public void setPrename(String prename) {
		this.prename = prename;
	}

	public String getStreet() { return this.street; }

	public void setStreet(String street) { this.street = street; }

	public String getZip() {
		return this.zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return "Customer{" +
				"customerNumber=" + customerNumber +
				", surname='" + surname + '\'' +
				", prename='" + prename + '\'' +
				", street='" + street + '\'' +
				", zip='" + zip + '\'' +
				", city='" + city + '\'' +
				", discount='" + discount + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		return Objects.equals(customerNumber, customer.customerNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerNumber);
	}

}
