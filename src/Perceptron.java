import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Perceptron {
    // ID - dla ułatwienia uczenia
    public String ID = "";

    // W - wagi
    private List<Double> m_weights = new ArrayList<>();

    // α - parametr (stała uczenia) wpływający na intensywność uczenia
    double m_learningRate = 0.1;

    // czy wagi mają być losowe
    private boolean m_randomWeights = true;

    class Data {
        public List<Double> d_inputs;
        public int d_expected;
    }
    // lista danych do uczenia
    private List<Data> m_data = new ArrayList<>();

    public Perceptron(double learningRate, boolean randomWeights) {
        m_learningRate = learningRate;
        m_randomWeights = randomWeights;
    }

    public boolean train(List<Double> inputs, int expected) {
        // uzupełnienie wag losowymi liczbami (0, 1) do ilości wejść
        while(m_weights.size() < inputs.size()) {
            m_weights.add(m_randomWeights ? Math.random() : 0);
        }

        double result = execute(inputs, false);
        m_data.add(new Data(){{
            d_inputs = inputs;
            d_expected = expected;
        }});

        if(result == expected) {
            return true;
        } else {
            for(int i = 0; i < m_weights.size(); i++) {
                double input = inputs.get(i);

                adjust(result, expected, input, i);
            }
            return false;
        }
    }

    public boolean retrain() {
        Collections.shuffle(m_data);

        int size = m_data.size();
        boolean success = true;

        for(int i = 0; i < size; i++) {
            Data training = m_data.get(0);
            m_data.remove(0);

            success = train(training.d_inputs, training.d_expected) && success;
        }

        return success;
    }

    private void adjust(double y, double d, double X, int index) {
        double del = delta(y, d, X);

        // W' = W + (d − y)αX
        m_weights.set(index, m_weights.get(index) + del);
    }

    private double delta(double y, double d, double X) {
        // (d − y)αX
        return (d - y) * m_learningRate * X;
    }

    public double execute(List<Double> inputs, boolean raw) {
        double result = 0;

        // Σⁿₗ₌₁ = wₗ * xₗ = WᵀX
        for(int i = 0; i < inputs.size(); i++) {
            result += inputs.get(i) * m_weights.get(i);
        }

        if(raw) return result;

        // 1 if     WᵀX >= 0.5
        // 0 else
        return result >= 0.5 ? 1 : 0;
    }
}