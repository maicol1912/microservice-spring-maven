# Configuration Microservices
*************************************************************
### Crear maven Project / XML por cada microservicio
* para cada microservicio debemos crear un proyecto maven
* file -> new -> module 
* debemos elegir el JDK, el groupId debe ser el mismo que el del padre de los microservicios

### como funciona los pom.xml
* debemos colocar cual es el padre de este microservicio que en este caso es el proyecto principal / pom.xml principal
* podemos copiar el parent del proyecto principal, las dependencias y lo demas necesario

### compartir dependencias
* todo lo que definamos en el pom.xml principal en el proyecto principal
* puede ser usado en todos los demas microservicios, son dependencias compartidas


### configurar microservicios como modulos
* debemos crear un modulo de maven por cada microservicio 
  * en el pom.xml del proyecto general apareceran estos modulos de los microservicios
       
            <modules>
               <module>product-service</module>
               <module>order-service</module>
                <module>inventory-service</module>
            </modules>


### Verificar el proyecto con maven
* podemos correr un comando el cual nos sirve para ver si todo esta funcionando de buena manera
* nos vamos a la consola de maven y escribimos

              maven clean verified


# Service discovery - Centralizar servicios
*************************************************************
### Funcion
* cuando estamos trabajando de manera local trabajamos con localhost y sabemos en que puerto estan corriendo
* cuando hacemos el despliegue las ips pueden ser dinamicas y cambiar
* por lo tanto si quemamos la url esta puede cambiar y dañar nuestro servicio
* o tambien podemos tener varias instancias de un microservicio, por lo tanto podemos enviar diferentes peticiones
* a los diferntes instancias para hacer un load_balancer

### Como funciona
* los microservicios se registran en el service discovery y este maneja y puede categorizar cada api y sus instanciass
* el service discovery trata de enviar las peticiones a los diferentes intsnacias, si todas no funcionan devuelve que el microservicio no esta disponible

### Como se implementa
* se crea un modulo de maven que se adjunta en el pom principal
* alli en el pom del service se define que el principal es el parent

### Configurar Eureka server
* debemos configurar la clase principal del proyecto con el EnableEurekaServer
* para configurar eureka se hace desde el application properties

### Configurar Eureka client
* para configurar eureka se hace desde el application properties
* si solo nos conectamos con url los servicios se registran pero como uknown

### Crear multiples instancias de EurekaClient
* para crear una multiple instancia de un servicio
* debemos colocarlo en el puerto 0, para que este tome un puerto libre
* debemos activar el pararel run en intellij para poder crear varias instancias

### Acceder a la url de los microservicios
* ahora se accede por medio del nombre con el cual se registro en eureka server
              
                 http://inventory-service/api/inventory

### Como es el funcionamiento de discovery service al caerse
* cuando un microservicio ya esta registrado en discovery service y discovery service se detiene
* los microservicios tienen la capacidad de seguir guardando la cache de los demas
* entonces ya sabe donde redirigir la peticion

* cuando un microservicio nuevo se va a registrar y el eureka service esta detenido
* entonces este no puede registrarse, el microservicio se detiene



# Spring cloud gateway - api gateway
*************************************************************
* api gateway es un servicio que nos permite redireccionar las peticiones del cliente
* hacia los diferentes microservicios dependiendo de su endpoint

* tambien se puede realizar la authenticacion de peticiones
* ademas se puede hacer load balancer con este 

* este tambien debe de tener la dependencia de eureka client

### Como funciona
* en el archivo de properties definimos el id de la ruta, con el nombre de cada microservicio
* despues definimos la url donde va a ir con el lb para hacer load balancer
* y despues las rutas que se redireccionaran a dicho servicio en caso de que llegue un path especifico

                spring.cloud.gateway.routes[1].id=order-service
                spring.cloud.gateway.routes[1].uri=lb://order-service
                spring.cloud.gateway.routes[1].predicates[0]=Path=/api/order

### Redireccion de peticiones
* al enviar un path especifico, este nos envia al nombre del servicio registrado en eureka-service
* y envia la peticion completa, con su url, con su metodo, y su cuerpo
* si probamos podemos consumir la api desde el api gateway o desde el mismo microservicio


# Seguridad de la aplicacion con KeyCloak
***********************************************
### KeyCloak
* keycloak es una dependencia o proyecto que nos ayuda a mantener la seguridad de nuestros microservicios

### Instalar Keycloak
* para instalar keycloak lo podemos hacer por medio de docker corriendo este comando
  

                docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:18.0.0 start-dev

### Configurar Keycloak
* despues de instalarlo debemos ir al localhost y el puerto en el que esta corriendo 
* colocar las credenciales que definimos al correr el contenedor de docker
* debemos crear otro realme 
* despues debemos crear un cliente, debemos agregar la propiedad client-id y de tipo openid-connect
* en la siguiente pantalla debemos modificar el AccessType por confidential ,debemos desactivar el direct access grant,debemos desactivar el Standard Flow Enabled, debemos activar el service accounts enabled

