package com.rubicon.water.order.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "rubicon_orders")
public class RubiconOrder implements IModel {

	public static enum OrderStatus {
		REQUESTED,
		IN_PROGRESS,
		DELIVERED,
		CANCELLED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "farm_id")
	private int farmId;

	@Column(name = "start_date_time")
	@JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date startDateTime;

	@Column(name = "duration")
	private int duration;

	@Column(name = "status")
	private OrderStatus status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getFarmId() {
		return farmId;
	}

	public void setFarmId(int farmId) {
		this.farmId = farmId;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}
