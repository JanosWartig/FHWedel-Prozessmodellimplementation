package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ForwardBackwardNavigationView extends HorizontalLayout {

    private Button back;
    private Button next;

    public ForwardBackwardNavigationView(String text, boolean isBackButton) {
        if (isBackButton) {
            this.back = new Button(text, event -> UI.getCurrent().getPage().getHistory().back());
            this.back.setIcon(VaadinIcon.CHEVRON_LEFT.create());
            getStyle().set("margin-top", "65px");
            add(this.back);
        } else {
            this.next = new Button(text);
            this.next.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
            this.next.setIconAfterText(true);
            this.next.setEnabled(false);

            getStyle().set("margin-top", "65px");
            add(this.next);
        }

    }

    public ForwardBackwardNavigationView(String nextText, String backText) {
        this.back = new Button(backText, event -> UI.getCurrent().getPage().getHistory().back());
        this.back.setIcon(VaadinIcon.CHEVRON_LEFT.create());

        this.next = new Button(nextText);
        this.next.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        this.next.setIconAfterText(true);
        this.next.setEnabled(false);

        getStyle().set("margin-top", "65px");
        add(this.back, this.next);
    }

    public Button getBack() {
        return this.back;
    }

    public Button getNext() {
        return this.next;
    }

    public void setNext(boolean enabled) {
        this.next.setEnabled(enabled);
    }
}
