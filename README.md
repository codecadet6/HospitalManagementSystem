# Secure Network-Based Hospital Management System
## Project Overview
This project is a **secure client–server Hospital Management System** developed in Java.  
It provides a networked solution for managing patient records with GUI support, authentication, and persistent storage.

- **Client–Server Architecture:** The server maintains all patient data and handles authentication.  
- **Swing GUI:** User-friendly interface for hospital staff to admit and search patients.  
- **Persistent Storage:** Patient records are saved to a file (`patients.dat`) and remain available after server restarts.  
- **Authentication & Access Control:** Operator login required to access the system; server validates credentials.  
- **Audit Logging:** All activities, including login attempts and patient operations, are logged (`audit.log`).  

---

## Features

1. **Secure Login**
   - Username and password required for access
   - Server-side verification to prevent unauthorized access

2. **Patient Management**
   - Admit new patients with ID, name, room number, and disease
   - Search existing patients by ID
   - View patient details including room assignment

3. **Data Persistence**
   - All patient data is stored using Java object serialization
   - Records remain intact after server shutdown and restart

4. **Audit Logging**
   - Server maintains logs of login attempts and patient-related actions

5. **GUI Interface**
   - User-friendly forms for admitting and searching patients
   - Clear messages for success or errors

---

## Technologies Used

- **Programming Language:** Java  
- **Networking:** Java Sockets (TCP/IP)  
- **GUI:** Java Swing  
- **Data Persistence:** File-based using Object Serialization  
- **Security:** Server-side authentication and access control  
- **Tools:** Command Prompt / Notepad (or any IDE)  

---

## How to Run

# Compile all Java files
javac Patient.java HospitalServer.java HospitalClient.java

# Start the Server
java HospitalServer

# Start the Client
java HospitalClient

## Login Credentials for Demo

| Username | Password  |
|----------|-----------|
| admin    | admin123  |
| staff    | staff123  |

## Use the GUI

After logging in, the graphical interface allows hospital staff to:

- Admit new patients with ID, name, room number, and disease details
- Search for existing patients by ID
- View patient details including room assignment
- Receive system messages for successful operations or errors

