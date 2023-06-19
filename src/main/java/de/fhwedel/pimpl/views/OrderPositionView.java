package de.fhwedel.pimpl.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import de.fhwedel.pimpl.model.Order;
import de.fhwedel.pimpl.repos.OrderRepo;

import java.util.Optional;

@SuppressWarnings("serial")
@SpringComponent
@UIScope
public class OrderPositionView extends Composite<Component> {

	@PropertyId("description")
	private TextField positionDescription = new TextField();
	@PropertyId("price")
	private TextField positionPrice = new TextField();
	@PropertyId("amount")
	private IntegerField positionAmount = new IntegerField();
	private FormLayout positionForm = new FormLayout();

	private Button positionSave = new Button("Position sichern", this::onOrderPosSaveClick);
	private Button positionReset = new Button("Rückgängig", this::onOrderPosResetClick);
	private HorizontalLayout positionControl = new HorizontalLayout(positionSave, positionReset);

	private VerticalLayout view = new VerticalLayout(positionForm, positionControl);

	private Optional<Order> order = Optional.empty();
	private Optional<Order.OrderPosition> position = Optional.empty();
	private OrderRepo orderRepo;
	private Binder<Order.OrderPosition> binder = new BeanValidationBinder<>(Order.OrderPosition.class);

	public OrderPositionView(OrderRepo orderRepo) {
		this.orderRepo = orderRepo;

		positionForm.addFormItem(positionDescription, "Beschreibung");
		positionForm.addFormItem(positionPrice, "Preis");
		positionForm.addFormItem(positionAmount, "Menge");

		binder.bindInstanceFields(this);
	}

	@Override
	protected Component initContent() {
		return view;
	}

	private void refreshControls() {
		positionForm.setEnabled(position.isPresent());
		positionSave.setEnabled(position.isPresent());
		positionReset.setEnabled(position.isPresent());
	}

	public void setOrder(Optional<Order> order) {
		this.order = order.flatMap(o -> o.getId() != null ? orderRepo.findById(o.getId()) : Optional.of(o));;
	}

	public void setPosition(Optional<Order.OrderPosition> position) {
		this.position = position;
		binder.readBean(position.orElse(null));
		refreshControls();
	}

	private void onOrderPosSaveClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		position.ifPresent(pos -> {
			order.ifPresent(o -> {
				if (binder.writeBeanIfValid(pos)) {
					o.addPosition(pos);
					order = Optional.of(orderRepo.save(o));
					refreshControls();
				}
			});
		});
	}

	private void onOrderPosResetClick(com.vaadin.flow.component.ClickEvent<Button> event) {
		position.ifPresent(p -> binder.readBean(p));
	}

}
