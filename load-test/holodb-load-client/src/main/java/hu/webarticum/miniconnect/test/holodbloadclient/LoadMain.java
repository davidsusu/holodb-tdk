package hu.webarticum.miniconnect.test.holodbloadclient;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.webarticum.miniconnect.api.MiniResult;
import hu.webarticum.miniconnect.api.MiniResultSet;
import hu.webarticum.miniconnect.api.MiniSession;
import hu.webarticum.miniconnect.api.MiniSessionManager;
import hu.webarticum.miniconnect.messenger.adapter.MessengerSessionManager;
import hu.webarticum.miniconnect.record.ResultRecord;
import hu.webarticum.miniconnect.record.ResultTable;
import hu.webarticum.miniconnect.server.ClientMessenger;

public class LoadMain {

    private static final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    

    private static String HOST = "localhost";

    private static int PORT = 3430;
    
    private static int FULL_MILLIS = 3_000;

    //private static int SLEEP_MILLIS = 10;
    private static int SLEEP_MILLIS = 1;

    //private static final String SQL_PATTERN_1 = "SELECT 1";
    private static final String SQL_PATTERN_1 = "SELECT * FROM employees WHERE id = ?";
    
    private static final String SQL_PATTERN_2 =
            "SELECT e.*, c.identifier FROM coupons c \n" +
            "LEFT JOIN employees e ON c.employee_id=e.id \n" +
            "WHERE c.id = ?";

    private static final String SQL_PATTERN_3 = "SELECT * FROM employees ORDER BY lastname ASC, firstname DESC LIMIT 20";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    private static volatile boolean stopped = false;
    
    public static void main(String[] args) throws InterruptedException {
        logger.info("LoadMain.main() started");
        /*
        Supplier<String> sqlSupplier1 = () -> SQL_PATTERN_1.replace("?", "" + ((int) (Math.random() * 100)));
        for (int i = 0; i < 100; i++) {
            startNewClientThread(sqlSupplier1);
        }
        */
        Supplier<String> sqlSupplier2 = () -> SQL_PATTERN_2.replace("?", "" + ((int) (Math.random() * 100)));
        for (int i = 0; i < 5; i++) {
            startNewClientThread(sqlSupplier2);
        }
        /*
        Supplier<String> sqlSupplier3 = () -> SQL_PATTERN_3;
        for (int i = 0; i < 1; i++) {
            startNewClientThread(sqlSupplier3);
        }
        */
        Thread.sleep(FULL_MILLIS);
        stopped = true;
    }
    
    private static void startNewClientThread(Supplier<String> sqlSupplier) {
        new Thread(() -> runClient(sqlSupplier)).start();
    }
    
    private static void runClient(Supplier<String> sqlSupplier) {
        logger.info("Thread started");
        
        List<Long> times = new ArrayList<>(200);
        int i = 0;
        try (ClientMessenger clientMessenger = new ClientMessenger(HOST, PORT)) {
            MiniSessionManager sessionManager = new MessengerSessionManager(clientMessenger);
            try (MiniSession session = sessionManager.openSession()) {
                session.execute("USE employment");
                while (!stopped) {
                    String sql = sqlSupplier.get();
                    i++;
                    try {
                        long start = System.currentTimeMillis();
                        MiniResult result = session.execute(sql);
                        if (!result.success()) {
                            logger.error("ERROR: {}, SQL: {}", result.error().message(), sql);
                            break;
                        }
                        try (MiniResultSet resultSet = result.resultSet()) {
                            while (resultSet.fetch() != null) {
                                // nothing to do
                            }
                        }
                        long end = System.currentTimeMillis();
                        Thread.sleep(SLEEP_MILLIS);
                        times.add(end - start);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
        
        logger.info("Thread finished (count: {}, times: {})", i, times);
    }

}
