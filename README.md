# iTest (Perfil Alumno)

#### Descripcción
Diseño, refactorización e implementación de la aplicación web iTest para el perfil del alumno

Este proyecto se encuentra en fase de desarrollo.

#### Configuración

Antes de hacer el despliegue de una versión se deben cambiar los siguientes parámetros:

- Conexión a base de datos ([application.yml](https://github.com/jmcolmenar/iTest/blob/master/src/main/resources/application.yml "application.yml"))

```
# Datasource properties
url:
username:
data-username:
password:
data-password:
```

- Credenciales cuenta de correo ([application.yml](https://github.com/jmcolmenar/iTest/blob/master/src/main/resources/application.yml "application.yml"))

```
# Mail properties
host:
port:
username:
password:
```

- URL de la aplicación ([AppUrlConstant.java](https://github.com/jmcolmenar/iTest/blob/master/src/main/java/com/itest/constant/AppUrlConstant.java "AppUrlConstant.java"))

```java
// The url of the application
public static final String APP_URL = 
```
