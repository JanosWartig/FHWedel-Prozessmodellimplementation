package de.fhwedel.pimpl.Utility;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CustomDialog {

    private com.vaadin.flow.component.dialog.Dialog dialog;

    private VerticalLayout dialogContent;
    private Label messageLabel;
    private Button closeButton;

    public CustomDialog(String message, String closeButton) {
        this.messageLabel = new Label(message);
        this.closeButton = new Button(closeButton);
        this.dialogContent = new VerticalLayout(this.messageLabel, this.closeButton);
        this.dialog = new com.vaadin.flow.component.dialog.Dialog();

        this.dialogContent.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, this.closeButton);
        this.dialog.setCloseOnEsc(false);
        this.dialog.setCloseOnOutsideClick(false);

        this.dialog.add(this.dialogContent);
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
