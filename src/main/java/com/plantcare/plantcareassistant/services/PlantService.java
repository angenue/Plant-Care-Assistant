package com.plantcare.plantcareassistant.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PlantService {

    private final RestTemplate restTemplate;

    public PlantService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


}


//https://perenual.com/api/species-list?key=sk-PlIJ657dffdc96fdb3485