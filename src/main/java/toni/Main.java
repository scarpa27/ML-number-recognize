package toni;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.util.obj.SerializeObject;

import java.io.File;
import java.io.IOException;

public class Main {

    public final static int DIM = 32;



    public static void main(String[] args) {

        ucitajTrening();
        ucitajTest("3.png");     //slike se nalaze u src\main\java\toni\files\test\og


        double[][] ulaziBrojevi  = F.calcUlazi("train/digit");
        double[][] idealBrojevi = F.calcIzlazi("train/digit");

        double[][] testUlaz  = F.calcUlazi("test/digit");


        masina(ulaziBrojevi, idealBrojevi, testUlaz);
//        loadmasina(testUlaz);

        F.ocisti();
        System.exit(0);
    }

    public static void ucitajTrening() {

        F.getDatoteke("train/og").forEach(img -> {
            try {Slike.og2bw(img,"train/og","train/bw");}
            catch (IOException e) { e.printStackTrace();}
        });

        F.getDatoteke("train/bw").forEach(img -> {
            try {Slike.splitDigits(img,"train/bw","train/digit",true);}
            catch (IOException e) { e.printStackTrace();}
        });
    }

    public static void ucitajTest(String fname) {
        try {Slike.og2bw(fname,"test/og","test/bw");}
        catch (IOException e) {e.printStackTrace();}

        try {Slike.splitDigits(fname,"test/bw","test/digit",false);}
        catch (IOException e) {e.printStackTrace();}
    }

    public static void loadmasina(double[][] test) {
        BasicNetwork mreza2 = null;
        try {mreza2= (BasicNetwork) SerializeObject.load(new File(F.put("mreza")));}
        catch (ClassNotFoundException | IOException e) {e.printStackTrace();}

        int i=test.length;
        for (double[] doubles : test) {
            assert mreza2 != null;
            MLData result = mreza2.compute(new BasicMLData(doubles));
            System.out.print(F.idx2char(F.maxAt(result.getData())));
        }
    }

    public static void masina (double[][] ulaz, double[][] ideal, double[][] test) {

        BasicNetwork mreza = new BasicNetwork();
        BasicNetwork mreza2 = new BasicNetwork();

        mreza.addLayer(new BasicLayer(null,true,DIM*DIM));
        mreza.addLayer(new BasicLayer(new ActivationSigmoid(),true,110));
        mreza.addLayer(new BasicLayer(new ActivationSigmoid(),true,12));

        mreza.getStructure().finalizeStructure();
        mreza.reset();

        MLDataSet data = new BasicMLDataSet(ulaz,ideal);
        Train uci = new Backpropagation(mreza,data);


        //učenje
        int epoh=0;
        long pocetak = System.currentTimeMillis();
        do {
            epoh++;
            uci.iteration();
            System.out.println(uci.getError()+" "+epoh);
        }
        while ((System.currentTimeMillis() - pocetak) < 30000);

        double min = 1.0d;
        for (int i=0;i<10;i++) {
            epoh++;
            uci.iteration();
            System.out.println(uci.getError()+" "+epoh);
            min = Math.min(min,uci.getError());
        }

        do {
            epoh++;
            uci.iteration();
            System.out.println(uci.getError()+" "+epoh);
        }
        while (uci.getError() > min*0.95);
        System.out.println("t: "+(System.currentTimeMillis() - pocetak)/1000.0);

        //SERIJALIZIRANJE
//        try {
//            SerializeObject.save(new File(F.put("mreza")),mreza);
//            mreza2= (BasicNetwork) SerializeObject.load(new File(F.put("mreza")));
//
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }


        for (double[] dbl : test) {
            MLData result = mreza.compute(new BasicMLData(dbl));
            System.out.print(F.idx2char(F.maxAt(result.getData())));
        }
        System.out.println("\n:)");
    }
}
