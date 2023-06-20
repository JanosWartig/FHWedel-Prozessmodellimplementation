package de.fhwedel.pimpl.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HeadlineSubheadlineView extends VerticalLayout {

    private Label headline;
    private Label subheadline;

    public HeadlineSubheadlineView(String headline, String subheadline, String fontsize) {
        this.headline = new Label(headline);
        this.subheadline = new Label(subheadline);

        this.headline.getStyle().set("font-weight", "bold").set("font-size", fontsize);
        this.setPadding(false);
        this.setSpacing(false);

        this.getStyle().set("margin-bottom", "12px");

        add(this.headline, this.subheadline);
    }

    public void setHeadline(String headline) {
        this.headline.setText(headline);
    }
    public void setSubheadline(String subheadline) {
        this.subheadline.setText(subheadline);
    }
}
