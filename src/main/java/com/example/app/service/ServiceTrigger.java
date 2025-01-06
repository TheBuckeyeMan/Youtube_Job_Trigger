package com.example.app.service;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceTrigger {
    private static final Logger log = LoggerFactory.getLogger(ServiceTrigger.class);
    private LastLog lastLog;
    private LastService lastService;
    private NextService nextService;
    private TriggerService triggerService;

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${aws.s3.bucket.logging}")
    private String loggingBucket;

    @Value("${aws.s3.key.logging}")
    private String loggingBucketKey;

    public ServiceTrigger(LastLog lastLog, LastService lastService, NextService nextService, TriggerService triggerService){
        this.lastLog = lastLog;
        this.lastService = lastService;
        this.nextService = nextService;
        this.triggerService = triggerService;
    }

    public void TriggerService(){
        //Initialization Logs
        log.info("Initializing Lambda...");
        log.info("The Active Environment is set to: " + environment);
        try{

            //Get the most recent
            String lastLogName = lastLog.getLastLog(loggingBucket, loggingBucketKey);
            log.info("The Last Log was: " + lastLogName);

            //Get the Last Service
            String lastServiceName = lastService.lastService(lastLogName);

            //Get the Next Service ARN
            String nextServiceARN = nextService.getNextServiceARN(lastServiceName);

            //Check if Service is Finished
            if (nextServiceARN == "Finished"){
                log.info("All Services has successfully triggered! Youtube Video Published!");
            } else {
                //Trigger Lambda or ECS
                triggerService.TriggerNextService(nextServiceARN);
                log.info("Final: The Lambda has finished and the next service: " + " has been tiggered");
            }



            log.info("Lambda Exiting");
        } catch (Exception e){
            log.error("Error triggering Service");
            //TODO Add in email Handiling
        }
    }
}