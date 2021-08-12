package com.carsaver.codereview.service;

import org.springframework.stereotype.Service;


// Need to implement runnable interface if we want Thread implementation
@Service
public class ZipCodeLookupService {

    /**
     * @param zipCode
     * @return - returns city for the given zipCode.
     */
    public String lookupCityByZip(String zipCode) {
        System.out.print("looking up city by zipCode (this might take a while)");

        try {
            //simulating a high latency call
            Thread.sleep(5000);
        } catch(Exception ignore) {

        }

        return zipCode;
    }

}
