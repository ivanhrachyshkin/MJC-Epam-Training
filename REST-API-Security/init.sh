#!/bin/bash
sudo yum update -y
sudo yum install -y java-1.8.0-openjdk
aws s3 cp s3://mjc--jar--hi/REST-API-Security.jar .
java -jar -Dspring.profiles.active=jwt REST-API-Security.jar