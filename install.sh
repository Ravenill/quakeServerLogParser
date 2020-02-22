#!/bin/bash
#Created by Rave

mvn clean install && \
rm -fr ~/quakeServer/stats/stats.jar && \
cp ./target/quakeStats-1.0-SNAPSHOT-jar-with-dependencies.jar ~/quakeServer/stats/stats.jar