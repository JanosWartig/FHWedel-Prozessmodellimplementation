package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

@Entity
public class Customer {

	public enum Salutation {
		FEMALE {
			@Override
			public String toString() { return "weiblich"; }
		},
		MALE {
			@Override
			public String toString() { return "männlich"; }
		}
	};

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
		return new Customer(Salutation.values()[r.nextInt(2)], Integer.valueOf("1" + (runnumber++)), createRandomName(),
				createRandomName());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "cust_id")
	private Integer id;

	private Integer customerNumber;

	@NotNull(message = "Pflichtangabe")
	private Salutation salutation;

	@NotNull(message = "Pflichtangabe")
	@Size(min = 2, message = "Mindestens zwei Zeichen Länge")
	private String surname;

	@NotNull(message = "Pflichtangabe")
	@Size(min = 2, message = "Mindestens zwei Zeichen Länge")
	private String prename;

	@NotNull
	@OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Address> addresses;

	public Customer() {
		this.addresses = new HashSet<>();
	}

	public Customer(Salutation salutation, Integer customerNumber, String surname, String prename) {
		this();
		this.salutation = salutation;
		this.customerNumber = customerNumber;
		this.surname = surname;
		this.prename = prename;
	}

	public Integer getId() {
		return id;
	}

	public Salutation getSalutation() {
		return salutation;
	}

	public void setSalutation(Salutation salut) {
		this.salutation = salut;
	}

	public Integer getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(Integer customerNumber) {
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

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void addAddress(Address addr) {
		addr.setCustomer(this);
		this.addresses.add(addr);
	}

	public void removeAddress(Address addr) {
		this.addresses.remove(addr);
		addr.setCustomer(null);
	}

	@Override
	public String toString() {
		return "Customer{" +
				"customerNumber=" + customerNumber +
				", salutation=" + salutation +
				", surname='" + surname + '\'' +
				", prename='" + prename + '\'' +
				", addresses=" + addresses +
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
