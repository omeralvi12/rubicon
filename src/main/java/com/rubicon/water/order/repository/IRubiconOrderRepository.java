package com.rubicon.water.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rubicon.water.order.model.RubiconOrder;

public interface IRubiconOrderRepository extends CrudRepository<RubiconOrder, Integer> {

	@Query(value = "SELECT * FROM rubicon_orders WHERE farm_id = :farmId", nativeQuery = true)
	public List<RubiconOrder> fetchOrdersByFarm(@Param(value = "farmId") int farmId);

	@Query(value = "SELECT * FROM rubicon_orders WHERE status = 0", nativeQuery = true)
	public List<RubiconOrder> fetchRequestedOrders();

	@Query(value = "SELECT * FROM rubicon_orders WHERE status = 1", nativeQuery = true)
	public List<RubiconOrder> fetchInProgressOrders();

	@Query(value = "SELECT * FROM rubicon_orders WHERE status = 2", nativeQuery = true)
	public List<RubiconOrder> fetchDeliveredOrders();

	@Query(value = "SELECT * FROM rubicon_orders WHERE status = 3", nativeQuery = true)
	public List<RubiconOrder> fetchCancelledOrders();
}
