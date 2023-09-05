package de.fhwedel.pimpl.views.Checkout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import de.fhwedel.pimpl.Utility.ConvertManager;
import de.fhwedel.pimpl.Utility.GlobalState;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.ForwardButton;
import de.fhwedel.pimpl.components.navigation.Routes;
import de.fhwedel.pimpl.model.AdditionalService;
import de.fhwedel.pimpl.model.Booking;
import de.fhwedel.pimpl.model.Claim;
import de.fhwedel.pimpl.repos.AdditionalServicesRepo;
import de.fhwedel.pimpl.repos.BookingRepo;
import de.fhwedel.pimpl.repos.ClaimRepo;
import de.fhwedel.pimpl.views.Guests.CheckInGuests;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;

@Route(Routes.PRE_CHECKOUT_ADDITIONAL_GUESTS)
public class PreCheckoutGuests extends Composite<Component> implements BeforeEnterObserver {

    private final GlobalState globalState = GlobalState.getInstance();
    private final Label headline = new Label("Zusaztleistungen erfassen");
    private final Grid<AdditionalService> additionalServices = new Grid<>();

    private final TextField selectedServiceName = new TextField("Name");
    private final NumberField selectedServicePrice = new NumberField("Preis");
    private final NumberField selectedServiceAmount = new NumberField("Menge");

    private final HorizontalLayout addServiceForm = new HorizontalLayout(selectedServiceName, selectedServicePrice, selectedServiceAmount);
    private final Button addService = new Button("Erfassen", event -> this.addService());
    private final VerticalLayout addedServices = new VerticalLayout(headline, additionalServices, addServiceForm, addService);

    private final ForwardButton forwardButton = new ForwardButton("Alle Gäste eingecheckt und alle Zusatzleistungen erfasst", event -> Routes.navigateTo(Routes.BOOKING_CHECK_OUT));

    private final PageLayout pageLayout = new PageLayout("Weitere Gäste einchecken und Zusatzleistungen erfassen", "Weitere Gäste überprüfen und Daten checken und zusätzlich die Zusatzleistungen erfassen.");

    private Booking currentBooking = null;
    private final ClaimRepo claimRepo;
    private final BookingRepo bookingRepo;

    private AdditionalService selectedAdditionalService = null;

    public PreCheckoutGuests(BookingRepo repo, AdditionalServicesRepo additionalServicesRepo, ClaimRepo claimRepo) {
        this.bookingRepo = repo;
        this.claimRepo = claimRepo;
        List<AdditionalService> services = additionalServicesRepo.findAll();
        this.forwardButton.setEnabled(true);
        this.selectedServiceName.setEnabled(false);
        this.selectedServicePrice.setEnabled(false);
        this.selectedServiceAmount.setEnabled(false);
        this.additionalServices.setItems(services);
        this.initAddedServicesTable();
        this.handleSupervisorMode();
        this.addedServices.setPadding(true);
        this.addService.setEnabled(false);
        this.headline.getStyle().set("font-weight", "bold").set("font-size", "16px").set("margin-top", "10px");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (this.globalState.getCurrentBookingID() == -1) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        Optional<Booking> booking = this.bookingRepo.findById(this.globalState.getCurrentBookingID());

        if (booking.isEmpty()) {
            Routes.navigateTo(Routes.BOOKINGS_SEARCH);
            return;
        }

        this.currentBooking = booking.get();
        this.pageLayout.add(new CheckInGuests(this.bookingRepo), addedServices, forwardButton);
    }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }

    private void initAddedServicesTable() {
        this.additionalServices.setHeight("250px");
        this.additionalServices.setWidth("700px");
        this.additionalServices.addColumn(AdditionalService::getName).setHeader("Name").setSortable(true);
        this.additionalServices.addColumn(AdditionalService::getPrice).setHeader("Preis").setSortable(true);
        this.additionalServices.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                AdditionalService additionalService = event.getFirstSelectedItem().get();
                this.selectedAdditionalService = additionalService;
                this.selectedServiceName.setValue(additionalService.getName());
                this.selectedServicePrice.setValue(additionalService.getPrice().doubleValue());
                this.selectedServiceAmount.setEnabled(true);
                this.addService.setEnabled(true);
            } else {
                this.selectedServiceName.setValue("");
                this.selectedServicePrice.setValue(null);
                this.selectedServiceAmount.setEnabled(false);
                this.addService.setEnabled(false);
                this.selectedAdditionalService = null;
            }
        });
    }

    private void handleSupervisorMode() {
        PropertyChangeListener listener = evt -> this.selectedServicePrice.setEnabled(this.globalState.isSupervisorModeActive());
        this.globalState.addPropertyChangeListener(listener);
    }

    private void addService() {
        System.out.println("Preis: " + this.selectedServicePrice.getValue().intValue());
        System.out.println("Steuer: " + this.selectedAdditionalService.getValueAddedTaxPercent());
        int USt = (int) Math.round((ConvertManager.convertEuroToCent(this.selectedServicePrice.getValue()) * (this.selectedAdditionalService.getValueAddedTaxPercent().doubleValue() / 100)));
        System.out.println("USt: " + USt);
        Claim claim = new Claim(this.selectedServiceAmount.getValue().intValue(),
                ConvertManager.convertEuroToCent(this.selectedServicePrice.getValue()),
                USt, this.globalState.getCurrentDate());

        claim.setAdditionalService(this.selectedAdditionalService);
        this.currentBooking.addClaim(claim);



        this.bookingRepo.save(this.currentBooking);



        this.additionalServices.deselectAll();
        this.selectedServiceName.setValue("");
        this.selectedServicePrice.setValue(null);
        this.selectedServiceAmount.setValue(null);
        this.selectedServiceAmount.setEnabled(false);
        this.selectedServicePrice.setEnabled(false);
        this.selectedAdditionalService = null;
        this.addService.setEnabled(false);
    }


}
