package com.example.app.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.app.exception.LogFileNotFoundException;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class LastLog {
    private static final Logger log = LoggerFactory.getLogger(LastLog.class);
    private S3Client s3Client;
    private EmailOnError emailOnError;

    public LastLog(S3Client s3Client, EmailOnError emailOnError){
        this.s3Client = s3Client;
        this.emailOnError = emailOnError;
    }

    public String getLastLog(String bucketName, String logFileKey){
        log.info("Attempting to get Logs from S3...");
        try{
            GetObjectRequest getLogsObjectRequest = GetObjectRequest.builder()
                                                .bucket(bucketName)
                                                .key(logFileKey)
                                                .build();

            ResponseInputStream<GetObjectResponse> s3LogsObject = s3Client.getObject(getLogsObjectRequest);
            //Write Response to a list
            List<String> youtubeLogs = new BufferedReader(new InputStreamReader(s3LogsObject)).lines().collect(Collectors.toList());
            log.info("The number of invokes recorded on the: " + logFileKey + " File is " + youtubeLogs.size());

            //Verify logs
            verifyLogs(youtubeLogs);

            //Log 5 most recent Logs
            log.info(" The Five most Recent Services Triggered Were: " + getMostRecentlyActivatedServices(youtubeLogs));

            //Return only the Last Service
            return lastServiceLog(youtubeLogs);

        } catch (LogFileNotFoundException e){
            log.error("Unable to Read Contents from the Log Bucket to get prior jobs. Step 1 in GetLogs.java Line 22.", e);
            return null;
        }
    }

    private List<String> verifyLogs(List<String> logs){
        try{
            if (logs == null || logs.isEmpty()){
                log.error("The Log file returned null. Able to read contents, But log file is either empty or a problem occured when writing to object");
                //TODO Add Email On Error
                throw new IllegalArgumentException();
            } else {
                log.info("Log File Successfuly Verified and contains Required Logs");
            }
        } catch (Exception e){
            log.error("Error occured while trying to verify the log file: ", e.getMessage(), e);
            //TODO Add Email On Error
        }
        return logs;
    }

    private List<String> getMostRecentlyActivatedServices(List<String> logs){
        try{
            if (logs.size() < 6){
                log.info("There is not enough data to check the last 5 logs. Please look again when log file has more logs");
                return null;
            } else {
            //Get 5 Most Recent Logs
            List<String> recentLogs = logs.subList(Math.max(0, logs.size() - 5), logs.size());
            return recentLogs;
            }
        } catch (Exception e){
            log.error("Error occured while trying to retrieve the 5 most recent logs from the list.");
            //TODO ADD EMAIL ERROR
            return null;
        }
    }

    private String lastServiceLog(List<String> logs){
        //Get last item in list and convert to String
        String LastServiceLogName = logs.get(logs.size() - 1);
        //Return value
        log.info("Logs Read Successfully.");
        return LastServiceLogName;
    }
}
