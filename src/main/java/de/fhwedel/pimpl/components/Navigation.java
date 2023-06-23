package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class Navigation extends HorizontalLayout {

    private Button edit;
    private Button finish;

    private Button cancel;

    public Navigation(String text, boolean isBackButton) {
        if (isBackButton) {
            this.edit = new Button(text);
            this.edit.setIcon(VaadinIcon.CHEVRON_LEFT.create());
            getStyle().set("margin-top", "65px");
            add(this.edit);
        } else {
            this.finish = this.createButton(text, VaadinIcon.CHEVRON_RIGHT);
            this.finish.setIconAfterText(true);
            this.finish.setEnabled(false);

            getStyle().set("margin-top", "65px");
            add(this.finish);
        }

    }

    public Navigation(String nextText, String backText) {
        this.edit = new Button(backText);
        this.edit.setIcon(VaadinIcon.CHEVRON_LEFT.create());

        this.finish = new Button(nextText);
        this.finish.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        this.finish.setIconAfterText(true);
        this.finish.setEnabled(false);

        getStyle().set("margin-top", "65px");
        add(this.edit, this.finish);
    }

    public Navigation(String edit, String finish, String cancel) {
        this.edit = this.createButton(edit, VaadinIcon.CHEVRON_LEFT);
        this.finish = this.createButton(finish, VaadinIcon.CHECK);
        this.cancel = this.createButton(cancel, VaadinIcon.ARROWS_CROSS);
        this.cancel.setIconAfterText(true);
        this.finish.getStyle().set("color", "green");
        this.cancel.getStyle().set("color", "red");

        this.getStyle().set("margin-top", "40px");
        add(this.edit, this.finish, this.cancel);
    }

    private Button createButton(String text, VaadinIcon icon) {
        Button button = new Button(text);
        button.setIcon(icon.create());
        return button;
    }


    public Button getEdit() {
        return this.edit;
    }

    public Button getFinish() {
        return this.finish;
    }

    public void setFinishButtonActive(boolean isActive) {
        if (isActive) {
            this.finish.setEnabled(true);
            this.finish.getStyle().set("opacity", "1");
        } else {
            this.finish.setEnabled(false);
            this.finish.getStyle().set("opacity", "0.5");
        }
    }

    public Button getCancel() {
        return cancel;
    }

}
