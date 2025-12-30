import java.io.Serializable;

public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String name;
    public int roomNo;
    public String disease;

    public Patient(int id, String name, int roomNo, String disease) {
        this.id = id;
        this.name = name;
        this.roomNo = roomNo;
        this.disease = disease;
    }

    @Override
    public String toString() {
        return "Patient ID: " + id +
               "\nName      : " + name +
               "\nRoom No   : " + roomNo +
               "\nDisease  : " + disease;
    }
}
