package com.brijeshsachde.customer.invitation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AWSS3ServiceTest {

    @Mock
    private S3Client s3Client;

    @InjectMocks
    private AWSS3Service awsS3Service;

    @Test
    void readFileByLineSuccess() {
        InputStream mockInputStream = this.getClass().getClassLoader().getResourceAsStream("input.txt");
        ResponseInputStream<GetObjectResponse> mockResponseInputStream = new ResponseInputStream<>(GetObjectResponse.builder().build(), AbortableInputStream.create(mockInputStream));
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockResponseInputStream);
        List<String> result = awsS3Service.readFileByLine("test", "test");
        assertEquals(6, result.size());
    }

    @Test
    void readFileByLineException() {
        when(s3Client.getObject(any(GetObjectRequest.class))).thenThrow(new RuntimeException("test"));
        List<String> result = awsS3Service.readFileByLine("test", "test");
        assertNull(result);
    }

}