package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.Header;

@SuppressWarnings("serial")
@Route(Routes.SELECT_ROOM_BOOKING_FAILED)
@SpringComponent
@UIScope
public class ErrorView extends VerticalLayout {

    Header header = new Header("Reservierung gescheitert", "Entweder ist kein Zimmer zur Verfügung oder die Preisbestimmung ist gescheitert.");

    public ErrorView() {
        VerticalLayout view = new VerticalLayout(header);
        this.add(view);
    }

}
