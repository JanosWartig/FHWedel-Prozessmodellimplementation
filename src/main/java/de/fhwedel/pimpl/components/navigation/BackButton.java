package de.fhwedel.pimpl.components.navigation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.Consumer;

public class BackButton extends Button {

    public BackButton(String text, Consumer<BackButton> clickHandler) {
        setText(text);
        setIcon(VaadinIcon.CHEVRON_LEFT.create());
        setIconAfterText(false);
        setEnabled(true);
        this.addClickListener(event -> clickHandler.accept(this));
    }

}