* despues de crear el client, podemos ver en credencial la credencial secret
* debemos ir al realm settings y en endpoints debemos abrir OpenID Endpoint Configuration
* debemos copiar el objeto de issuer que es un endpoint

#### la url que copiamos del issuer la usaremos en nuestro proyecto despues

### Dependencias en Api gateway
* en api gateway debemos descargar las siguientes dependencias

                <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
                </dependency>

                <!--dependencia para usar Oauth con keycloak-->
                <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-security</artifactId>
                </dependency>
* en el aplication de nuestra api gateway debemos definir una propiedad con el valor de la variable issuer


                spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8181/realms/spring-boot-microservices-realm

* despues debemos crear una clase de securityConfig en el api gateway para saber como se comporta y como debe de manejar la authenticacion en la app
* ahora al definir la autenticacfion al tratar de acceder a los endpoints por medio de api-gateway nos dira Unauthorized

### Configurar Token OAuth 2.0
* en postman debemos ir a Authorization, debemos buscar OAuth 2.0
                
                Token Name: token
                Grant Type: Client Credentials
                Access Token URL: en el archivo de donde tomamos el issuer, en keycloak tomamos el valor del objeto "token_endpoint"
                Client ID: el client-id que definimos en keycloak al crear el cliente
                Client Secret: el secret de keycloak que se ve en el client y las credenciales
                Scope: openid offline_access
                Client Authentication: Send as basic auth header

* al colocar todos los datos correctamente le damos en get token
* este nos devuelve un token 

* despues de tener el access token 
* en la parte superior de OAuth 2.0 debemos llenar asi los valores

                 Token: nombre del token creado, se agrega automaticamente
                 despues debemos pegar el token generado
                 Header Prefix: Bearer 

#### al enviar la peticion con este Authorization y este token, tendremos acceso a el Api-gateway
#### el servicio de keycloak puede estar caido para hacer el login con el token, pero debe estar corriendo para la creacion de clientes


## Configurar discovery Service con la authenticacion de api-gateway
* en el discovery service, debemos definir una clase de SecurityConfig donde se configurara una authenticacion por medio de user y password
* debemos instalar la siguiente depedencia en el discovery service

                  <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-security</artifactId>
                  </dependency>
* como eureka service necesita authenticacion, los microservicios no se pueden conectar eureka server
* al definir el archivo de SecurityConfig en el eureka server, nos despliega una pantalla con usuario y contraseña

### Conectarse a eurekaServer desde microservicios con authenticacion
* en el aplication properties debemos cambiar la forma de conectarnos al eureka ahora se debe hacer de la siguiente manera
* por medio de la definicion de un usuario:password

                  eureka.client.serviceUrl.defaultZone=http://eureka:password@localhost:8761/eureka


# Circuit Braker
***********************************************

### Que es?
* al tener microservicios, y al hacer llamadas entre ellos, pueden suceder problemas
* como que el microservicio no esta disponible, por lo tanto esto afectara el funcionamiento de nuestra app
* lo que se busca con circuit braker es cuando falla un microservicio, inmediatamente responder con un mensaje de caida de este
* ademas si un microservicio no esta funcionando, para las llamadas ya que no tiene sentido seguir llamando ese microservicio
* ademas podemos usar el cache de los otros microservicios, mientras estos no esten funcionando 
* para esto sera usado RESILIENCE4J

### Estados de circuit Bracker
En el estado cerrado:  el circuit breaker permite el flujo normal de tráfico, permitiendo que las solicitudes pasen a través de él sin interrupciones. Si alguna de estas solicitudes falla, el circuit breaker registra el error. Si la tasa de error de las solicitudes excede un umbral predefinido, el circuit breaker pasa al estado abierto.
En el estado abierto, el circuit breaker deja de permitir que las solicitudes pasen a través de él y comienza a responder inmediatamente con una respuesta de error. Durante un período de tiempo predefinido, el circuit breaker mantiene esta respuesta de error y no permite el flujo de tráfico a través de él. Una vez que se alcanza el final del período de tiempo, el circuit breaker pasa al estado semiabierto.
En el estado semiabierto, el circuit breaker permite un flujo limitado de tráfico a través de él. Durante este estado, el circuit breaker monitorea la tasa de error de las solicitudes que pasan a través de él. Si la tasa de error sigue siendo alta, el circuit breaker vuelve al estado abierto. Si la tasa de error baja por debajo del umbral predefinido, el circuit breaker vuelve al estado cerrado y permite el flujo normal de tráfico a través de él.

