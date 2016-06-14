FROM java:8
MAINTAINER Ed Bayudan <ed.bayudan@yahoo.com>
EXPOSE 8888
ENV JAVA_OPTS -Dspring.profiles.active=docker
ADD config-server.tar /
ENTRYPOINT ["/config-server/bin/config-server"]
