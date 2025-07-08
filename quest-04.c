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

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include <unistd.h>

#define MAX_LEITORES 10

int leitores_ativos = 0;
pthread_mutex_t mutex_contador = PTHREAD_MUTEX_INITIALIZER;
sem_t sem_escrita;

// Simula leitura
void* leitor(void* arg) {
    pthread_mutex_lock(&mutex_contador);
    leitores_ativos++;
    if (leitores_ativos == 1) sem_wait(&sem_escrita); // primeiro bloqueia a escrita
    pthread_mutex_unlock(&mutex_contador);

    printf("👀 Leitor lendo...\n");
    sleep(1);

    pthread_mutex_lock(&mutex_contador);
    leitores_ativos--;
    if (leitores_ativos == 0) sem_post(&sem_escrita); // último libera a escrita
    pthread_mutex_unlock(&mutex_contador);
    return NULL;
}

// Simula escrita
void* escritor(void* arg) {
    sem_wait(&sem_escrita);
    printf("✏️ Escritor escrevendo...\n");
    sleep(2);
    sem_post(&sem_escrita);
    return NULL;
}

int main() {
    sem_init(&sem_escrita, 0, 1);

    pthread_t threads[20];

    // Exemplo: 15 leitores e 5 escritores
    for (int i = 0; i < 15; i++) pthread_create(&threads[i], NULL, leitor, NULL);
    for (int i = 15; i < 20; i++) pthread_create(&threads[i], NULL, escritor, NULL);

    for (int i = 0; i < 20; i++) pthread_join(threads[i], NULL);

    sem_destroy(&sem_escrita);
    return 0;
}
