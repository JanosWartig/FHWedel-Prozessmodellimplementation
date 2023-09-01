package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CustomDialog {
    private final com.vaadin.flow.component.dialog.Dialog dialog;
    private final Button closeButton;
    public CustomDialog(String message, String closeButton) {
        Label messageLabel = new Label(message);
        this.closeButton = new Button(closeButton);
        VerticalLayout dialogContent = new VerticalLayout(messageLabel, this.closeButton);
        this.dialog = new com.vaadin.flow.component.dialog.Dialog();

        dialogContent.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, this.closeButton);
        this.dialog.setCloseOnEsc(false);
        this.dialog.setCloseOnOutsideClick(false);

        this.dialog.add(dialogContent);
    }
    public void open() {
        this.dialog.open();
    }
    public Button getCloseButton() {
        return this.closeButton;
    }
    public com.vaadin.flow.component.dialog.Dialog getDialog() {
        return this.dialog;
    }

}
