jboss-javaee5-mi-jndi
========================

What is it?
-----------

JBoss EAP5 のインスタンスを複数起動し、ネーミングサービスが個別に動いていることを確認します。

同じ jndi-name を利用して違うデータベースへのアクセスをしていることが確認できたら成功です。

System requirements
--------------------------------

+ Maven 3.0 or better
+ JBoss EAP5
+ JDK6
+ RDBMS(e.g. PostgreSQL)

Usage
---------------

設定セットとして node1, node2 を用意し、  

+ node1: デフォルトのポート
+ node2: デフォルトから 100 ずらしたポート

を利用することとします。

また、node1, node2 それぞれの DB を作成しておいてください。  
ここでは、 ローカルに立てた PostgreSQL 9 を利用し、DB名をそれぞれ node1, node2 としています。  
それぞれの DB には、serial 型の id カラム、 text 型の value カラムを持つ test テーブルを作成し、  
別々の内容を格納しておきます※。

また、$PROJECT は、 本プロジェクト(jboss-javaee5-mi-jndi) のインストールディレクトリを、  
$JBOSS_HOME は、JBoss EAP5 のインストールディレクトリを指すとします。

※ 初期化スクリプトとして、$PROJECT/src/main/resources/init_node*.sql があります。

``` sh
// APP build
$ cd $PROJECT
$ mvn clean install package

// JBoss set up
$ cd $JBOSS_HOME/server
$ cp -rp default node1
$ cp -rp default node2
$ vim node2/conf/bindingservice.beans/META-INF/bindings-jboss-beans.xml

// change ports-default -> ports-01
before:
<bean name="ServiceBindingManagementObject"
    class="org.jboss.services.binding.managed.ServiceBindingManagementObject">
       
    <constructor>
    <!-- The name of the set of bindings to use for this server -->
        <parameter>${jboss.service.binding.set:ports-default}</parameter>
       
after:
<bean name="ServiceBindingManagementObject"
    class="org.jboss.services.binding.managed.ServiceBindingManagementObject">
       
    <constructor>
    <!-- The name of the set of bindings to use for this server -->
        <parameter>${jboss.service.binding.set:port-01}</parameter>

$ cp $PROJECT/target/jboss-javaee5-mi-jndi-<version>.war node1/deploy/jboss-javaee5-mi-jndi.war
$ cp $PROJECT/target/jboss-javaee5-mi-jndi-<version>.war node2/deploy/jboss-javaee5-mi-jndi.war
$ cp $JBOSS_HOME/docs/examples/jca/postgres-ds.xml node1/deploy
$ vim node1/deploy/postgres-ds.xml

// edit db env
before:
<datasources>
    <local-tx-datasource>
    <jndi-name>PostgresDS</jndi-name>
    <connection-url>jdbc:postgresql://[servername]:[port]/[database name]</connection-url>
    <driver-class>org.postgresql.Driver</driver-class>
    <user-name>x</user-name>
    <password>y</password>

after:
<datasources>
    <local-tx-datasource>
    <jndi-name>jndiDS</jndi-name>
    <connection-url>jdbc:postgresql://localhost:5432/node1</connection-url>
    <driver-class>org.postgresql.Driver</driver-class>
    <user-name>postgres</user-name>
    <password>postgres</password>

$ cp $JBOSS_HOME/docs/examples/jca/postgres-ds.xml node2/deploy
$ vim node2/deploy/postgres-ds.xml

// edit db env
before:
<datasources>
    <local-tx-datasource>
    <jndi-name>PostgresDS</jndi-name>
    <connection-url>jdbc:postgresql://[servername]:[port]/[database name]</connection-url>
    <driver-class>org.postgresql.Driver</driver-class>
    <user-name>x</user-name>
    <password>y</password>

after:
<datasources>
    <local-tx-datasource>
    <jndi-name>jndiDS</jndi-name>
    <connection-url>jdbc:postgresql://localhost:5432/node2</connection-url>
    <driver-class>org.postgresql.Driver</driver-class>
    <user-name>postgres</user-name>
    <password>postgres</password>

// JBoss start
$ cd $JBOSS_HOME/bin
$ ./run.sh -c node1
$ ./run.sh -c node2
```

Access the application 
---------------------
 
The application will be running at the following URL:  

+ node1: <http://localhost:8080/jboss-javaee5-mi-jndi/dbconn>
+ node2: <http://localhost:8180/jboss-javaee5-mi-jndi/dbconn>

それぞれ別のデータベースの内容が表示されれば成功です。