package de.fhwedel.pimpl.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.model.Order;
import de.fhwedel.pimpl.repos.OrderRepo;

import java.util.Optional;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class OrderView extends Composite<Component> {

	@PropertyId("orderNumber")
	private IntegerField onum = new IntegerField();
	private DatePicker createDate = new DatePicker();
	private FormLayout orderForm = new FormLayout();

	private Button orderSave = new Button("Auftrag sichern", this::onOrderSaveClick);
	private Button orderReset = new Button("Rückgängig", this::onOrderResetClick);
	private HorizontalLayout orderCtrl = new HorizontalLayout(orderSave, orderReset);

	private Grid<Order.OrderPosition> positions = new Grid<>();
	private Button positionNew = new Button("Neue Position", this::onPosNewClick);
	private HorizontalLayout positionControl = new HorizontalLayout(positionNew);

	private Button posBack = new Button("Zurück zum Auftrag", this::onPosBackClick);

	private OrderPositionView posView;

	private VerticalLayout view = new VerticalLayout(orderForm, orderCtrl, positions, positionControl);

	private OrderRepo orderRepo;
	private Optional<Order> order;
	private Optional<Order.OrderPosition> positionSelected;
	private Binder<Order> binder = new BeanValidationBinder<>(Order.class);

	public OrderView(OrderPositionView posView, OrderRepo orderRepo) {
		this.posView = posView;
		this.orderRepo = orderRepo;

		orderForm.addFormItem(onum, "Auftragsnummer");
		orderForm.addFormItem(createDate, "Auftragsdatum");

		positions.addColumn(Order.OrderPosition::getDescription).setHeader("Beschreibung");
		positions.addColumn(Order.OrderPosition::getAmount).setHeader("Menge");
		positions.addColumn(Order.OrderPosition::getPrice).setHeader("Einzelpreis");
		positions.setSelectionMode(Grid.SelectionMode.SINGLE);
		positions.setHeight("300px");

		binder.bindInstanceFields(this);
	}

	@Override
	protected Component initContent() {
		return view;
	}

	private void refreshControls() {
		orderForm.setEnabled(order.isPresent());
		orderSave.setEnabled(order.isPresent());
		orderReset.setEnabled(order.isPresent());
	}

	public void setOrder(Optional<Order> order) {
		order = order.flatMap(o -> o.getId() != null ? orderRepo.findById(o.getId()) : Optional.of(o));
		this.order = order;
		binder.readBean(order.orElse(null));
		order.ifPresent(o -> positions.setItems(DataProvider.ofCollection(o.getPositions())));
		refreshControls();
	}

	public Optional<Order> getOrder() {
		return order;
	}

	public void setOrderPosSelected(Optional<Order.OrderPosition> position) {
		positionSelected = position;
		posView.setOrder(order);
		posView.setPosition(positionSelected);
		positionSelected.ifPresent( pos -> {
			view.removeAll();
			view.add(posBack, posView);
		});
	}


	private void onOrderSaveClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		order.ifPresent(c -> {
			if (binder.writeBeanIfValid(c)) {
				setOrder(Optional.of(orderRepo.save(c)));
			}
		});
	}

	private void onOrderResetClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		order.ifPresent(o -> binder.readBean(o));
	}

	private void onPosNewClick(ClickEvent<Button> buttonClickEvent) {
		order.ifPresent(o -> {
			Order.OrderPosition newPosition = new Order.OrderPosition();
			setOrderPosSelected(Optional.of(newPosition));
		});
	}

	private void onPosBackClick(ClickEvent<Button> buttonClickEvent) {
		view.removeAll();
		view.add(orderForm, orderCtrl, positions, positionControl);
		setOrder(order);
	}

}
