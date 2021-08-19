# await-maven-plugin

| service | master  | dev |
| :---: | :---: | :---: |
| CI Build | [![Build Status](https://semaphoreci.com/api/v1/slem1/await-maven-plugin/branches/master/shields_badge.svg)](https://semaphoreci.com/slem1/await-maven-plugin)  | [![Build Status](https://semaphoreci.com/api/v1/slem1/await-maven-plugin/branches/dev/shields_badge.svg)](https://semaphoreci.com/slem1/await-maven-plugin)  |
| Test | [![Coverage Status](https://coveralls.io/repos/github/slem1/await-maven-plugin/badge.svg?branch=master)](https://coveralls.io/github/slem1/await-maven-plugin?branch=master) | [![Coverage Status](https://coveralls.io/repos/github/slem1/await-maven-plugin/badge.svg?branch=dev)](https://coveralls.io/github/slem1/await-maven-plugin?branch=dev)  |

await-maven-plugin is a plugin to pause maven build until some service is available.

## Example configuration

```xml
            <plugin>
                <groupId>com.github.slem1</groupId>
                <artifactId>await-maven-plugin</artifactId>
                <version>1.2</version>
                <executions>
                    <execution>
                        <phase>process-test-classes</phase>
                        <goals>
                            <goal>Await</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <awaitSkip>false</awaitSkip>
                    <poll>
                        <attempts>3</attempts>
                        <sleep>1000</sleep>
                    </poll>
                    <tcpConnections>
                        <tcpConnection>
                            <host>localhost</host>
                            <port>5432</port>
                        </tcpConnection>
                    </tcpConnections>
                    <httpConnections>
                        <httpConnection>
                            <url>http://mywebservice:9090</url>
                            <statusCode>200</statusCode>
                        </httpConnection>
                    </httpConnections>    
                </configuration>
            </plugin>

```

With the above configuration, the maven build will pause after process-test-classes and wait for the availability of
two services: 

  - a tcp service on localhost:5432 (postgres)
  - a 200 OK http response from http://mywebservice:9090.

The plugin will make 3 attempts on to reach each service, waiting 1000ms between each try.

## Parameters description


### awaitSkip
Set to true if you want to skip plugin execution. Can also be passed as -DawaitSkip

```xml
 <awaitSkip>true</awaitSkip>
```

### poll
The polling configuration object. Apply to each service to contact.

```xml
 <poll>
     <attempts>3</attempts>
     <sleep>1000</sleep>
 </poll>
```

#### attempts
Max number of attempts to reach a service.

```xml
     <attempts>3</attempts>
```

#### sleep
Time to wait (in ms) between two attempts.

```xml
     <sleep>1000</sleep>
```

### tcpConnections
A collection of tcpConnection elements.

#### tcp
A tcp connection configuration.

```xml
    <tcpConnection>
      <host>localhost</host>
      <port>5432</port>
    </tcpConnection>
```

##### host

The tcp host.
```xml
    <host>localhost</host>
```

##### port

The tcp port.
```xml
    <port>5432</port>
```

##### priority

Defines the order in which the connection will be attempted across tcpConnection and httpConnection. The 0 value is the highest priority.
By default, if not defined, the priority is the lowest (Integer.MAX_VALUE). 
```xml
    <priority>100</priority>
```

### httpConnections

A collection of http or https connections.

#### httpConnection

The configuration of a connection to a service running on http.
```xml
  <httpConnection>
    <url>http://mywebservice:9090</url>
    <statusCode>200</statusCode>
  </httpConnection>
```
##### url

The service URL.
```xml
  <url>http://mywebservice:9090</url>
```

The service URL can include basic auth information when running on http
```xml
  <url>http://user:pass@mywebservice:9090</url>
```

##### statusCode

The expected status code response.
```xml
   <statusCode>200</statusCode>
```

##### priority

Defines the order in which the connection will be attempted across tcpConnection and httpConnection. The 0 value is the highest priority.
By default, if not defined, the priority is the lowest (Integer.MAX_VALUE). 
```xml
    <priority>100</priority>
```

##### skip SSL certificate verification

Set true if you want to skip SSL certificate verification.

```xml
    <skipSSLCertVerification>true</skipSSLCertVerification>
```

## Example use case

Wait for a docker container startup and service up with docker-compose-maven-plugin before running integration tests.

```xml
<build>
        <plugins>
            <plugin>
                <groupId>com.dkanejs.maven.plugins</groupId>
                <artifactId>docker-compose-maven-plugin</artifactId>
                <version>2.0.1</version>
                <configuration>
                    <composeFile>../docker/docker-compose.yml</composeFile>
                    <detachedMode>true</detachedMode>
                </configuration>
                <executions>
                    <execution>
                        <phase>process-test-classes</phase>
                        <goals>
                            <goal>up</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

           <plugin>
                <groupId>com.github.slem1</groupId>
                <artifactId>await-maven-plugin</artifactId>
                <version>1.2</version>
                <executions>
                    <execution>
                        <phase>process-test-classes</phase>
                        <goals>
                            <goal>Await</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <poll>
                        <attempts>3</attempts>
                        <sleep>5000</sleep>
                    </poll>
                    <httpConnections>
                        <httpConnection>
                            <url>http://localhost:27080</url>
                            <statusCode>200</statusCode>
                        </httpConnection>
                    </httpConnections>
                </configuration>
            </plugin>
        </plugins> 
 </build>
 
 ```
