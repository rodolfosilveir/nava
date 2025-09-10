# Transactions

`Microserviço responsável pelo gerenciamento roteiros de filmes da Cooperfilme`

# Pré Requisitos

Para que seja possível rodar essa aplicação é necessário atender alguns requisitos básicos.

- JDK 21 LTS
- Gradle 8.8+
- PostgreSQL 14+

# Arquivos de documentação

- Cooperfilmes.postman_collection.json (Com todas as requisições para a API)
- Case Java-Angular-Full-Stack.pdf (Teste recebido)

# Compilando para IDEAs como IntelliJ

Assim como todo projeto *Gradle*, é necessário primeiramente realizarmos a geração dos fontes. Conforme o exemplo abaixo:

```bash
./gradlew clean build
```

# Compilando e inicializando com Docker

Executar os comandos docker abaixo

```bash
docker compose build
docker compose up
```

# Documentação Swagger

É possivel acessar os endpoints disponiveis para visualização no seguinte endereço, depois que o container estiver rodando

http://localhost:8080/swagger-ui/index.html#/

Depois, na sessão 'Usuarios', executar a operação de Login, e com o token, utlizar no botão 'Authorize' no canto superior direito
Colar o token no campo 'Value', clica em 'Authorize' e depois 'Close'
A partir desse momento, qualquer operação da aplicação estará autenticada e poderá ser executada

# Executando e testando com Postman

Existe um arquivo postman 'Cooperfilmes.postman_collection.json' na raiz do projeto com todas as operações disponiveis
É necessario autenticar com dos usuarios que ja estão configurado no ambiente, pegar o token e setar nas demais requisições como Bearer

# cURLs de exemplo

- Logins (Utilizar o token gerado no campo header Authorization substituindo ••••••)
```bash
curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "ana.analista@cooperfilme.com",
    "password": "ana.analista"
}'

curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "renato.revisor@cooperfilme.com",
    "password": "renato.revisor"
}'

curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "aparecido.aprovador@cooperfilme.com",
    "password": "aparecido.aprovador"
}'

curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "apolo.aprovador@cooperfilme.com",
    "password": "apolo.aprovador"
}'

curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "pedro.aprovador@cooperfilme.com",
    "password": "pedro.aprovador"
}'

curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "cintia.cliente@client.com",
    "password": "cintia.cliente"
}'

curl --location 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "login": "clara.cliente@client.com",
    "password": "clara.cliente"
}'
```

- Criando um roteiro de exemplo
```bash
curl --location 'http://localhost:8080/script' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6ImNsYXJhLmNsaWVudGVAY2xpZW50LmNvbSIsImV4cCI6MTc1NzUzMTY5OH0.LqLXxgJNMBJ9tZ9lYDQMOVjo8ZqmEc7NLh7J6cSbiXw' \
--data '{
  "name": "O velho oeste",
  "content": "Então os xerifes apontaram a arma e atiraram."
}'
```

- Obtendo os dados do roteiro
```bash
curl --location 'http://localhost:8080/script' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6ImFwYXJlY2lkby5hcHJvdmFkb3JAY29vcGVyZmlsbWUuY29tIiwiZXhwIjoxNzU3NTM1Mjc4fQ.KIP72FpvZZQgpr4htLDaxT2OTPz6m3vhS6Da_WGnvPg'
```

