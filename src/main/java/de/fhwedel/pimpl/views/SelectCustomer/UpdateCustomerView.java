package de.fhwedel.pimpl.views.SelectCustomer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

import java.util.Optional;

@Route("/update-customer")
@SpringComponent
@UIScope
public class UpdateCustomerView extends Composite<Component> implements BeforeEnterObserver {

    private IntegerField customerNumber = new IntegerField();
    @PropertyId("salutation")
    private ComboBox<Customer.Salutation> customerSalutation = new ComboBox<>(null, Customer.Salutation.values());
    @PropertyId("surname")
    private TextField customerSurname = new TextField();
    @PropertyId("prename")
    private TextField customerPrename = new TextField();
    private IntegerField customerDiscount = new IntegerField();
    private FormLayout customerForm = new FormLayout();
    private Button customerUpdate = new Button("Kunde aktualisieren");
    private Button customerDelete = new Button("Kunde löschen");

    private HorizontalLayout customerControl = new HorizontalLayout(customerUpdate, customerDelete);

    private VerticalLayout customersForm = new VerticalLayout(
            new HeadlineSubheadlineView("Kunde aktualisieren", "Aktualisiere den bestehenden Kunden.", Constants.HEADLINE_1),
            new HeadlineSubheadlineView("Allgemein", "Aktualisiere die allgemeinen Daten des Kunden.", Constants.HEADLINE_2),
            customerForm,
            customerControl);
    private VerticalLayout view;


    private CustomerRepo customerRepo;
    private AddressView addressView;

    public UpdateCustomerView(CustomerRepo repo, AddressView addressView) {
        this.customerRepo = repo;
        this.addressView = addressView;

        customerForm.addFormItem(customerNumber, "Kundennummer");
        customerForm.addFormItem(customerSalutation, "Anrede");
        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(customerDiscount, "Rabatt");

        view = new VerticalLayout(customersForm, addressView);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Integer id = Integer.valueOf(event.getLocation().getQueryParameters().getParameters().get("id").get(0));
        Optional<Customer> customerOptional = this.customerRepo.findById(id);
        Customer customer = null;
        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
            this.customerNumber.setValue(customer.getCustomerNumber());
            this.customerSalutation.setValue(customer.getSalutation());
            this.customerSurname.setValue(customer.getSurname());
            this.customerPrename.setValue(customer.getPrename());
        }
        System.out.println(customer);
    }

    @Override
    protected Component initContent() {
        return view;
    }
}
