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
        try{

            //Determing the next service
            String nextService = this.getNextService(lastService);

            //Get the next service ARN
            String nextServiceARN = this.getARN(nextService);

            //Log NExt Service ARN
            log.info("The ARN of the Next Service is: " + nextServiceARN);
            
            //Return the ARN of the next service
            return nextServiceARN;

        } catch (Exception e){
            log.error("Error occured while trying to determing the Next Service from the Last Service", e.getMessage(),e);
            //TODO Add in email error tracking here
            return null;
        }
    }

    private String getNextService(String lastService){
        log.info("Attempting to determing the next service from the prior service: " + lastService);
        try{
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
                    return null;
            }
        } catch (Exception e) {
            log.error("Error while trying to get the next service form the current one", e.getMessage(), e);
            //TODO Add in email Error Handling
            return null;
        }
    }

    private String getARN(String nextService){
        log.info("Attempting to determin the ARN for service: " + nextService);
        try{
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
                    return "Finished";
                default:
                    log.warn("Error getting ARN of Next Lambda. Check formatting");
                    return null;
                }
        } catch (Exception e){
            log.error("Error occured while trying to get lambda ARN for service: " + nextService, e.getMessage(),e);
            //TODO Add in custom email handling
            return null;
        }
    }
}
