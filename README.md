🎵 Spotify History Analyzer - Distributed System

<img width="1268" height="412" alt="Screenshot from 2026-01-16 11-24-36" src="https://github.com/user-attachments/assets/0f4f7fa4-e095-4d6a-92a6-9646ca98d8de" />


A distributed application built with Java RMI (Remote Method Invocation) designed to parse and analyze Spotify Extended Streaming History data. This project demonstrates key distributed systems design patterns, focusing on stateless server architecture and secure communication.

    ⚠️ Note: This project is currently under active development. Features and architecture are subject to change.

🚀 Key Features (Implemented)

    -Distributed Architecture: Uses Java RMI for communication between Client and Server.

    -Token-Based Authentication: login mechanism using signed Tokens (RSA signature).

    -Stateless Server: The server does not maintain user session state in memory, ensuring scalability.

    -Client Session State: The session history and cache are maintained by the Client and transferred via DTOs.

    -Smart Caching (Idempotency): The server detects repeated queries (e.g., searching for the same year twice) and returns cached results instantly without re-processing files.

    -Data Analysis: Calculate the total number of songs listened to in a specific year.

🏗️ Design Patterns & Architecture

This project implements several architectural patterns studied in Distributed Systems engineering:

    1. Remote Facade: The SpotifyServiceImpl acts as a facade, hiding the complexity of file parsing (GSON) and data processing from the client.

    2. Data Transfer Object (DTO): Uses Java Records (SessionDTO, StreamRecordDTO, Token) to transport data across the network efficiently and immutably.

    3. Authenticator & Token: A dedicated TokenGenerator creates signed tokens upon login. A TokenStore validates them before every operation, decoupling security from business logic.

    4. Client Session State: To keep the server stateless, the History and Cache are stored in a SessionDTO held by the Client. This object is passed to the server with every request and returned with updates.


📂 Project Structure

```text
├── client
│   ├── Client.java         # Handles user input and local state (SessionDTO)
│   └── Main.java           # Entry point for the Client
├── server
│   ├── Server.java         # Registers the RMI Service
│   ├── SpotifyServiceImpl.java # Implementation of business logic (Facade)
│   ├── TokenGenerator.java     # Singleton for generating RSA signatures
│   └── TokenStore.java         # Validates tokens and signatures
└── common
    ├── SpotifyService.java     # RMI Interface
    ├── SessionDTO.java         # "Suitcase" for History and Cache
    ├── StreamRecordDTO.java    # Represents a single song record
    └── Token.java              # Security token record
```

🛠️ Getting Started
Prerequisites

    Java Development Kit (JDK) 21.

    Maven (for dependency management, e.g., GSON).

    Your Spotify data files (JSON format).
Installation

    1. Clone the repository:
    git clone https://github.com/Micg25/spotify-history-analyzer-distributed-system.git
    cd spotify-history-analyzer-distributed-system

    2. Add Data: Place your Spotify JSON files (e.g., Streaming_History_Audio_2023_1.json) inside the folder named: Spotify Extended Streaming History/ in the project root.

    3. Compile:
    mvn clean install

  Usage
    
    1. Start the Server: Run the server.Server class. It will bind the service to the RMI registry (default port 1099).
    2. Start the Client: Run the client.Main class.


Michele Guglielmino's Project for Ingegneria dei Sistemi Distribuiti - UNICT.
    
    
