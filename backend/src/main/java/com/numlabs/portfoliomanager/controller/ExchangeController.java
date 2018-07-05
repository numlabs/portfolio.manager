package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExchangeController {

    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("exchanges")
    public ResponseEntity<List<Exchange>> getExchanges() {
        return new ResponseEntity<List<Exchange>>(exchangeService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "exchange/{id}")
    public ResponseEntity<Exchange> getExchange(@PathVariable("id") Long id) {
        Exchange exchange = exchangeService.findExchange(Long.valueOf(id));
        return new ResponseEntity<>(exchange, HttpStatus.OK);
    }
}
