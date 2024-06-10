import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Graf labelSetGraf = Graf.nacitanieSuboru("src\\Florida.hrn");
        labelSetGraf.printInfo();
        labelSetGraf.labelSet(1,420);


    }
}