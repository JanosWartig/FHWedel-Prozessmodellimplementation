package de.fhwedel.pimpl.views.SelectCustomer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
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
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Navigation;
import de.fhwedel.pimpl.components.Header;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

@Route(Routes.CUSTOMER_UPDATE)
@SpringComponent
@UIScope
public class UpdateCustomerView extends Composite<Component> implements BeforeEnterObserver {

    private final String headlineCheckCustomer = "Deine Daten überprüfen";
    private final String subheadlineCheckCustomer = "Sind alle Daten korrekt?";

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

    private final Button customerEdit = new Button("Daten bearbeiten");
    private final Button customerUpdate = new Button("Aktualisieren");
    private final Button customerDelete = new Button("Löschen");

    private final Header header = new Header(this.headlineCheckCustomer, this.subheadlineCheckCustomer);

    private final HorizontalLayout customerControl = new HorizontalLayout(customerUpdate, customerDelete);

    private final VerticalLayout view;

    private final CustomerRepo customerRepo;
    private Customer customer;

    public UpdateCustomerView(CustomerRepo repo) {
        this.customerRepo = repo;

        FormLayout customerForm = new FormLayout();
        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(street, "Straße");
        customerForm.addFormItem(zip, "PLZ");
        customerForm.addFormItem(city, "Stadt");
        customerForm.addFormItem(customerDiscount, "Rabatt");

        this.customerEdit.addClickListener(event -> this.initEditCustomerView());
        this.customerUpdate.addClickListener(event -> this.onUpdateCustomerClick());
        this.customerDelete.addClickListener(event -> this.onDeleteCustomerClick());

        Navigation navigation = new Navigation(
                "Zimmerkategorie auswählen", false
        );
        navigation.setFinishButtonActive(true);
        navigation.getFinish().addClickListener(event -> Routes.navigateTo(Routes.SELECT_ROOM_START));

        VerticalLayout customersForm = new VerticalLayout(
                header,
                customerForm,
                customerControl,
                customerEdit,
                navigation);
        view = new VerticalLayout(customersForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.resetView();
        Customer customer = GlobalState.getInstance().getCurrentCustomer();
        this.customerSurname.setValue(customer.getSurname());
        this.customerPrename.setValue(customer.getPrename());
        this.street.setValue(customer.getStreet());
        this.zip.setValue(customer.getZip());
        this.city.setValue(customer.getCity());
        this.customer = customer;
    }

    @Override
    protected Component initContent() {
        return view;
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

        this.header.setHeadline(this.headlineCheckCustomer);
        this.header.setSecondHeadline(this.subheadlineCheckCustomer);
    }

    private void initEditCustomerView() {
        this.customerControl.setVisible(true);
        this.customerEdit.setVisible(false);
        String headlineUpdateCustomer = "Deine Daten aktualisieren";
        this.header.setHeadline(headlineUpdateCustomer);
        String subheadlineUpdateCustomer = "Aktualisiere deine Daten.";
        this.header.setSecondHeadline(subheadlineUpdateCustomer);
        this.customerSurname.setEnabled(true);
        this.customerPrename.setEnabled(true);
        this.street.setEnabled(true);
        this.zip.setEnabled(true);
        this.city.setEnabled(true);
        this.customerDiscount.setEnabled(true);
    }

    private void onUpdateCustomerClick() {
        this.customer.setSurname(this.customerSurname.getValue());
        this.customer.setPrename(this.customerPrename.getValue());
        this.customer.setStreet(this.street.getValue());
        this.customer.setZip(this.zip.getValue());
        this.customer.setCity(this.city.getValue());

        this.customerRepo.save(this.customer);
    }

    private void onDeleteCustomerClick() {
        this.customerRepo.deleteById(this.customer.getId());
        Routes.navigateTo(Routes.CUSTOMER_START);
    }

}
