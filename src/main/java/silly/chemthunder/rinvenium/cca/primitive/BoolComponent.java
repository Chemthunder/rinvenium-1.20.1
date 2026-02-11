package silly.chemthunder.rinvenium.cca.primitive;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface BoolComponent extends Component {
    boolean getBool();
    void setBool(boolean value);
}
