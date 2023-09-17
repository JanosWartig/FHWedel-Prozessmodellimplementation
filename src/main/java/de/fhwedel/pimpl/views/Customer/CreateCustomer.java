package de.fhwedel.pimpl.views.Customer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
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

@Route(Routes.CREATE_CUSTOMER)
@SpringComponent
@UIScope
public class CreateCustomer extends Composite<Component> implements BeforeEnterObserver {

    @PropertyId("surname")
    private final TextField customerSurname = new TextField();
    @PropertyId("prename")
    private final TextField customerPrename = new TextField();
    private final TextField customerStreet = new TextField();
    private final TextField customerZIP = new TextField();
    private final TextField customerCity = new TextField();
    private final IntegerField customerDiscount = new IntegerField();
    private final FormLayout customerForm = new FormLayout();

    private final ForwardButton forwardButton = new ForwardButton("Zimmerkategorie auswählen und Kunde anlegen", event -> this.createNewCustomer());

    private final BackButton backButton = new BackButton("Kunde suchen", event -> Routes.navigateTo(Routes.SEARCH_CUSTOMER));

    private final HorizontalLayout navigation = new HorizontalLayout(backButton, forwardButton);

    private final PageLayout pageLayout = new PageLayout("Kunde anlegen", "Erstelle einen neuen Kunden.", customerForm, navigation);

    private final CustomerRepo customerRepo;

    public CreateCustomer(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
        this.initForm();
        customerDiscount.setValue(0);
        customerDiscount.setEnabled(GlobalState.getInstance().isSupervisorModeActive());
        this.forwardButton.setEnabled(true);
        this.pageLayout.getHeader().getIsSupervisorCheckbox().addValueChangeListener(event -> customerDiscount.setEnabled(event.getValue()));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.resetForm();
    }

    @Override
    protected Component initContent() {
        return pageLayout;
    }

    private void initForm() {
        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(customerStreet, "Straße");
        customerForm.addFormItem(customerZIP, "PLZ");
        customerForm.addFormItem(customerCity, "Land");
        customerForm.addFormItem(customerDiscount, "Rabatt");
    }

    private void resetForm() {
        customerSurname.setValue("");
        customerPrename.setValue("");
        customerStreet.setValue("");
        customerZIP.setValue("");
        customerCity.setValue("");
        customerDiscount.setValue(0);
    }

    private boolean validateFields() {
        if (this.customerDiscount.getValue() < 0 || this.customerDiscount.getValue() > 100) {
            Notifications.showErrorNotification("Rabatt muss zwischen 0 und 100 liegen");
            return false;
        }
        return true;
    }

    private void createNewCustomer() {
        if (!this.validateFields()) return;

        Customer customer = new Customer(
                this.customerSurname.getValue(),
                this.customerPrename.getValue(),
                this.customerStreet.getValue(),
                this.customerZIP.getValue(),
                this.customerCity.getValue(),
                this.customerDiscount.getValue()
        );

        Customer newCustomer = this.customerRepo.save(customer);
        String customerNumber = "customer_" + newCustomer.getId();
        newCustomer.setCustomerNumber(customerNumber);
        this.customerRepo.save(newCustomer);
        GlobalState.getInstance().setCurrentCustomerID(newCustomer.getId());

        Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD);
    }



}
