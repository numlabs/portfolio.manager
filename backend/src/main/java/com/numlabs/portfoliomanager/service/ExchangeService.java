package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Exchange;

import java.util.List;


public interface ExchangeService {

    public List<Exchange> findAll();

    Exchange findExchange(Long id);

    Exchange getExchangeByCode(String exchangeCode);
}
