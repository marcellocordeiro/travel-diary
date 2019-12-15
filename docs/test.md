# Testes
Fizemos testes utilizando o JUnit para os métodos do banco de dados. Os testes se encontram em `src/androidTest/java/com/myapp/traveldiary`.
Como estamos utilizando LiveData para coletar os dados, foi preciso criar um método chamado `blockingQueue` que espera pelo dado
antes de retornar. Dessa maneira conseguimos testar o retorno dos métodos do banco.
