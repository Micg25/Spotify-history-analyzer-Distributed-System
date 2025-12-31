package common;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public record SessionDTO(List<StreamRecordDTO> currentResult, Map<String, String> history ) implements Serializable {

public SessionDTO() {
        this(new ArrayList<>(), new HashMap<>());
    }
}
