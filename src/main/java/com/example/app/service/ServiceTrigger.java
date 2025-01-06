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

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${next.lambda.lambda1arn}")
    private String lambda1arn;

    @Value("${next.lambda.lambda2arn}")
    private String lambda2arn;

    @Value("${next.lambda.lambda3arn}")
    private String lambda3arn;

    @Value("${next.lambda.ecstaskarn}")
    private String ecstaskarn;

    @Value("${next.lambda.lambda5arn}")
    private String lambda5arn;

    @Value("${next.lambda.lambda6arn}")
    private String lambda6arn;

    @Value("${ecs.cluster.name}")
    private String ecsClusterName;

    @Value("${aws.s3.bucket.logging}")
    private String loggingBucket;

    @Value("${aws.s3.key.logging}")
    private String loggingBucketKey;

    public ServiceTrigger(LastLog lastLog){
        this.lastLog = lastLog;
    }

    public void TriggerService(){
        //Initialization Logs
        log.info("Initializing Lambda...");
        log.info("The Active Environment is set to: " + environment);
        try{

            //Get the most recent
            String LastLog = lastLog.getLastLog(loggingBucket, loggingBucketKey);
            log.info("The Last Log was: " + LastLog);

            //Get the Last Service





            log.info("Final: The Lambda has finished and the next service: " + " has been tiggered");

        } catch (Exception e){
            log.error("Error triggering Service");
        }
    }
}