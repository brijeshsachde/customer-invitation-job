package com.brijeshsachde.customer.invitation.service;

import com.brijeshsachde.customer.invitation.constant.StringConstants;
import com.brijeshsachde.customer.invitation.model.Customer;
import com.brijeshsachde.customer.invitation.util.FileUtil;
import com.brijeshsachde.customer.invitation.util.GPSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class InvitationService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AWSS3Service awsS3Service;

    @Autowired
    private CustomerService customerService;

    @Value("${s3.bucket-name}")
    private String bucketName;

    @Value("${s3.file-name}")
    private String fileName;

    @Value("${invitation.threshold-distance: 0.0}")
    private double threshold;

    @Value("${invitation.office.latitude: 0.0}")
    private double officeLatitude;

    @Value("${invitation.office.longitude: 0.0}")
    private double officeLongitude;

    public void inviteCustomers() {
        List<String> fileContent = awsS3Service.readFileByLine(bucketName, fileName);
        if (fileContent != null) {
            List<Customer> customers = customerService.jsonStringListToCustomers(fileContent);
            LOG.debug("Customers: " + customers.toString());
            List<Customer> customersToInvite = new ArrayList<>();
            for (Customer customer : customers) {
                if (GPSUtil.distance(officeLatitude, officeLongitude, customer.getLatitude(), customer.getLongitude()) <= threshold) {
                    customersToInvite.add(customer);
                }
            }
            LOG.debug("Customers to invite: " + customersToInvite.toString());
            generateOutput(customersToInvite);
        }
    }

    private void generateOutput(List<Customer> customersToInvite) {
        Collections.sort(customersToInvite, Comparator.comparingLong(Customer::getId));
        List<String> outputList = customerService.customersToJsonStringList(customersToInvite);
        StringBuilder sb = new StringBuilder();
        for (String customer : outputList) {
            sb.append(customer).append("\n");
        }
        LOG.info("\nOutput: \n" + sb.toString());
        if(customersToInvite.size() > 0) {
            FileUtil.listToFile(outputList, StringConstants.OUTPUT_FILE_NAME);
        }
    }

}