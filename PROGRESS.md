#  Progress Log

## 30th Jan, 2023 
 - Created Repo for event driven work space 
 - Created Gateway Service infra 
 - Created Product Service infra 
 - Created Discovery Service infra

## 1st Feb, 2023
- Completed wiring between the services
- Booted multi instances for services
- Boot Axon server (on Docker)
  - Run - docker pull axoniq/axonserver:latest
  - Run - docker run -d --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver:latest
  - Open http://localhost:8024/
