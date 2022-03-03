plugins {
  java
  id("org.springframework.boot") version "2.6.2"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("java-library") // QueryDSL must have
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

group = "be.nazarimilad"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
  maven(url = "https://repo.spring.io/milestone") // Spring milestones
  maven(url = "https://repo.spring.io/snapshot") // Spring milestones
}

dependencies {
  // Spring Boot
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.3")
  implementation("org.springframework.boot:spring-boot-starter-validation:2.6.3")

  // GraphQL
  implementation("org.springframework.experimental:graphql-spring-boot-starter:1.0.0-M4")
  implementation("com.graphql-java:graphql-java-extended-scalars:17.0") // extended scalars (Long, Datetime, etc) (https://github.com/graphql-java/graphql-java-extended-scalars)

  // Hibernate
  implementation("org.hibernate:hibernate-core:5.6.5.Final")
  annotationProcessor("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")
  annotationProcessor("javax.annotation:javax.annotation-api:1.3.2")
  annotationProcessor("org.hibernate:hibernate-jpamodelgen:5.6.5.Final")

  // QueryDSL
  annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jpa")        // annotation processor for the entities
  annotationProcessor("com.querydsl:querydsl-apt:5.0.0:general")    // annotation processor for the DTO's
  implementation("com.querydsl:querydsl-core:5.0.0")
  implementation("com.querydsl:querydsl-jpa:5.0.0")

  // database
  runtimeOnly("com.h2database:h2")

  // application properties
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.6.3")
}