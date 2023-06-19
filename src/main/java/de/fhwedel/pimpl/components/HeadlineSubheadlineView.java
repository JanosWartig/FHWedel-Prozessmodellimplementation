package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HeadlineSubheadlineView extends VerticalLayout {

    public HeadlineSubheadlineView(String headline, String subheadline, String fontsize) {
        Label headlineSection = new Label(headline);
        Label subheadlineSection = new Label(subheadline);

        headlineSection.getStyle().set("font-weight", "bold").set("font-size", fontsize);
        this.setPadding(false);
        this.setSpacing(false);

        this.getStyle().set("margin-bottom", "12px");

        add(headlineSection, subheadlineSection);
    }
}
