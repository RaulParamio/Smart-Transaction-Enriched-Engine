# ETAPA 1: Construcción
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos absolutamente todo el contenido de tu carpeta actual
COPY . .

# Truco: Buscamos dónde está el pom.xml y compilamos desde ahí
RUN find . -name "pom.xml" -exec dirname {} \; > project_dir.txt \
    && cd $(cat project_dir.txt) \
    && mvn clean package -DskipTests

# ETAPA 2: Ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el .jar buscando en cualquier carpeta target que se haya creado
COPY --from=build /app/**/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]