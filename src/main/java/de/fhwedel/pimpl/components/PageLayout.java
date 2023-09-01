package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PageLayout extends VerticalLayout {

    private final Header header;

    public PageLayout(String title, String description, Component... components) {
        this.header = new Header(title, description);
        header.setSpacing(false);
        header.setPadding(false);
        setPadding(true);
        setWidth("1250px");
        add(header);
        add(components);
    }

    public Header getHeader() {
        return this.header;
    }

}
