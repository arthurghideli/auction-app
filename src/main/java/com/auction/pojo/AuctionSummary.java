package com.auction.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionSummary implements Serializable {

	private static final long serialVersionUID = -4684571816573907639L;
	private Bid bid;
	private Double totalCollected;
	private String output;
}
