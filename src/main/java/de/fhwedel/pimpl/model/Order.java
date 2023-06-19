package de.fhwedel.pimpl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ordertab")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "order_id")
	private Integer id;

	@NotNull(message = "Pflichtangabe")
	@Column(unique=true, updatable = false)
	private Integer orderNumber;

	@NotNull(message = "Pflichtangabe")
	private LocalDate createDate;

	@ElementCollection
	@OrderColumn
	private List<OrderPosition> positions;

	public Order() {
		this.positions = new ArrayList<>();
	}

	public Order(Integer orderNumber, LocalDate createDate) {
		this();
		this.orderNumber = orderNumber;
		this.createDate = createDate;
	}

	public Integer getId() {
		return id;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public List<OrderPosition> getPositions() {
		return positions;
	}

	public void addPosition(OrderPosition pos) {
		this.positions.add(pos);
	}

	public void removePosition(OrderPosition pos) {
		this.positions.remove(pos);
	}

	@Override
	public String toString() {
		return "Order{" +
				"orderNumber=" + orderNumber +
				", createDate=" + createDate +
				", positions=" + positions +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Order order = (Order) o;
		return Objects.equals(orderNumber, order.orderNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderNumber);
	}

	@Embeddable
	public static class OrderPosition {

		@NotNull(message = "Pflichtangabe")
		@Size(min = 1, message = "Mindestens ein Zeichen Länge")
		private String description;
		@NotNull(message = "Pflichtangabe")
		@Min(0)
		private int price = 0;
		@NotNull(message = "Pflichtangabe")
		@Min(1)
		private int amount = 1;

		public OrderPosition() {
		}

		public OrderPosition(String description, int price, int amount) {
			this();
			this.description = description;
			this.price = price;
			this.amount = amount;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		@Override
		public String toString() {
			return "OrderPosition {" +
					"description='" + description + '\'' +
					", singlePrice=" + price +
					", amount=" + amount +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			OrderPosition that = (OrderPosition) o;
			return price == that.price && amount == that.amount && Objects.equals(description, that.description);
		}

		@Override
		public int hashCode() {
			return Objects.hash(description, price, amount);
		}

	}
}