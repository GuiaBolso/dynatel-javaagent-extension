# Dynatel - Dynatrace OpenTelemetry javaagent extension
=========================

![Maven Central](https://img.shields.io/maven-central/v/com.picpay/dynatel-javaagent-extension)

### Funcionalidades

A única funcionalidade dessa extensão do open telemetry javaagent é fazer a leitura dos metadados fornecidos pelo Dynatrace OneAgent e injeta-los
como atributos nos recursos que serão criados pelo OpenTelemetrySDK, dessa forma, fazendo um [enriquecimento de métrica e traces](https://www.dynatrace.com/support/help/extend-dynatrace/extend-data).

### Requisitos
- Sua aplicação precisa está usando o [javaagent do opentelemetry](https://opentelemetry.io/docs/instrumentation/java/automatic/)

### Habilitando a extensão
Para ativar a extensão para ativar a propriedade `-Dotel.javaagent.extensions=<<PATH ABSOLUTO DO JAR DESSA EXTENSÃO>>`.
