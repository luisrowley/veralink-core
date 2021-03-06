# Veralink Core
Veralink-core is a REST API for signing and verifying payloads in order to prove they haven't been tampered with.
It uses cryptographic signatures and a user verification system to perform checks. It also supports persistence over a MySQL database. 

## Setting up the dependencies

1) Clone this repo:
```bash
git clone https://github.com/luisrowley/veralink-core
```
2) Create resources path and file for Spring configuration:
```bash
mkdir -p veralink-core/src/main/resources
vi veralink-core/src/main/resources/application.properties
```
3) Copy the following content to **application.properties** (replace **< user >** and **< pass >** with desired):
```text
spring.datasource.url=jdbc:mysql://localhost:3306/veralink?useSSL=false
spring.datasource.username=<USER>
spring.datasource.password=<PASS>
spring.jpa.properties.hibernate.dialect= org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto=update
spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
server.ssl.key-store-type=PKCS12
```
4) Navigate to the root project folder:
```bash
cd veralink-core
```
5) Copy the following configuration to a file called **.env**. 
Replace variables with desired ones and please note that **user** and **pass** must be the same to those from the previous step:
```text
MYSQLDB_USER=<USER>
MYSQLDB_USER_PASSWORD=<PASS>
MYSQLDB_DATABASE=veralink
MYSQLDB_LOCAL_PORT=3308
MYSQLDB_DOCKER_PORT=3306
SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8080
JWT_TOKEN_KEY=<RANDOM-32-BIT-KEY>
KEYSTORE_PASS=<RANDOM-SECURE-PASS>
KEYSTORE_ALIAS="eckey"
KEYSTORE_PATH="/root/cacerts/keystore.jks"
KEYSTORE_TYPE="PKCS12"
PROD="false"
META_TITLE="Veralink REST API"
META_DESCRIPTION="Endpoint specification with controllers for user creation, as well as signature and verification of payloads."
META_URL_ROOT="https://veralink.me"
META_CONTACT_EMAIL="apis@veralink.me"
META_LICENCE_URL="https://github.com/git/git-scm.com/blob/main/MIT-LICENSE.txt"

```

## Running with Docker

### 1) Setup **Docker** and **docker-compose plugin** in your system:
- Install the dependencies as explained in the [official site](https://docs.docker.com/engine/install/ubuntu/)
- Then you can just run:
```bash
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```
- Checking installation:
```bash
sudo docker run hello-world
```

### 2) Build the entire project using docker-compose:
```bash
sudo docker-compose up --build
```

### 3) Once the MySQL server image is running, you can create the user and DB with:

- Get into the Docker image shell:
```bash
sudo docker exec -it <container-id> bash
```

- Then login into the MySQL console:
```bash
mysql -u root -p
```
- Then create the **database** and **user**
```mysql
CREATE DATABASE veralink;
CREATE USER 'username'@'%' identified by 'password';
grant all on veralink.* to 'username'@'%';
```

### 4) Re-run image without building it:
- Kill the Docker instance with **Ctrl+C**, and then run:
```bash
sudo docker-compose up
```

## Running with only Maven

Before running these are the required packages:
### 1) Java JDK (version >= 11) and JRE. This is needed for development purposes.
- Check if your package manager contains this version by running:
```bash
  sudo apt update
  apt search openjdk
```
- Then installing by:
```bash
  sudo apt install openjdk-11-jre
```
- Setting JAVA_HOME (Environment Variable) with:
```bash
  export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
```
- Edit your PATH environment variable with JAVA_HOME:
```bash
  export PATH=$PATH:$JAVA_HOME/bin
```
- Checking installation:
```bash
  java -version
```
- For more instructions you can follow [the docs](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-linux-platforms.html#GUID-79FBE4A9-4254-461E-8EA7-A02D7979A161)

### 2) Install MySQL server if none available on the system
```bash
wget https://dev.mysql.com/get/mysql-apt-config_0.8.22-1_all.deb
sudo dpkg -i mysql-apt-config*
sudo apt update
sudo apt install mysql-server
sudo systemctl status mysql # check OK
mysql_secure_installation # optional
```

### 3) Create the DB and user

- login into the MySQL console:
```bash
mysql -u root -p
```
- Then create the **database** and **user**
```mysql
CREATE DATABASE veralink;
CREATE USER 'username'@'%' identified by 'password';
grant all on veralink.* to 'username'@'%';
```

### 4) Run the project with Maven:
```bash
mvn spring-boot:run
```

## Interacting with the API

- The first request must be either authentication via **login** or **creating** a new user account if none exists:
This is done via **POST** request to the endpoint **/api/user/signin** and **/api/user/create** respectively:

![User login request](img/REST-singin-user.png)

AND

![User login request](img/REST-create-user.png)

- Subsequent requests must include the returned **Bearer Token** as an **Authorization** HTTP header in order to be authorized:

![User login request](img/REST-sign-headers.png)

- Now we could send a request of payload signature to the **/api/signature/sign** endpoint and a JSON formatted payload like so:

![User login request](img/REST-sign.png)

- And verify the result of the encoded payload like this:

![User login request](img/REST-verify.png)

## Understanding the Results

- A valid payload with its corresponding EC signature gets verified OK if its data hasn't been tampered with. We may see this as the following **/verify** response: 

![User login request](img/REST-correct-sign.png)

- An invalid payload signature gets a **isVerified: false** on the response body:

![User login request](img/REST-incorrect-sign.png)

## Swagger specification

To have a more comprehensible list of HTTP request and their possible payloads, please visit the project's [Swagger page](http://localhost:8080/swagger-ui.html) at.

### Authors
- LeWiSs

