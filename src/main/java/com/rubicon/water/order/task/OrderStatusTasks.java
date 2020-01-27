package com.rubicon.water.order.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rubicon.water.order.model.RubiconOrder;
import com.rubicon.water.order.repository.IRubiconOrderRepository;
import com.rubicon.water.order.util.DateUtil;

@Component
public class OrderStatusTasks {

	@Autowired
	IRubiconOrderRepository repository;

	@Scheduled(cron = "1 * * * * ?")
	public void updateRequestedOrders() {
		List<RubiconOrder> orders = repository.fetchRequestedOrders();
		Date currentDate = new Date();

		if (orders != null && orders.size() > 0) {
			for (RubiconOrder order : orders) {
				if (currentDate.getTime() >= order.getStartDateTime().getTime()) {
					order.setStatus(RubiconOrder.OrderStatus.IN_PROGRESS);
					repository.save(order);
					System.out.println("Water delivery to " + order.getFarmId() + " started");
				}
			}
		}
	}

	@Scheduled(cron = "2 * * * * ?")
	public void updateInProgressOrders() {
		List<RubiconOrder> orders = repository.fetchInProgressOrders();
		Date currentDate = new Date();

		if (orders != null && orders.size() > 0) {
			for (RubiconOrder order : orders) {
				if (currentDate.getTime() >= DateUtil.getEndDate(order.getStartDateTime(), order.getDuration()).getTime()) {
					order.setStatus(RubiconOrder.OrderStatus.DELIVERED);
					repository.save(order);
					System.out.println("Water delivery to " + order.getFarmId() + " stopped");
				}
			}
		}
	}
}
