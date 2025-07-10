# Atividade-01
1- inicialmente definimos o numero de clientes máximo e o numero de barbeiros, criamos um array com 10 posição que sria a fila referente a chegada dos clientes.
Na main criamos 2 uma thread para cada barbeiro e cada barbeiro vai realizar seu trabalho indefinidamente.
na thread de gerar clientes, ela gera clientes indefinidamente e para cada novo cliente recebe um ID que é um numero crescente.
offer() verifica se tem o espaço na fila, se houver espaço ele entra, se não houver imprime que a fila está cheia e o cliente vai embora (sem bloquear o programa).
Na Classe Cliente apenas guarda o id do cliente.
A classe barbeiro executa a função run quando sua thread for iniciada. Na função run o barbeiro espera té que tenha um cliente, quando tiver um cliente exibe a mensagem de inicio do corte e espera entre 5 a 10 segundos para simular o tempo do corte e exibe a mesangem de corte finalizado
