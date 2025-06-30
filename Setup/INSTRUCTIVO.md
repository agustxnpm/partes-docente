## Instalación y configuración del entorno de desarrollo

> ESTE PROYECTO ESTÁ PREPARADO Y PROBADO PARA SER DESARROLLADO SOBRE SISTEMAS **LINUX**
>


> En la raíz de este directorio existe el script ´lpl´ para facilitar la ejecución de varios comandos. En el presente instructivo se indicará en cada paso, si corresponde, la opción de ejecución mediante este script. 

## Setup

### Software necesario previamente

1. Instalar [Git](https://git-scm.com/download/linux)

1. Instalar [Docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/) y [Docker Compose](https://docs.docker.com/compose/install/)
    > **¡CONFIGURACIÓN IMPORTANTE ANTES DE CONTINUAR!**
    >
    1. No olvidar los pasos de post instalación para ejecutar docker sin priviliegios de `root`.
        ```sh
        sudo groupadd docker
        sudo usermod -aG docker $USER
        ```
        Para hacer efectivos los cambios en los grupos, reiniciar la terminal o ejecutar
        ```sh
        newgrp docker
        ```
    1. *Opcional:* Para que docker no arranque de forma automática al inicio:
        ```sh
        sudo systemctl disable docker.service
        sudo systemctl disable containerd.service
        ```
    1. Crear el archivo `/etc/docker/daemon.json` con el siguiente conenido:
        ```json
        {
          "userns-remap": "TU_NOMBRE_DE_USUARIO"
        }
        ```
    1. Editar los archivos `/etc/subuid` y `/etc/subgid`. Agregar la línea:
        ```
        TU_NOMBRE_DE_USUARIO:1000:65536
        ```

1. Iniciar servicio docker `sudo systemctl start docker`
    > Este comando puede variar según la distro de linux utilizada.


### Obtener el código para trabajar

1. Clonar el repositorio.

1. Ir al directorio clonado `cd <repo_dir>`

1. Dar permisos de ejecución al script `lpl`: `chmod +x lpl`.

1. Hacer el build de las imágenes Docker `./lpl build` 

1. Levantar los servidores `./lpl up`
      > Este paso toma un tiempo debido a que debe descargar las dependencias del proyecto. Para monitorear el progreso utilizar `./lpl logs`.
      >
      > Cuando la aplicación esté lista se verá el mensaje:
      >
      > `backend | [...] Started BackendApplication in xxx seconds`

1. Verificar funcionamiento ingresando a http://localhost:8080/ . Si todo funciona correctamente debería responder el siguiente JSON:
      ```json
      {
      "data": "Hello Labprog!",
      "message": "Server Online",
      "status": 200
      }
      ```

## Desarrollar con Docker

Para los siguientes pasos asegurarse de que el servicio de Docker esté corriendo, se puede ejecutar el comando `docker ps`.

El script `lpl` en la raíz del repositorio tiene una serie de comandos útiles abreviados para asistir en el proceso de desarrollo.

### Conectarse a los servidores por línea de comandos

Para conectarse al servidor **backend**, una vez corriendo los servicios, ejecutar: ```./lpl sh backend-pd```

De la misma forma es posible conectarse a cualquiera de los contenedores solo indicando el nombre del mismo.

### Detener los servicios

Para detener los servicios configurados en el archivo de docker-compose ejecutar: ```./lpl down```

El siguiente comando es para detener por completo el servicio de docker. En este caso, si los servicios están corriendo se detendrán y cuando docker sea iniciado nuevamente, estos contenedores serán levantados de forma automática.

`sudo systemctl stop docker`

## Desarrollar en Java en el backend

El servidor de backend despliga automáticamente el código compilado. Luego de modificar los archivos locales se debe ejecutar el siguiente comando:

1. `./lpl compile`

Esto compilará el código en el servidor. Si no hay errores de compilación se desplegará al instante.

En ciertas ocaciones, debido a algún error de compilación que haya sido corregido, es posible que el backend no vuelva a desplegar la aplicación. En este caso, sólo es necesario reiniciar el backend.

1. `./lpl restart backend`




