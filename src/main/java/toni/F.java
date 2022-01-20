package toni;

import java.io.File;
import java.nio.file.Path;


public class F {

    static public String put (String file) {
        return String.valueOf(Path.of(System.getProperty("user.dir")+Path.of("\\src\\main\\java\\toni\\inputs\\"+file)));
    }

    static public String[] getDatoteke () {
        File f = new File(String.valueOf(put("")));
        return f.list();
    }
}
