package Parser;

/**
 * A pair
 *
 * @param <K> Key of the pair
 * @param <V> Value of the pair
 */
public class Pair<K, V> {
    private K key;
    private V value;

    /**
     * Constructor of the Pair.
     *
     * @param key   key
     * @param value value
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the key
     *
     * @return K
     */
    public K getKey() {
        return this.key;
    }

    /**
     * Get the value
     *
     * @return V
     */
    public V getValue() {
        return this.value;
    }
}
