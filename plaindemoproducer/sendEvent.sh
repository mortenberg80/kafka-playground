#!/bin/bash

PERSON_ID=123

if [ ! -z $1 ]
then
  PERSON_ID=$1
fi


curl -XPOST "http://localhost:8080/test/$PERSON_ID"
