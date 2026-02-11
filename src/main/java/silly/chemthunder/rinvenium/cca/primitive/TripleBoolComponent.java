package silly.chemthunder.rinvenium.cca.primitive;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface TripleBoolComponent extends Component {
    boolean getTripleBoolValue1();
    boolean getTripleBoolValue2();
    boolean getTripleBoolValue3();
    void setTripleBoolValue1(boolean value);
    void setTripleBoolValue2(boolean value);
    void setTripleBoolValue3(boolean value);
}
