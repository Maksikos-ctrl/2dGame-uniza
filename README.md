# Multiplayer Game Server
## _Real-Time Multiplayer Game Server with UDP Networking_

[![Build Status](https://travis-ci.org/Maksikos-ctrl/Multiplayer2dGame-Server.svg?branch=master)](https://travis-ci.org/Maksikos-ctrl/Multiplayer2dGame-Server)



The Multiplayer Game Server is a robust and efficient server application designed for real-time multiplayer games. It handles player connections, manages game state, and facilitates real-time communication between players using UDP sockets.

- Real-time multiplayer interactions
- Dynamic port binding and automatic retry
- ✨ Scalable and efficient network handling ✨

## Table of Contents

- [Features](#features)
- [Tech](#tech)
- [Installation](#installation)
  - [Prerequisites](#prerequisites)
  - [Building](#building)
  - [Running](#running)
- [Usage](#usage)
- [Development](#development)
- [Acknowledgments](#acknowledgments)
- [License](#license)

## Features

- Real-time communication with UDP sockets
- Efficient packet handling for player connections and movements
- Dynamic port binding to avoid conflicts
- Easy integration with existing game clients

## Tech

The Multiplayer Game Server uses the following technologies and libraries:

- [Java](https://www.oracle.com/java/) - Programming language for the server implementation
- [Maven](https://maven.apache.org/) - Dependency management and build automation
- [Travis CI](https://travis-ci.org/) - Continuous integration and build automation

## Installation

### Prerequisites

- **Java 11 or later**: Make sure you have Java 11 or a later version installed on your machine. You can download it from [Oracle's official site](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use an open-source version like [OpenJDK](https://openjdk.java.net/).
- **Maven**: Maven is used for dependency management and building the project. You can download and install Maven from the [Apache Maven website](https://maven.apache.org/download.cgi).

### Building

To build the project, follow these steps:

1. **Clone the repository**: 

   ```sh
   git clone https://github.com/Maksikos-ctrl/Multiplayer2dGame-Server.git
   cd src/main
   ```

    Build the project using Maven:

    ```sh

    mvn clean install
    ```
    This command will compile the code, run the tests, and package the application into a JAR file.

## Running

To start the server, use the Maven command to execute the main class:

```sh

mvn exec:java -Dexec.mainClass="com.maksikos.game.GameLauncher"
```

- Ensure the server is allowed to bind to the configured port (default: 1331). If the port is already in use, the server will attempt to bind to the next available port.

### Usage
    Start the Server: Run the server using the command provided in the Running section.
    Connect Clients: Configure your game client to connect to the server’s IP address and port. The default port is 1331 unless otherwise configured.
    Monitor Activity: Check the server logs for connection activity, packet processing, and any errors.

### Development

Interested in contributing? Awesome! Here’s how you can get started with development:
Building

To build the project from source:

```sh

mvn clean install
```

## Acknowledgments

We'd like to thank the following individuals and organizations for their contributions and support:

- **[OpenJDK](https://openjdk.java.net/)**: For providing the open-source Java Development Kit used to build and run the project.
- **[Apache Maven](https://maven.apache.org/)**: For the powerful build automation tool that manages dependencies and builds our project.
- **[GitHub](https://github.com/)**: For hosting the project and providing version control through Git.
- **[Travis CI](https://travis-ci.org/)**: For continuous integration and automated build status checking.

Special thanks to our contributors and the community who provided feedback, reported issues, and suggested improvements.

If you have any feedback or suggestions, feel free to [open an issue](https://github.com/yourusername/your-repo/issues) or contribute to the project by [submitting a pull request](https://github.com/yourusername/your-repo/pulls).


### License

This project is licensed under the ANSYS License - see the [LICENSE](https://www.ansys.com/academic/students) file for details.









