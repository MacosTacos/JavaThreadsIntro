package threads.sync.buffer;

public class BoundedBuffer<T> {
    private final T[] buffer;
    private int count = 0;
    private int in = 0;
    private int out = 0;

    @SuppressWarnings("unchecked")
    public BoundedBuffer(int size) {
        buffer = (T[]) new Object[size];
    }

    public synchronized void put(T item) throws InterruptedException {
        while (count == buffer.length) {
            System.out.printf("tried to put %d, waiting\n", item);
            wait();
        }
        buffer[count++] = item;
        System.out.printf("putting %d:\n", item);
        printBuffer();
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        T item;
        while (count == 0) {
            System.out.printf("tried to take, waiting\n");
            wait();
        }
        System.out.println("TAKING");
        printBuffer();
        item = buffer[count-1];
        buffer[--count] = null;
        System.out.println("AFTER TAKING");
        printBuffer();
        //System.out.printf("taking %d, last item: %d\n", item, buffer[out]);
        notifyAll();
        return item;
    }

    private void printBuffer() {
        for (int i = 0; i < buffer.length; i++) {
            System.out.printf("%d ", buffer[i]);
        }
        System.out.println();
    }
}

