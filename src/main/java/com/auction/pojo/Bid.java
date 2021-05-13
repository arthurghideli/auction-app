package com.auction.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid implements Serializable {

	private static final long serialVersionUID = 6214816999779129574L;
	
	@NotNull(message = "No nome for bidder?")
	private String ownerName;
	@NotNull(message = "no bid?")
	private Float bid;
	
}
