package com.auction.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.auction.exception.AuctionException;
import com.auction.pojo.AuctionSummary;
import com.auction.pojo.Bid;
import com.auction.service.AuctionService;

@RunWith(MockitoJUnitRunner.class)
public class AuctionControllerTest {
	@Mock
	private AuctionService auctionService;
	
	private AuctionController getController() {
		return new AuctionController(auctionService);
	}
	
	@Test
	public void open1() {
		assertEquals(ResponseEntity.ok("Auction is now open!"), getController().open());
	}
	@Test
	public void open2() {
		when(getController().open()).thenThrow(new AuctionException("Error"));
		assertEquals(ResponseEntity.ok().body("Error"),getController().open());
	}
	@Test
	public void open3() {
		when(getController().open()).thenThrow(new RuntimeException("Error"));
		assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error"),getController().open());
	}
	
	@Test
	public void sendBid1() {
		assertEquals(ResponseEntity.ok().build(), getController().sendBid(Bid.builder().bid(0.1F).ownerName("Test").build()));
	}
	@Test
	public void sendBid2() {
		when( getController().sendBid(any())).thenThrow(new AuctionException("Error"));
		assertEquals(ResponseEntity.ok().body("Error"),getController().sendBid(Bid.builder().bid(0.1F).ownerName("Test").build()));
	}
	@Test
	public void sendBid3() {
		when( getController().sendBid(any())).thenThrow(new RuntimeException("Error"));
		assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error"),getController().sendBid(Bid.builder().bid(0.1F).ownerName("Test").build()));
	}
	
	
	@Test
	public void close1() {
		AuctionSummary summary = new AuctionSummary();
		try {
			when(auctionService.closeAuction()).thenReturn(summary);
			assertEquals(ResponseEntity.ok().body(summary), getController().close());
		} catch (AuctionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void close2() {
		when( getController().close()).thenThrow(new AuctionException("Error"));
		assertEquals(ResponseEntity.ok().body("Error"),getController().close());
	}
	@Test
	public void close3() {
		when( getController().close()).thenThrow(new RuntimeException("Error"));
		assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error"),getController().close());
	}
}
