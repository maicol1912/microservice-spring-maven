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