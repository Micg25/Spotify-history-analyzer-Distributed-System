package common;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public record SessionDTO(List<StreamRecordDTO> currentResult, List<String> history ) implements Serializable {

public SessionDTO() {
        this(new ArrayList<>(), new ArrayList<>());
    }
}
