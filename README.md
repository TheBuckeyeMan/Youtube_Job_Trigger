# Youtube_Job_Trigger
Lambda to Trigger The different youtube services in aws

# Exceptions

## Types of Exceptions

### IOException
Exception while Reading to or Writing to files, accessing resources over a network, or working with streams
Ex: File Not found, Network disconnected durring stream operation

### RuntimeException
Generic exception occure when there is an issue with Program logic

#### Subclasses
1.NullPointerException: Thrown when attempting to access a method or property on a null object reference
    Used to check if a Variable defined WITHIN the method is null ex. private String getName(){String name = determineName(); if (name == null){throw new NullPointerException}}
2.IllegalArgumentException: Error Occurs if a method has been passed an illegal or inapropriate argument
    Used if Input parameter to a method is invalid Ex. getName(name) -> If the value of name is null or invalid, we should use IllegalArgumentException
3.IndexOutOfBoundsException: 

### SQLException
Exception that occue while queering a database
Ex: Queering a table that does not exist, Violating a unique key constraint

### HttpClientErrorException
Exception that occure while making HTTP Request response is a 404 or 400
Ex: Invalid API Endpoint, Sending Incorrect Params in an HTTP Request

### TimeoutException
Exception that occurs when an HTTP Request takes to long to execute
Ex: Network Request taking to long. Waiting for computation result in multi-threaded application

### IndexOutOfBoundsException
Exception that occurs when manipulating a list or array, or trying to access an index that is not in the array
Ex: Trying to access an array index of 5 when there are only 3 eleents in the array