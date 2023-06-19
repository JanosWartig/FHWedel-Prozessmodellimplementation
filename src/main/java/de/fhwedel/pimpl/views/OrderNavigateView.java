package de.fhwedel.pimpl.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.model.Order;
import de.fhwedel.pimpl.repos.OrderRepo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class OrderNavigateView extends Composite<Component> {

	private DatePicker orderQuery = new DatePicker();
	private Button orderSearch = new Button("Suchen", event -> search( orderQuery.getValue() == null ? Optional.empty() : Optional.of(orderQuery.getValue()) ) );
	private Grid<Order> orders = new Grid<>();
	private VerticalLayout ordersForm = new VerticalLayout(orderQuery, orderSearch, orders);

	private Button orderNew = new Button("Neuer Auftrag", event -> selectOrder( Optional.of(new Order()) ) );
	private HorizontalLayout orderControl = new HorizontalLayout(orderNew);

	private Button orderBack = new Button("Zurück zur Auftragsübersicht", event -> backToOrderSearch());
	private OrderView orderView;

	private VerticalLayout view;

	private Optional<Order> orderSelected;
	private OrderRepo orderRepo;

	public OrderNavigateView(OrderView orderView, OrderRepo orderRepo) {
		this.orderView = orderView;
		this.orderRepo = orderRepo;

		orders.addColumn(Order::getOrderNumber).setHeader("Auftragsnummer").setSortable(true);
		orders.addColumn(Order::getCreateDate).setHeader("Auftragsdatum").setSortable(true);
		orders.setSelectionMode(SelectionMode.SINGLE);
		orders.setHeight("300px");
		orders.addItemDoubleClickListener(event -> selectOrder( Optional.of(event.getItem()) ) );

		view = new VerticalLayout(ordersForm, orderControl);

		selectOrder(Optional.empty());
	}

	@Override
	protected Component initContent() {
		return view;
	}

	public void search(Optional<LocalDate> query) {
		List<Order> items = query.map(date -> orderRepo.findByCreateDate( date )).orElse(Collections.emptyList());
		orders.setItems(DataProvider.ofCollection(items));
	}

	public void selectOrder(Optional<Order> order) {
		order = order.flatMap(o -> o.getId() != null ? orderRepo.findById(o.getId()) : Optional.of(o));
		orderSelected = order;
		orderView.setOrder(order);
		orderSelected.ifPresent(o -> {
			view.removeAll();
			view.add(orderBack, orderView);
		});
		refreshControls();
	}

	public void backToOrderSearch() {
		view.removeAll();
		view.add(ordersForm, orderControl);
		selectOrder(Optional.empty());
	}

	private void refreshControls() {
	}

}
