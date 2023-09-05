package de.fhwedel.pimpl.views.Customer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Notifications;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.BackButton;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

import java.util.Optional;

@Route(Routes.UPDATE_CUSTOMER)
@SpringComponent
@UIScope
public class UpdateCustomer extends Composite<Component> implements BeforeEnterObserver {

    @PropertyId("surname")
    private final TextField customerSurname = new TextField();
    @PropertyId("prename")
    private final TextField customerPrename = new TextField();
    @PropertyId("street")
    private final TextField street = new TextField();
    @PropertyId("zip")
    private final TextField zip = new TextField();
    @PropertyId("city")
    private final TextField city = new TextField();
    private final IntegerField customerDiscount = new IntegerField();

    private final FormLayout customerForm = new FormLayout();

    private final Button customerEdit = new Button("Daten bearbeiten");
    private final Button customerUpdate = new Button("Aktualisieren");
    private final Button customerDelete = new Button("Löschen");

    private final HorizontalLayout customerControl = new HorizontalLayout(customerUpdate, customerDelete);

    private final ForwardButton forwardButton = new ForwardButton("Zimmerkategorie auswählen", event -> {
        if (!validateFields()) return;
        Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD);
    });

    private final BackButton backButton = new BackButton("Kunde suchen", event -> Routes.navigateTo(Routes.SEARCH_CUSTOMER));

    private final HorizontalLayout navigation = new HorizontalLayout(backButton, forwardButton);

    private final PageLayout pageLayout = new PageLayout("Kunde überprüfen", "Überprüfe, ob alle Daten richtig sind.", customerForm, customerControl, customerEdit, navigation);

    private Customer currentCustomer = null;
    private final CustomerRepo customerRepo;

    public UpdateCustomer(CustomerRepo repo) {
        this.customerRepo = repo;
        this.initForm();
        this.forwardButton.setEnabled(true);
        this.customerEdit.addClickListener(event -> this.initEditCustomerView());
        this.customerUpdate.addClickListener(event -> this.onUpdateCustomerClick());
        this.customerDelete.addClickListener(event -> this.onDeleteCustomerClick());
        this.pageLayout.getHeader().getIsSupervisorCheckbox().addValueChangeListener(event -> this.customerDiscount.setEnabled(event.getValue()));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.resetView();
        Optional<Customer> customer = this.customerRepo.findById(GlobalState.getInstance().getCurrentCustomerID());
        if (customer.isEmpty()) {
            Routes.navigateTo(Routes.SEARCH_CUSTOMER);
            return;
        }

        this.currentCustomer = customer.get();

        this.customerSurname.setValue(customer.get().getSurname());
        this.customerPrename.setValue(customer.get().getPrename());
        this.street.setValue(customer.get().getStreet());
        this.zip.setValue(customer.get().getZip());
        this.city.setValue(customer.get().getCity());
        this.customerDiscount.setValue(customer.get().getDiscount());
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initForm() {
        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(street, "Straße");
        customerForm.addFormItem(zip, "PLZ");
        customerForm.addFormItem(city, "Stadt");
        customerForm.addFormItem(customerDiscount, "Rabatt");
    }

    private void resetView() {
        this.customerSurname.setEnabled(false);
        this.customerPrename.setEnabled(false);
        this.street.setEnabled(false);
        this.zip.setEnabled(false);
        this.city.setEnabled(false);
        this.customerDiscount.setEnabled(false);
        this.customerControl.setVisible(false);
        this.customerEdit.setVisible(true);
    }

    private void initEditCustomerView() {
        this.customerControl.setVisible(true);
        this.customerEdit.setVisible(false);
        this.customerSurname.setEnabled(true);
        this.customerPrename.setEnabled(true);
        this.street.setEnabled(true);
        this.zip.setEnabled(true);
        this.city.setEnabled(true);
        this.customerDiscount.setEnabled(GlobalState.getInstance().isSupervisorModeActive());
    }

    private void onUpdateCustomerClick() {
        if (validateFields()) {
            this.currentCustomer.setSurname(this.customerSurname.getValue());
            this.currentCustomer.setPrename(this.customerPrename.getValue());
            this.currentCustomer.setStreet(this.street.getValue());
            this.currentCustomer.setZip(this.zip.getValue());
            this.currentCustomer.setCity(this.city.getValue());
            this.currentCustomer.setDiscount(this.customerDiscount.getValue());

            this.customerRepo.save(this.currentCustomer);
            this.resetView();
        }
    }

    private void onDeleteCustomerClick() {
        this.customerRepo.deleteById(GlobalState.getInstance().getCurrentCustomerID());
        Routes.navigateTo(Routes.SEARCH_CUSTOMER);
    }

    private boolean validateFields() {
        if (this.customerDiscount.getValue() < 0 || this.customerDiscount.getValue() > 100) {
            Notifications.showErrorNotification("Rabatt muss zwischen 0 und 100 liegen");
            return false;
        }
        return true;
    }

}
