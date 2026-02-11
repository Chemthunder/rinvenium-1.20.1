package silly.chemthunder.rinvenium.cca.primitive;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface IntComponent extends Component {
    int getInt();
    void setInt(int value);
    void addValueToInt(int count);
    void incrementInt();
    void decrementInt();
}
