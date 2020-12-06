import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class Simulation {

    public static void main(String[] args) {
        Entrepot e = new Entrepot(5000);
        launchFile(e,"Simulation.txt");
    }

    private static void parse(String s, Entrepot e) throws IOException {
        try {
            String[] sTab = s.split(" ");
            switch(sTab[1]) {
                case "rien":
                    break;
                case "lot":
					/*e.recevoirLot(new Lot(Integer.valueOf(sTab[5]),
									  Double.valueOf(sTab[3]),
									  Double.valueOf(sTab[4]),
									  sTab[2]));*/
                    break;
                case "meuble":
					/*Meuble meuble = new Meuble(sTab[2],
						PieceMaison.valueOf(sTab[3]),
						Integer.valueOf(sTab[4]));
					for(int i = 5; i < sTab.length;i+=2)
						meuble.addToComposition(sTab[i],
									Integer.valueOf(sTab[i+1]));
					e.commandeMeuble(meuble);*/
            }
        }catch(Exception ex) {
            throw new IOException("File format not correct");
        }
    }

    private static void launchFile(Entrepot e, String string) {
        try {
            File f= new File(string);
            Stream<String> s = Files.lines(f.toPath());
            s.forEach( l -> {try {
                parse(l,e);
            }
            catch (IOException e1) {e1.printStackTrace();}});
        }
        catch(IOException ex) {
            ex.getStackTrace();
        }
    }



}