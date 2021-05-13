package com.auction.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.auction.config.HazelcastFactory;
import com.auction.constants.Constants;
import com.auction.exception.AuctionException;
import com.auction.pojo.Auction;
import com.auction.pojo.Bid;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@RunWith(MockitoJUnitRunner.class)
public class AuctionServiceTest {
	@Mock
	private HazelcastFactory hazelcastFactory;
	@Mock
	private HazelcastInstance hazelcastInstance;
	@Mock
	private IMap imap;
	
	@Test
	public void openAuction1() {
		when(hazelcastFactory.getHazelcastInstance()).thenReturn(hazelcastInstance);
		when(hazelcastInstance.getMap(Constants.DEFAULT_AUCTION)).thenReturn(imap);
		try {
			getService().openAction();
		} catch (AuctionException e) {
			assertEquals(e.getMessage(), "Auction is already open!");
		}
	}
	@Test
	public void openAuction2() {
		when(hazelcastFactory.getHazelcastInstance()).thenReturn(hazelcastInstance);
		when(hazelcastInstance.getMap(Constants.DEFAULT_AUCTION)).thenReturn(imap);
		Auction auction = Auction.builder()
				.open(true)
				.bids(new TreeMap<Float, List<Bid>>())
				.build();
		
		when(imap.isEmpty()).thenReturn(true);
		try {
			getService().openAction();
		} catch (AuctionException e) {
		}
	}

	@Test
	public void countBid1() {
		when(hazelcastFactory.getHazelcastInstance()).thenReturn(hazelcastInstance);
		when(hazelcastInstance.getMap(Constants.DEFAULT_AUCTION)).thenReturn(imap);
		Auction auction = Auction.builder()
				.open(true)
				.bids(new TreeMap<Float, List<Bid>>())
				.build();
		
		when(imap.get(anyString())).thenReturn(auction);
		try {
			getService().countBid(Bid.builder().bid(0.1F).ownerName("Test").build());
		} catch (AuctionException e) {
			assertEquals(e.getMessage(), "There is no active auction to receive bid");
		}
	}

	@Test
	public void closeAuction1() {
		when(hazelcastFactory.getHazelcastInstance()).thenReturn(hazelcastInstance);
		when(hazelcastInstance.getMap(Constants.DEFAULT_AUCTION)).thenReturn(imap);
		Auction auction = Auction.builder()
				.open(true)
				.bids(new TreeMap<Float, List<Bid>>())
				.build();
		
		when(imap.get(anyString())).thenReturn(auction);
		try {
			getService().closeAuction();
		} catch (AuctionException e) {
			assertEquals(e.getMessage(), "There is no active auction to be closed!");
		}
	}
	@Test
	public void closeAuction2() {
		when(hazelcastFactory.getHazelcastInstance()).thenReturn(hazelcastInstance);
		when(hazelcastInstance.getMap(Constants.DEFAULT_AUCTION)).thenReturn(imap);
		TreeMap<Float, List<Bid>> tree= new TreeMap<Float, List<Bid>>();
		List<Bid> bids = new ArrayList<>();
		Bid bid = Bid.builder().bid(0.1F).ownerName("Test").build();
		bids.add(bid);
		tree.put(0.1F, bids);
		Auction auction = Auction.builder()
				.open(true)
				.bids(tree)
				.build();
		
		when(imap.get(anyString())).thenReturn(auction);
		try {
			getService().closeAuction();
			assertEquals(bid, getService().closeAuction().getBid());
		} catch (AuctionException e) {
		}
	}


	private AuctionService getService() {
		return new AuctionService(hazelcastFactory);
	}
}
