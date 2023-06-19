package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ForwardBackwardNavigationView extends HorizontalLayout {
    public ForwardBackwardNavigationView(String forwardText, String forwardDestination) {
        Button navigateBackwards = new Button("Zurück", event -> UI.getCurrent().getPage().getHistory().back());
        navigateBackwards.setIcon(VaadinIcon.CHEVRON_LEFT.create());

        Button navigateForward = new Button(forwardText, event -> UI.getCurrent().navigate("/" + forwardDestination));
        navigateForward.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        navigateForward.setIconAfterText(true);
        navigateForward.setEnabled(false);

        add(navigateBackwards, navigateForward);
        getStyle().set("margin-top", "65px");
    }

}
