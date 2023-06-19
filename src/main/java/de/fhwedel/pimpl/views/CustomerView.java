package de.fhwedel.pimpl.views;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.model.Customer.Salutation;
import de.fhwedel.pimpl.repos.CustomerRepo;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class CustomerView extends Composite<Component> {

	private IntegerField customerNumber = new IntegerField();
	@PropertyId("salutation")
	private ComboBox<Salutation> customerSalutation = new ComboBox<>(null, Salutation.values());
	@PropertyId("surname")
	private TextField customerSurname = new TextField();
	@PropertyId("prename")
	private TextField customerPrename = new TextField();
	private FormLayout customerForm = new FormLayout();

	private Button customerNew = new Button("Neuer Kunde", this::onCustNewClick);
	private Button customerSave = new Button("Kunde sichern", this::onCustSafeClick);
	private Button customerReset = new Button("Rückgängig", this::onCustResetClick);
	private Button customerDelete = new Button("Kunde löschen", this::onCustDeleteClick);
	private HorizontalLayout customerControl = new HorizontalLayout(customerNew, customerSave, customerReset, customerDelete);

	private VerticalLayout view = new VerticalLayout(customerForm, customerControl);

	private Optional<Customer> customer;
	private CustomerRepo customerRepo;
	private Binder<Customer> binder = new BeanValidationBinder<>(Customer.class);
	private Set<Runnable> listenersNew = new HashSet<>();
	private Set<Consumer<Customer>> listenersUpdate = new HashSet<>();
	private Set<Runnable> listenersDelete = new HashSet<>();

	public CustomerView(CustomerRepo customerRepo) {
		this.customerRepo = customerRepo;

		customerForm.addFormItem(customerNumber, "Kundennummer");
		customerForm.addFormItem(customerSalutation, "Anrede");
		customerForm.addFormItem(customerSurname, "Nachname");
		customerForm.addFormItem(customerPrename, "Vorname");

		binder.forField(customerNumber)
				.withValidator(custnum -> custnum != null && custnum.toString().startsWith("1"),
						"Fehlerhafte Kundennummer, muss mit 1 beginnen")
				.asRequired().bind(Customer::getCustomerNumber, Customer::setCustomerNumber);
		binder.bindInstanceFields(this);
	}

	@Override
	protected Component initContent() {
		return view;
	}

	public void listenToNew(Runnable listener) {
		listenersNew.add(listener);
	}

	public void listenToDelete(Runnable listener) {
		listenersDelete.add(listener);
	}

	public void listenToUpdate(Consumer<Customer> listener) {
		listenersUpdate.add(listener);
	}

	public void setCustomer(Optional<Customer> customer) {
		this.customer = customer.flatMap(cust -> cust.getId() != null ? customerRepo.findById(cust.getId()) : Optional.of(cust));
		binder.readBean(this.customer.orElse(null));
		refreshControls();
	}

	public Optional<Customer> getCustomer() {
		return customer;
	}

	private void refreshControls() {
		customerForm.setEnabled(customer.isPresent());
		customerSave.setEnabled(customer.isPresent());
		customerReset.setEnabled(customer.isPresent());
		customerDelete.setEnabled(customer.map(a -> a.getId() != null).orElse(false));
	}

	private void onCustNewClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		Optional<Customer> newCust = Optional.of(new Customer());
		setCustomer(newCust);
		listenersNew.forEach(Runnable::run);
	}

	private void onCustSafeClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		customer.ifPresent(c -> {
			if (binder.writeBeanIfValid(c)) {
				Customer customerSaved = customerRepo.save(c);
				setCustomer(Optional.of(customerSaved));
				listenersUpdate.forEach(l -> l.accept(customerSaved));
			}
		});
	}

	private void onCustResetClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		customer.ifPresent(c -> binder.readBean(c));
	}

	private void onCustDeleteClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		customer.ifPresent(c -> {
			Dialog d = new Dialog();
			d.setCloseOnEsc(true);
			d.add(new Label("Kunde wirklich löschen?"));
			d.add(new HorizontalLayout(new Button("Ja, wirklich", ev -> {
				d.close();
				customerRepo.delete(c);
				setCustomer(Optional.empty());
				listenersDelete.forEach(Runnable::run);
			}), new Button("Oops, lieber doch nicht", ev -> d.close())));
			d.open();
		});
	}

}
