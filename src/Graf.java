import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Graf {
    int v; // POČET VRCHOLOV GRAFU
    int h; // POČET HRÁN GRAFU
    int[][] H; // POLE ÚDAJOV O HRANÁCH
    int NEKONECNO = Integer.MAX_VALUE / 2;

    public Graf(int pocetVrcholov, int pocetHran) {
        this.v = pocetVrcholov;
        this.h = pocetHran;
        this.H = new int[this.h + 1][3];
    }

    // NAČÍTANIE ZO SÚBORU:
    /* KAŽDÝ RIADOK - 3 ČÍSLA (ČÍSLO VRCHOLU 1, ČÍSLO VRCHOLU 2, OHODNOTENIE HRANY)
     *  POČET VRCHOLOV SA URĆÍ AUTOMATICKY
     *  POČET HRÁN JE ROVNÝ POČTU RIADKOV SÚBORU
     *  POČET VRCHOLOV JE ROVNÝ NAJVAČŠIEMU ČÍSLU V ÚDAJOCH O HRANÁCH */
    public static Graf nacitanieSuboru(String nazovSuboru) throws FileNotFoundException {

        // OTVORENIE SÚBORU
        Scanner sc = new Scanner(new FileInputStream(nazovSuboru));

        // ZÍSTÍM POČET VRCHOLOV A POČET HRÁN
        int pocetVrcholov = 0;
        int pocetHran = 0;
        while (sc.hasNext()) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            int c = sc.nextInt();

            pocetHran++;
            // KONTROLA - JE TEBA ZVÝŠIŤ POČET VRCHOLOV?
            if (pocetVrcholov < a) {
                pocetVrcholov = a;
            }
            if (pocetVrcholov < b) {
                pocetVrcholov = b;
            }

        }
        sc.close();
        // VYTVORÍM INŠTANCIU GRAFU S NAČÍTANÝMI PREMENNÝMI
        Graf g = new Graf(pocetVrcholov, pocetHran);

        // OPAT OTVÁRAME SÚBOR, MÁME POČET VRCHOLOV A HRÁN, MÁM ALOKOVANÚ PAMAT
        sc = new Scanner(new FileInputStream(nazovSuboru));

        // NAČÍTÁVAM VŠETKY HRANY, UKLADÁM DO PAMATE
        for (int i = 1; i < pocetHran; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            int c = sc.nextInt();

            g.H[i][0] = a;
            g.H[i][1] = b;
            g.H[i][2] = c;
        }
        return g;
    }

    public void printInfo() {
        System.out.println("POČET VRCHOLOV: " + v);
        System.out.println("POČET HRÁN: " + h);
    }

    public void labelSet(int from, int to) {
        int[] cena = new int[v + 1];
        int[] cesta = new int[v + 1]; // VRCHOL z ktorého sme sa dostali do iného VRCHOLU - ukladanie predchodcov

        boolean[] pouzite = new boolean[v + 1];
        List<int[]>[] susedneHrany; // pomocný list na susedné hrany
        susedneHrany = new List[v + 1];

        Arrays.fill(cena, NEKONECNO); // Naplnenie pola na NEKONEČNO
        Arrays.fill(cesta, 0); // na začiatku sme sa ešte nepohli do iného VRCHOLU - cesta = 0

        cena[from] = 0; // sme v štartovacom VRCHOLE, do ostatných sme ešte nevstúpili
        susedneHrany[from] = new ArrayList<>();

        for (int i = 1; i <= h; i++) { // prechadzame každou HRANOU
            int v1 = H[i][0]; // zistíme vrchol1
            int v2 = H[i][1]; // zistíme vrchol2
            int c = H[i][2]; // zistíme cenu

            int[] hrana = new int[]{v2, c}; // vytvarame pole s druhym vrcholom a jej cenou

            if (susedneHrany[v1] == null) { // ak neexistuje zoznam susednych hran pre tento vrchol, vytvorime ho
                susedneHrany[v1] = new ArrayList<>();
            }
            susedneHrany[v1].add(hrana);
        }

        int temp = from;

        while (temp != to) { // pokiaľ sa aktuálny VRCHOL nerovná cieľovému
            pouzite[temp] = true; // označíme, že vrchol bol spracovaný
            if (susedneHrany[temp] != null) {
                for (int[] hrana : susedneHrany[temp]) { // ideme cez všetky susedne hrany k momentalnemu vrcholu temp
                    int v2 = hrana[0];
                    int c = hrana[1];
                    if (cena[v2] > cena[temp] + c) { // Ak sa cena nejakého VRCHOLU zmenila, aktualizujeme jeho cestu
                        cena[v2] = cena[temp] + c; // v1 - cena najkratšej cesty do vrcholu V1, c - cena hrany medzi V1 a V2
                                                // v2 - cena najkratšej cesty do vrcholu V2
                        cesta[v2] = temp;        // vieme sa dostať do V2 s nižšou cenou cez V1
                    }
                }
            }

            int min = NEKONECNO;
            for (int i = 1; i <= v; i++) { // prechádzame všetky VRCHOLY v grafe
                if (!pouzite[i] && cena[i] < min) { // hľadame len nepoužité a zmenu ceny
                    min = cena[i];                  // pokiaľ platí podmienka, minimálna cena sa prepíše na pozíciu daného vrchola v poli
                    temp = i;                       // aby sa posunul while loop, premenime temp na dany VRCHOL a pokračujeme ďalej
                }
            }
        }


        System.out.println("ALGORITMUS SKONČIL.");
        System.out.println("VYPÍSANIE CESTY:" + "\n");

        List<Integer> cestaList = new ArrayList<>();
        System.out.println("CENA JE: " + cena[to]);
        int temp2 = to;
        while (temp2 != from) {
            cestaList.add(temp2);
            temp2 = cesta[temp2];
        }
        cestaList.add(from);
        System.out.println("CESTA:");
        for(int i = cestaList.size() - 1; i > 0; i--) {
            System.out.print(cestaList.get(i) + " -> ");
        }
        System.out.print(cestaList.get(0));
    }

}

