# Customer Invitation Job

Customer Invitation Job

## Setup

### Installation

* Install [JDK 1.8](https://www.oracle.com/java/technologies/javase-downloads.html) or later
* Install [Gradle](https://gradle.org/install/)


### Configuration

Update property values in src\main\resources\application.properties if required.

* Input File: Update **s3.bucket-name** and **s3.file-name** to change AWS S3 bucket and Object respectively.
* Distance: Update **invitation.threshold-distance** (in KMs) to change range of distance to consider to invite customers
* Base Location Coordinates: Update *i*nvitation.office.latitude** and **invitation.office.longitude** to change base location coordinates.

## Build and Start Application

### Unix:
```sh
./gradlew bootRun
```

### Windows:
```sh
gradlew.bat bootRun
```

## Run Tests

### Unix:
```sh
./gradlew clean test
```

### Windows:
```sh
gradlew.bat clean test
```
