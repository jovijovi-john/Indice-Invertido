package items;

public interface Item<Key, Value> {
    public Value get(Key key);
    public void put(Key key, Value value);
}