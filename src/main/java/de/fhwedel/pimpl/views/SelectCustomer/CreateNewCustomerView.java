package de.fhwedel.pimpl.views.SelectCustomer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Notifications;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.components.Header;
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

    private Navigation navigation = new Navigation(
            "Zimmerkategorie auswählen und Kunde anlegen", false
    );

    private VerticalLayout customersForm = new VerticalLayout(
            new Header("Kunde anlegen", "Erstelle einen neuen Kunden.", Constants.HEADLINE_1),
            customerForm,
            navigation);
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

        this.navigation.getFinish().addClickListener(event -> {
            Customer newCustomer = this.createNewCustomer();
            GlobalState.getInstance().setCurrentCustomer(newCustomer);
            Routes.navigateTo(Routes.SELECT_ROOM_START);
        });

        this.navigation.setFinishButtonActive(true);

        view = new VerticalLayout(customersForm);
    }

    @Override
    protected Component initContent() {
        return view;
    }

    private Customer createNewCustomer() {
        Customer customer = new Customer(
                this.customerSurname.getValue(),
                this.customerPrename.getValue(),
                this.customerStreet.getValue(),
                this.customerZIP.getValue(),
                this.customerCity.getValue(),
                this.customerDiscount.getValue()
        );

        this.customerRepo.save(customer);

        return customer;
    }

    /**
    private boolean validateFields() {
        Validator<String> notEmptyValidator = Validator.notEmpty("Dieses Feld darf nicht leer sein");
        Validator<String> minSizeValidator = Validator.min(2, "Mindestens zwei Zeichen erforderlich");

        ValidationResult surnameValidation = notEmptyValidator.apply(customerSurname.getValue());
        ValidationResult prenameValidation = notEmptyValidator.apply(customerPrename.getValue());
        ValidationResult streetValidation = notEmptyValidator.apply(customerStreet.getValue());
        ValidationResult zipValidation = notEmptyValidator.apply(customerZIP.getValue());
        ValidationResult cityValidation = notEmptyValidator.apply(customerCity.getValue());

        if (surnameValidation.isError()) {
            Notifications.showErrorNotification(surnameValidation.getErrorMessage());
            return false;
        }

        if (prenameValidation.isError()) {
            Notifications.showErrorNotification(prenameValidation.getErrorMessage());
            return false;
        }

        if (streetValidation.isError()) {
            Notifications.showErrorNotification(streetValidation.getErrorMessage());
            return false;
        }

        if (zipValidation.isError()) {
            Notifications.showErrorNotification(zipValidation.getErrorMessage());
            return false;
        }

        if (cityValidation.isError()) {
            Notifications.showErrorNotification(cityValidation.getErrorMessage());
            return false;
        }

        return true;
    }
     */

}
