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