# Turnos Médicos — Frontend 🩺📱

Aplicación Android para la gestión de turnos médicos, desarrollada con **Kotlin** y utilizando **Clean Architecture + MVVM**.

Este repositorio contiene exclusivamente el **frontend/mobile** de la aplicación. El funcionamiento completo del sistema requiere el backend desarrollado con **Java y Spring Boot**.

🔗 **Backend:** [Repositorio del Backend](https://github.com/MatiasOrtizF/turnos-medicos-server)

## ✨ Características

- Registro e inicio de sesión de usuarios.
- Autenticación mediante JWT.
- Consulta de médicos.
- Reserva de turnos médicos.
- Modificación de turnos.
- Cancelación de turnos.
- Comunicación con una API REST.
- Gestión de sesiones y credenciales.
- Navegación entre las diferentes pantallas de la aplicación.

## 🏗️ Arquitectura

La aplicación utiliza **Clean Architecture** junto con el patrón **MVVM**, separando las responsabilidades de presentación, dominio y acceso a datos.

```text
app/
├── data/       # Acceso a datos y comunicación con la API
├── domain/     # Modelos y lógica de negocio
├── ui/         # Interfaces y ViewModels
└── utils/      # Funciones y utilidades generales
```

## 🔌 Comunicación con el Backend

La aplicación no contiene la lógica principal de persistencia de usuarios, médicos y turnos.

La comunicación se realiza mediante **Retrofit** contra una API REST desarrollada con Spring Boot.

```text
Android App
    │
    │ Retrofit / HTTP
    ▼
Spring Boot API
    │
    │ Spring Data JPA
    ▼
MySQL
```

## 🔐 Autenticación

La autenticación utiliza **JSON Web Tokens (JWT)**.

El flujo general es:

```text
Usuario
   │
   │ Login
   ▼
Android App
   │
   │ POST /login
   ▼
Spring Boot
   │
   │ Validación
   ▼
JWT
   │
   ▼
Android App
   │
   │ Bearer Token
   ▼
Endpoints protegidos
```

## 🛠️ Tecnologías

- Kotlin
- Android
- MVVM
- Clean Architecture
- Retrofit
- Dagger Hilt
- Navigation Component
- JWT

## 🚀 Requisitos

Para ejecutar correctamente la aplicación se necesita:

- Android Studio
- JDK
- Un dispositivo Android o emulador.
- El **backend de Turnos Médicos** funcionando y accesible desde la aplicación.

### Configuración

1. Clonar este repositorio.
2. Clonar y configurar el [Backend de Turnos Médicos](https://github.com/MatiasOrtizF/turnos-medicos-server).
3. Iniciar el backend.
4. Configurar en la aplicación la URL correspondiente a la API.
5. Ejecutar la aplicación desde Android Studio.

> ⚠️ **Importante:** El frontend depende de la API REST del backend. Sin el backend funcionando, las funcionalidades que requieren usuarios, médicos o turnos no estarán disponibles.

## 👨‍💻 Autor

**Matias Ortiz**

- GitHub: [MatiasOrtizF](https://github.com/MatiasOrtizF)
- Portfolio: [Portfolio](https://matiasortizf.github.io/portfolio/)
