# Veralink Core
Veralink-core is a REST API for signing and verifying payloads in order to prove they haven't been tampered with.
It uses cryptographic signatures and a user verification system to perform checks. It also supports persistence over a MySQL database. 

## Dependencies

For installation of the required packages, these are the steps:
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

### 3) Setup **Docker** and **docker-compose plugin** in your system:
- Install the dependencies as explained in the [official site](https://docs.docker.com/engine/install/ubuntu/)
- Then you can just run:
```bash
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```
- Checking installation:
```bash
sudo docker run hello-world
- ```

## Installation
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
KEYSTORE_PASS="9N0Lp4-gh9lPIvlJx_k2wIRM30FbKc_Shf7cG6g3z_9PhN9EUrOOmGVg-kwMhfVONEGgpD_Yvi5HLSUky-kytw"
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
6) Build the entire project using docker-compose:
```bash
sudo docker-compose up --build
```

## Usage

## See it in action

### Understanding the results

