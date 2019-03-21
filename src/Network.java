import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Network {
    // lista K perceptronów - "neurony"
    private List<Perceptron> m_neurons = new ArrayList<>();

    // mapa - język => lista stosunków liter do całości
    private Map<String, List<List<Double>>> m_ratios = new HashMap<>();

    // lista klas
    private List<String> m_labels = new ArrayList<>();

    public Network(String trainingDataPath, double learningRate, boolean randomWeights) throws IOException {
        File mainDirectory = new File(trainingDataPath);
        File[] languages = mainDirectory.listFiles((current, name) -> new File(current, name).isDirectory());

        for(File language : languages) {
            m_labels.add(language.getName());

            File[] trainingData = language.listFiles((current, name) -> name.contains(".txt") && new File(current, name).isFile());

            List<List<Double>> ratios = new ArrayList<>();
            for(File train : trainingData) {
                String file = Files.readString(Paths.get(train.getPath()));
                ratios.add(this.getRatios(file));
            }

            m_ratios.put(language.getName(), ratios);
        }

        for(int neuron = 0; neuron < m_labels.size(); neuron++) {
            Perceptron perceptron = new Perceptron(learningRate, randomWeights);
            perceptron.ID = m_labels.get(neuron);

            m_neurons.add(perceptron);
        }

        // dodanie datasetu do zestawu treningowego kazdego perceptronu
        for(Perceptron neuron : m_neurons) {
            // trenowanie perceptronu na wszystkich klasach, ale wynik = 1 tylko dla jego własnej
            for(Map.Entry<String, List<List<Double>>> dataset : m_ratios.entrySet()) {
                int output = dataset.getKey().equals(neuron.ID) ? 1 : 0;

                List<List<Double>> data = dataset.getValue();

                // trenuj własną klasę prceptrona <ilość klas + 1> razy
                // dla zwiększenia dokładności
                int limit = data.size() * (output * (m_ratios.size() + 1));
                for (int i = 0; i < limit; i++) {
                    neuron.train(data.get(i % data.size()), output);
                }
            }
        }
    }

    private List<Double> getRatios(String file) {
        List<Double> result = new ArrayList<>(26);

        Map<Character, Integer> counts = new LinkedHashMap<>();
        int countAll = 0;
        for(int i = 0; i < file.length(); i++) {
            char c = file.charAt(i);
            if(c >= 'a' && c <= 'z') c -= 32;

            // policz tylko znaki A-Z
            if(!(c >= 'A' && c <= 'Z')) continue;

            countAll++;
            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        Map<Character, Integer> sortedCounts = new TreeMap<>(counts);
        for(char ch = 'A'; ch <= 'Z'; ch++) {
            result.add((double)sortedCounts.getOrDefault(ch, 0) / countAll);
        }

        return result;
    }

    public boolean train() {
        boolean trained = false;

        int maxIterations = 100000;
        while(maxIterations-- > 0 && trained) {
            boolean success = true;
            for(Perceptron neuron : m_neurons) {
                success = neuron.retrain() && success;
            }

            if(success) trained = true;
        }

        return trained;
    }

    public String predict(String text) {
        List<Double> ratios = getRatios(text);

        List<Double> predictions = new ArrayList<>();
        for(Perceptron neuron : m_neurons) {
            predictions.add(neuron.execute(ratios, true));
        }

        double max = predictions.get(0);
        for (double x : predictions) {
            if (x > max) {
                max = x;
            }
        }

        return m_labels.get(predictions.indexOf(max));
    }
}
