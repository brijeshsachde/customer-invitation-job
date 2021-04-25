package com.brijeshsachde.customer.invitation.service;

import com.brijeshsachde.customer.invitation.constant.StringConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private AWSS3Service awsS3Service;

    @Mock
    CustomerService customerService;

    @InjectMocks
    InvitationService invitationService;

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @BeforeEach
    void beforeEach() throws IOException {
        ReflectionTestUtils.setField(invitationService, "bucketName", "test");
        ReflectionTestUtils.setField(invitationService, "fileName", "test");
        ReflectionTestUtils.setField(invitationService, "threshold", 100.00);
        ReflectionTestUtils.setField(invitationService, "officeLatitude", 53.339428);
        ReflectionTestUtils.setField(invitationService, "officeLongitude", -6.257664);
        Files.deleteIfExists(Paths.get(StringConstants.OUTPUT_FILE_NAME));
    }

    @Test
    void inviteCustomersSuccess() throws IOException {
        List<String> mockFileContent = new LinkedList<>();
        mockFileContent.add("{\"latitude\": \"53.1229599\", \"user_id\": 6, \"name\": \"Theresa Enright\", \"longitude\": \"-6.2705202\"}");
        mockFileContent.add("{\"latitude\": \"51.92893\", \"user_id\": 1, \"name\": \"Alice Cahill\", \"longitude\": \"-10.27699\"}");
        mockFileContent.add("{\"latitude\": \"53.2451022\", \"user_id\": 4, \"name\": \"Ian Kehoe\", \"longitude\": \"-6.238335\"}");
        mockFileContent.add("{\"latitude\": \"53.1302756\", \"user_id\": 5, \"name\": \"Nora Dempsey\", \"longitude\": \"-6.2397222\"}");
        mockFileContent.add("{\"latitude\": \"51.8856167\", \"user_id\": 2, \"name\": \"Ian McArdle\", \"longitude\": \"-10.4240951\"}");
        mockFileContent.add("{\"latitude\": \"52.3191841\", \"user_id\": 3, \"name\": \"Jack Enright\", \"longitude\": \"-8.5072391\"}");
        when(awsS3Service.readFileByLine(anyString(), anyString())).thenReturn(mockFileContent);
        when(customerService.customersToJsonStringList(any())).thenCallRealMethod();
        when(customerService.jsonStringListToCustomers(any())).thenCallRealMethod();
        invitationService.inviteCustomers();
        assertTrue(Files.exists(Paths.get(StringConstants.OUTPUT_FILE_NAME)));
        assertEquals("{\"name\":\"Ian Kehoe\",\"user_id\":4}" + System.lineSeparator() +
                "{\"name\":\"Nora Dempsey\",\"user_id\":5}" + System.lineSeparator() +
                "{\"name\":\"Theresa Enright\",\"user_id\":6}" + System.lineSeparator(), readFile(StringConstants.OUTPUT_FILE_NAME, StandardCharsets.UTF_8));
    }

    @Test
    void inviteCustomersNoCustomerInvited() throws IOException {
        List<String> mockFileContent = new LinkedList<>();
        mockFileContent.add("{\"latitude\": \"51.92893\", \"user_id\": 1, \"name\": \"Alice Cahill\", \"longitude\": \"-10.27699\"}");
        mockFileContent.add("{\"latitude\": \"52.3191841\", \"user_id\": 3, \"name\": \"Jack Enright\", \"longitude\": \"-8.5072391\"}");
        mockFileContent.add("{\"latitude\": \"51.8856167\", \"user_id\": 2, \"name\": \"Ian McArdle\", \"longitude\": \"-10.4240951\"}");
        when(awsS3Service.readFileByLine(anyString(), anyString())).thenReturn(mockFileContent);
        when(customerService.customersToJsonStringList(any())).thenCallRealMethod();
        when(customerService.jsonStringListToCustomers(any())).thenCallRealMethod();
        invitationService.inviteCustomers();
        assertFalse(Files.exists(Paths.get(StringConstants.OUTPUT_FILE_NAME)));
    }

    @Test
    void inviteCustomersAllCustomerInvited() throws IOException {
        List<String> mockFileContent = new LinkedList<>();
        mockFileContent.add("{\"latitude\": \"53.1229599\", \"user_id\": 6, \"name\": \"Theresa Enright\", \"longitude\": \"-6.2705202\"}");
        mockFileContent.add("{\"latitude\": \"53.2451022\", \"user_id\": 4, \"name\": \"Ian Kehoe\", \"longitude\": \"-6.238335\"}");
        mockFileContent.add("{\"latitude\": \"53.1302756\", \"user_id\": 5, \"name\": \"Nora Dempsey\", \"longitude\": \"-6.2397222\"}");
        when(awsS3Service.readFileByLine(anyString(), anyString())).thenReturn(mockFileContent);
        when(customerService.customersToJsonStringList(any())).thenCallRealMethod();
        when(customerService.jsonStringListToCustomers(any())).thenCallRealMethod();
        invitationService.inviteCustomers();
        assertTrue(Files.exists(Paths.get(StringConstants.OUTPUT_FILE_NAME)));
        assertEquals("{\"name\":\"Ian Kehoe\",\"user_id\":4}" + System.lineSeparator() +
                "{\"name\":\"Nora Dempsey\",\"user_id\":5}" + System.lineSeparator() +
                "{\"name\":\"Theresa Enright\",\"user_id\":6}" + System.lineSeparator(), readFile(StringConstants.OUTPUT_FILE_NAME, StandardCharsets.UTF_8));
    }

    @Test
    void inviteCustomersFileReadIssue() throws IOException {
        when(awsS3Service.readFileByLine(anyString(), anyString())).thenReturn(null);
        invitationService.inviteCustomers();
        assertFalse(Files.exists(Paths.get(StringConstants.OUTPUT_FILE_NAME)));
    }

    @Test
    void inviteCustomersNoCustomers() throws IOException {
        List<String> mockFileContent = new LinkedList<>();
        mockFileContent.add("{\"latitude\": \"53.1229599\", \"id\": 6, \"name\": \"Theresa Enright\", \"longitude\": \"-6.2705202\"}");
        mockFileContent.add("{\"latitude\": \"test\", \"user_id\": 1, \"name\": \"Alice Cahill\", \"longitude\": \"-10.27699\"}");
        mockFileContent.add(null);
        when(awsS3Service.readFileByLine(anyString(), anyString())).thenReturn(mockFileContent);
        when(customerService.jsonStringListToCustomers(any())).thenReturn(new LinkedList<>());
        when(customerService.customersToJsonStringList(any())).thenCallRealMethod();
        invitationService.inviteCustomers();
        assertFalse(Files.exists(Paths.get(StringConstants.OUTPUT_FILE_NAME)));
    }

    @Test
    void inviteCustomersNoCustomersInFile() throws IOException {
        when(awsS3Service.readFileByLine(anyString(), anyString())).thenReturn(new LinkedList<>());
        when(customerService.customersToJsonStringList(any())).thenCallRealMethod();
        when(customerService.jsonStringListToCustomers(any())).thenCallRealMethod();
        invitationService.inviteCustomers();
        assertFalse(Files.exists(Paths.get(StringConstants.OUTPUT_FILE_NAME)));
    }

}