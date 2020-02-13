

Based on https://dzone.com/articles/building-a-web-app-using-spring-boot-angular-6-and

Download maven: http://maven.apache.org/download.cgi ([zip](http://mirror.serverion.com/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.zip)) and unzip

```powershell
mkdir angular-spring
cd angular-spring
Invoke-WebRequest http://mirror.serverion.com/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.zip -OutFile apache-maven-3.6.3-bin.zip
Expand-Archive -Path .\apache-maven-3.6.3-bin.zip -DestinationPath .
$env:Path += ";$pwd\apache-maven-3.6.3\bin"
mvn --version
```





```bash
npm install -g @angular/cli
mkdir -p tutorial-server
Out-File -FilePath tutorial-server/pom.xml -InputObject @"
<plugin>
    <artifactId>maven-resources-plugin</artifactId>
    <executions>
        <execution>
            <id>copy-resources</id>
            <phase>validate</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}/classes/resources/</outputDirectory>
                <resources>
                    <resource>
                        <directory>${project.parent.basedir}/tutorial-web/src/main/web/dist/np-app/</directory>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
"@


mkdir -p tutorial-web/src/main

Out-File -FilePath tutorial-web/pom.xml -InputObject @"
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.3</version>
    <configuration>
        <nodeVersion>v8.11.3</nodeVersion>
        <npmVersion>6.3.0</npmVersion>
        <workingDirectory>src/main/web/</workingDirectory>
    </configuration>
    <executions>
        <execution>
            <id>install node and npm</id>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
        </execution>
        <execution>
            <id>npm install</id>
            <goals>
                <goal>npm</goal>
            </goals>
        </execution>
        <execution>
            <id>npm run build</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
        </execution>
        <execution>
            <id>prod</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>run-script build</arguments>
            </configuration>
            <phase>generate-resources</phase>
        </execution>
    </executions>
</plugin>
"@

cd tutorial-web/src/main
# Generate Angular app
# output directory is `web`
# application name is `np-app`
ng new --skip-git --directory web --style sass --routing false np-app
cd ..\..

```



## Create Skeleton Spring Boot App

https://start.spring.io/

See also: https://spring.io/guides/gs/spring-boot/

```
Project: Maven Project
Language: Java
Spring Boot: 2.2.4
Project Metadata:
- Group: com.swathisprasad
- Artifact: tutorial-server
- Options:
  - Name: tutorial-name
  - Description: Demo project for Spring Boot
  - Package Name: com.swathisprasad.springboot
  - Packaging: War
  - Java: 8
```



Create new class:

```java
package com.swathisprasad.tutorial;

import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


@Controller
public class HomeController {
       @GetMapping("/home")
       public String home(Model model) {
            return "forward:/index.html";
       }

       @GetMapping("/")
       public String index() {
            return "forward:/home";
       }
   
}
```



Here is the complete `tutorial-server/pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.2.4.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.swathisprasad</groupId>
	<artifactId>tutorial</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>war</packaging>
	<name>tutorial</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
    
        <dependency>
            <groupId>com.swathisprasad.tutorial</groupId>
            <artifactId>tutorial-web</artifactId>
            <version>${project.version}</version>
            <scope>runtime</scope>
        </dependency>
 	</dependencies>

	<build>
      <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <configuration>
                    <packagingExcludes>WEB-INF/lib/tomcat-*.jar</packagingExcludes>
                    <warName>tutorial-app</warName>
                </configuration>
            </plugin>
            <plugin>
                <artifactId>maven-resources-plugin</artifactId>
                <executions>
                    <execution>
                        <id>copy-resources</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>${basedir}/target/classes/resources/</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>/../tutorial-web/src/main/web/dist/np-app/</directory>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
	</build>

</project>
```



## Build and Run

```powershell
mvn clean install
mvn spring-boot:run
```



```powershell
$env:JAVA_HOME="C:\Program Files (x86)\Java\jdk1.8.0_231"
```



## Publish to Azure

### Configure Maven Plugin

```bash
mvn com.microsoft.azure:azure-webapp-maven-plugin:1.8.0:config
```



For more info on the plugin, see:

* https://docs.microsoft.com/en-us/java/api/overview/azure/maven/azure-webapp-maven-plugin/readme?view=azure-java-stable
* [Configure Maven Plugin](https://docs.microsoft.com/en-us/azure/app-service/app-service-web-get-started-java#configure-the-maven-plugin)



### Publish War

```powershell
mvn package azure-webapp:deploy
# 
```



The war file can also be deployed using Powershell:

```powershell
Publish-AzWebapp -ResourceGroupName learn-web-apps -Name vinod-hhw -ArchivePath $pwd\target\tutorial-0.0.1-SNAPSHOT.war
```







* https://spring.io/guides/gs/spring-boot/
* https://dzone.com/articles/building-a-web-app-using-spring-boot-angular-6-and
* https://www.devglan.com/spring-cloud/spring-cloud-gateway-static-content
* https://github.com/jdubois/spring-on-azure
* https://adamtheautomator.com/azure-pipelines-tutorial/