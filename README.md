# Rozwiązanie zadania

## Budowanie

Projekt można zbudować wykorzystując maven:
```
mvn assembly\:assembly
```
Można go również zbudować z wykorzystaniem wrapperów mavena.

Linux:
```
./mvnw assembly\:assembly 
```

# Uruchamianie
Zakładając, że jesteśmy w głównym folderze
```
java -jar target/pretiuszadanie-jar-with-dependencies.jar 
```