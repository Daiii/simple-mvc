package cn.self.zhangbo.kernel.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过{@link ConcurrentHashMap}实现的线程安全HashSet
 *
 * @param <E> 元素
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> {

    private static final Boolean PRESENT = Boolean.TRUE;

    private final ConcurrentHashMap<E, Boolean> map;

    public ConcurrentHashSet() {
        map = new ConcurrentHashMap<>();
    }

    public ConcurrentHashSet(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }

    public ConcurrentHashSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
        map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    public ConcurrentHashSet(Iterable<E> iterable) {
        if (iterable instanceof Collection) {
            final Collection<E> collection = (Collection<E>) iterable;
            this.map = new ConcurrentHashMap<>((int) (collection.size() / 0.75f));
            this.addAll(collection);
        } else {
            this.map = new ConcurrentHashMap<>();
            for (E e : iterable) {
                this.add(e);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o).equals(PRESENT);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }
}
