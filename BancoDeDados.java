/*
▸Na implementação de um Banco de Dados, há uma restrição para que no máximo 10
consultas sejam realizadas simultaneamente, ao passo que apenas 1 operação de
escrita (insert, update ou delete) pode ocorrer simultaneamente.
▸Caso uma 11a consulta tente ser realizada, ela deve ser bloqueada até que alguma
consulta finalize
▸No momento da operação de escrita, não pode haver consultas no banco de dados
▸Implemente uma classe que discipline o acesso ao Banco de dados
▹Implemente as 4 operações CRUD (Create, Read, Update e Delete)
▸Crie um programa para testar e mostrar o funcionamento da(s) sua(s) classe(s)
*/

import java.util.concurrent.locks.*;

public class BancoDeDados {
    private final Lock lock = new ReentrantLock();
    private final Condition podeLer = lock.newCondition();
    private final Condition podeEscrever = lock.newCondition();

    private int leitoresAtivos = 0;
    private boolean escrevendo = false;


    public void read(int idThread) throws InterruptedException {
        lock.lock();
        
        try {
            while (escrevendo || leitoresAtivos >= 10) {
                podeLer.await();
            }
            leitoresAtivos++;
        } finally {
            lock.unlock();
        }

        System.out.println("Thread " + idThread + " lendo...");
        Thread.sleep(1000); // simula tempo de leitura
        System.out.println("Thread " + idThread + " terminou leitura.");

        lock.lock();
        try {
            leitoresAtivos--;
            if (leitoresAtivos == 0) {
                podeEscrever.signal();
            }
            podeLer.signal(); // libera próxima leitura (se for < 10)
        } finally {
            lock.unlock();
        }
    }

    public void write(int idThread, String operacao) throws InterruptedException {
        lock.lock();
        try {
            while (escrevendo || leitoresAtivos > 0) {
                podeEscrever.await();
            }
            escrevendo = true;
        } finally {
            lock.unlock();
        }

        System.out.println("Thread " + idThread + " escrevendo (" + operacao + ")...");
        Thread.sleep(1500);
        System.out.println("Thread " + idThread + " terminou " + operacao + ".");

        lock.lock();
        try {
            escrevendo = false;
            podeEscrever.signal();
            podeLer.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void create(int idThread) throws InterruptedException {
        write(idThread, "CREATE");
    }

    public void update(int idThread) throws InterruptedException {
        write(idThread, "UPDATE");
    }

    public void delete(int idThread) throws InterruptedException {
        write(idThread, "DELETE");
    }

    public void readOnly(int idThread) throws InterruptedException {
        read(idThread);
    }
}
