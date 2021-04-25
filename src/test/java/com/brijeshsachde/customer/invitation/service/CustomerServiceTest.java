package com.brijeshsachde.customer.invitation.service;

import com.brijeshsachde.customer.invitation.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Test
    void jsonStringListToCustomersSuccess() {
        List<String> testValidInput = new LinkedList<>();
        testValidInput.add("{\"latitude\": \"53.1229599\", \"user_id\": 6, \"name\": \"Theresa Enright\", \"longitude\": \"-6.2705202\"}");
        testValidInput.add("{\"latitude\": \"53.2451022\", \"user_id\": 4, \"name\": \"Ian Kehoe\", \"longitude\": \"-6.238335\"}");
        List<Customer> result = customerService.jsonStringListToCustomers(testValidInput);
        assertEquals(2, result.size());
    }
    @Test
    void jsonStringListToCustomersInvalidInput() {
        List<String> testInvalidInput = new LinkedList<>();
        testInvalidInput.add("{\"latitude\": \"53.1229599\", \"id\": 6, \"name\": \"Theresa Enright\", \"longitude\": \"-6.2705202\"}");
        testInvalidInput.add("{\"latitude\": \"test\", \"user_id\": 4, \"name\": \"Ian Kehoe\", \"longitude\": \"-6.238335\"}");
        testInvalidInput.add("{\"latitude\": \"53.2451022\", \"user_id\": 4, \"name\": \"Ian Kehoe\", \"longitude\": \"-6.238335\"");
        testInvalidInput.add("");
        testInvalidInput.add(null);
        List<Customer> result = customerService.jsonStringListToCustomers(testInvalidInput);
        assertEquals(0, result.size());
    }

    @Test
    void customersToJsonStringListSuccess() {
        List<Customer> testCustomers = new ArrayList<>();
        testCustomers.add(new Customer(6, "Theresa Enright", "53.1229599", "-6.2705202"));
        testCustomers.add(new Customer(4, "Ian Kehoe", "53.2451022", "-6.238335"));
        List<String> result = customerService.customersToJsonStringList(testCustomers);
        assertEquals(2, result.size());
    }

    @Test
    void customersToJsonStringListInvalidInput() {
        List<Customer> testCustomers = new ArrayList<>();
        testCustomers.add(new Customer(6, "Theresa Enright", "53.1229599", "-6.2705202"));
        testCustomers.add(null);
        List<String> result = customerService.customersToJsonStringList(testCustomers);
        System.out.println(result.toString());
        assertEquals(1, result.size());
    }

}