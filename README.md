# ADA TEST 


## Tecnologías usadas
- Java version 17.0.7  

- Spring Boot 3.0.1   

- Oracle 12c  

- SonarLint  


![Static Badge](https://img.shields.io/badge/Java%2017%20-blue) 
![Static Badge](https://img.shields.io/badge/Spring%20Boot%203.1.0%20-green) 
![Static Badge](https://img.shields.io/badge/Oracle%2012c-red)
![Static Badge](https://img.shields.io/badge/SonarLint-blue)

## ARQUITECTURA DEL PROYECTO
- Modular con principios SOLID



## Modelo de datos

![image](https://github.com/Stivennevits/ada-test/assets/108912463/7fa273c2-d589-4a9e-9e0c-6f001b13ee05)
- Modelo para ejecutar el procedimiento almacenado
![image](https://github.com/Stivennevits/ada-test/assets/108912463/bf98d8cd-42e7-4a46-aec7-4530fcbebde0)

## Scripts de creación de tablas

- Crear la tabla COMPANY
 ```sql 
 CREATE TABLE COMPANY (
    ID NUMBER(38) PRIMARY KEY NOT NULL,
    CODE VARCHAR2(10) UNIQUE NOT NULL,
    NAME VARCHAR2(25),
    DESCRIPTION VARCHAR2(50)
);
```
- Crear tabla APPLICATION
```sql
 CREATE TABLE APPLICATION (
    ID NUMBER(38) PRIMARY KEY NOT NULL,
    CODE VARCHAR2(10) UNIQUE,
    NAME VARCHAR2(25),
    DESCRIPTION VARCHAR2(50),
    CREATED_AT DATE,
    CREATED_BY VARCHAR2(15)
)

```

- Crear tabla VERSION
```sql
    CREATE TABLE VERSION (
    ID NUMBER(38) NOT NULL PRIMARY KEY,
    APP_ID NUMBER(38),
    VERSION VARCHAR2(255 CHAR),
    DESCRIPTION VARCHAR2(255 CHAR),
    CONSTRAINT VERSION_pk UNIQUE (APP_ID, VERSION),
    CONSTRAINT VERSION_APPLICATION_ID_APP_fk FOREIGN KEY (APP_ID) REFERENCES APPLICATION(ID)
);

```

- Crear la tabla COMPANY_VERSION
```sql
    CREATE TABLE COMPANY_VERSION (
    ID NUMBER(38) NOT NULL PRIMARY KEY,
    COMPANY_ID NUMBER(38),
    VERSION_ID NUMBER(38),
    VERSION_COMPANY_DESCRIPTION VARCHAR2(50),
    CONSTRAINT COMPANY_VERSION_pk UNIQUE (COMPANY_ID, VERSION_ID),
    CONSTRAINT COMPANY_VERSION_COMPANY_ID_COMPANY_fk FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID),
    CONSTRAINT COMPANY_VERSION_VERSION_ID_VERSION_fk FOREIGN KEY (VERSION_ID) REFERENCES VERSION(ID)
);
```
- Crear la tabla TMP_LLENAR_CAMPOS
```sql
    CREATE TABLE TMP_LLENAR_CAMPOS (
    COMPANY_ID                  NUMBER(38) UNIQUE,
    COMPANY_VERSION_ID          NUMBER(38) UNIQUE,
    VERSION_ID                  NUMBER(38) UNIQUE,
    APP_ID                      NUMBER(38) UNIQUE,
    COMPANY_CODE                VARCHAR2(10) UNIQUE,
    COMPANY_NAME                VARCHAR2(25),
    COMPANY_DESCRIPTION         VARCHAR2(50),
    VERSION_COMPANY_DESCRIPTION VARCHAR2(50),
    VERSION                     VARCHAR2(15) UNIQUE,
    DESCRIPTION                 VARCHAR2(50),
    APP_CODE                    VARCHAR2(10) UNIQUE,
    APP_NAME                    VARCHAR2(25),
    APP_DESCRIPTION             VARCHAR2(50)
);
```
## Procedimiento almacenado 
- Pära ejecutar el sp, se propuso la creación de dos atributos adicionales como la fecha de creación y el nombre del creador.


```sql
create OR REPLACE PROCEDURE INSERT_TABLES(
    vUserName IN VARCHAR2
)
IS
    v_COUNT_COMPANY_ID NUMBER;
    v_COUNT_COMPANY_CODE NUMBER;
    v_COUNT_APP_ID NUMBER;
    v_COUNT_APP_CODE NUMBER;
    v_COUNT_VERSION_ID NUMBER;
    v_COUNT_VERSION NUMBER;
    v_COUNT_COMPANY_VERSION_ID NUMBER;
    v_COUNT_COMPANY_VERSION NUMBER;

    CURSOR CTemporal IS
        SELECT COMPANY_ID, COMPANY_VERSION_ID, VERSION_ID,
               APP_ID, COMPANY_CODE, COMPANY_NAME, COMPANY_DESCRIPTION,
               VERSION_COMPANY_DESCRIPTION, VERSION, DESCRIPTION, APP_CODE, APP_NAME, APP_DESCRIPTION
        FROM TMP_LLENAR_CAMPOS ;

        v_COMPANY_ID TMP_LLENAR_CAMPOS.COMPANY_ID%TYPE;
        v_COMPANY_VERSION_ID  TMP_LLENAR_CAMPOS.COMPANY_VERSION_ID%TYPE;
        v_VERSION_ID TMP_LLENAR_CAMPOS.VERSION_ID%TYPE;
        v_APP_ID TMP_LLENAR_CAMPOS.APP_ID%TYPE;
        v_COMPANY_CODE TMP_LLENAR_CAMPOS.COMPANY_CODE%TYPE;
        v_COMPANY_NAME TMP_LLENAR_CAMPOS.COMPANY_NAME%TYPE;
        v_COMPANY_DESCRIPTION TMP_LLENAR_CAMPOS.COMPANY_DESCRIPTION%TYPE;
        v_VERSION_COMPANY_DESCRIPTION TMP_LLENAR_CAMPOS.VERSION_COMPANY_DESCRIPTION%TYPE;
        v_VERSION TMP_LLENAR_CAMPOS.VERSION%TYPE;
        v_DESCRIPTION TMP_LLENAR_CAMPOS.DESCRIPTION%TYPE;
        v_APP_CODE TMP_LLENAR_CAMPOS.APP_CODE%TYPE;
        v_APP_NAME TMP_LLENAR_CAMPOS.APP_NAME%TYPE;
        v_APP_DESCRIPTION TMP_LLENAR_CAMPOS.APP_DESCRIPTION%TYPE;

BEGIN
    DBMS_OUTPUT.PUT_LINE('PROCEDURE FOR INSERT INTO TABLES WAS CALLED ');
    OPEN CTemporal;
    LOOP
        FETCH CTemporal  INTO v_COMPANY_ID, v_COMPANY_VERSION_ID, v_VERSION_ID, v_APP_ID,v_COMPANY_CODE,v_COMPANY_NAME,
            v_COMPANY_DESCRIPTION,v_VERSION_COMPANY_DESCRIPTION,v_VERSION,v_DESCRIPTION,v_APP_CODE,v_APP_NAME,v_APP_DESCRIPTION ;
         EXIT WHEN CTemporal%NOTFOUND;


        SELECT COUNT(*) INTO v_COUNT_COMPANY_ID FROM COMPANY WHERE ID =  v_COMPANY_ID;
        IF v_COUNT_COMPANY_ID = 0 THEN
            SELECT COUNT(*) INTO v_COUNT_COMPANY_CODE FROM COMPANY WHERE CODE =  v_COMPANY_CODE;
            IF v_COUNT_COMPANY_CODE = 0 THEN
                DBMS_OUTPUT.PUT_LINE('*******************');
                DBMS_OUTPUT.PUT_LINE('INSERT INTO COMPANY VALUES ID -> '|| v_COMPANY_ID || ' -- CODE -> ' || v_COMPANY_CODE || ' -- NAME -> ' || v_COMPANY_NAME || ' -- DESCRIPTION -> ' || v_COMPANY_DESCRIPTION);
                 INSERT INTO COMPANY(ID, CODE, NAME, DESCRIPTION)
                    VALUES (v_COMPANY_ID,v_COMPANY_CODE,v_COMPANY_NAME,v_COMPANY_DESCRIPTION );
            END IF;
            ELSE
                DBMS_OUTPUT.PUT_LINE('REGISTRO DUPLICADO INTO COMPANY');
        END IF;
        
        SELECT COUNT(*) INTO v_COUNT_APP_ID FROM APPLICATION WHERE ID = v_APP_ID;
        IF v_COUNT_APP_ID = 0 THEN
            SELECT COUNT(*) INTO v_COUNT_APP_CODE FROM APPLICATION WHERE CODE =  v_APP_CODE;
            IF v_COUNT_APP_CODE = 0 THEN
                DBMS_OUTPUT.PUT_LINE('*******************');
                DBMS_OUTPUT.PUT_LINE('INSERT INTO APPLICATION VALUES ID -> '|| v_APP_ID || ' -- CODE -> ' || v_APP_CODE || ' -- NAME -> ' || v_APP_NAME || ' -- DESCRIPTION -> ' || v_APP_DESCRIPTION || ' -- CREATED_AT -> ' || SYSDATE || ' -- CREATED_BY -> ' || vUserName);
                INSERT INTO APPLICATION(ID, CODE, NAME, DESCRIPTION, CREATED_AT, CREATED_BY)
                    VALUES (v_APP_ID, v_APP_CODE, v_APP_NAME, v_APP_DESCRIPTION, SYSDATE, vUserName);
            END IF;
            ELSE
                DBMS_OUTPUT.PUT_LINE('REGISTRO DUPLICADO INTO APPLICATION');
        END IF;

        SELECT COUNT(*) INTO v_COUNT_VERSION_ID FROM VERSION WHERE ID =  v_VERSION_ID;
        IF v_COUNT_VERSION_ID = 0 THEN
            SELECT COUNT(*) INTO v_COUNT_VERSION FROM VERSION WHERE ID =  v_VERSION_ID AND VERSION = v_VERSION;
            IF v_COUNT_VERSION = 0 THEN
                DBMS_OUTPUT.PUT_LINE('*******************');
                DBMS_OUTPUT.PUT_LINE('INSERT INTO VERSION VALUES ID -> '|| v_APP_ID || ' -- VERSION -> ' || v_VERSION || ' -- DESCRIPTION -> ' || v_DESCRIPTION );
                INSERT INTO VERSION(ID, APP_ID, VERSION, DESCRIPTION)
                    VALUES (v_VERSION_ID, v_APP_ID, v_VERSION, v_DESCRIPTION);
            END IF;
            ELSE
                DBMS_OUTPUT.PUT_LINE('REGISTRO DUPLICADO INTO VERSION');
        END IF;

        SELECT COUNT(*) INTO v_COUNT_COMPANY_VERSION_ID FROM COMPANY_VERSION WHERE ID =  v_COMPANY_VERSION_ID;
        IF v_COUNT_COMPANY_VERSION_ID = 0 THEN 
            SELECT COUNT(*) INTO v_COUNT_COMPANY_VERSION FROM COMPANY_VERSION WHERE COMPANY_ID =  v_COMPANY_VERSION_ID AND VERSION_ID = v_VERSION_ID;
            IF v_COUNT_COMPANY_VERSION = 0 THEN 
                 DBMS_OUTPUT.PUT_LINE('*******************');
                DBMS_OUTPUT.PUT_LINE('INSERT INTO COMPANY_VERSION VALUES ID -> '|| v_COMPANY_VERSION_ID || ' -- COMPANY_ID -> ' || v_COMPANY_ID || ' -- VERSION_ID -> ' || v_VERSION_ID || ' -- VERSION_COMPANY_DESCRIPTION -> ' || v_VERSION_COMPANY_DESCRIPTION  );
                INSERT INTO COMPANY_VERSION(ID, COMPANY_ID, VERSION_ID, VERSION_COMPANY_DESCRIPTION)
                    VALUES (v_COMPANY_VERSION_ID, v_COMPANY_ID, v_VERSION_ID,v_VERSION_COMPANY_DESCRIPTION);
            END IF;
            ELSE
                DBMS_OUTPUT.PUT_LINE('REGISTRO DUPLICADO INTO COMPANY_VERSION ');
        END IF;
        COMMIT ;
    END LOOP;
    
    CLOSE CTemporal;
    DBMS_OUTPUT.PUT_LINE('PROCEDURE FOR INSERT INTO TABLES WAS FINISHED ');
END;
/




```


## API REST 
- Se crearon los endpoints para todas las entidades
- Se Anexa link de postman para sus respectivas pruebas
  
[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://interstellar-shadow-897690.postman.co/collection/24950810-545eaf04-f094-4d35-bf7e-6b97d2f686ac?source=rip_markdown)

#COMPANY

#### CREATE

```http
  POST /api/ada/company
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/2afb8c1f-833d-4b41-9c86-2e6371de7076)

- Vaalidaciones
  
  ![image](https://github.com/Stivennevits/ada-test/assets/108912463/c5a37f7e-961b-461f-9ca7-c940877bb331)

  ![image](https://github.com/Stivennevits/ada-test/assets/108912463/4b465f75-cb7c-4b1b-800e-7f10ce1c5d13)

  
#### UPDATE

```http
  PUT /api/ada/company/update/2
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/0f3b6395-a0b3-4f45-827e-3f970496f743)

- Vaalidaciones
  ![image](https://github.com/Stivennevits/ada-test/assets/108912463/54c95b9e-9071-4b0b-92de-f81cb4eec248)
![image](https://github.com/Stivennevits/ada-test/assets/108912463/3cc18f60-6372-41d0-81aa-df58ace4b99d)

#### GET ALL 

```http
  GET /api/ada/company/get-all
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/c2c31640-9237-45a7-a2e6-ae23ca44f86d)

#### GET BY ID

```http
  GET /api/ada/company/get/1
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/b9afeb5a-9522-4207-9cab-4095df9fd245)

- Vaalidaciones
  ![image](https://github.com/Stivennevits/ada-test/assets/108912463/a3cb7da1-1ebd-44de-9282-951097737ddf)

#### DELETE

```http
  GET /api/ada/company/delete/2
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/32c9714c-8a36-4237-97a9-164dd8832a08)

- Vaalidaciones
![image](https://github.com/Stivennevits/ada-test/assets/108912463/d0636c89-aa38-43a8-bd1e-aed518eac571)

### RESPONDER CON JSON AL  RECIBIR EL CÓDIGO DE UNA EMPRESA 

#### FIND ENTITIES

```http
  GET /api/ada/company/get-entities
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/37a331e0-c849-4f2e-b1d1-8afd382adbde)

- Vaalidaciones
![image](https://github.com/Stivennevits/ada-test/assets/108912463/98ba0818-88a2-4189-8de7-d8a02502bf29)
![image](https://github.com/Stivennevits/ada-test/assets/108912463/de6a6fcc-a0e1-43db-b8d9-b046b0e9088a)


# Prueba Senior
Para abordar este punto se tomó un enfoque diferente, en el cual se propone subir un archivo txt para hacer los insert en la tabla de forma masiva.
Se valida el archivo con el fín de evitar inserts inapropiados validando los tipos de datos, los registros duplicados se devuelven en el endpoint con su respectivo mensaje del porqué no se tuvieron en cuenta, con el fin de que el usuario sepa que registros no se pudieron insertar. Además tambien se hacen diferentes validaciones como:
- Que el archivo tenga la estructura apropiada
- Que no hayan ids duplicados de la entidad COMPANY_VERSION
- Que el parámetro correspondiente al companyId exista en los registros, sino es añadido a los registros de error.
- Que el parámetro corresponfiente al versionId exista en los registros, sino es añadido a los registros de error.
- Si no quedaron registros válidos no se realiza el insert.

Finalmente se hace insert con los registros que superaron los diferentes filtros.

El archivo txt debe tener la siguiente estructura:
- companyVersionId|companyId|versionId|description

  ![image](https://github.com/Stivennevits/ada-test/assets/108912463/00225ba0-5b20-47c4-bbd2-81ace822d6b2)

```http
  POST /api/ada/company-version/massive
```
![image](https://github.com/Stivennevits/ada-test/assets/108912463/71b611d0-8146-40fa-ab42-c44a15652a17)


![image](https://github.com/Stivennevits/ada-test/assets/108912463/953eb825-f3fb-4208-a3ac-524724812381)

## Respuesta con los registros duplicados

```json
[
    {
        "companyVersionId": 1,
        "companyId": 2,
        "versionId": 3,
        "description": "desc 2",
        "error": "Registro duplicado"
    },
    {
        "companyVersionId": 1,
        "companyId": 2,
        "versionId": 3,
        "description": "desc 4",
        "error": "Registro duplicado"
    },
    {
        "companyVersionId": 22,
        "companyId": 28,
        "versionId": 4,
        "description": "desc 8",
        "error": "Ya hay un registro dentro del archivo con el mismo ID"
    },
    {
        "companyVersionId": 24,
        "companyId": 10,
        "versionId": 11,
        "description": "desc 10",
        "error": "Ya existe un registro en COMPANY_VERSION con el mismo ID"
    },
    {
        "companyVersionId": 22,
        "companyId": 2,
        "versionId": 4,
        "description": "desc 6",
        "error": "Ya hay un registro dentro del archivo con el mismo ID"
    },
    {
        "companyVersionId": 23,
        "companyId": 6,
        "versionId": 6,
        "description": "desc 9",
        "error": "Ya existe un registro en COMPANY_VERSION con el mismo ID"
    },
    {
        "companyVersionId": 8,
        "companyId": 2,
        "versionId": 3,
        "description": "desc 5",
        "error": "Ya existe un registro en COMPANY_VERSION con el mismo ID"
    },
    {
        "companyVersionId": 1,
        "companyId": 2,
        "versionId": 3,
        "description": "desc 1",
        "error": "No existe una empresa con el id 2"
    },
    {
        "companyVersionId": 3,
        "companyId": 2,
        "versionId": 3,
        "description": "desc 3",
        "error": "Ya existe un registro en COMPANY_VERSION con el mismo ID"
    },
    {
        "companyVersionId": 22,
        "companyId": 23,
        "versionId": 4,
        "description": "desc 7",
        "error": "Ya hay un registro dentro del archivo con el mismo ID"
    }
]
```


## Código 



```java

    @Data
    public class MassiveRequest {
        private Long id;
        private Long companyId;
        private Long versionId;
        private String description;
    }

   @Data
   public class CompanyVersionResponse {
       private Long companyVersionId;
       private Long companyId;
       private Long versionId;
       private String description;
       private String error;
   }

     @PostMapping(MASSIVE)
    @ResponseStatus(OK)
    public List<CompanyVersionResponse> massive (@RequestParam MultipartFile file){
        log.info("CompanyVersionController::massive --file [{}] ", file.getOriginalFilename());
        return service.massive(file);
    }

   public List<CompanyVersionResponse> massive(MultipartFile file) {
        log.info("CompanyVersionService::massive --file [{}] ", file.getOriginalFilename());
        List<MassiveRequest> requests = new ArrayList<>();
        readAndValidateFile(file,requests);
        log.info("list {}", requests );
        List<CompanyVersionResponse> responses = new ArrayList<>();
        processDuplicateRequests(requests, responses);
        List<Long> companyVersionIds = repository.findIds();
        List<Long> companyIds = repository.findCompanyIds();
        List<Long> versionIds = repository.findVersionIds();
        processRequests(requests, companyVersionIds, companyIds, versionIds, responses);
        processVersionRecords(requests, responses);
        if(!requests.isEmpty()){
            repository.saveAll(CompanyVersionMapper.mapToMassive(requests));
        }
        return responses;
    }

    public static List<CompanyVersionRecord> mapToMassive(List<MassiveRequest> requests) {
        List<CompanyVersionRecord> records = new ArrayList<>();
        for (MassiveRequest request : requests) {
            CompanyVersionRecord companyVersionRecord = new CompanyVersionRecord();
            companyVersionRecord.setId(request.getId());
            companyVersionRecord.setCompanyId(request.getCompanyId());
            companyVersionRecord.setVersionId(request.getVersionId());
            companyVersionRecord.setVersionCompanyDescription(request.getDescription());
            records.add(companyVersionRecord);
        }
        return records;
    }


   public static CompanyVersionResponse mapToResponse(MassiveRequest massiveRequest, String error) {
        CompanyVersionResponse response = new CompanyVersionResponse();
        response.setCompanyVersionId(massiveRequest.getId());
        response.setCompanyId(massiveRequest.getCompanyId());
        response.setVersionId(massiveRequest.getVersionId());
        response.setDescription(massiveRequest.getDescription());
        response.setError(error);
        return response;
    }



 private void processDuplicateRequests(List<MassiveRequest> requests, List<CompanyVersionResponse> responses) {
        log.info("CompanyVersionService::processDuplicateRequests  ");
        Map<String, MassiveRequest> uniqueRequests = new HashMap<>();
        for (MassiveRequest request : requests) {
            String key = request.getId() + "-" + request.getCompanyId() + "-" + request.getVersionId();
            if (uniqueRequests.containsKey(key)) {
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "Registro duplicado");
                responses.add(response);
            } else {
                uniqueRequests.put(key, request);
            }
        }
        requests.clear();
        requests.addAll(uniqueRequests.values());
    }

    private void processVersionRecords(List<MassiveRequest> requests, List<CompanyVersionResponse> responses) {
        log.info("CompanyVersionService::processVersionRecords  ");
        Iterator<MassiveRequest> iterator = requests.iterator();
        while (iterator.hasNext()) {
            MassiveRequest request = iterator.next();
            Optional<CompanyVersionRecord> versionRecord = repository.findByCompanyIdAndVersionId(request.getCompanyId(), request.getVersionId());
            if (!requests.isEmpty() && versionRecord.isPresent()) {
                iterator.remove();
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "Ya existe un registro con el mismo id de empresa y id de versión");
                responses.add(response);
            }
        }
    }


    private void processRequests(List<MassiveRequest> requests, List<Long> companyVersionIds, List<Long> companyIds, List<Long> versionIds, List<CompanyVersionResponse> responses) {
        log.info("CompanyVersionService::processRequests  ");
        List<MassiveRequest> requestsToRemove = new ArrayList<>();
        Iterator<MassiveRequest> iterator = requests.iterator();
        while (iterator.hasNext()) {
            MassiveRequest request = iterator.next();
            if ((!requests.isEmpty()) && (requests.stream().filter(r -> r.getId().equals(request.getId())).count() > 1)) {
                requestsToRemove.add(request);
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "Ya hay un registro dentro del archivo con el mismo ID");
                responses.add(response);
            } else if (!requests.isEmpty() && companyVersionIds.contains(request.getId())) {
                requestsToRemove.add(request);
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "Ya existe un registro en COMPANY_VERSION con el mismo ID");
                responses.add(response);
            } else if (!requests.isEmpty() && !companyIds.contains(request.getCompanyId())) {
                CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "No existe una empresa con el id " + request.getCompanyId());
                responses.add(response);
                requestsToRemove.add(request);
            } else {
                if (!requests.isEmpty() && !versionIds.contains(request.getVersionId())) {
                    CompanyVersionResponse response = CompanyVersionMapper.mapToResponse(request, "No existe una versión con el id " + request.getVersionId());
                    responses.add(response);
                    requestsToRemove.add(request);
                }
            }
        }
        requests.removeAll(requestsToRemove);
    }


    public void readAndValidateFile(MultipartFile file, List<MassiveRequest> requests) {
        log.info("CompanyVersionService::readAndValidateFile  ");
        if (file.isEmpty()) {
            throw new AdaException(i18NComponent.getMessage(ErrorMessages.FILE_IS_EMPTY, file.getOriginalFilename()));
        }
        Pattern pattern = Pattern.compile("^(\\d+)\\|(\\d+)\\|(\\d+)\\|(.+)$");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (!matcher.find()) {
                    throw new AdaException(i18NComponent.getMessage(ErrorMessages.ERROR_INVALID_FILE, line));
                }
                Long id = Long.valueOf(matcher.group(1));
                Long companyId = Long.valueOf(matcher.group(2));
                Long versionId = Long.valueOf(matcher.group(3));
                String description = matcher.group(4);

                MassiveRequest request = new MassiveRequest();
                request.setId(id);
                request.setCompanyId(companyId);
                request.setVersionId(versionId);
                request.setDescription(description);

                requests.add(request);
            }
        } catch (IOException e) {
            throw new AdaException(i18NComponent.getMessage(e.getMessage()));
        }
    }
```


## CALCULADORA
Se realizó la calculadora haciendo un endpoint para cada operación, puede encontrarse el código en el controlador CalculateController


### SUMA
```http
  GET /api/ada/calculator/add
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/f218b209-3963-4b79-9056-06b948f3b181)



### RESTA
```http
  GET /api/ada/calculator/substract
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/fb47ea44-0eff-4e92-ba78-88b5b95eb295)



### MULTIPLICACIÓN
```http
  GET /api/ada/calculator/multiply
```
![image](https://github.com/Stivennevits/ada-test/assets/108912463/f641cbc7-8294-4d16-be83-7d94bcd55532)




### DIVISIÓN
```http
  GET /api/ada/calculator/divide
```
![image](https://github.com/Stivennevits/ada-test/assets/108912463/a38b1120-7b55-4ce5-80b4-294c33e0cfd7)



### RAICES
```http
  GET /api/ada/calculator/roots
```
![image](https://github.com/Stivennevits/ada-test/assets/108912463/303e77a4-c4eb-41ef-8aba-1e3ab902c731)


### POTENCIAS
```http
  GET /api/ada/calculator/power
```
![image](https://github.com/Stivennevits/ada-test/assets/108912463/f642da1d-fd07-4ac9-b96f-91483f4c1ee4)


# CREAR PALABRAS A PARTIR DE UN ARRAY DE CARACTERES

En este punto también se propuso un enfoque diferente, en el cual el endpoint recibe una palabra y valída si dicha palabra se puede escribir con los caracteres disponibles, sin ser repetidos. Al final devuelve si fue posible o no, y si fue posible devuelve una lista con las posiciones utilizadas para escribir la palabra 

```http
  GET /api/ada/words/find-word
```

![image](https://github.com/Stivennevits/ada-test/assets/108912463/29b157b0-62e4-4cf5-8e17-299dec031331)

![image](https://github.com/Stivennevits/ada-test/assets/108912463/428aba45-96c0-4565-8ad7-5fcb427c57fe)























