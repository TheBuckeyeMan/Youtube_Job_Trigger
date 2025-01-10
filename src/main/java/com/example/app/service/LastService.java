package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LastService {
    private static final Logger log = LoggerFactory.getLogger(LastService.class);

    public String lastService(String lastLog){
        log.info("Attempting to Read Prior Service From Last Log...");
        try{
            //Check if Prior Service Succeeded or Failed
            boolean SorF = this.priorServiceSucceedorFail(lastLog);

            //Prior Service
            String priorService = this.getLastService(lastLog);

            //Return Prior Service
            return priorService;

        } catch (Exception e){
            log.error("Unable to get last service from last log. Line 16 LastService.java", e.getMessage(),e);
            //TODO Add in Email Error Handling
            return null;
        }
    }

    private boolean priorServiceSucceedorFail(String lastLog){
        log.info("Checking if Prior Service Succeeded or failed");
        try{
            if (lastLog == null){
                log.error("Error: The last log is null.");
                throw new IllegalArgumentException();
                //TODO Add in Email
            } else if (lastLog.contains("Success")){
                log.info("Prior Log: " + lastLog + " indicates a success");
                return true;
            } else {
                log.info("Prior Log: " + lastLog + " indicates an Error occured");
                return false;
            }
        } catch (Exception e){
            log.error("Unable to Verify if prior service Succeeded or Failed");
            //TODO Add in email 
            return false;
        }
    }

    private String getLastService(String lastLog){
        log.info("Attempting to Convert last log to Last Service");
        try{
            if (lastLog.contains("On: ")){
                String priorService = lastLog.substring(lastLog.indexOf("On: ") + 4).trim();
                log.info("The Prior Service was: " + priorService);
                return priorService;
            } else {
                log.error("The Log recorded in log file is of the incorrect format. Please ensure it is formatted in the (Succcess: Success occured at: 2025-01-06T12:12:32.910250700 On: youtube-service-X,) format. It is required we have (On: ) before the name of the last service");
                //TODO Add in email Error Handling 
                return null;
            }
        } catch (Exception e){
            log.error("Error occured while trying to convery the lastLog to LastService", e.getMessage(), e);
            //TODO Add in Email Error Handling
            return null;
        }
    }
}