#!/usr/bin/env bash

JAR_PATH=$(ls -1tr target/SparkTutorial*.jar | head -n1)

echo $JAR_PATH

spark-submit \
        --class "com.github.sparktutorial.DataFrameTutorialJob" \
        --master "local[*]" \
        --driver-memory 10g \
        --executor-memory 10g \
        \
        ${JAR_PATH} \
        \
        config.file=./config/config.properties 2>&1 | tee run.log
