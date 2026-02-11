package silly.chemthunder.rinvenium.cca.primitive;

public interface DoubleIntComponent {
    int getDoubleIntValue1();
    int getDoubleIntValue2();
    void setDoubleIntValue1(int value);
    void setDoubleIntValue2(int value);
    void addToDoubleIntValue1(int count);
    void addToDoubleIntValue2(int count);
    void incrementDoubleIntValue1();
    void incrementDoubleIntValue2();
    void decrementDoubleIntValue1();
    void decrementDoubleIntValue2();
}
