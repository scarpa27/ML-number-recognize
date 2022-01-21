package toni;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Slike {

    public static int[] idx = new int[12];

    public static void og2bw (String fname, String inputFolder, String outputFolder) throws IOException {
        BufferedImage ogslika = getSlika(fname,inputFolder);

        BufferedImage bwslika = new BufferedImage(ogslika.getWidth(),ogslika.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = bwslika.createGraphics();
        g2d.drawImage(ogslika,0,0,Color.WHITE,null);
        g2d.dispose();

        File output = new File(F.put(outputFolder+"/"+fname));
        ImageIO.write(bwslika,"png",output);
    }

    public static void splitDigits(String fname, String inputFolder, String outputFolder, boolean train) throws IOException {
        BufferedImage slika = getSlika(fname,inputFolder);
        ArrayList<Integer> koord_x = prepoznaj_x(slika);            //početna i krajnja x koordinata svake znamenke
        ArrayList<Integer> koord_y = prepoznaj_y(slika,koord_x);    //                  y
        ArrayList<BufferedImage> slike = new ArrayList<>();



        for (int i=0; i<koord_x.size(); i+=2) {
            int sx1 = koord_x.get(i);
            int sx2 = koord_x.get(i+1);
            int sy1 = koord_y.get(i);
            int sy2 = koord_y.get(i+1);
            int x = sx2 - sx1 + 1;
            int y = sy2 - sy1 + 1;


            int z = Math.max(x,y);
            BufferedImage nova = new BufferedImage(z,z,slika.getType());
            Graphics2D g2d = nova.createGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0,0,z,z);
            g2d.drawImage(slika,(z-x)/2,(z-y)/2,x+(z-x)/2,y+(z-y)/2,sx1,sy1,sx2+1,sy2+1,null);
            g2d.dispose();

            slike.add(resize(nova,Main.DIM));
        }

        for (BufferedImage bufferedImage : slike) {
            File output = new File(F.put(outputFolder + "/" + fname.charAt(0) + String.format("_%03d", idx(train ? fname.charAt(0) : '0')) + ".png"));
            ImageIO.write(bufferedImage, "png", output);
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
                for (int j = inx.get(k); j <= inx.get(k + 1); j++) {
                    if (slika.getRGB(j, i) != -1) {
                        prelomi.add(i);
                        break;}}}}


        ret.add(prelomi.get(0));
        for (int i = 1; i < prelomi.size(); i++) {
            if (prelomi.get(i) - prelomi.get(i-1) != 1) {
                ret.add(prelomi.get(i-1));
                ret.add(prelomi.get(i));
            }
        }
        ret.add(prelomi.get(prelomi.size()-1));

        return ret;
    }

    public static BufferedImage resize (BufferedImage original,int dim) {
        Image ret = original.getScaledInstance(dim,dim,Image.SCALE_DEFAULT);
        BufferedImage out = new BufferedImage(dim,dim,original.getType());
        out.getGraphics().drawImage(ret,0,0,null);
        return out;
    }

    public static BufferedImage getSlika (String fname, String folder) {
        File input = new File(F.put(folder+"/"+fname));
        try {
            return ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void nonWhite2Black (String folder) {
        F.getDatoteke(folder).forEach(img -> {
            File input = new File(F.put(folder+"/"+img));
            BufferedImage ogslika = null;
            try {ogslika = ImageIO.read(input);}
            catch (IOException e) {e.printStackTrace();}

            for (int i = 0; i < ogslika.getHeight(); i++) {
                for (int j = 0; j < ogslika.getWidth(); j++) {
                    if (ogslika.getRGB(j,i) != -1) {
                        Color color = new Color(0,0,0);
                        ogslika.setRGB(j,i,color.getRGB());
                    }
                }
            }

            try {ImageIO.write(ogslika,"png",input);}
            catch (IOException e) {e.printStackTrace();}
        });
    }

    public static int idx (char chr) {
        if (chr !='d' && chr!='s') {
            return idx[chr-'0']++;}
        else {
            return idx[chr == 'd' ? 10 : 11]++;}
    }
}
