package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.Utility.Constants;
import de.fhwedel.pimpl.Utility.Routes;
import de.fhwedel.pimpl.components.HeadlineSubheadlineView;

@SuppressWarnings("serial")
@Route(Routes.ROOM_CALCULATE_PRICE)
@SpringComponent
@UIScope
public class CalculatePrice extends VerticalLayout {

    HeadlineSubheadlineView headlineSubheadlineView = new HeadlineSubheadlineView("Preis bestimmen",
            "Hier wird der Preis für den Kunden berechnet.", Constants.HEADLINE_1);

    private VerticalLayout view = new VerticalLayout(headlineSubheadlineView);

    public CalculatePrice() {

        this.add(view);
    }


}
