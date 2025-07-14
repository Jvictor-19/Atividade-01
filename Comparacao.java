/*
▸Utilizando como base sua implementação thread safe do ArrayList, compare o
desempenho com a versão original que não é thread safe utilizando apenas 1 thread
▸Faça a comparação para os métodos de inserção, busca e remoção, variando o
tamanho da lista e mostrando o tempo necessário para a realizar a operação com os
tamanhos variados da lista. Adicionalmente, informe quantas operações (inserção,
busca e remoção separadamente) podem ser realizadas por segundo em ambas as
listas
▸Repita os testes mas agora utilizando 16 threads para comparar sua implementação
thread safe com a classe Vector
▸Cada thread realiza uma quantidade predefinida de operações de inserção, busca e
remoção com valores aleatórios
▸Informe os valores obtidos nos testes realizados
*/

import java.util.*;
import java.util.concurrent.*;

public class Comparacao {
    public static void main(String[] args) throws InterruptedException {
        int[] sizes = {10_000, 50_000, 100_000};

        System.out.println("==== CENÁRIO 1: SINGLE THREAD ====");
        for (int size : sizes) {
            System.out.println("\nTamanho da lista: " + size);

            System.out.println("ArrayList:");
            testSingleThreadList(new ArrayList<>(), size);

            System.out.println("ThreadSafeArrayList:");
            testSingleThreadCustom(new ThreadSafeArrayList<>(), size);
        }

        System.out.println("\n==== CENÁRIO 2: MULTI THREAD (16 threads) ====");
        int numThreads = 16;
        int opsPerThread = 10_000;

        System.out.println("\nThreadSafeArrayList:");
        testMultiThreadCustom(new ThreadSafeArrayList<>(), numThreads, opsPerThread);

        System.out.println("\nVector:");
        testMultiThreadList(new Vector<>(), numThreads, opsPerThread);
    }

    /**
     * Teste single thread para classes que estendem List (ArrayList, Vector)
     */
    private static void testSingleThreadList(List<Integer> list, int numOperations) {
        Random rand = new Random();

        // Inserção
        long start = System.nanoTime();
        for (int i = 0; i < numOperations; i++) {
            list.add(rand.nextInt());
        }
        long end = System.nanoTime();
        print("Inserção", numOperations, start, end);

        // Busca
        start = System.nanoTime();
        for (int i = 0; i < numOperations; i++) {
            list.get(rand.nextInt(list.size()));
        }
        end = System.nanoTime();
        print("Busca", numOperations, start, end);

        // Remoção
        start = System.nanoTime();
        for (int i = 0; i < numOperations; i++) {
            list.remove(list.size() - 1);
        }
        end = System.nanoTime();
        print("Remoção", numOperations, start, end);
    }

    /**
     * Teste single thread para ThreadSafeArrayList
     */
    private static void testSingleThreadCustom(ThreadSafeArrayList<Integer> list, int numOperations) {
        Random rand = new Random();

        // Inserção
        long start = System.nanoTime();
        for (int i = 0; i < numOperations; i++) {
            list.add(rand.nextInt());
        }
        long end = System.nanoTime();
        print("Inserção", numOperations, start, end);

        // Busca
        start = System.nanoTime();
        for (int i = 0; i < numOperations; i++) {
            list.get(rand.nextInt(list.size()));
        }
        end = System.nanoTime();
        print("Busca", numOperations, start, end);

        // Remoção
        start = System.nanoTime();
        for (int i = 0; i < numOperations; i++) {
            list.remove(list.size() - 1);
        }
        end = System.nanoTime();
        print("Remoção", numOperations, start, end);
    }

    /**
     * Teste multi thread para classes que implementam List (Vector)
     */
    private static void testMultiThreadList(List<Integer> list, int numThreads, int opsPerThread) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random rand = new Random();

        // Inserção
        long start = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    list.add(rand.nextInt());
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        long end = System.nanoTime();
        print("Inserção", numThreads * opsPerThread, start, end);

        // Busca
        executor = Executors.newFixedThreadPool(numThreads);
        start = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    list.get(rand.nextInt(list.size()));
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        end = System.nanoTime();
        print("Busca", numThreads * opsPerThread, start, end);

        // Remoção
        executor = Executors.newFixedThreadPool(numThreads);
        start = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                synchronized (list) {
                    for (int j = 0; j < opsPerThread; j++) {
                        if (list.size() > 0)
                            list.remove(list.size() - 1);
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        end = System.nanoTime();
        print("Remoção", numThreads * opsPerThread, start, end);
    }

    /**
     * Teste multi thread para ThreadSafeArrayList
     */
    private static void testMultiThreadCustom(ThreadSafeArrayList<Integer> list, int numThreads, int opsPerThread) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Random rand = new Random();

        // Inserção
        long start = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    list.add(rand.nextInt());
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        long end = System.nanoTime();
        print("Inserção", numThreads * opsPerThread, start, end);

        // Busca
        executor = Executors.newFixedThreadPool(numThreads);
        start = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    list.get(rand.nextInt(list.size()));
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        end = System.nanoTime();
        print("Busca", numThreads * opsPerThread, start, end);

        // Remoção
        executor = Executors.newFixedThreadPool(numThreads);
        start = System.nanoTime();
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    if (list.size() > 0)
                        list.remove(list.size() - 1);
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.MINUTES);
        end = System.nanoTime();
        print("Remoção", numThreads * opsPerThread, start, end);
    }

    /**
     * Imprime resultados formatados.
     */
    private static void print(String op, int ops, long start, long end) {
        double time = (end - start) / 1_000_000_000.0;
        double opsPerSec = ops / time;
        System.out.printf("%s: %.3f s, ops/s: %.0f\n", op, time, opsPerSec);
    }
}