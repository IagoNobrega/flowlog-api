# Flowlog API

Este projeto é um backend Spring Boot para registrar blocos de foco e gerar um diagnóstico de produtividade.

## Endpoints

- `POST /registro-foco`
  - Registra um bloco de trabalho.
  - Campos obrigatórios:
    - `nivelFoco`: inteiro entre 1 e 5
    - `tempoMinutos`: inteiro maior que 0
    - `comentario`: texto que descreve o que foi feito ou o que causou distração
  - Campos opcionais:
    - `categoria`: exemplo `coding`, `reunião`, `estudo`
    - `registradoEm`: data e hora do registro no formato `yyyy-MM-dd'T'HH:mm:ss`

- `GET /diagnostico-produtividade`
  - Retorna:
    - média do nível de foco
    - tempo total focado
    - total de registros
    - feedback automático baseado nos dados

## Como executar

Este projeto usa Java 17. Execute este comando a partir do diretório do projeto (onde está o `pom.xml`):

```bash
./mvnw spring-boot:run
```

Se você estiver na pasta pai do projeto, navegue para dentro primeiro:

```bash
cd com.iago.flowlog
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## Exemplos

### Registrar foco

```bash
curl -X POST http://localhost:8080/registro-foco \
  -H "Content-Type: application/json" \
  -d '{"nivelFoco":5,"tempoMinutos":45,"comentario":"Sessão de foco intensa","categoria":"coding"}'
```

### Obter diagnóstico

```bash
curl http://localhost:8080/diagnostico-produtividade
```

## Observações

- O projeto usa H2 em memória para persistência simples.
- Validações retornam `400 Bad Request` quando o payload é inválido.
