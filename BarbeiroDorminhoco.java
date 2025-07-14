/*
▸Implemente o problema do Barbeiro Dorminhoco
▸Imagine que:
▹Existem 2 barbeiros
▹A fila pode ter no máximo 10 clientes esperando
▹Caso o cliente chegue e a fila esteja cheia, exiba uma mensagem informativa

▸Modele o seu programa para que
▹O corte de cabelo de um cliente demore um tempo aleatório entre 5s e 10s
▹Um novo cliente chegue aleatoriamente entre 4s e 6s
*/

import java.util.Random;
import java.util.concurrent.*;

public class BarbeiroDorminhoco {
    private static final int NUM_CADEIRAS = 10;
    private static final int NUM_BARBEIROS = 2;

    private static BlockingQueue<Cliente> filaClientes = new ArrayBlockingQueue<>(NUM_CADEIRAS);
    private static Random random = new Random();

    public static void main(String[] args) {
        // Inicia os barbeiros
        for (int i = 1; i <= NUM_BARBEIROS; i++) {
            new Thread(new Barbeiro(i)).start();
        }

        // Inicia os clientes chegando aleatoriamente
        new Thread(() -> {
            int idCliente = 1;
            while (true) {
                try {
                    Thread.sleep((4 + random.nextInt(3)) * 1000); // 4 a 6 segundos
                    Cliente cliente = new Cliente(idCliente++);

                    if (filaClientes.offer(cliente)) {
                        System.out.println("Cliente " + cliente.id + " entrou na fila.");
                    } else {
                        System.out.println("Fila cheia. Cliente " + cliente.id + " foi embora.");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Classe do cliente
    static class Cliente {
        int id;
        Cliente(int id) {
            this.id = id;
        }
    }

    // Classe do barbeiro
    static class Barbeiro implements Runnable {
        int id;

        Barbeiro(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Cliente cliente = filaClientes.take(); // Espera por um cliente
                    System.out.println("Barbeiro " + id + " começou a cortar o cabelo do Cliente " + cliente.id);
                    Thread.sleep((5 + random.nextInt(6)) * 1000); // 5 a 10 segundos
                    System.out.println("Barbeiro " + id + " terminou o corte do Cliente " + cliente.id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

    sem_destroy(&filled_slots);

    return 0;
}
