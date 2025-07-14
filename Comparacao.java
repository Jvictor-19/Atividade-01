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

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <sys/time.h>

#define TOTAL_OPERACOES 100000
#define NUM_THREADS 16
#define THREAD_SAFE 1  // coloque 0 para testar lista não thread-safe

// ---------- Estrutura da lista ligada ----------
typedef struct No {
    int valor;
    struct No* prox;
} No;

typedef struct {
    No* cabeca;
    pthread_mutex_t mutex;
} Lista;

void inicializar(Lista* lista) {
    lista->cabeca = NULL;
    pthread_mutex_init(&lista->mutex, NULL);
}

void inserir(Lista* lista, int valor) {
#if THREAD_SAFE
    pthread_mutex_lock(&lista->mutex);
#endif
    No* novo = (No*) malloc(sizeof(No));
    novo->valor = valor;
    novo->prox = lista->cabeca;
    lista->cabeca = novo;
#if THREAD_SAFE
    pthread_mutex_unlock(&lista->mutex);
#endif
}

int buscar(Lista* lista, int valor) {
#if THREAD_SAFE
    pthread_mutex_lock(&lista->mutex);
#endif
    No* atual = lista->cabeca;
    while (atual != NULL) {
        if (atual->valor == valor) {
#if THREAD_SAFE
            pthread_mutex_unlock(&lista->mutex);
#endif
            return 1;
        }
        atual = atual->prox;
    }
#if THREAD_SAFE
    pthread_mutex_unlock(&lista->mutex);
#endif
    return 0;
}

void remover(Lista* lista) {
#if THREAD_SAFE
    pthread_mutex_lock(&lista->mutex);
#endif
    if (lista->cabeca != NULL) {
        No* temp = lista->cabeca;
        lista->cabeca = lista->cabeca->prox;
        free(temp);
    }
#if THREAD_SAFE
    pthread_mutex_unlock(&lista->mutex);
#endif
}

void destruir(Lista* lista) {
    No* atual = lista->cabeca;
    while (atual != NULL) {
        No* temp = atual;
        atual = atual->prox;
        free(temp);
    }
    pthread_mutex_destroy(&lista->mutex);
}

// ---------- Medição de tempo ----------
double tempo_agora() {
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec / 1e6;
}

// ---------- Função executada por cada thread ----------
typedef struct {
    Lista* lista;
    int operacoes_por_thread;
} ArgumentoThread;

void* operacoes(void* arg) {
    ArgumentoThread* args = (ArgumentoThread*) arg;
    Lista* lista = args->lista;
    int n = args->operacoes_por_thread;

    for (int i = 0; i < n; i++) {
        int v = rand() % 1000;
        inserir(lista, v);
        buscar(lista, v);
        remover(lista);
    }
    return NULL;
}

// ---------- Função principal ----------
int main() {
    srand(time(NULL));
    Lista lista;
    inicializar(&lista);

    pthread_t threads[NUM_THREADS];
    ArgumentoThread args[NUM_THREADS];
    int ops_por_thread = TOTAL_OPERACOES / NUM_THREADS;

    double inicio = tempo_agora();

    for (int i = 0; i < NUM_THREADS; i++) {
        args[i].lista = &lista;
        args[i].operacoes_por_thread = ops_por_thread;
        pthread_create(&threads[i], NULL, operacoes, &args[i]);
    }

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    double fim = tempo_agora();

    double tempo_total = fim - inicio;
    int total_ops = TOTAL_OPERACOES * 3; // inserção + busca + remoção
    double ops_por_segundo = total_ops / tempo_total;

    printf("🔧 Modo: %s\n", THREAD_SAFE ? "Thread-Safe" : "Não Thread-Safe");
    printf("🧵 Threads: %d\n", NUM_THREADS);
    printf("⏱️ Tempo total: %.4f segundos\n", tempo_total);
    printf("⚙️ Operações por segundo: %.2f\n", ops_por_segundo);

    destruir(&lista);
    return 0;
}
