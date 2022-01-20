package toni;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Slike {

    public static void og2bw (String fname) throws IOException {
        File input = new File(F.put(fname));
        BufferedImage ogslika = ImageIO.read(input);

        BufferedImage bwslika = new BufferedImage(ogslika.getWidth(),ogslika.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = bwslika.createGraphics();
        g2d.drawImage(ogslika,0,0,Color.WHITE,null);
        g2d.dispose();

        File output = new File(F.put("slika1bw.png"));
        ImageIO.write(bwslika,"png",output);
    }

    public static void splitDigits(String fname) throws IOException {
        BufferedImage slika = ImageIO.read(new File(F.put(fname)));
        ArrayList<Integer> prelomi = new ArrayList<>();     //x koordinata svih stupaca koji sadrže znamenku
        ArrayList<Integer> koord_x = prepoznaj_x(slika);     //početna i krajnja x koordinata svake slike
        ArrayList<Integer> koord_y = new ArrayList<>();     //početna i krajnja y
        ArrayList<BufferedImage> slike = new ArrayList<>();

        for (int i=0; i<koord_x.size(); i+=2) {
            int sx1 = koord_x.get(i);
            int sx2 = koord_x.get(i+1);
            int sy1 = 0;                        //proširit
            int sy2 = slika.getHeight() - 1;    //proširit
            int x = sx2 - sx1 + 1;
            int y = sy2 - sy1 + 1;

            System.out.println((i/2+1)+". slika je "+sx1+" "+sx2);

            BufferedImage nova = new BufferedImage(x,y,slika.getType());
            Graphics2D g2d = nova.createGraphics();
            g2d.drawImage(slika,0,0,x,y,sx1,sy1,sx2+1,sy2+1,null);
            g2d.dispose();

            slike.add(nova);
        }

        for (int i = 0; i < slike.size(); i++) {
            File output = new File(F.put("izlaz"+i+".png"));
            ImageIO.write(slike.get(i),"png",output);
        }
    }

    public static ArrayList<Integer> prepoznaj_x (BufferedImage slika) {
        ArrayList<Integer> prelomi = new ArrayList<>();
        ArrayList<Integer> ret = new ArrayList<>();

        for (int i=0; i<slika.getWidth(); i++) {
            for (int j=0; j<slika.getHeight(); j++) {
                if (slika.getRGB(i,j) != -1) {
                    prelomi.add(i);
                    break;}}}

        ret.add(prelomi.get(0));
        for (int i = 1; i < prelomi.size(); i++) {
            if(prelomi.get(i)- prelomi.get(i-1) >1) {
                ret.add(prelomi.get(i-1));
                ret.add(prelomi.get(i));
            }
        }
        ret.add(prelomi.get(prelomi.size()-1));

        return ret;
    }

    public static ArrayList<Integer> prepoznaj_y (BufferedImage slika, ArrayList<Integer> inx) {
        ArrayList<Integer> prelomi = new ArrayList<>();
        ArrayList<Integer> ret = new ArrayList<>();

        for (int k = 0; k < inx.size(); k += 2) {
            for (int i = 0; i < slika.getHeight(); i++) {
                for (int j = inx.get(k); j < inx.get(k + 1); j++) {
                    if (slika.getRGB(j, i) != -1) {
                        prelomi.add(i);
                        break;}}}}

        ret.add(prelomi.get(0));
        for (int i = 1; i < prelomi.size(); i++) {
            if (prelomi.get(i) - prelomi.get(i-1) > 1) {
                ret.add(prelomi.get(i-1));
                ret.add(prelomi.get(i));
            }
        }

        return ret;
    }
}
