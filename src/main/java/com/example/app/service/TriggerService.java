package com.example.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.arns.Arn;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.RunTaskRequest;
import software.amazon.awssdk.services.ecs.model.RunTaskResponse;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@Service
public class TriggerService {
    private static final Logger log = LoggerFactory.getLogger(TriggerService.class);
    private final EcsClient ecsClient;
    private final LambdaClient lambdaClient;

    @Value("${ecs.cluster.name}")
    private String ecsClusterName;

    @Value("${ecs.task.executionRoleArn}")
    private String ecsExecutionRoleArn;

    @Value("${next.lambda.lambda2arn}")
    private String lambda2arn;

    @Value("${next.lambda.lambda6arn}")
    private String lambda6arn;

    public TriggerService(EcsClient ecsClient, LambdaClient lambdaClient){
        this.ecsClient = ecsClient;
        this.lambdaClient = lambdaClient;
    }

    public void TriggerNextService(String nextServiceARN){
        log.info("Attempting to Trigger the next Service Programatically...");
        try{
            //Validate ARN
            String validatedNextServiceArn = this.validateARN(nextServiceARN);

            //Determing if next service is ecs or Lambda
            String ARNTYPE = determineType(validatedNextServiceArn);

            //Trigger Service
            if (ARNTYPE == "ECS"){
                TriggerEcs(validatedNextServiceArn,ecsClusterName,ecsExecutionRoleArn);
            } else if (ARNTYPE == "Lambda"){
                if (validatedNextServiceArn == lambda2arn){
                    TriggerLambda(validatedNextServiceArn);
                    TriggerLambda(lambda6arn);
                } else {
                  TriggerLambda(validatedNextServiceArn);
                }
            } else {
                log.error("The ARN of: " + validatedNextServiceArn + " was not an ECS or Lambda function. Please investigate");
            }
        } catch (Exception e) {
            log.error("Error occured while triggering the next service via API", e.getMessage(),e);
            //TODO Add in email Error Handling
        }
    }

    private String determineType(String validatedNextServiceArn){
        log.info("Attempting to determing if the next service is ECS Task or Lambda Function");
        try{
            if (validatedNextServiceArn.startsWith("arn:aws:ecs")){
                log.info("The next service to be triggered is an ECS Task with the ARN of: " + validatedNextServiceArn);
                return "ECS";
            } else if (validatedNextServiceArn.startsWith("arn:aws:lambda")){
                log.info("The next service to be triggered is a Lambda Function with the ARN of: " + validatedNextServiceArn);
                return "Lambda";
            } else {
                log.error("The next service is neither Lambda Function Nor ECS Task, please investigate");
                //TODO Add email error
            }
            return "";
        } catch (Exception e){
            log.error("Error occured while trying to determing if the Next service is an ECS Task or Lambda Function", e.getMessage(), e);
            //TODO Add in email error 
            return null;
        }
    }

    private void TriggerEcs(String ARN, String ecsClusterName, String ecsExecutionRoleArn){
        log.info("Attempting to Trigger the ECS Task with ARN of: " + ARN);
        try{
            //Create ECS Task Execution Request
            log.info("Attempting to Build the ECS Task Execution Request");
            RunTaskRequest runTaskRequest = RunTaskRequest.builder()
            .cluster(ecsClusterName) // Cluster name: "youtube-cluster"
            .taskDefinition(ARN) // Task definition ARN
            .launchType("FARGATE")
            .networkConfiguration(builder -> builder
            .awsvpcConfiguration(vpc -> vpc
            .subnets(
            "subnet-0b9b1b37c75908735", 
                        "subnet-03eccd7b2757b12cd", 
                        "subnet-0f0de6e5e9ebf805d") // Subnet IDs
            .securityGroups("sg-04d0b06614d60cdf6") // Security Group ID
            .assignPublicIp("ENABLED"))) // Assign Public IP
            .overrides(override -> override.containerOverrides(builder -> builder
            .name("youtube-service-4")
            .build()))
            .build();

            //Execute the Task
            log.info("Attempting to Execute the ECS Task with ARN: " + ARN);
            RunTaskResponse runTaskResponse = ecsClient.runTask(runTaskRequest);
            log.info("ECS Task triggered successfully: " + runTaskResponse.tasks());

        } catch (Exception e){
            log.error("Error occured while trying to trigger ECS Task with ARN of: " + ARN, e.getMessage(),e);
            //TODO Add in email error 
        }
    }

    private void TriggerLambda(String ARN){
        log.info("Attempting to Trigger the lambda function with ARN of: " + ARN);
        try{
            //Build Lambda Invoke Request
            log.info("Attempting to Build Lambda Invoke Request");
            InvokeRequest request = InvokeRequest.builder()
                .functionName(ARN)
                .build();

            //Execute the Request
            log.info("Attempting to Execute the Lambda Function with ARN: " + ARN);
            InvokeResponse response = lambdaClient.invoke(request);
            log.info("Lambda function triggered: " + ARN + " with status code: " + response.statusCode());
        } catch (Exception e) {
            log.error("Error occured while trying to trigger lambda function with ARN of: " + ARN, e.getMessage(),e);
            //TODO Add in email error 
        }
    }

    private String validateARN(String nextServiceARN){
        log.info("Attempting to Validate the NExt Service ARN: " + nextServiceARN);
        try{
            if (nextServiceARN == null){
                log.error("The Next Service ARN: " + nextServiceARN + " is null. Error getting ARN");
                throw new IllegalArgumentException("The value of nextServiceARN is null. TriggerService.java Line 145");
            } else {
                return nextServiceARN;
            }
        } catch (Exception e){
            log.error("Error occured while trying to validate the next lambda ARN: " + nextServiceARN, e.getMessage(),e);
            //TODO Add in email error
            return null;
        }
    }
}
