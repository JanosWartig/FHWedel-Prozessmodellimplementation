package de.fhwedel.pimpl.views.SelectCustomer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.ForwardBackwardNavigationView;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;
import de.fhwedel.pimpl.model.Address;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.AddressRepo;
import de.fhwedel.pimpl.repos.CustomerRepo;
import de.fhwedel.pimpl.views.actions.Actions;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class AddressView extends Composite<Component> {


	private FormLayout addressForm = new FormLayout();

	private Button addressNew = new Button("Neue Adresse hinzufügen", this::onAddrNewClick);
	private Button addressSave = new Button("Adresse aktualisieren", this::onAddrSafeClick);
	private Button addressReset = new Button("Rückgängig", this::onAddrResetClick);
	private Button addressDelete = new Button("Adresse löschen", this::onAddrDeleteClick);
	private Button testToTorder = new Button("Auftragsansicht", this::onSearchOrderClick);

	private HorizontalLayout addressControl = new HorizontalLayout(addressNew, addressSave, addressReset, addressDelete, testToTorder);

	private VerticalLayout view = new VerticalLayout();

	private Optional<Address> address = Optional.empty();
	private Optional<Customer> customer = Optional.empty();
	private CustomerRepo customerRepo;
	private AddressRepo addressRepo;
	private Binder<Address> binder = new BeanValidationBinder<>(Address.class);
	private Set<Runnable> listenersNew = new HashSet<>();
	private Set<Consumer<Address>> listenersUpdate = new HashSet<>();
	private Set<Runnable> listenersDelete = new HashSet<>();
	private Actions actions;

	private Grid<Address> addresses = new Grid<>();

	public AddressView(CustomerRepo customerRepo, AddressRepo addressRepo, Actions actions) {
		this.customerRepo = customerRepo;
		this.addressRepo = addressRepo;
		this.actions = actions;

		addresses.addColumn(Address::getStreet).setHeader("Strasse");
		addresses.addColumn(Address::getZip).setHeader("PLZ");
		addresses.addColumn(Address::getCity).setHeader("Ort");
		addresses.setSelectionMode(Grid.SelectionMode.SINGLE);
		addresses.setHeight("300px");
		addresses.addSelectionListener( event -> selectAddress(event.getFirstSelectedItem()) );

		this.view.add(new HeadlineSubheadlineView("Addressen", "Aktualisiere die bestehenden Addressen zu dem Kunden.", Constants.HEADLINE_2));
		this.view.add(addresses);
		this.view.add(addressForm);
		this.view.add(addressControl);
		this.view.add(new ForwardBackwardNavigationView("Ein Zimmer auswählen", Routes.CUSTOMER_START));

		binder.bindInstanceFields(this);
	}

	@Override
	protected Component initContent() {
		return view;
	}

	public void selectAddress(Optional<Address> address) {
		this.setAddress(address);
	}

	public void listenToNew(Runnable listener) {
		listenersNew.add(listener);
	}

	public void listenToDelete(Runnable listener) {
		listenersDelete.add(listener);
	}

	public void listenToUpdate(Consumer<Address> listener) {
		listenersUpdate.add(listener);
	}

	public void setAddress(Optional<Address> address) {
		this.address = address;
		this.customer = address.map(addr -> addr.getCustomer());
		binder.readBean(address.orElse(null));
		refreshControls();
	}

	public Optional<Address> getAddress() {
		return address;
	}

	public void setCustomer(Optional<Customer> customer) {
		this.address = Optional.empty();
		this.customer = customer;
		refreshControls();
	}

	public Optional<Customer> getCustomer() {
		return customer;
	}

	private void refreshControls() {
		addressForm.setEnabled(address.isPresent());
		addressNew.setEnabled(customer.map(c -> c.getId() != null).orElse(false));
		addressSave.setEnabled(address.isPresent());
		addressReset.setEnabled(address.isPresent());
		addressDelete.setEnabled(address.map(a -> a.getId() != null).orElse(false));
	}


	private void onAddrNewClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		customer.ifPresent(c -> {
			Optional<Address> newAddress = Optional.of(new Address());
			address = newAddress;
			listenersNew.forEach(Runnable::run);
			refreshControls();
		});
	}

	private void onAddrSafeClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		address.ifPresent(addr -> {
			customer.ifPresent(cust -> {
				if (binder.writeBeanIfValid(addr)) {
					Address addressSaved = addressRepo.save(addr);
					if (addr.getCustomer() == null) {
						Customer customerSaved = customerRepo.save(cust);
						setCustomer(Optional.of(customerSaved));
					}
					refreshControls();
					listenersUpdate.forEach(l -> l.accept(addressSaved));
				}
			});
		});
	}

	private void onAddrResetClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		setAddress(address);
	}

	private void onAddrDeleteClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		address.ifPresent(a -> {
			Dialog d = new Dialog();
			d.setCloseOnEsc(true);
			d.add(new Label("Adresse wirklich löschen?"));
			d.add(new HorizontalLayout(new Button("Ja, wirklich", ev -> {
				d.close();
				Customer c = a.getCustomer();
				customer = Optional.of(customerRepo.save(c));
				setAddress(Optional.empty());
				listenersDelete.forEach(Runnable::run);
			}), new Button("Oops, lieber doch nicht", ev -> d.close())));
			d.open();
		});
	}

	private void onSearchOrderClick(ClickEvent<Button> buttonClickEvent) {
		actions.triggerRequestOrderView(LocalDate.now());
	}

}
