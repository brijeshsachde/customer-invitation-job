package com.brijeshsachde.customer.invitation.service;

import com.brijeshsachde.customer.invitation.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CustomerService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public List<Customer> jsonStringListToCustomers(List<String> jsonStringList) {
        List<Customer> customers = new LinkedList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (String jsonString : jsonStringList) {
            if (jsonString != null) {
                try {
                    customers.add(mapper.readValue(jsonString, Customer.class));
                } catch (JsonProcessingException e) {
                    LOG.error("Error parsing %s: %s", jsonString, e);
                }
            }
        }
        return customers;
    }

    public List<String> customersToJsonStringList(List<Customer> customers) {
        List<String> jsonStringList = new LinkedList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (Customer customer : customers) {
            if (customer != null) {
                try {
                    jsonStringList.add(mapper.writeValueAsString(customer));
                } catch (JsonProcessingException e) {
                    LOG.error("Error parsing %s: %s", customer, e);
                }
            }
        }
        return jsonStringList;
    }

}