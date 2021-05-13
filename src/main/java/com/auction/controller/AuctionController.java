package com.auction.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auction.exception.AuctionException;
import com.auction.pojo.Bid;
import com.auction.service.AuctionService;

/**
 * Simples controller to demonstrate a auction case
 * @author Arthur hideli
 *
 */
@RestController
@RequestMapping(value = "/auction")
public class AuctionController {
	
	private AuctionService auctionService;
	
	@Autowired
	public AuctionController(AuctionService auctionService) {
		this.auctionService = auctionService;
	}

	@PostMapping( path = "/open")
	@ResponseBody
	public ResponseEntity<?> open() {
		try {
			auctionService.openAction();
			return ResponseEntity.ok().body("Auction is now open!");
		} catch (AuctionException e) {
			return ResponseEntity.ok().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping( path = "/sendBid")
	@ResponseBody
	public ResponseEntity<?> sendBid(@RequestBody @Valid Bid entryBid) {
		try {
			auctionService.countBid(entryBid);
			return ResponseEntity.ok().build();
		} catch (AuctionException e) {
			return ResponseEntity.ok().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@DeleteMapping( path = "/close")
	@ResponseBody
	public ResponseEntity<?> close() {
		try {
			return ResponseEntity.ok().body(auctionService.closeAuction());
		} catch (AuctionException e) {
			return ResponseEntity.ok().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
