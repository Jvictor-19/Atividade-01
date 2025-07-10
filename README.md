# Atividade-01
1- 
Inicialmente definimos o numero de clientes máximo e o numero de barbeiros, criamos um array com 10 posição que sria a fila referente a chegada dos clientes.
Na main criamos 2 uma thread para cada barbeiro e cada barbeiro vai realizar seu trabalho indefinidamente.
na thread de gerar clientes, ela gera clientes indefinidamente e para cada novo cliente recebe um ID que é um numero crescente.
offer() verifica se tem o espaço na fila, se houver espaço ele entra, se não houver imprime que a fila está cheia e o cliente vai embora (sem bloquear o programa).
Na Classe Cliente apenas guarda o id do cliente.
A classe barbeiro executa a função run quando sua thread for iniciada. Na função run o barbeiro espera té que tenha um cliente, quando tiver um cliente exibe a mensagem de inicio do corte e espera entre 5 a 10 segundos para simular o tempo do corte e exibe a mesangem de corte finalizado

2-
list → é a lista real (um ArrayList) onde os elementos são armazenados.
lock → é um bloqueio de leitura/escrita que: Permite várias leituras simultâneas, mas só uma escrita por vez, bloqueando todas as outras.
No void add : lock.writeLock().lock() → pega o bloqueio de escrita. Outras threads que estejam lendo ou escrevendo ficam esperando. list.add(element) → adiciona o elemento à lista. finally { ... } → libera o bloqueio, mesmo que ocorra erro.
No void remove : faz a mesma coisa que o add, so que ele remove ao invéz de adicionar
No metodo get : lock.readLock().lock() → pega o bloqueio de leitura. Permite várias threads lendo ao mesmo tempo (ótimo desempenho). Mas se alguém estiver escrevendo, precisa esperar. Retorna o elemento da posição index. 
No metodo size : Apenas consulta o tamanho da lista. Usa leitura compartilhada, sem travar outras leituras.
No metodo contains : Verifica se o elemento existe. Também é uma operação de leitura, então múltiplas threads podem fazer isso ao mesmo tempo.

4-
lock: é o cadeado geral, que controla o acesso à estrutura.
podeLer: condição usada por quem quer ler, para esperar a vez.
podeEscrever: condição usada por quem quer escrever, para esperar a vez.
No metódo read: A leitura só entra se não houver escrita acontecendo e menos de 10 leitores já ativos, caso contrário, ela fica esperando com await(). Nos println é simulado a leitura e dps ele finaliza a leitura fazendo as seguintes operações: Reduz o número de leitores. Se chegou a zero, libera escritores que estavam esperando. Também libera outros leitores se ainda houver espaço (menos de 10).
No método Write: Escrita só começa se não houver NENHUM leitor nem outro escritor. Se não puder, espera com await().simula a escrita e dps finaliza a escrita fazendo a s seguintes operações: Marca que a escrita terminou. Libera um escritor que possa estar esperando. Libera todos os leitores (eles podem começar se não houver mais escrita).
Nos Métodos create, update, delete, readOnly: Cada um chama write() com a operação correspondente, exceto o readOnly() que chama read().


