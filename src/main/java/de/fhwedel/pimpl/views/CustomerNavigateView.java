package de.fhwedel.pimpl.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.model.Address;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;
import de.fhwedel.pimpl.views.SelectCustomer.AddressView;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class CustomerNavigateView extends Composite<Component> {

	private TextField customersQuery = new TextField();
	private Button customersSearch = new Button("Suchen", event -> search( Optional.of(customersQuery.getValue()) ));
	private Grid<Customer> customers = new Grid<>();

	private Button nextView = new Button("Was geht", event -> UI.getCurrent().navigate("/search"));

	private Label headline = new Label("Kunden suchen");
	private VerticalLayout customersForm = new VerticalLayout(headline, customersQuery, customersSearch, nextView, customers);

	private CustomerView customerView;

	private Grid<Address> addresses = new Grid<>();
	private AddressView addressView;

	private VerticalLayout view;

	private CustomerRepo customerRepo;


	public CustomerNavigateView(CustomerView customerView, AddressView addressView, CustomerRepo customerRepo) {
		this.customerView = customerView;
		this.addressView = addressView;
		this.customerRepo = customerRepo;

		customers.addColumn(Customer::getCustomerNumber).setHeader("Kundennummer").setSortable(true);
		customers.addColumn(Customer::getSurname).setHeader("Name").setSortable(true);
		customers.setSelectionMode(SelectionMode.SINGLE);
		customers.setHeight("300px");
		customers.addSelectionListener( event -> { if(event.getFirstSelectedItem().isPresent()) selectCustomer(event.getFirstSelectedItem()); } );

		addresses.addColumn(Address::getStreet).setHeader("Strasse");
		addresses.addColumn(Address::getZip).setHeader("PLZ");
		addresses.addColumn(Address::getCity).setHeader("Ort");
		addresses.setSelectionMode(SelectionMode.SINGLE);
		addresses.setHeight("300px");
		addresses.addSelectionListener( event -> selectAddress(event.getFirstSelectedItem()) );

		this.headline.getStyle()
				.set("font-weight", "bold")
				.set("font-size", "24px");

		view = new VerticalLayout(customersForm, customerView, addresses, addressView);

		selectCustomer(Optional.empty());

		customerView.listenToNew( () -> showAddresses( Optional.empty() ) );
		customerView.listenToUpdate( customer -> {
			search( Optional.of(customersQuery.getValue()) );
			selectCustomer( Optional.of(customer) );
		});
		customerView.listenToDelete( () -> {
			search( Optional.of(customersQuery.getValue()) );
			selectCustomer( Optional.empty() );
		} );
		addressView.listenToUpdate( addr -> showAddresses(Optional.of(addr.getCustomer())) );
		addressView.listenToDelete( () -> showAddresses(customerView.getCustomer()));
	}

	@Override
	protected Component initContent() {
		return view;
	}

	public void search(Optional<String> query) {
		List<Customer> items = query.map(str -> customerRepo.findBySurnameContaining(str)).orElse(Collections.emptyList());
		customers.setItems(DataProvider.ofCollection(items));
	}

	public void selectCustomer(Optional<Customer> customer) {
		customerView.setCustomer(customer);
		showAddresses(customer);
	}

	public void showAddresses(Optional<Customer> customer) {
		customer = customer.flatMap(cust -> customerRepo.findById(cust.getId()));
		addresses.setItems(DataProvider.ofCollection(customer.map(c -> c.getAddresses()).orElse(Collections.emptySet())));
		addressView.setAddress(Optional.empty());
		addressView.setCustomer(customer);
	}

	public void selectAddress(Optional<Address> address) {
		addressView.setAddress(address);
	}

}
