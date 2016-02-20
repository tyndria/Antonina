/**
 * Created by Антонина on 13.02.16.
 */
public class Remark {
    private String type;
    private String name;
    private String input;
    private String details;

    public Remark(String type, String name, String input, String details) {
        this.type = type;
        this.name = name;
        this.input = input;
        this.details = details;
    }

    public String toString() {
        return "Type of remark: " + this.type + "; Name: " + this.name + "; Input data caused this remark: " + this.input + "; Details: " + this.details;
    }
}
