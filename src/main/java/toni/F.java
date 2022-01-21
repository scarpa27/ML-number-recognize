package toni;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class F {

    static public int char2idx (char c) {
        return switch (c) {
            case 'd' -> 10;
            case 's' -> 11;
            default -> (c - '0');
        };
    }

    static public char idx2char (int idx) {
        return switch (idx) {
            case 10 -> '-';
            case 11 -> '/';
            default -> (char)(idx + '0');
        };
    }

    static public int maxAt (double[] array) {
        int maxAt = 0;
        for (int i = 0; i < array.length; i++) {
            maxAt = array[i] > array[maxAt] ? i : maxAt;
        }
        return maxAt;
    }

    static public String put (String file) {
        return String.valueOf(Path.of(System.getProperty("user.dir")+Path.of("\\src\\main\\java\\toni\\files\\"+file)));
    }

    static public ArrayList<String> getDatoteke (String folder) {
        File f = new File(put(folder));
        return new ArrayList<>(Arrays.asList(f.list()));
    }

    static public double[] img2numarr (String ime, String folder) {
        double[] ret = new double[Main.DIM*Main.DIM];
        BufferedImage slika = Slike.getSlika(ime,folder);
        int cnt = 0;
        for (int i = 0; i < Main.DIM; i++) {
            for (int j = 0; j < Main.DIM; j++) {
                ret[cnt++] = slika.getRGB(j,i) == -1 ? 0.0 : 1.0;
            }
        }
        return ret;
    }


    static public double[][] calcUlazi (String folder) {
        ArrayList<String> dat = getDatoteke(folder);
        double[][] ulaziBrojevi  = new double[dat.size()][Main.DIM*Main.DIM];

        for (int i = 0; i < dat.size(); i++) {
            ulaziBrojevi[i] = img2numarr(dat.get(i),folder);
        }

        return ulaziBrojevi;
    }

    static public double[][] calcIzlazi (String folder) {
        ArrayList<String> dat = getDatoteke(folder);
        double[][] idealBrojevi = new double[dat.size()][12];
        for (int i = 0; i < dat.size(); i++) {
            idealBrojevi[i][char2idx(dat.get(i).charAt(0))] = 1.0;
        }
        return idealBrojevi;
    }

    static public void ocisti () {
        File
        dir = new File(put("test/bw"));
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(File::delete);
        dir = new File(put("test/digit"));
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(File::delete);
        dir = new File(put("train/bw"));
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(File::delete);
        dir = new File(put("train/digit"));
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(File::delete);
    }
}
