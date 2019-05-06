package com.gangbin.bookstore.domain;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

public class Trade {

	//Trade �����Ӧ�� id
	private Integer tradeId;
	
	//���׵�ʱ��
	private Timestamp tradeTime;

	//Trade �����Ķ�� TradeItem
	private Set<TradeItem> items = new LinkedHashSet<TradeItem>();
	
	public void setItems(Set<TradeItem> items) {
		this.items = items;
	}
	
	public Set<TradeItem> getItems() {
		return items;
	}

	//�� Trade ������ User �� userId
	private Integer userId;
	
	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Timestamp getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Timestamp tradeTime) {
		this.tradeTime = tradeTime;
	}

	@Override
	public String toString() {
		return "Trade [tradeId=" + tradeId + ", tradeTime=" + tradeTime
				+ ", userId=" + userId + "]";
	}
}

