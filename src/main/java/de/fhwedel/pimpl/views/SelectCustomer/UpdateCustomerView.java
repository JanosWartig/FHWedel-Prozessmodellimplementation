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
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.ForwardBackwardNavigationView;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;
import de.fhwedel.pimpl.model.Customer;
import de.fhwedel.pimpl.repos.CustomerRepo;

import java.util.Optional;

@Route(Routes.CUSTOMER_UPDATE)
@SpringComponent
@UIScope
public class UpdateCustomerView extends Composite<Component> implements BeforeEnterObserver {

    private String headlineCheckCustomer = "Deine Daten überprüfen";
    private String subheadlineCheckCustomer = "Sind alle Daten korrekt?";
    private String headlineUpdateCustomer = "Deine Daten aktualisieren";
    private String subheadlineUpdateCustomer = "Aktualisiere deine Daten.";

    @PropertyId("surname")
    private TextField customerSurname = new TextField();
    @PropertyId("prename")
    private TextField customerPrename = new TextField();
    @PropertyId("street")
    private TextField street = new TextField();
    @PropertyId("zip")
    private TextField zip = new TextField();
    @PropertyId("city")
    private TextField city = new TextField();
    private IntegerField customerDiscount = new IntegerField();
    private FormLayout customerForm = new FormLayout();

    private Button customerEdit = new Button("Daten bearbeiten");
    private Button customerUpdate = new Button("Aktualisieren");
    private Button customerDelete = new Button("Löschen");

    private HeadlineSubheadlineView headlineSubheadlineView = new HeadlineSubheadlineView(this.headlineCheckCustomer,
            this.subheadlineCheckCustomer, Constants.HEADLINE_1);

    private ForwardBackwardNavigationView forwardBackwardNavigationView = new ForwardBackwardNavigationView(
            "Zimmerkategorie auswählen", false
    );

    private HorizontalLayout customerControl = new HorizontalLayout(customerUpdate, customerDelete);

    private VerticalLayout customersForm = new VerticalLayout(
            headlineSubheadlineView,
            customerForm,
            customerControl,
            customerEdit,
            this.forwardBackwardNavigationView);
    private VerticalLayout view;

    private CustomerRepo customerRepo;
    private Customer customer;

    public UpdateCustomerView(CustomerRepo repo) {
        this.customerRepo = repo;

        customerForm.addFormItem(customerSurname, "Nachname");
        customerForm.addFormItem(customerPrename, "Vorname");
        customerForm.addFormItem(street, "Straße");
        customerForm.addFormItem(zip, "PLZ");
        customerForm.addFormItem(city, "Stadt");
        customerForm.addFormItem(customerDiscount, "Rabatt");

        this.customerEdit.addClickListener(event -> this.initEditCustomerView());
        this.customerUpdate.addClickListener(event -> this.onUpdateCustomerClick());
        this.customerDelete.addClickListener(event -> this.onDeleteCustomerClick());

        this.forwardBackwardNavigationView.setNext(true);
        this.forwardBackwardNavigationView.getNext().addClickListener(event -> {
            Routes.navigateTo(Routes.ROOM_START);
        });

        view = new VerticalLayout(customersForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.resetView();
        Integer id = Integer.valueOf(event.getLocation().getQueryParameters().getParameters().get("id").get(0));
        Optional<Customer> customerOptional = this.customerRepo.findById(id);
        Customer customer = null;
        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
            this.customerSurname.setValue(customer.getSurname());
            this.customerPrename.setValue(customer.getPrename());
            this.street.setValue(customer.getStreet());
            this.zip.setValue(customer.getZip());
            this.city.setValue(customer.getCity());
            this.customer = customer;
        } else {
            Routes.navigateTo(Routes.CUSTOMER_START);
        }
        System.out.println(customer);
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

        this.headlineSubheadlineView.setHeadline(this.headlineCheckCustomer);
        this.headlineSubheadlineView.setSubheadline(this.subheadlineCheckCustomer);
    }

    private void initEditCustomerView() {
        this.customerControl.setVisible(true);
        this.customerEdit.setVisible(false);
        this.headlineSubheadlineView.setHeadline(this.headlineUpdateCustomer);
        this.headlineSubheadlineView.setSubheadline(this.subheadlineUpdateCustomer);
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
