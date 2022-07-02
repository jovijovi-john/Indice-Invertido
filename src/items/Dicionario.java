package items;

public interface Dicionario<Key, Value> {
    public Value get(Key key);
    public void put(Key key, Value value);
}