package silly.chemthunder.rinvenium.cca.primitive;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface DoubleBoolComponent extends Component {
    boolean getDoubleBoolValue1();
    boolean getDoubleBoolValue2();
    void setDoubleBoolValue1(boolean value);
    void setDoubleBoolValue2(boolean value);
}
