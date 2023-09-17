package de.fhwedel.pimpl.views.Customer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Route(Routes.SEARCH_CUSTOMER)
@SpringComponent
@UIScope
public class SearchCustomer extends Composite<Component> implements BeforeEnterObserver {

    private final TextField customersQuery = new TextField();
    private final Grid<Customer> customers = new Grid<>();
    private final ForwardButton forwardButton = new ForwardButton("Neuen Kunden erstellen", event -> Routes.navigateTo(Routes.CREATE_CUSTOMER));

    Button customersSearch = new Button("Suchen", event -> search(Optional.of(customersQuery.getValue())));
    private final CustomerRepo customerRepo;
    private final PageLayout pageLayout = new PageLayout("Kunde suchen", "Suche den Kunden durch Eingabe des Nachnamens.",
            customersQuery, customersSearch, customers, forwardButton);

    public SearchCustomer(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
        this.forwardButton.setEnabled(true);
        this.initCustomersTable();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        GlobalState.getInstance().setCurrentBookingID(-1);
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initCustomersTable() {
        customers.addColumn(Customer::getPrename).setHeader("Vorname").setSortable(true);
        customers.addColumn(Customer::getSurname).setHeader("Name").setSortable(true);
        customers.addColumn(Customer::getCustomerNumber).setHeader("Kundennummer").setSortable(true);
        customers.setSelectionMode(SelectionMode.SINGLE);
        customers.setHeight("200px");
        customers.setWidth("700px");
        customers.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                Customer customer = event.getFirstSelectedItem().get();
                GlobalState.getInstance().setCurrentCustomerID(customer.getId());
                Routes.navigateTo(Routes.UPDATE_CUSTOMER);
            }
        });
    }

    private void search(Optional<String> query) {
        List<Customer> items = query.map(q -> customerRepo.findBySurnameContainingIgnoreCaseOrPrenameContainingIgnoreCaseOrCustomerNumberContainingIgnoreCase(q, q, q))
                .orElse(Collections.emptyList());
        customers.setItems(DataProvider.ofCollection(items));
    }


}
