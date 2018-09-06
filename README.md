#await-maven-plugin

await-maven-plugin is a plugin to pause maven build until some service is available.

## Example configuration

```xml
            <plugin>
                <groupId>fr.sle</groupId>
                <artifactId>await-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
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
                        <sleep>1000</sleep>
                    </poll>
                    <tcps>
                        <tcp>
                            <host>localhost</host>
                            <port>5432</port>
                        </tcp>
                    </tcps>
                    <httpz>
                        <http>
                            <url>http://mywebservice:9090</url>
                            <statusCode>200</statusCode>
                        </http>
                    </httpz>    
                </configuration>
            </plugin>

```

With the above configuration, the maven build will pause after process-test-classes and wait for the availability of
two services: 

  - a tcp service on localhost:5432 (postgres)
  - a 200 OK http response from http://mywebservice:9090.

The plugin will make 3 attempts on to reach each service, waiting 1000ms between each try.

## Parameters description

### poll
The polling configuration object. Apply to each service to contact.

```xml
 <poll>
     <attempts>3</attempts>
     <sleep>1000</sleep>
 </poll>
```

#### attempts
Max number of attempts to reach a service

```xml
     <attempts>3</attempts>
```

#### sleep
Time to wait (in ms) between two attempts

```xml
     <sleep>1000</sleep>
```

### tcps
A collection of tcp services elements

#### tcp
A tcp configuration

```xml
    <tcp>
      <host>localhost</host>
      <port>5432</port>
    </tcp>
```

##### hostname
```xml
    <host>localhost</host>
```

tcp host

##### port
```xml
    <port>5432</port>
   
```
tcp port

### httpz
A collection of http or https services

#### http
A service running on http
```xml
  <http>
    <url>http://mywebservice:9090</url>
    <statusCode>200</statusCode>
  </http>
```
##### url
The service URL
```xml
  <url>http://mywebservice:9090</url>
```

##### statusCode
The expected status code response
```xml
   <statusCode>200</statusCode>
```

## Example use case

Wait for a docker container startup and service up with docker-compose-maven-plugin

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
                <groupId>fr.sle</groupId>
                <artifactId>await-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
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
                        <sleep>3000</sleep>
                    </poll>
                    <tcps>
                        <tcp>
                            <host>localhost</host>
                            <port>5432</port>
                        </tcp>
                    </tcps>
                </configuration>
            </plugin>
        </plugins> 
 </build>
 
 ```