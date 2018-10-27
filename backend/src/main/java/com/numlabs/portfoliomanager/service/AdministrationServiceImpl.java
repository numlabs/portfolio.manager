package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Property;
import com.numlabs.portfoliomanager.repository.AdministrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministrationServiceImpl implements AdministrationService {

    @Autowired
    private AdministrationRepository administrationRepository;

    @Override
    public Property getProperty(String key) {
        return administrationRepository.getProperty(key);
    }
}
