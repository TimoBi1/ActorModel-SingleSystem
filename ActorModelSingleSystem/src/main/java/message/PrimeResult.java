package message;

import java.util.ArrayList;
import java.util.List;

public class PrimeResult {

    private List<Long> results = new ArrayList<>();

    public PrimeResult(List<Long> results) {
        this.results = results;
    }

    public List<Long> getResults() {
        return results;
    }


}
