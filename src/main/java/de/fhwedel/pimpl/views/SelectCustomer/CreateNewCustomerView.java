package de.fhwedel.pimpl.views.SelectCustomer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.components.ForwardBackwardNavigationView;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;
import de.fhwedel.pimpl.model.Customer;

@Route("/create-new-customer")
@SpringComponent
@UIScope
public class CreateNewCustomerView extends Composite<Component> {

    private IntegerField customerNumber = new IntegerField();
    @PropertyId("salutation")
    private ComboBox<Customer.Salutation> customerSalutation = new ComboBox<>(null, Customer.Salutation.values());
    @PropertyId("surname")
    private TextField customerSurname = new TextField();
    @PropertyId("prename")
    private TextField customerPrename = new TextField();
    private TextField customerStreet = new TextField();
    private TextField customerZIP = new TextField();
    private TextField customerCountry = new TextField();
    private IntegerField customerDiscount = new IntegerField();
    private FormLayout customerForm = new FormLayout();
    private Button customerNew = new Button("Neuer Kunde");
    private Button customerSave = new Button("Kunde sichern");
    private Button customerReset = new Button("Rückgängig");
    private Button customerDelete = new Button("Kunde löschen");
    private HorizontalLayout customerControl = new HorizontalLayout(customerNew, customerSave, customerReset, customerDelete);

    private VerticalLayout customersForm = new VerticalLayout(
            new HeadlineSubheadlineView("Kunde anlegen", "Erstelle einen neuen Kunden.", Constants.HEADLINE_1),
            customerForm,
            customerControl,
            new ForwardBackwardNavigationView("Ein Zimmer auswählen", "search"));
    private VerticalLayout view;

    public CreateNewCustomerView() {
        customerForm.addFormItem(customerNumber, "Kundennummer");
        customerForm.addFormItem(customerSalutation, "Anrede");
        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(customerStreet, "Straße");
        customerForm.addFormItem(customerZIP, "PLZ");
        customerForm.addFormItem(customerCountry, "Land");
        customerForm.addFormItem(customerDiscount, "Rabatt");

        view = new VerticalLayout(customersForm);
    }

    @Override
    protected Component initContent() {
        return view;
    }
}
