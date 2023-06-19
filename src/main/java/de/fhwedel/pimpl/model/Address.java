package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "addr_id")
	private Integer id;

	@NotNull
	@Size(min = 1, message = "Mindestens ein Zeichen Länge")
	private String street;

	@NotNull
	@Size(min = 1, message = "Mindestens ein Zeichen Länge")
	private String zip;

	@NotNull
	@Size(min = 1, message = "Mindestens ein Zeichen Länge")
	private String city;

	@ManyToOne
	private Customer customer;

	public Address() {
	}

	public Address(String street, String zip, String city) {
		this();
		this.street = street;
		this.zip = zip;
		this.city = city;
	}

	public Integer getId() {
		return id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String plz) {
		this.zip = plz;
	}


	public String getCity() {
		return city;
	}

	public void setCity(String ort) {
		this.city = ort;
	}

	public Customer getCustomer() {
		return customer;
	}

	protected void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Address{" +
				"street='" + street + '\'' +
				", zip='" + zip + '\'' +
				", city='" + city + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Address address = (Address) o;
		return Objects.equals(id, address.id) && Objects.equals(customer, address.customer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, customer);
	}

}