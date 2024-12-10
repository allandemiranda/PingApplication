
# Simple Ping Application

## Overview

The **Simple Ping Application** is a Java-based tool designed to monitor network connectivity by regularly pinging specified hosts using ICMP and TCP/IP protocols, as well as performing traceroute operations. Bad results are processed and optionally sent to an external API for further analysis or reporting.

This project is built with Java 17 and Maven, utilizing a modular architecture to ensure scalability, maintainability, and efficient scheduling of network diagnostics tasks.

---

## Key Features

1. **ICMP and TCP/IP Ping**
   - Implements regular pings to specified hosts using both ICMP and TCP/IP protocols.
   - Configurable command templates to adapt to different system environments.
   - Manages concurrent ping operations to ensure no overlap occurs for the same host.

2. **Traceroute**
   - Executes traceroute operations to diagnose routing paths for specific hosts.

3. **Batch Job Scheduler**
   - Configures and schedules tasks for pings and traceroutes at specified intervals.
   - Ensures tasks are executed in a non-blocking and efficient manner.

4. **Dynamic Configuration**
   - Utilizes the `PropertiesConfig` interface for accessing application settings, including:
     - Hostnames to monitor.
     - Delay intervals between tasks.
     - Commands and parameters for pings and traceroutes.

5. **Reporting**
   - Gathers bad results from tasks and submits structured reports to an external API using the `ReportService` interface.

---

## Project Structure

The project follows a modular and layered architecture to ensure scalability, maintainability, and separation of concerns. Below is a detailed explanation of the structure and the relationships between components:

### Workflow Starting Point: `BatchJobsConfig`
- The **`BatchJobsConfig`** interface is the entry point for the application's workflow.
   - It receives dependencies and configuration properties through the **`InitializationFactory`**.
   - It sets up and configures jobs for ICMP pings, TCP/IP pings, and traceroutes.
   - It manages the workflow of jobs, orchestrating their execution in a scheduled and efficient manner.

### Controllers: Mediating Communication
- The **Controller** interfaces serve as intermediaries between the workflow jobs and backend services.
   - Communication is standardized using:
      - **Request Models**: Inputs encapsulated in DTOs or simple request objects.
      - **Response Models**: Outputs wrapped in the generic class **`ResponseFactory<T>`**.
   - This design ensures a clear barrier between:
      - The workflow and the controllers (frontend of the application).
      - The controllers and the services (backend logic).

### Key Controller Interfaces
1. **`ReportController`**
   - Handles operations related to reports.
   - Processes reports encapsulated in **`ReportDto`** and returns responses via **`ResponseFactory`**, indicating success or failure.

2. **`PingController`**
   - Manages operations for ICMP pings, TCP/IP pings, and traceroutes.
   - Responsible for retrieving and submitting results using protocol-specific DTOs.

### Service Layer: Logic and Data Management
- Each method in a `Controller` interacts with one or more **Service** interfaces.
- The **Service** interfaces handle:
   - Data manipulation (internal or external to the application).
   - Communication with repositories for local storage or external APIs.
- This creates another barrier where services manage business logic independently of the controllers.

---

### Structural Map

