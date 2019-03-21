import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String text = "Republika Francuska jest unitarnym państwem demokratycznym, w którym ważną rolę odgrywa prezydent. Jest również piątym spośród najlepiej rozwiniętych krajów świata i jedenastym w rankingu warunków życia. Najważniejsze ideały Francji sformułowane zostały w Deklaracji Praw Człowieka i Obywatela i w występującym na drukach urzędowych i monetach haśle rewolucji francuskiej liberté, égalité, fraternité („wolność, równość, braterstwo”). Kraj należy do grona założycieli Unii Europejskiej. Ma największą powierzchnię spośród państw Wspólnoty. Francja jest także członkiem-założycielem Organizacji Narodów Zjednoczonych oraz wchodzi w skład m.in. Frankofonii, G7 oraz Unii Łacińskiej. Jest stałym członkiem Rady Bezpieczeństwa ONZ, w której posiada prawo weta.\n" +
                "Francja ze względu na swoją liczbę ludności, potencjał gospodarczy, pozycję w Europie, wpływy w dawnych koloniach, silną armię (trzeci po Rosji i USA arsenał nuklearny), uchodzi za jedno z najpotężniejszych państw świata.";

        Network n = new Network("C:\\Users\\stern\\IdeaProjects\\NAI3\\data\\", 1, false);
        n.train();

        String prediction = n.predict(text);
        System.out.println(prediction);
    }
}
