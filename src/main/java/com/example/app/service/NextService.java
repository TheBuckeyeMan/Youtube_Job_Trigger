package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NextService {
    private static final Logger log = LoggerFactory.getLogger(NextService.class);

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


    public String getNextServiceARN(String lastService){
        log.info("Attempting to determing the Next Service ARN...");

        //Determing the next service
        String nextService = this.getNextService(lastService);

        //Get the next service ARN
        String nextServiceARN = this.getARN(nextService);

        //Log NExt Service ARN
        log.info("The ARN of the Next Service is: " + nextServiceARN);
        
        //Return the ARN of the next service
        return nextServiceARN;
    }

    private String getNextService(String lastService){
        log.info("Attempting to determing the next service from the prior service: " + lastService);
            switch(lastService) {
                case "youtube-service-1,":
                    return "youtube-service-2";
                case "youtube-service-2,":
                    return "youtube-service-3";
                case "youtube-service-3,":
                    return "youtube-service-4";
                case "youtube-service-4,":
                    return "youtube-service-5";
                case "youtube-service-5,":
                    return "Finished";
                default:
                    log.warn("lastService was not recognized. Check formatting");
                    throw new IllegalArgumentException("Unable to determine the next service for: lastService variable on getNextService method NextService.java line 73");
            }
    }

    private String getARN(String nextService){
        log.info("Attempting to determin the ARN for service: " + nextService);
            switch(nextService) {
                case "youtube-service-1":
                    return lambda1arn;
                case "youtube-service-2":
                    return lambda2arn;
                case "youtube-service-3":
                    return lambda3arn;
                case "youtube-service-4":
                    return ecstaskarn;
                case "youtube-service-5":
                    return lambda5arn;
                case "Finished":
                    return null;
                default:
                    log.warn("Error getting ARN of Next Lambda. Check formatting");
                    throw new IllegalArgumentException("Unable to determine the ARN for: nextService variable on getARN method NextService.java line 100");
                }
    }
}
