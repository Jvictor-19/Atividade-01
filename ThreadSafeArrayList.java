/*
▸O seu desafio é implementar um ArrayList que seja thread safe. Lembre-se que as
operações de consulta não causam condição de corrida umas com as outras,
entretanto, as inserções e remoções causam condição de corrida entre elas.
*/

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeArrayList<T> {
    private final List<T> list = new ArrayList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // Adiciona um elemento com trava de escrita
    public void add(T element) {
        lock.writeLock().lock();
        try {
            list.add(element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Remove um elemento com trava de escrita
    public void remove(T element) {
        lock.writeLock().lock();
        try {
            list.remove(element);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Consulta segura com trava de leitura
    public T get(int index) {
        lock.readLock().lock();
        try {
            return list.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    // Tamanho da lista (leitura)
    public int size() {
        lock.readLock().lock();
        try {
            return list.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    // Verifica se contém um elemento (leitura)
    public boolean contains(T element) {
        lock.readLock().lock();
        try {
            return list.contains(element);
        } finally {
            lock.readLock().unlock();
        }
    }
}
