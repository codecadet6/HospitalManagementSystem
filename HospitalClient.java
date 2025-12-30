import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class HospitalClient {

    private static boolean authenticated = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showLogin());
    }

    // ---------- LOGIN UI ----------
    private static void showLogin() {
        JFrame frame = new JFrame("Hospital Login");
        frame.setSize(300, 180);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JLabel status = new JLabel("");

        JButton loginBtn = new JButton("Login");

        frame.setLayout(new GridLayout(4, 2));
        frame.add(new JLabel("Username:"));
        frame.add(userField);
        frame.add(new JLabel("Password:"));
        frame.add(passField);
        frame.add(loginBtn);
        frame.add(status);

        loginBtn.addActionListener(e -> {
            if (login(userField.getText(), new String(passField.getPassword()))) {
                frame.dispose();
                new HospitalUI();
            } else {
                status.setText("Invalid credentials");
            }
        });

        frame.setVisible(true);
    }

    // ---------- LOGIN LOGIC ----------
    private static boolean login(String user, String pass) {
        try {
            Socket socket = new Socket("localhost", 5000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("LOGIN");
            out.writeObject(user);
            out.writeObject(pass);
            out.flush();

            String response = (String) in.readObject();
            socket.close();

            authenticated = response.equals("SUCCESS");
            return authenticated;

        } catch (Exception e) {
            return false;
        }
    }
}

// ================= MAIN UI =================
class HospitalUI extends JFrame {

    private JTextField idField, nameField, roomField, diseaseField, searchField;
    private JTextArea output;

    HospitalUI() {
        setTitle("Hospital Management System");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(5, 2));

        idField = new JTextField();
        nameField = new JTextField();
        roomField = new JTextField();
        diseaseField = new JTextField();

        form.add(new JLabel("Patient ID:"));
        form.add(idField);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Room No:"));
        form.add(roomField);
        form.add(new JLabel("Disease:"));
        form.add(diseaseField);

        JButton addBtn = new JButton("Admit");
        JButton clearBtn = new JButton("Clear");
        form.add(addBtn);
        form.add(clearBtn);

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        output = new JTextArea(8, 40);
        output.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
        add(new JScrollPane(output), BorderLayout.SOUTH);

        addBtn.addActionListener(e -> admit());
        searchBtn.addActionListener(e -> search());
        clearBtn.addActionListener(e -> clear());

        setVisible(true);
    }

    private void admit() {
        try {
            Socket socket = new Socket("localhost", 5000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            Patient p = new Patient(
                    Integer.parseInt(idField.getText()),
                    nameField.getText(),
                    Integer.parseInt(roomField.getText()),
                    diseaseField.getText()
            );

            out.writeObject("ADD");
            out.writeObject(p);
            out.flush();

            output.setText((String) in.readObject());
            socket.close();

        } catch (Exception e) {
            output.setText("Error occurred");
        }
    }

    private void search() {
        try {
            Socket socket = new Socket("localhost", 5000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject("SEARCH");
            out.writeInt(Integer.parseInt(searchField.getText()));
            out.flush();

            Object res = in.readObject();
            output.setText(res == null ? "Patient not found" : res.toString());
            socket.close();

        } catch (Exception e) {
            output.setText("Error occurred");
        }
    }

    private void clear() {
        idField.setText("");
        nameField.setText("");
        roomField.setText("");
        diseaseField.setText("");
        searchField.setText("");
        output.setText("");
    }
}
