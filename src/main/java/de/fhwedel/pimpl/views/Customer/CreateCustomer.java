package de.fhwedel.pimpl.views.Customer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.components.Header;
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

    private final Navigation navigation = new Navigation("Kunde suchen","Zimmerkategorie auswählen und Kunde anlegen");

    private final Header header = new Header("Kunde anlegen", "Erstelle einen neuen Kunden.");

    private VerticalLayout customersForm = new VerticalLayout(
            header,
            customerForm,
            navigation);
    private final VerticalLayout view;

    private final CustomerRepo customerRepo;

    public CreateCustomer(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;

        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(customerStreet, "Straße");
        customerForm.addFormItem(customerZIP, "PLZ");
        customerForm.addFormItem(customerCity, "Land");
        customerForm.addFormItem(customerDiscount, "Rabatt");

        customerDiscount.setValue(0);
        customerDiscount.setEnabled(GlobalState.getInstance().isSupervisorModeActive());

        this.navigation.getForwardNavigation().addClickListener(event -> {
            if (!this.validateFields()) return;
            Customer newCustomer = this.createNewCustomer();
            GlobalState.getInstance().setCurrentCustomer(newCustomer);
            Routes.navigateTo(Routes.SELECT_ROOM_CATEGORY_AND_BOOKING_PERIOD);
        });

        this.navigation.getBack().addClickListener(event -> Routes.navigateTo(Routes.SEARCH_CUSTOMER));

        this.navigation.setFinishButtonActive(true);

        this.header.getIsSupervisorCheckbox().addValueChangeListener(event -> customerDiscount.setEnabled(event.getValue()));

        this.view = new VerticalLayout(customersForm);
        this.view.setWidth("850px");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.customerSurname.setValue("");
        this.customerPrename.setValue("");
        this.customerStreet.setValue("");
        this.customerZIP.setValue("");
        this.customerCity.setValue("");
        this.customerDiscount.setValue(0);
        GlobalState.getInstance().resetGlobalState();
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

        GlobalState.getInstance().setCurrentCustomer(customer);
        this.customerRepo.save(customer);

        return customer;
    }

    private boolean validateFields() {
        if (this.customerDiscount.getValue() < 0 || this.customerDiscount.getValue() > 100) {
            Notifications.showErrorNotification("Rabatt muss zwischen 0 und 100 liegen");
            return false;
        }
        return true;
    }

}
