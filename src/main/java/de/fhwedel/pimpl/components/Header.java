package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import de.fhwedel.pimpl.Utility.GlobalState;

import java.time.LocalDate;

public class Header extends VerticalLayout implements BeforeEnterObserver {

    private final Checkbox isSupervisor;
    private final DatePicker datePicker;
    private final Label headline;
    private final Label secondHeadline;

    public Header(String headline, String secondHeadline, String fontsize) {
        this.isSupervisor = new Checkbox("Supervisor-Modus");
        this.datePicker = new DatePicker("Heute einstellen");
        HorizontalLayout settings = new HorizontalLayout(this.isSupervisor, this.datePicker);
        settings.setJustifyContentMode(JustifyContentMode.CENTER);
        settings.setAlignItems(Alignment.CENTER);
        settings.setPadding(false);

        this.headline = new Label(headline);
        this.secondHeadline = new Label(secondHeadline);

        this.headline.getStyle().set("font-weight", "bold").set("font-size", fontsize).set("margin-top", "30px");

        this.setPadding(false);
        this.setSpacing(false);

        this.getStyle().set("margin-bottom", "12px");

        this.isSupervisor.addValueChangeListener(event -> {
            boolean isChecked = event.getValue();
            boolean oldValue = GlobalState.getInstance().isSupervisorModeActive();

            if (isChecked != oldValue) {
                if (isChecked) {
                    GlobalState.getInstance().setSupervisorModeActive(true);
                    Notification.show("Supervisor-Modus aktiviert!");
                } else {
                    GlobalState.getInstance().setSupervisorModeActive(false);
                    Notification.show("Supervisor-Modus deaktiviert!");
                }
            }

        });


            this.datePicker.addValueChangeListener(event -> {
                LocalDate newValue = event.getValue();
                LocalDate oldValue = GlobalState.getInstance().getCurrentDate();

                if (!newValue.equals(oldValue)) {
                    GlobalState.getInstance().setCurrentDate(newValue);
                    Notification.show("Aktuelles Datum verändert!");
                }

            });


        add(settings, this.headline, this.secondHeadline);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        this.isSupervisor.setValue(GlobalState.getInstance().isSupervisorModeActive());
        this.datePicker.setValue(GlobalState.getInstance().getCurrentDate());
    }

    public void setHeadline(String headline) {
        this.headline.setText(headline);
    }
    public void setSecondHeadline(String secondHeadline) {
        this.secondHeadline.setText(secondHeadline);
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }
}
