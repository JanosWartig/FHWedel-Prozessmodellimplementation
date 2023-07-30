package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Navigation extends HorizontalLayout {

    private Button back;
    private Button forwardNavigation;

    private Button cancel;

    public Navigation(String text, boolean isBackButton) {
        if (isBackButton) {
            this.back = new Button(text);
            this.back.setIcon(VaadinIcon.CHEVRON_LEFT.create());
            add(this.back);
        } else {
            this.forwardNavigation = this.createButton(text, VaadinIcon.CHEVRON_RIGHT);
            this.forwardNavigation.setIconAfterText(true);
            this.forwardNavigation.setEnabled(false);
            add(this.forwardNavigation);
        }

    }

    public Navigation(String backText, String nextText) {
        this.back = new Button(backText);
        this.back.setIcon(VaadinIcon.CHEVRON_LEFT.create());

        this.forwardNavigation = new Button(nextText);
        this.forwardNavigation.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        this.forwardNavigation.setIconAfterText(true);
        this.forwardNavigation.setEnabled(false);

        add(this.back, this.forwardNavigation);
    }

    public Navigation(String back, String forward, String cancel) {
        this.back = this.createButton(back, VaadinIcon.CHEVRON_LEFT);
        this.forwardNavigation = this.createButton(forward, VaadinIcon.CHEVRON_RIGHT);
        this.cancel = new Button(cancel);

        this.forwardNavigation.setIconAfterText(true);
        this.cancel.setIconAfterText(true);

        this.cancel.getStyle().set("color", "red");
        add(this.back, this.forwardNavigation, this.cancel);
    }

    private Button createButton(String text, VaadinIcon icon) {
        Button button = new Button(text);
        button.setIcon(icon.create());
        return button;
    }


    public Button getBack() {
        return this.back;
    }

    public Button getForwardNavigation() {
        return this.forwardNavigation;
    }

    public void setFinishButtonActive(boolean isActive) {
        if (isActive) {
            this.forwardNavigation.setEnabled(true);
            this.forwardNavigation.getStyle().set("opacity", "1");
        } else {
            this.forwardNavigation.setEnabled(false);
            this.forwardNavigation.getStyle().set("opacity", "0.5");
        }
    }

    public Button getCancel() {
        return cancel;
    }

}
