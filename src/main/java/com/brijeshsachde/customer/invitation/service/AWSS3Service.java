package com.brijeshsachde.customer.invitation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Service
public class AWSS3Service {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private S3Client s3Client;

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder().credentialsProvider(AnonymousCredentialsProvider.create())
                .region(Region.AWS_GLOBAL).build();
    }

    public List<String> readFileByLine(String bucketName, String fileName) {
        List<String> fileContent = new LinkedList<>();
        ResponseInputStream<GetObjectResponse> responseInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(fileName).build();
            responseInputStream = s3Client.getObject(getObjectRequest);
            inputStreamReader = new InputStreamReader(responseInputStream);
            reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
        } catch (Exception e) {
            LOG.error("Exception while reading file", e);
            return null;
        } finally {
            if (responseInputStream != null) {
                try {
                    responseInputStream.close();
                } catch (IOException e) {
                    LOG.error("Exception while closing responseInputStream", e);
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    LOG.error("Exception while closing inputStreamReader", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.error("Exception while closing reader", e);
                }
            }
        }
        return fileContent;
    }

}