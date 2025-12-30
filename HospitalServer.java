import java.io.*;
import java.net.*;
import java.util.*;

public class HospitalServer {

    private static final int PORT = 5000;
    private static final String DATA_FILE = "patients.dat";
    private static final String LOG_FILE = "audit.log";

    private static Map<Integer, Patient> patientDB = new HashMap<>();
    private static Map<String, String> users = new HashMap<>();

    public static void main(String[] args) {
        loadUsers();
        loadData();

        System.out.println("Hospital Server running on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // ---------- USER SETUP ----------
    private static void loadUsers() {
        // In real systems → database / hashed passwords
        users.put("admin", "admin123");
        users.put("staff", "staff123");
    }

    // ---------- DATA PERSISTENCE ----------
    @SuppressWarnings("unchecked")
    private static void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            patientDB = (HashMap<Integer, Patient>) in.readObject();
            System.out.println("Patient data loaded.");
        } catch (Exception e) {
            System.out.println("No previous data found.");
        }
    }

    private static synchronized void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(patientDB);
        } catch (IOException e) {
            System.out.println("Failed to save data.");
        }
    }

    // ---------- LOGGING ----------
    private static synchronized void log(String msg) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(new Date() + " : " + msg + "\n");
        } catch (IOException ignored) {}
    }

    // ---------- CLIENT HANDLER ----------
    static class ClientHandler extends Thread {
        private Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                String command = (String) in.readObject();

                // ---- LOGIN ----
                if (command.equals("LOGIN")) {
                    String user = (String) in.readObject();
                    String pass = (String) in.readObject();

                    if (users.containsKey(user) && users.get(user).equals(pass)) {
                        out.writeObject("SUCCESS");
                        log("Login success for user: " + user);
                    } else {
                        out.writeObject("FAIL");
                        log("Login failed for user: " + user);
                    }
                    out.flush();
                }

                // ---- ADD PATIENT ----
                else if (command.equals("ADD")) {
                    Patient p = (Patient) in.readObject();
                    patientDB.put(p.id, p);
                    saveData();
                    out.writeObject("Patient admitted successfully.");
                    log("Patient admitted: ID " + p.id);
                    out.flush();
                }

                // ---- SEARCH PATIENT ----
                else if (command.equals("SEARCH")) {
                    int id = in.readInt();
                    out.writeObject(patientDB.get(id));
                    log("Patient searched: ID " + id);
                    out.flush();
                }

                socket.close();

            } catch (Exception e) {
                System.out.println("Client error.");
            }
        }
    }
}