```
                                                                                BatchJobsConfig (start of flow)
                                                                                            |
                                                                                            + <--- InitializationFactory (injects dependencies and configurations)
                                                                                            |
                                 _________________________________                          | 
  +--------------------------    | Scheduled task basket in loop |                          |
  |                ONE JOB       |              | X X X X X | <---- ICMP Ping Job <---------+
--+--                [X] <==(<config info>)==   | X X X X X | <---- TCP/IP Ping Job <-------+                       [Sleep Job <time> from <config info> (delay)]
| J |            +-- RUN <---+   |              | X X X X X | <---- Traceroute Job <--------+                                   |           ^
| O |            |           |   |_______________________________|                                                              |           |
| B |            |           |                                                                                                  |           |
--+--            |           +--------------------------------------------------------------------------------------------------+           |
  |              |                                                                                                                          |
  |              |                                           +------------------------------------------------------------------------------+------------------------------------------------------------------------------------------+
  |              |                                           |                                                                                                                                                                         |
  |              |                                           *                                                                                                                                                                         |
  |              |                                       * false *                                                                                                                                                                     |                                                        
  |              v                                           *                                                                                                                                                                         |
  |          |------------------------------|                |                *                                  |---------------------------------------------|                                                      |------------------------------------|
  |          | Request a Ping or Traceroute | ---> [If bad ping/trace] ----* true *----------------------------> | Request lest ICMP/TCP/Trace (three request) | ---> [Joing ICMP/TCP/Trace DTOs to <Report DTO>] --->|      Post Bad Report Data          |
  |          |------------------------------|                                 *                                  |---------------------------------------------|                                                      |------------------------------------|
  |               |                      ^                                                                              |                             ^                                                                  |                            ^
  |               +                      |                                                                              +                             |                                                                  +                            |
  |  (<host> from <config info>)         |                                                                  (<host> from <config info>)               |                                                           (<Report DTO>)                      |
  |               +                      +                                                                              +                             +                                                                  +                            +
  |               |       (ResponseFactory<ICMP/TCP/Trace DTO>)                                                         |              (ResponseFactory<ICMP/TCP/Trace DTO>)                                             |                  (ResponseFactory<VOID>)
  |               |                      +                                                                              |                             +                                                                  |                            +
  |               v                      |                                                                              v                             |                                                                  v                            |
  +--   [============= Ping Controller ===================================================================================== Ping Controller ============================]-----------------+-----------------[================= Report Controller ========================]
  |               |                      ^                                                                              |                             ^                                                                  |                            ^
  |               |                      |                                                                              |                             |                                                                  |                            |
  |               +                      +                                                                              +                             +                                                                  +                            |
  |           (<host>)     (ResponseFactory<ICMP/TCP/Trace DTO>)                                                     (<host>)           (ResponseFactory<ICMP/TCP/Trace DTO>)                                      (<Report DTO>)            (ResponseFactory<VOID>)
  |               +                      +                                                                              +                             +                                                                  +                            +
  |               |                      |                                                                              |                             |                                                                  |                            |
--+--             |                      +                                                                              |                             +                                                                  |                            +
| H |             |   [Building ResponseFactory<ICMP/TCP/Trace DTO>]                                                    |         [Building ResponseFactory<ICMP/TCP/Trace DTO>]                                         |            [Building ResponseFactory<VOID>]
| A |             +                      +                                                                              +                             +                                                                  +                            +
| N | [Request start ping/trace <host>]  |                                                              [Request lest ICMP/TCP/Trace <host>]          |                                                      [Request post <Report DTO>]              |      
| D |             +                      +                                                                              +                             +                                                                  +                            |
| L |             |             (<ICMP/TCP/Trace DTO>)                                                                  |                (Optional<ICMP/TCP/Trace DTO>)                                                  |                            +
| E |             |                      +                                                                              |                             +                                                                  |                         (<INT>)
--+--             |                      |                                                                              |                             |                                                                  |                            +
  |               v                      |                                                                              v                             |                                                                  v                            |
  +--   [========= ICMP/TCP/Trace Service ================================================================================== ICMP/TCP/Trace Service ======================] -----------------+---------------[================== Report Service ===========================]
  |               |                      ^                                                                              |                             ^                                                                  |                            ^
  |               |                      |                                                                              |                             |                                                                  +                            |
  |               |                      +-----------------------------------------------+                              +                             |                                                 [Serialization <Report DTO> to <JSON>]        |  
  |               |                                                                      |              [Get <ID ICMP/TCP/Trace> from <host>]         |                                                                  +                            |
  |               |    [Joing <Terminal/Network> + <host> = Entity<ICMP/TCP/Trace>]      |                              +                             |                                                                  |                            |
--+--             |                 +                          +                         +                              |                             |                                                                  +                            +          
| T |             |                 |                          |                (<ICMP/TCP/Trace DTO>)                  |                             |                                                               (<JSON>)                     (<INT>)         
| H |             |                 +                          |                         +                              +                             |                                                                  +                            +         
| R |             |  (<Terminal DTO> or <Network DTO>)         |                         |                            (<ID>)                          |                                                                  |                            |          
| O |             |                 +                          |                         |                              +                             +                               +----------------+-----------------+                            +          
| W |             |                 |                          |      [=== Mapping to <ICMP/TCP/Trace DTO> ===]         |                (Optional<ICMP/TCP/Trace DTO>)               |                |                                 [Getting HTTP Status Code]                       
--+--             v                 |                          |                         ^                              |                             +                               |                +                                              +  
  |          [=== http/terminal Tools ===]                     +                         |                              +                             |                               |      [Building HTTP query request]                            |
  |               |                 ^               (Entity<ICMP/TCP/Trace>)             |             [Get Entity<ICMP/TCP/Trace> by <ID>]           |                               |                 +                                             |
  |               +                 |                          +                         +                              +                             |                               |                 |                 +---------------------------+                  
  | (<command> or <http request>)   |                          |               (Entity<ICMP/TCP/Trace>)                 |                             |                               |                 |                 |                  
  |               +                 |                          |                         +                              |                             +                               |                 +                 +                                  
  |               |                 |                          +                         |                              |           [=== Mapping to <ICMP/TCP/Trace DTO> ===]         |           (Query<POST>)     (Query<ANSWER>)                            
  |               v                 |              [Save Entity<ICMP/TCP/Trace>]         |                              |                             +                               |                 +                 +                                   
  |       [=== CMD or Network from SO ===]                     +                         |                              |                             |                               |                 |                 |           
  |                                                            |                         |                              |                             |                               |                 v                 |                                    
  |                                                            |                         |                              |                             +                               |               [=== Network Tools ===]  
  |                                                            |                         |                              |               (Optional<Entity<ICMP/TCP/Trace>>)            |                 |                 ^                   
  |                                                            |                         |                              |                             +                               |                 |                 |
  |                                                            |                         |                              |                             |                               |                v                 |
  |                                                            v                         |                              v                             |                               |            [=== External Report API ===] 
  |                                       [===================== ICMP/TCP/Trace DataBase ================================= ICMP/TCP/Trace DataBase ==========================]        |                    
  |                                                            |                         ^                              |                             ^                               +----------------------------------+                                        
  |                                                            |                         |                              |                             |                                                                  |    
  |                                                            |                         +                              |                             |                                                                  |      
  |                                                            |              (Entity<ICMP/TCP/Trace>)                  |              (Optional<Entity<ICMP/TCP/Trace>>)                                                v
  |                                                            |                         +                              |                             +                                              [=== Appending Log Warning on app.log file ===]
  |                                                            v                         |                              v                             |                                                                
  |                                          [=== Persist Entity<ICMP/TCP/Trace> ===] ---+         [=== Find Entity<ICMP/TCP/Trace> by <ID> ===] -----+                                                               
  |                                                            |                                                  |           ^                                                                       
  |                                                            |                                                  |           |                                                                                                       
  |                                                            v                                                  v           |
  |                                              ___________________________________________________________________________________________________
  +--                                           | *** ICMP MAP<ID, ENTITY>  ***  +  *** TCP MAP<ID, ENTITY>  ***  +  *** Trace MAP<ID, ENTITY>  *** |


```
This architecture promotes a clear separation of responsibilities:
- **Workflow Configuration**: Managed by the `BatchJobsConfig` interface.
- **Intermediation**: Handled by `Controller` interfaces, bridging the workflow and services.
- **Business Logic**: Encapsulated in the `Service` interfaces, ensuring modularity and reusability.

