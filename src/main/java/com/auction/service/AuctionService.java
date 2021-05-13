package com.auction.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.auction.config.HazelcastFactory;
import com.auction.constants.Constants;
import com.auction.exception.AuctionException;
import com.auction.pojo.Auction;
import com.auction.pojo.AuctionSummary;
import com.auction.pojo.Bid;
import com.hazelcast.core.IMap;

import lombok.extern.slf4j.Slf4j;

/**
 * This class contains the basic functionalities for auction challenge
 * @author Arthur hideli
 *
 */
@Service
@Slf4j
public class AuctionService {
	
	private final HazelcastFactory hazelcastClientFactory;
	
	public AuctionService(HazelcastFactory hazelcastClientFactory) {
		this.hazelcastClientFactory = hazelcastClientFactory;
	}
	
	/**
	 * Initialize temporary data storage for an auction
	 * @throws AuctionException if is opened.
	 */
	public void openAction() throws AuctionException {
		IMap<String, Auction> imap = getAuctionDataMap();
		if (!imap.isEmpty()) throw new AuctionException("Auction is already open!");
		
		Auction auction = Auction.builder()
				.open(true)
				.bids(new TreeMap<Float, List<Bid>>())
				.build();
		imap.put(Constants.DEFAULT_AUCTION, auction);
		log.info("Auction data structure initialized sucessfully");
	}
	
	/**
	 * Count, sums and save incoming bids for an auction
	 * @param entryBid 
	 * @throws AuctionException is there is no active auction
	 */
	public void countBid(Bid entryBid) throws AuctionException {
		
		IMap<String, Auction> imap = getAuctionDataMap();
		if (imap.isEmpty()) throw new AuctionException("There is no active auction to receive bid");
		
		Auction auction = (Auction) imap.get(Constants.DEFAULT_AUCTION); // get the auction position
		TreeMap<Float, List<Bid>> bids = auction.getBids();
		
		if (auction.getBidCount() == 999) {
			return; // ignoring bids 
		}
		
		auction.setBidCount((short) (auction.getBidCount() + 1)); // increment bid count
		bids.computeIfAbsent(entryBid.getBid(), list -> new ArrayList<>()); // prevent fail
		
		List<Bid> bidList = bids.get(entryBid.getBid()); // get the bid list for a value
		bidList.add(entryBid);
		
		bids.put(entryBid.getBid(),bidList);
		
		auction.setBids(bids); // overwrite the data
		
		imap.put(Constants.DEFAULT_AUCTION, auction); // write data into sigleton memory
		log.info("Auction data updated sucessfully");
	}
	
	/**
	 * Close session e return the winner of an auction. Also clears the storage witch results in a no opened auction
	 * @return {@link AuctionSummary} with a friendly data structure for auction challenge
	 * @throws AuctionException if there is no active auction to be casted
	 */
	public AuctionSummary closeAuction() throws AuctionException {
		log.info("Closing auction ... ");
		IMap<String, Auction> imap = getAuctionDataMap();
		if (imap.isEmpty()) throw new AuctionException("There is no active auction to be closed!");
		
		AuctionSummary summary = null;
		
		Auction auction = (Auction) imap.get(Constants.DEFAULT_AUCTION); // get the auction position
		TreeMap<Float, List<Bid>> bids = auction.getBids(); // get bids by value
		log.info("Getting the winner ... ");
		for (Map.Entry<Float, List<Bid>> bidMap: bids.entrySet()) { // search for the first bid that is unique in the auction
			List<Bid> bidList = bidMap.getValue(); // get the bid for this value
		
			if (bidList.size() == 1) { // hell yeah, is unique
				Bid winner = bidList.get(0); // get size 1 position 0
				
				// build a friendly summary
				double totalCollected = auction.getBidCount() * 0.98;
				summary = AuctionSummary
						.builder()
						.bid(winner)
						.totalCollected(totalCollected)
						.output(getWinnerOutput(winner, totalCollected))
						.build();
				log.info("Winner founded ");
				break;
			}
		}
		
		getAuctionDataMap().destroy(); // destroy this auction instance from the storage
		log.info("Auction closed");
		return summary;
	}

	private String getWinnerOutput(Bid winner, double totalCollected) {
		return new StringBuilder().append("Vencedor: ").append(winner.getOwnerName()).append(" , com lance de ").append(winner.getBid()).append(" e arrecadação de ").append(totalCollected).toString();
	}

	private IMap<String, Auction> getAuctionDataMap() {
		return hazelcastClientFactory.getHazelcastInstance().getMap(Constants.DEFAULT_AUCTION);
	}
	
}