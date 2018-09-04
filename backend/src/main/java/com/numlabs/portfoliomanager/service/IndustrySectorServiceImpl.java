package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.IndustrySector;
import com.numlabs.portfoliomanager.repository.IndustrySectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndustrySectorServiceImpl implements IndustrySectorService {

    @Autowired
    private IndustrySectorRepository industrySectorRepository;

    @Override
    public List<IndustrySector> findAll() {
        return industrySectorRepository.findAll();
    }

    @Override
    public IndustrySector getById(Long id) {
        return industrySectorRepository.findById(id);
    }
}
