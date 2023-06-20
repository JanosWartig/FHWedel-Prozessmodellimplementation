package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import de.fhwedel.pimpl.Utility.Routes;

public class ForwardBackwardNavigationView extends HorizontalLayout {

    private Button back;
    private Button forward;


    public ForwardBackwardNavigationView(String forwardText, String forwardDestination) {
        this.back = new Button("Zurück", event -> UI.getCurrent().getPage().getHistory().back());
        this.back.setIcon(VaadinIcon.CHEVRON_LEFT.create());

        this.forward = new Button(forwardText, event -> Routes.navigateTo(forwardDestination));
        this.forward.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        this.forward.setIconAfterText(true);
        this.forward.setEnabled(false);

        add(this.back, this.forward);
        getStyle().set("margin-top", "65px");
    }

    public Button getBack() {
        return this.back;
    }

    public Button getForward() {
        return this.forward;
    }

    public void setForward(boolean enabled) {
        this.forward.setEnabled(enabled);
    }
}
