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
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.ForwardBackwardNavigationView;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

@Route(Routes.CUSTOMER_CREATE)
@SpringComponent
@UIScope
public class CreateNewCustomerView extends Composite<Component> {

    private IntegerField customerNumber = new IntegerField();
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
            "Ein Zimmer auswählen und Kunde anlegen", Routes.CUSTOMER_START
    );

    private VerticalLayout customersForm = new VerticalLayout(
            new HeadlineSubheadlineView("Kunde anlegen", "Erstelle einen neuen Kunden.", Constants.HEADLINE_1),
            customerForm,
           forwardBackwardNavigationView);
    private VerticalLayout view;

    private CustomerRepo customerRepo;

    public CreateNewCustomerView(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;

        customerForm.addFormItem(customerNumber, "Kundennummer");
        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(customerStreet, "Straße");
        customerForm.addFormItem(customerZIP, "PLZ");
        customerForm.addFormItem(customerCity, "Land");
        customerForm.addFormItem(customerDiscount, "Rabatt");

        this.forwardBackwardNavigationView.getForward().addClickListener(event -> {
            this.createNewCustomer();
        });

        this.forwardBackwardNavigationView.setForward(true);

        view = new VerticalLayout(customersForm);
    }

    @Override
    protected Component initContent() {
        return view;
    }

    private void createNewCustomer() {
        Customer customer = new Customer();
        customer.setCustomerNumber(this.customerNumber.getValue());
        customer.setSurname(this.customerSurname.getValue());
        customer.setPrename(this.customerPrename.getValue());
        customer.setStreet(this.customerStreet.getValue());
        customer.setZip(this.customerZIP.getValue());
        customer.setCity(this.customerCity.getValue());

        this.customerRepo.save(customer);
    }
}
