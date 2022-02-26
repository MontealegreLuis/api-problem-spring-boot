# Maven

_This guide assumes you already generated an access token as described in [this guide](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/docs/installation/authentication.md)._

You can authenticate to GitHub Packages with Apache Maven by editing your `~/.m2/settings.xml` configuration file.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo1.maven.org/maven2</url>
        </repository>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/montealegreluis/api-problem-spring-boot</url>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    <server>
      <id>github</id>
      <username>USERNAME</username>
      <password>TOKEN</password>
    </server>
  </servers>
</settings>
```

Replace `USERNAME` and `TOKEN` accordingly.

Add the following entry to your `dependencies` in your project's `pom.xml` file.

```xml
<dependency>
  <groupId>com.montealegreluis</groupId>
  <artifactId>api-problem-spring-boot</artifactId>
  <version>1.0.0</version>
</dependency>
```

Please find what the latest version is [here](https://github.com/MontealegreLuis/api-problem-spring-boot/packages/1275243).
