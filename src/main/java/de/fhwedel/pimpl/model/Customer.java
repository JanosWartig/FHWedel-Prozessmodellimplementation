package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.Random;

@Entity
public class Customer {

	private static int runnumber = 1;

	private static String createRandomName() {
		Random r = new Random();
		int len = r.nextInt(8) + 2;
		StringBuilder sb = new StringBuilder();

		sb.append((char) (r.nextInt(26) + 'A'));
		while (len-- > 0) {
			sb.append((char) (r.nextInt(26) + 'a'));
		}

		return sb.toString();
	}

	public static Customer createRandomCustomer() {
		Random r = new Random();
		return new Customer(createRandomName(), createRandomName(), createRandomName(), createRandomName(), createRandomName());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Integer id;

	@Column(name = "customer_number")
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

	public Customer(String surname, String prename, String street, String zip, String city) {
		this();
		this.customerNumber = String.valueOf(Integer.valueOf("1" + (runnumber++)));
		this.surname = surname;
		this.prename = prename;
		this.street = street;
		this.zip = zip;
		this.city = city;
	}

	public Integer getId() {
		return id;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
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
