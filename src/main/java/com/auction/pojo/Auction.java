package com.auction.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auction implements Serializable {
	
	private static final long serialVersionUID = 6086642894799919396L;
	
	private TreeMap<Float, List<Bid>> bids;
	private short bidCount;
	private boolean open;
}
