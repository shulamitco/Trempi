package shulamit.trempi.models;

public class Contact {
    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return accurateNumber(number);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        String newNumber = accurateNumber(number);
        return   name + " " + newNumber;
    }
    private String accurateNumber(String number) {
        if (number.charAt(0) == '0') {
            number = "+972" + number.substring(1);
        }
        number = number.replaceAll("-","");
        number = number.replaceAll(" ","");
        return number;
    }
}
