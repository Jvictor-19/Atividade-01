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

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>

#define MAX_CLIENTES 10
#define NUM_BARBEIROS 2

typedef struct {
    int id;
} Cliente;

Cliente fila[MAX_CLIENTES];
int inicio = 0;
int fim = 0;
int total_clientes = 0;

sem_t empty_slots;        // Vagas disponíveis na fila
sem_t filled_slots;       // Clientes esperando
pthread_mutex_t mutex_fila;

int cliente_id = 1;

// Função que simula o corte de cabelo
void cortar_cabelo(int barbeiro_id, Cliente cliente) {
    int tempo = 5 + rand() % 6; // 5 a 10 segundos
    printf("🧔‍♂️ Barbeiro %d está cortando o cabelo do Cliente %d (tempo: %ds)\n", barbeiro_id, cliente.id, tempo);
    sleep(tempo);
    printf("✅ Barbeiro %d terminou o corte do Cliente %d\n", barbeiro_id, cliente.id);
}

// Thread do barbeiro (consumidor)
void* barbeiro_thread(void* arg) {
    int barbeiro_id = *((int*) arg);
    while (1) {
        sem_wait(&filled_slots); // Espera cliente
        pthread_mutex_lock(&mutex_fila);

        Cliente cliente = fila[inicio];
        inicio = (inicio + 1) % MAX_CLIENTES;
        total_clientes--;

        pthread_mutex_unlock(&mutex_fila);
        sem_post(&empty_slots); // Libera vaga na fila

        cortar_cabelo(barbeiro_id, cliente);
    }
    pthread_exit(NULL);
}

// Thread geradora de clientes
void* cliente_thread(void* arg) {
    while (1) {
        sleep(4 + rand() % 3); // 4 a 6 segundos

        Cliente novo_cliente;
        novo_cliente.id = cliente_id++;

        if (sem_trywait(&empty_slots) == 0) {
            pthread_mutex_lock(&mutex_fila);

            fila[fim] = novo_cliente;
            fim = (fim + 1) % MAX_CLIENTES;
            total_clientes++;

            printf("👤 Cliente %d entrou na fila. Total na fila: %d\n", novo_cliente.id, total_clientes);

            pthread_mutex_unlock(&mutex_fila);
            sem_post(&filled_slots);
        } else {
            printf("❌ Cliente %d foi embora: fila cheia!\n", novo_cliente.id);
        }
    }
    pthread_exit(NULL);
}

int main() {
    srand(time(NULL));

    pthread_t barbeiros[NUM_BARBEIROS];
    pthread_t gerador_clientes;
    int ids[NUM_BARBEIROS];

    // Inicializa mutex e semáforos
    pthread_mutex_init(&mutex_fila, NULL);
    sem_init(&empty_slots, 0, MAX_CLIENTES);
    sem_init(&filled_slots, 0, 0);

    // Cria threads dos barbeiros
    for (int i = 0; i < NUM_BARBEIROS; i++) {
        ids[i] = i + 1;
        pthread_create(&barbeiros[i], NULL, barbeiro_thread, &ids[i]);
    }

    // Cria thread que gera novos clientes
    pthread_create(&gerador_clientes, NULL, cliente_thread, NULL);

    // Mantém o programa principal vivo (infinito)
    pthread_join(gerador_clientes, NULL);
    for (int i = 0; i < NUM_BARBEIROS; i++) {
        pthread_join(barbeiros[i], NULL);
    }

    // Libera recursos (nunca chega aqui, mas é boa prática)
    pthread_mutex_destroy(&mutex_fila);
    sem_destroy(&empty_slots);
    sem_destroy(&filled_slots);

    return 0;
}
