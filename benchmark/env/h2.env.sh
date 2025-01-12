export JDBC_URL="jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;QUERY_CACHE_SIZE=0;INIT=RUNSCRIPT FROM '../db/h2-init.sql'"
export DB_USERNAME=''
export DB_PASSWORD=''
export DB_DRIVER='org.h2.Driver'
export DB_TYPE='h2'
export SQL_DIALECT='org.hibernate.dialect.H2Dialect'
