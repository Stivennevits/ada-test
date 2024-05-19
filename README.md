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




