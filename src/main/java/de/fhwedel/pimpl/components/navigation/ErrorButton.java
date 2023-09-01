package de.fhwedel.pimpl.components.navigation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.Consumer;

public class ErrorButton extends Button {

    public ErrorButton(String text, Consumer<ErrorButton> clickHandler) {
        setText(text);
        setIcon(VaadinIcon.FROWN_O.create());
        setIconAfterText(false);
        setEnabled(true);
        this.addClickListener(event -> clickHandler.accept(this));
    }

}
