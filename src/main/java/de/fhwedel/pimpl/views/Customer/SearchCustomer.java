package de.fhwedel.pimpl.views.Customer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@Route(Routes.CUSTOMER_START)
@SpringComponent
@UIScope
public class SearchCustomer extends Composite<Component> implements BeforeEnterObserver {

	private TextField customersQuery = new TextField();
	private Button customersSearch = new Button("Suchen", event -> search( Optional.of(customersQuery.getValue() ) ));
	private Grid<Customer> customers = new Grid<>();
	private Button navigateToCreateNewCustomer = new Button("Neuen Kunden anlegen", event -> createNewCustomer());
	private VerticalLayout customersForm = new VerticalLayout(
			new Header("Kunde suchen", "Suche den Kunden durch Eingabe des Nachnamens."),
			customersQuery, customersSearch, customers, navigateToCreateNewCustomer);
	private VerticalLayout view;
	private CustomerRepo customerRepo;

	public SearchCustomer(CustomerRepo customerRepo) {
		this.customerRepo = customerRepo;

		customers.addColumn(Customer::getCustomerNumber).setHeader("Kundennummer").setSortable(true);
		customers.addColumn(Customer::getSurname).setHeader("Name").setSortable(true);
		customers.setSelectionMode(SelectionMode.SINGLE);
		customers.setHeight("200px");
		customers.setWidth("700px");
		customers.addSelectionListener( event -> {
			if(event.getFirstSelectedItem().isPresent()) {
				Customer customer = event.getFirstSelectedItem().get();
				GlobalState.getInstance().setCurrentCustomer(customer);
				UI.getCurrent().navigate(Routes.CUSTOMER_UPDATE);
			}
		});

		this.navigateToCreateNewCustomer.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
		this.navigateToCreateNewCustomer.setIconAfterText(true);
		this.navigateToCreateNewCustomer.setEnabled(false);

		view = new VerticalLayout(customersForm);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		this.customers.setItems();
	}

	@Override
	protected Component initContent() {
		return view;
	}

	private void search(Optional<String> query) {
		List<Customer> items = query.map(str -> customerRepo.findBySurnameContainingIgnoreCase(str)).orElse(Collections.emptyList());
		customers.setItems(DataProvider.ofCollection(items));
		this.navigateToCreateNewCustomer.setEnabled(items.isEmpty());
	}

	private void createNewCustomer() {
		UI.getCurrent().navigate("/create-new-customer");
		this.navigateToCreateNewCustomer.setEnabled(false);
	}



}
