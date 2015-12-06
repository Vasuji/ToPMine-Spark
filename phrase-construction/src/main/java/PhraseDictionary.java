import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.mutable.MutableLong;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by Jin on 11/18/2015.
 */

// contains phrases and words
public class PhraseDictionary implements Serializable {
    private final Map<String, Pair<Integer, Long>> phraseToIdxAndCount;
    private final Map<Integer, String> idxToPhrase;

    public PhraseDictionary(String dictFilePath) throws IOException {

        phraseToIdxAndCount = new HashMap<>();
        idxToPhrase = new HashMap<>();

        File file = new File(dictFilePath);

        try(FileInputStream fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

            String line; // TODO: Assumption: ex) "new phrase,123"
            int idx = 0;

            while((line = br.readLine()) != null) {
                String phrase = line.split(",")[0].trim().toLowerCase(); // use lower case only
                long count = Long.parseLong(line.split(",")[1].trim());
                phraseToIdxAndCount.put(phrase, Pair.of(idx, count));
                idxToPhrase.put(idx, phrase);
                idx++;
            }
        }
    }

    public PhraseDictionary(Map<String, MutableLong> phraseToCount) {
        phraseToIdxAndCount = new HashMap<>();
        idxToPhrase = new HashMap<>();

        int idx = 0;
        for(Map.Entry<String, MutableLong> entry : phraseToCount.entrySet()) {
            phraseToIdxAndCount.put(entry.getKey(), Pair.of(idx, entry.getValue().longValue()));
            idxToPhrase.put(idx, entry.getKey());
            idx++;
        }
    }

    public Integer getIdxOfPhrase(String phrase) {
        if(!phraseToIdxAndCount.containsKey(phrase)) {
            return -1;
        }
        return phraseToIdxAndCount.get(phrase).getLeft();
    }

    public Long getCountOfPhrase(String phrase) {
        if(!phraseToIdxAndCount.containsKey(phrase)) {
            return 0L;
        }
        return phraseToIdxAndCount.get(phrase).getRight();
    }

    public String getPhrase(int idx) {
        return idxToPhrase.get(idx);
    }

    public long getSize() {
        return phraseToIdxAndCount.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(); // TODO: builder may not be able to handle all entries if dictionary is too big
        for(Map.Entry<String, Pair<Integer, Long>> entry : phraseToIdxAndCount.entrySet()) {
            // ex) support vector machines,4,300
            // where index is 4 and count is 300
            builder.append(entry.getKey() + "," + entry.getValue().getLeft().toString() + "," + entry.getValue().getRight().toString());
            builder.append(System.getProperty("line.separator")); // add a new line for each entry
        }
        return builder.toString();
    }
}
