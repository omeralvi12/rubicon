package com.rubicon.water.order.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rubicon.water.order.model.BaseResponse;
import com.rubicon.water.order.model.RubiconOrder;
import com.rubicon.water.order.repository.IRubiconOrderRepository;
import com.rubicon.water.order.util.DateUtil;

@RestController
public class RubiconOrderController {

	@Autowired
	IRubiconOrderRepository repository;

	/**
	 * This is a REST API method that returns the order
	 * @param id
	 * @return BaseResponse
	 */
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public BaseResponse getOrder(@RequestParam(value="id") int id) {
		BaseResponse response = new BaseResponse();
		RubiconOrder order = repository.findById(id).orElse(null);

		if (order != null) {
			response.setCode(200);
			response.setResponse("Success");
			response.setModel(order);
		} else {
			response.setCode(404);
			response.setResponse("Order Not Found");
			response.setModel(order);
		}

		return response;
	}

	/**
	 * This is the REST API method that is called to save the order
	 * @param request
	 * @return BaseResponse
	 */
	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public BaseResponse placeOrder(@RequestBody RubiconOrder request) {
		BaseResponse response = new BaseResponse();

		try {

			List<RubiconOrder> orders = repository.fetchOrdersByFarm(request.getFarmId());
			boolean isOverlapping = checkOverlappingOrders(orders, request.getStartDateTime(), request.getDuration());

			if (!isOverlapping) {
				request.setStatus(RubiconOrder.OrderStatus.REQUESTED);
				repository.save(request);
				response.setModel(request);
				response.setCode(200);
				response.setResponse("Success");
			} else {
				response.setCode(404);
				response.setResponse("An Order Already Exists");
			}

		} catch (Exception ex) {
			response.setCode(404);
			response.setResponse("Some Error has Occurred");
		}

		return response;
	}

	/**
	 * This is the REST API method that is used to cancel the order
	 * @param order
	 * @return BaseResponse
	 */
	@RequestMapping(value = "/order", method = RequestMethod.PUT)
	public BaseResponse cancelOrder(@RequestBody RubiconOrder order) {
		BaseResponse response = new BaseResponse();
		RubiconOrder dbOrder = repository.findById(order.getId()).orElse(null);

		if (dbOrder != null && dbOrder.getStatus() != RubiconOrder.OrderStatus.DELIVERED) {
			dbOrder.setStatus(RubiconOrder.OrderStatus.CANCELLED);
			repository.save(dbOrder);

			response.setCode(200);
			response.setResponse("Success");
			response.setModel(dbOrder);

		} else if (dbOrder != null && dbOrder.getStatus() == RubiconOrder.OrderStatus.DELIVERED) {
			response.setCode(404);
			response.setResponse("Order Already Delivered");
			response.setModel(order);

		} else {
			response.setCode(404);
			response.setResponse("Order Not Found");
			response.setModel(order);
		}

		return response;
	}

	/**
	 * This methods checks if the incoming order overlaps an existing order for the farm in that duration
	 * @param orders
	 * @param requestedStartTime
	 * @param requestedDuration
	 * @return boolean
	 * @throws ParseException
	 */
	private boolean checkOverlappingOrders(List<RubiconOrder> orders, Date requestedStartTime, int requestedDuration) throws ParseException {
		boolean isOverlapping = false;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date requestEndDate = DateUtil.getEndDate(requestedStartTime, requestedDuration);

		if (orders != null && orders.size() > 0) {
			Date startDateTime = null;
			Date endDateTime = null;
			for (RubiconOrder order : orders) {
				startDateTime = format.parse(format.format(order.getStartDateTime()));
				endDateTime = DateUtil.getEndDate(order.getStartDateTime(), order.getDuration());

				if (startDateTime.getTime() <= requestEndDate.getTime() && requestedStartTime.getTime() <= endDateTime.getTime()) {
					isOverlapping = true;
					break;
				}
			}
		}

		return isOverlapping;
	}
}
