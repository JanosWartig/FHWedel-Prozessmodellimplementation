package de.fhwedel.pimpl.components.navigation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.Consumer;

public class ForwardButton extends Button {

    public ForwardButton(String text, Consumer<ForwardButton> clickHandler) {
        setText(text);
        setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        setIconAfterText(true);
        setEnabled(false);
        this.addClickListener(event -> clickHandler.accept(this));
    }

}


