package de.fhwedel.pimpl.views;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.Tabs.Orientation;
import com.vaadin.flow.component.tabs.Tabs.SelectedChangeEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.views.actions.Actions;

@SuppressWarnings("serial")
@Route("/test")
@UIScope
public class MainView extends Composite<Component> {

	private Tab t1 = new Tab("Kunden");
	private Tab t2 = new Tab("Aufträge");
	private Tabs ts = new Tabs(false, t1, t2);
	private AppLayout view = new AppLayout();

	private CustomerNavigateView customerView;
	private OrderNavigateView orderNavigateView;

	private Map<Tab, Component> tabs = new HashMap<>();

	public MainView(CustomerNavigateView customerView, OrderNavigateView orderNavigateView, Actions actions) {
		this.customerView = customerView;
		this.orderNavigateView = orderNavigateView;

		tabs.put(t1, customerView);
		tabs.put(t2, orderNavigateView);
		ts.setOrientation(Orientation.VERTICAL);
		ts.addSelectedChangeListener(this::tabChanged);
		view.addToDrawer(ts);
		ts.setSelectedTab(t1);

		actions.registerRequestSearchOrder( (date) -> {
			ts.setSelectedTab(t2);
			orderNavigateView.search( Optional.of(date) );
		});
	}

	@Override
	protected Component initContent() {
		return view;
	}

	private void tabChanged(SelectedChangeEvent event) {
		view.setContent(tabs.get(event.getSelectedTab()));
	}

}
