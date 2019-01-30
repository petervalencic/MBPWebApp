# MBPWebApp
Program za prikaz meritev iz Arduino merilne enote.

Program prevedite z Javo 1.7 ali novejšo. Za to uporabite ukaz`mvn clean package`.
Naveden ukaz bo zgeneriral `.war` paket, katerega je potrebno namestiti na
Tomcat 7 ali novejši aplikacijski strežnik.

Za pravilno delovanje aplikacije je potrebno v Tomcatovem `context.xml` definirati
JNDI povezavo do baze ter URL povezavo do Arduino merilca podatkov. Za to je potrebno
v `context.xml` dodati konfiguracijo podobno:
```
<Environment
      name="arduinoDataUrl" value="http://arduino.ip.address.lan"
      type="java.lang.String" override="false"
      />

<Resource
      auth="Container"
      description="MBP akvarij"
      ame="jdbc/AKVARIJ"
      type="javax.sql.DataSource"
      username="#-- UPORABNIK --#"
      password="#-- GESLO --#"
      driverClassName="com.mysql.jdbc.Driver" 
      url="jdbc:mysql://db.host.example.com/mbp_akvarij"
      maxActive="15"
      maxIdle="3"
      validationInterval="30000"
      validationQuery="SELECT 1"
/>
``` 
