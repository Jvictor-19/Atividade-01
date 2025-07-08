/*
▸O seu desafio é implementar um ArrayList que seja thread safe. Lembre-se que as
operações de consulta não causam condição de corrida umas com as outras,
entretanto, as inserções e remoções causam condição de corrida entre elas.
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct No {
    int valor;
    struct No* prox;
} No;

typedef struct {
    No* cabeca;
    pthread_mutex_t mutex;
} Lista;

// Inicializa a lista
void inicializar(Lista* lista) {
    lista->cabeca = NULL;
    pthread_mutex_init(&lista->mutex, NULL);
}

// Insere um valor na frente da lista
void inserir(Lista* lista, int valor) {
    pthread_mutex_lock(&lista->mutex);
    No* novo = (No*) malloc(sizeof(No));
    novo->valor = valor;
    novo->prox = lista->cabeca;
    lista->cabeca = novo;
    pthread_mutex_unlock(&lista->mutex);
}

// Remove o primeiro valor (se existir)
void remover(Lista* lista) {
    pthread_mutex_lock(&lista->mutex);
    if (lista->cabeca != NULL) {
        No* temp = lista->cabeca;
        lista->cabeca = lista->cabeca->prox;
        free(temp);
    }
    pthread_mutex_unlock(&lista->mutex);
}

// Busca um valor na lista
int buscar(Lista* lista, int valor) {
    pthread_mutex_lock(&lista->mutex);
    No* atual = lista->cabeca;
    while (atual != NULL) {
        if (atual->valor == valor) {
            pthread_mutex_unlock(&lista->mutex);
            return 1; // Encontrado
        }
        atual = atual->prox;
    }
    pthread_mutex_unlock(&lista->mutex);
    return 0; // Não encontrado
}

// Libera memória
void destruir(Lista* lista) {
    pthread_mutex_lock(&lista->mutex);
    No* atual = lista->cabeca;
    while (atual != NULL) {
        No* temp = atual;
        atual = atual->prox;
        free(temp);
    }
    pthread_mutex_unlock(&lista->mutex);
    pthread_mutex_destroy(&lista->mutex);
}
