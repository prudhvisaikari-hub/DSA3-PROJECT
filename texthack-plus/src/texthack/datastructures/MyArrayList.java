package texthack.datastructures;

/**
 * A minimal generic dynamic array, written from scratch to avoid
 * java.util.ArrayList. Doubles capacity on overflow, halves on
 * heavy underuse to keep memory bounded.
 */
public class MyArrayList<T> {

    private Object[] data;
    private int size;

    public MyArrayList() {
        data = new Object[8];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) return;
        int newCapacity = data.length * 2;
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }

    public void add(T item) {
        ensureCapacity(size + 1);
        data[size++] = item;
    }

    public void add(int index, T item) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = item;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        return (T) data[index];
    }

    public void set(int index, T item) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        data[index] = item;
    }

    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        T removed = (T) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) System.arraycopy(data, index + 1, data, index, numMoved);
        data[--size] = null;
        return removed;
    }

    public boolean contains(T item) {
        for (int i = 0; i < size; i++) {
            if (data[i] == null ? item == null : data[i].equals(item)) return true;
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < size; i++) data[i] = null;
        size = 0;
    }

    /** Simple in-place quicksort using a supplied comparator, no java.util.Comparator dependency. */
    public interface Cmp<T> {
        int compare(T a, T b);
    }

    public void sort(Cmp<T> cmp) {
        quicksort(0, size - 1, cmp);
    }

    private void quicksort(int lo, int hi, Cmp<T> cmp) {
        if (lo >= hi) return;
        T pivot = get((lo + hi) / 2);
        int i = lo, j = hi;
        while (i <= j) {
            while (cmp.compare(get(i), pivot) < 0) i++;
            while (cmp.compare(get(j), pivot) > 0) j--;
            if (i <= j) {
                T tmp = get(i);
                set(i, get(j));
                set(j, tmp);
                i++;
                j--;
            }
        }
        if (lo < j) quicksort(lo, j, cmp);
        if (i < hi) quicksort(i, hi, cmp);
    }

    @SuppressWarnings("unchecked")
    public T[] toArray(T[] template) {
        // caller supplies a correctly-typed zero-length array, e.g. new String[0]
        Object arrObj = java.lang.reflect.Array.newInstance(template.getClass().getComponentType(), size);
        Object[] result = (Object[]) arrObj;
        for (int i = 0; i < size; i++) result[i] = data[i];
        return (T[]) result;
    }
}
