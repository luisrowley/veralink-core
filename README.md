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
```bash
python3 kairos.py [-h] [-w WORDLIST] [-u URL] [-n ROUNDS] [-X HTTP_METHOD] [-d FIELD_DATA]
```

## How does it work?
This assessment tool relies on a structural flaw on the design of a login system in which the existence of a user is checked beforehand. In a vulnerable web, once a first call to check if a user exists is made to the backend, only then the system checks if the password matches and hashes the plain-text password submited by the user:

```php
<?php

// get user from DB based in POST param
$user = $db->query('SELECT * FROM users WHERE username="'.$_POST['userid'].'"');

// check user exists
if ($user) {
    ...
  // calculate password hash from POST
  $pass = hash_password($_POST['passwd']);
  // get user data
  $db_password = mysqli_fetch_row($user)['db_password'];
  
  // check if received password matches with one stored in the database
  if ($pass === $db_password) {
      // user login is valid
	...
  } else {
    // error: wrong password
    ...
  }
} else {
// error: user doesn't exist
...
}  
```

Therefore the **extra time** it takes for the backend system to compute the password hash allows us to determine if a user exists or not in the system independently of any log messages.

## Requirements
- [Python3](https://www.python.org/downloads/) (version > 3.6 or more)
- [pip](https://pypi.org/project/pip/) to the latest version

## See it in action

### Launch sample Request
```bash
python3 kairos.py -w samplelists/usernames.txt -u http://mysite.com/login
```

This repository includes a short wordlist of usernames for testing purposes (under *samplelists/* folder). For a more in-depth analysis please consider providing a more complete list of your choice as long as it follows the same format.

URL targets should be pages with standard login forms. Kairos will try to guess which **user** and **password** parameters are correct for as form input names and then it will craft the request based on these. If a correct **user/pass** combination is found, then the results are cached for that given URL domain and reused next time.

Kairos sends the request for a given **user/pass** multiple times (n=10 by default) and then calculates the [median](https://en.wikipedia.org/wiki/Median) of those values to avoid network connection issues generating ***outliars***. The value of rounds ***n*** can be set higher to gain more robust statistics on the results:

```bash
python3 kairos.py -w samplelists/usernames.txt -u http://mysite.com/login -n 50
```

Please consider that incrementing the factor ***n*** will be very noisy on the target network, so use with precaution.

Optionally you can also manually indicate the name and password input fields for the request like this:

```bash
python3 kairos.py -w samplelists/usernames.txt -u http://mysite.com/login -n 50 -d userfield,passfield
```

Remember you can find these by intercepting the request with **Burp** or **Chrome Devtools -> Network tab** for instance.

### Understanding the results

After sucessful page requests have been made, a small report is shown as the output:

![Kairos simple test](img/kairos_test_simple.png)

The data is divided in three columns, with number of requests made (rounds) and average time for these. At the bottom we can find additional values such as the **Maximum time difference** between the longest and shortest time spans and the **Median** of all requests.

We can optionally get a detailed report in which the most relevant values are highlighted in different colors:

![Kairos detail test](img/kairos_test_detail.png)

In the detailed report we calculate how each **average time value** deviates from the **aggregate median**, giving us a **Deviation factor**.

In this example we can see how the **admin** user takes the longest time, highlighted as a found user.
## Caveats

As with many other tools, there are challenges to overcome. One of them could be that the web application has in place some tipe of Web-based firewall or [WAF](https://www.cloudflare.com/learning/ddos/glossary/web-application-firewall-waf/) limiting the number of requests for automated scripts.

There is also the possibility of a web app system that is well-designed in a way that is not vulnarable to this attack, for instance calculating the password hash also beforehand, thus fixing response time to worst case execution time (this would have a very negative performance impact on the application).

## Disclaimer

For educational purposes only. The author(s) are not responsible for any misuse or damage caused by this tool to any user(s) or third parties.