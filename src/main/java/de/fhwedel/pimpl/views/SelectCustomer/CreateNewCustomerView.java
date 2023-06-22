package de.fhwedel.pimpl.views.SelectCustomer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.ForwardBackwardNavigationView;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

@Route(Routes.CUSTOMER_CREATE)
@SpringComponent
@UIScope
public class CreateNewCustomerView extends Composite<Component> {

    @PropertyId("surname")
    private TextField customerSurname = new TextField();
    @PropertyId("prename")
    private TextField customerPrename = new TextField();
    private TextField customerStreet = new TextField();
    private TextField customerZIP = new TextField();
    private TextField customerCity = new TextField();
    private IntegerField customerDiscount = new IntegerField();
    private FormLayout customerForm = new FormLayout();

    private ForwardBackwardNavigationView forwardBackwardNavigationView = new ForwardBackwardNavigationView(
            "Zimmerkategorie auswählen und Kunde anlegen", false
    );

    private VerticalLayout customersForm = new VerticalLayout(
            new HeadlineSubheadlineView("Kunde anlegen", "Erstelle einen neuen Kunden.", Constants.HEADLINE_1),
            customerForm,
           forwardBackwardNavigationView);
    private VerticalLayout view;

    private CustomerRepo customerRepo;

    public CreateNewCustomerView(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;

        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(customerStreet, "Straße");
        customerForm.addFormItem(customerZIP, "PLZ");
        customerForm.addFormItem(customerCity, "Land");
        customerForm.addFormItem(customerDiscount, "Rabatt");

        this.forwardBackwardNavigationView.getNext().addClickListener(event -> {
            Customer newCustomer = this.createNewCustomer();
            GlobalState.getInstance().setCurrentCustomerID(newCustomer.getId());
            Routes.navigateTo(Routes.ROOM_START);
        });

        this.forwardBackwardNavigationView.setNext(true);

        view = new VerticalLayout(customersForm);
    }

    @Override
    protected Component initContent() {
        return view;
    }

    private Customer createNewCustomer() {
        Customer customer = new Customer();
        customer.setSurname(this.customerSurname.getValue());
        customer.setPrename(this.customerPrename.getValue());
        customer.setStreet(this.customerStreet.getValue());
        customer.setZip(this.customerZIP.getValue());
        customer.setCity(this.customerCity.getValue());

        this.customerRepo.save(customer);

        return customer;
    }
}