en el archivo de properties definimos todas estas caracteristicaz
## Ver estado de un microservicio
* podemos acceder al estado de un microservicio, para saber si esta corriendo o no
* se debe colocar la ip y el puerto donde esta corriendo y el /actuator/health
* este debe tener implementado el circuit braker

                  http://localhost:8081/actuator/health


### Conclusion
* hay una anotacion para el control de errores que es @CircuitBraker
* hay una anotacion para definir un tiempo limite en que una peticion puede tardar en responde @TimeLimitter
* hay una anotacion para definir el numero de intentos antes de que la app lanze un error



# Distributed Tracing
***********************************************

Distributed tracing en microservicios de Spring se refiere a la capacidad de rastrear y analizar el flujo de solicitudes que se producen entre los diferentes componentes de un sistema de microservicios, que se ejecutan en distintos nodos o servidores.

* en aplicaciones en dev es facil ver los logs pero en aplicaciones en produccion con muchas solicitued y logs se vuelve dificil
* si algun request falla en algun punto o parte del proceso sabremos que esta mal y donde esta mal 

### Dependencias necesarias
* debemos usar 2 dependencias una es sleuth para el siguimietndo de erroes y la otra zikpin para una interfaz grafica de sleuth

                    <!--esta dependencia es para trabajr con el distributed tracing para ver donde fallaron las app-->
                    <dependency>
                      <groupId>org.springframework.cloud</groupId>
                      <artifactId>spring-cloud-starter-sleuth</artifactId>
                    </dependency>

                    <!--depedencia para tabajr con una interfz grafica de sleuth-->
                    <dependency>
                      <groupId>org.springframework.cloud</groupId>
                      <artifactId>spring-cloud-sleuth-zipkin</artifactId>
                    </dependency>

### Inslatar zipkin
* se necesita un contenedor de docker corriendo lo cual lo hacemos con el comando 

                    docker run -d -p 9411:9411 openzipkin/zipkin

### Configuracion sleuth
* debemos configurar el aplication.properties 

                    #Cofiguracion sleuth y zipkin
                    spring.zipkin.base-url=http://localhost:94911
                    #hacemos el 100% de las peticiones hagan trace
                    spring.sleuth.sampler.probability= 1.0

### Con esta configuracion los logs de las aplicaciones tienen, el nombre del servicio, pero seguidos de IDs unicos para poder identificar dichas peticiones


## Panel de control de zipkin
* para esto debemos entrar al localhost y al puerto donde se esta corriendo 
                    
                    http://localhost:9411/zipkin/
* aca debemos darle a run query
* en el panel de control,se pueden ver las ejeuciones de request que fallaron
* al entrar alli muestra que servicios estuviron implicitos
* al entrar a la peticion, spans es el numero de intentos 
* y aca nos muestra informacion de la peticion, status, mensaje error


* tambien podemosa acceder a las peticiones buenas
* debemos modificar el limit de la consulta
* las verdes son las que se realizaron bien, y las rojas las que tuvieron error 

### Flujo 
* este log de zipkin nos muestra el flujo que cumplio la peticion, como por ejemplo el flujo que paso desde api-gateway al order service y despues al product service 

* este nos muestra un spanId y por medio de este spanId podemos buscarlo en el log y ver mas detalle de este error

* tambein podemos definir un span en nuestros servicios, con un nombre definido
* para que en los logs salga este, y podamos diferenciarlo

# Event driven with Kafka
***********************************************

* para el uso de kafka vamos a usar kafka confluent
* https://developer.confluent.io/quickstart/kafka-docker/

* debemos copiarnos el archivo .yml y crearlo al inicio de nuestra carpeta  

## Dependencias
* tener kafka corriendo como contenedor

                      <dependency>
                        <groupId>org.springframework.kafka</groupId>
                        <artifactId>spring-kafka</artifactId>
                        <version>2.9.0</version>
                      </dependency>
* despues debemos agregar en el propertie hacia que direccion ip y puerto esta corriendo kafka
* en caso de que sea un producer debemos configurar las caracteristicaz para producer

                      #Kafka Properties
                      spring.kafka.bootstrap-servers=localhost:9092

                      #este es para que kafka sepa como debemos de serializar la key y enviarla a kafka broker en este caso este value sera un String
                      spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer

                      #este es para que kafka sepa como debemos de serializar el value y enviarla a kafka broker en este caso este value sera un JSON
                      spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

* si es un consumer debemos crear las caracteristicaz para un consumer
* aca iria consumer y deserializer

                      spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
                      spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer

* para poder Usar las entidades que llegan o se envian en los kafka cluster
* debemos definir el mdelo de mapping tanto en el consumer como el producer
                      
                      # aca definimos un tipo de mapping en este caso el modelo de Order placed que llega que seria la ubicacion del modelo OrderPlacedEvent, esto se debe hacer en el consumer como en el producer
                      spring.kafka.producer.properties.spring.json.type.mapping=event:com.maicol1912.orderservice.event.OrderPlacedEvent