---

## Configuration

### `config.properties`

The application uses the `PropertiesConfig` interface to load settings from a `config.properties` file.

### Dependencies

- **Java 17**: Ensures compatibility with modern Java features.
- **Maven**: Manages project dependencies and build lifecycle.
- **Owner Library**: Simplifies configuration management.

---

## Setup and Execution

1. Clone the repository and navigate to the project directory.
2. Ensure Java 17 and Maven are installed.
3. Configure `config.properties` with desired settings.
4. Build and run the application:
   ```bash
   mvn clean install
   java -jar target/simple-ping-app.jar
   ```
---

## Test Coverage

The project includes a comprehensive suite of tests to ensure reliability and robustness. Below is the current test coverage:

### Overall Coverage
- **Classes**: 80% (25/31)
- **Methods**: 83% (65/78)
- **Lines**: 79% (259/326)
- **Branches**: 63% (122/192)

---

## Reporting API (Optional)

A Node.js script (`LocalReportAPI.js`) is provided to emulate an external reporting API for testing purposes. While not part of the main Java project, it demonstrates integration with external systems.

### Running the API Server

1. Ensure Node.js is installed.
2. Navigate to the project directory and run:
   ```bash
   node LocalReportAPI.js
   ```
3. The server will be available at `http://localhost:3000`.

