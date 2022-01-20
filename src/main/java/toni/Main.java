package toni;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Slike.og2bw("slika1.png");

        Slike.splitDigits("slika1bw.png");
    }

}
