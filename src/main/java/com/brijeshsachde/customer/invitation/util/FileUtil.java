package com.brijeshsachde.customer.invitation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileUtil {

    private final static Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    public static void listToFile(List<String> list, String fileName) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);
            for (String str : list) {
                writer.write(str + System.lineSeparator());
            }
        } catch (IOException e) {
            LOG.error("Exception while writing the file", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOG.error("Exception while closing writer", e);
                }
            }
        }
    }

}