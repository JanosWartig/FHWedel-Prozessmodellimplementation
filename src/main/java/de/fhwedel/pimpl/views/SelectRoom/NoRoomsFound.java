package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;

@SuppressWarnings("serial")
@Route(Routes.ROOM_NOT_FOUND)
@SpringComponent
@UIScope
public class NoRoomsFound extends VerticalLayout {

    HeadlineSubheadlineView headlineSubheadlineView = new HeadlineSubheadlineView("Keine Zimmer gefunden",
            "Es konnten leider keine Zimer gefunden werden.", Constants.HEADLINE_1);

    Button changeRoomCategory = new Button("Zimmerkategorie bearbeiten");
    Button stopBooking = new Button("Reservierung abbrechen");

    HorizontalLayout horizontalLayout = new HorizontalLayout(changeRoomCategory, stopBooking);

    private VerticalLayout view = new VerticalLayout(headlineSubheadlineView, horizontalLayout);

    public NoRoomsFound() {
        this.horizontalLayout.setPadding(false);
        this.stopBooking.getStyle().set("color", "red");

        this.changeRoomCategory.addClickListener(event -> {
            Routes.navigateTo(Routes.ROOM_START);
        });

        this.add(view);
    }


}
