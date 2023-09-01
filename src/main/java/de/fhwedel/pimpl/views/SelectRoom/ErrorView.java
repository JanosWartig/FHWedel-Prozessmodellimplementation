package de.fhwedel.pimpl.views.SelectRoom;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.components.PageLayout;
import de.fhwedel.pimpl.components.navigation.Routes;

@Route(Routes.SELECT_ROOM_BOOKING_FAILED)
@SpringComponent
@UIScope
public class ErrorView extends Composite<Component> {

    private final PageLayout pageLayout = new PageLayout("Reservierung gescheitert", "Entweder ist kein Zimmer zur Verfügung oder die Preisbestimmung ist gescheitert.");

    public ErrorView() { }

    @Override
    protected Component initContent() {
        return this.pageLayout;
    }
}
