import com.sun.org.apache.xpath.internal.operations.String;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Melanie Erb on 11.10.2016.
 */
public class Spam extends AbstractMap<String , Double> implements Map<String, Double>{

    private String[] keys;
    private Double[] values;
    private int index = 0;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    public Spam() {
        keys = new String[MAX_ARRAY_SIZE];
        values = new Double[MAX_ARRAY_SIZE];
    }

    @Override
    public Double put(String key, Double value) {
        if(containsKey(key)){
            for (int i = 0; i <= index; i++) {
                if(key == keys[i]){
                    values[i]+=value;
                    return values[i];
                }
            }
        } else {
            keys[index] = key;
            values[index] = value;
            index++;
        }
        return value;
    }

    @Override
    public Set<Entry<String, Double>> entrySet() {

        return null;
    }

    @Override
    public boolean containsValue(Object value) {
        Iterator<Entry<String ,Double>> i = entrySet().iterator();
        if (value!=null) {
            while (i.hasNext()) {
                Entry<String, Double> e = i.next();
                if (value.equals(e.getValue()))
                    return true;
            }
        }
        return false;
    }


}
