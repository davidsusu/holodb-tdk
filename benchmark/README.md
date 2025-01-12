# Benchmark keretrendszer a HoloDB performanciájának összehasonlításához

Ez a keretrendszer a *HoloDB: szintetikus adatok on-the-fly szolgáltatása relációs adatbázisként*
című TDK-dolgozat performanciatesztjeinek futtatásához készült.

A használat előtt az `env` könyvtárban megadott környezeti változókat érdemes átnézni, kiigazítani.
A tesztek előkészítéséhez az `init.sh` szkript futtatható,
egy-egy test suite futtatása előtt pedig a `reset.sh`.

A keretrendszer a `run-suite.sh` fájlból indítva `bash` környezetben használható,
de maga a `run.sh` bármilyen POSIX-kompatibilis shellen indítható.
A Gradle, Micronaut, Hibernate és egyéb függőségek aktuálisan legújabb verzióit használtam,
ezek a Java SDK 11+ meglétét igénylik.

A keretrendszert saját kézi tesztelésem megkönnyítéséhez írtam,
nem törekedtem az olvashatósági-strukturális áttekinthetőségre,
a megoldásnak lehetnek ránézésre kontraintuitív aspektusai.
Ugyanakkor a szkriptek és Gradle-projektek átnézésével a logika szerintem könnyen megérthető.
A fő követelmény a fő alkalmazásnak (`micronaut-db-benchmark`)
az authentikus futtatása és egyszerűen tartása volt,
a tesztelő rendszer pedig teljesen ennek lett alárendelve.

A `run-suite.sh` out-of-the-box indítható,
alapból a `complex-writeable` teszteket fogja futtatni.
A szkript csak-olvasható teszteknél a `micronaut-db-benchmark/src/main/resources`
könyvtárban lévő HoloDB konfigurációs fájlokban a `tableDefaults.writeable` beállítást
átmenetileg `false` értékre írja át.

Az adatbázisok betöltése nem része a repónak.
A HoloDB által generált adatbázisból kell kiexportálni az adatokat két helyre:
a H2 számára a `db/csv` könyvtárba a táblanév prefixű, `.csv` kiterjesztésű fájlokba,
illetbe ezekből a MySQL-nél egy kiinduló adatbázisba (ld. a `db/mysql-reset.py` szkriptet).
A MySQL-hez az adatbáziselérés beállítása is szükséges (ld. a `env/mysql.env.sh` fájlt).

A HoloDB szerver verziója dockeresen fut,
a `tmp/holodb-docker` könyvtárba betöltődik a `general-docs` projekt `holodb-standalone` példája,
melyet inicializáció aztán enyhén testreszab,
például kicseréli a konfigurációt a `holodb-fine.yaml` változatra.

A dolgozatban bemutatott teszteknél a H2-t a lekérdezésszintű tesztekhez
a `QUERY_CACHE_SIZE=0` opcióval használtam,
míg az alkalmazáson keresztüli integrált tesztnél ezt elhagytam
az authentikusság kedvéért
(tényleges funkcionális teszteket aligha futtatnak ilyesfajta cache-elés nélkül).

A `run-suite.sh` fájl a tesztek futtatása között fixen várakozik néhány másodpercet,
plusz előtte kivárja, míg a load-átlag egy megadott érték alá alá esik.
Ez a saját limitált tesztkörnyezetemben hasznos biztosítéknak bizonyult,
erősebb gépekhez ezek a részek egyszerűen kikommentezhetők.
