FROM quay.io/ukhomeofficedigital/openjdk8:v0.1.2

USER root

ADD pttg-family-migration-api*.jar /

ADD run.sh /
RUN /bin/bash -c 'chmod +x /run.sh'

EXPOSE 8081

ENTRYPOINT /run.sh
