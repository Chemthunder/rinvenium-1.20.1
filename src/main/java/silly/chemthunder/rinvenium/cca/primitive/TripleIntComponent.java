package silly.chemthunder.rinvenium.cca.primitive;

public interface TripleIntComponent {
    int getTripleIntValue1();
    int getTripleIntValue2();
    int getTripleIntValue3();
    void setTripleIntValue1(int value);
    void setTripleIntValue2(int value);
    void setTripleIntValue3(int value);
    void addToTripleIntValue1(int count);
    void addToTripleIntValue2(int count);
    void addToTripleIntValue3(int count);
    void incrementTripleIntValue1();
    void incrementTripleIntValue2();
    void incrementTripleIntValue3();
    void decrementTripleIntValue1();
    void decrementTripleIntValue2();
    void decrementTripleIntValue3();
}