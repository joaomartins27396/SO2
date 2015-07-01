#compilar os .java
all: 
	javac -d classes src/*.java

		
#serviço de nomes
reg:
	rmiregistry -J-classpath -Jclasses:. 9000 


#executar servidor	
rs:
	java -classpath classes:lib/postgresql.jar so2trabalho/AtendePedido

#executa client

rc:
	java -classpath classes so2trabalho/JanelaCliente
