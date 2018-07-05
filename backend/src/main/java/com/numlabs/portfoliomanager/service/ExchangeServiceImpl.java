package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Override
    public List<Exchange> findAll() {
        List<Exchange> elements = new ArrayList<>();
        exchangeRepository.findAll().forEach(e->elements.add(e));

        return elements;
    }

    @Override
    public Exchange findExchange(Long id) {
        return exchangeRepository.findById(id);
    }

    @Override
    public Exchange getExchangeByCode(String exchangeCode) {
        return exchangeRepository.getExchangeByCode(exchangeCode);
    }
}
