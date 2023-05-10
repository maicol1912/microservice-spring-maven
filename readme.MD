## Configuration Microservices
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


## Service discovery - Centralizar servicios
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



## Spring cloud gateway - api gateway
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