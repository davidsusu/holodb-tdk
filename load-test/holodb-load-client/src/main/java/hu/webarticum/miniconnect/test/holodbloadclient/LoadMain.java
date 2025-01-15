package hu.webarticum.miniconnect.test.holodbloadclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

import hu.webarticum.miniconnect.api.MiniSession;
import hu.webarticum.miniconnect.api.MiniSessionManager;
import hu.webarticum.miniconnect.messenger.adapter.MessengerSessionManager;
import hu.webarticum.miniconnect.server.ClientMessenger;

public class LoadMain {

    private static String HOST = "localhost";

    private static int PORT = 3430;
    
    private static final String SQL_PATTERN_1 = "SELECT * FROM employees WHERE id = ?";
    
    private static final String SQL_PATTERN_2 =
            "SELECT e.*, c.identifier FROM coupons c \n" +
            "LEFT JOIN employees e ON c.employee_id=e.id \n" +
            "WHERE c.id = ?";

    private static final String SQL_PATTERN_3 = "SELECT * FROM employees ORDER BY lastname ASC, firstname DESC LIMIT 20";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    private static volatile boolean stopped = false;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start: " + LocalDateTime.now().format(formatter));
        Supplier<String> sqlSupplier1 = () -> SQL_PATTERN_1.replace("?", "" + ((int) (Math.random() * 100)));
        for (int i = 0; i < 15; i++) {
            startNewClientThread(sqlSupplier1);
        }
        
        Supplier<String> sqlSupplier2 = () -> SQL_PATTERN_2.replace("?", "" + ((int) (Math.random() * 100)));
        for (int i = 0; i < 15; i++) {
            startNewClientThread(sqlSupplier2);
        }
        /*
        Supplier<String> sqlSupplier3 = () -> SQL_PATTERN_3;
        for (int i = 0; i < 15; i++) {
            startNewClientThread(sqlSupplier3);
        }*/
        
        Thread.sleep(10000);
        stopped = true;
    }
    
    private static void startNewClientThread(Supplier<String> sqlSupplier) {
        new Thread(() -> runClient(sqlSupplier)).start();
    }
    
    private static void runClient(Supplier<String> sqlSupplier) {
        long threadId = Thread.currentThread().getId();
        System.out.println("Thread (" + threadId + ") started at " + LocalDateTime.now().format(formatter));
        int i = 0;
        try (ClientMessenger clientMessenger = new ClientMessenger(HOST, PORT)) {
            MiniSessionManager sessionManager = new MessengerSessionManager(clientMessenger);
            try (MiniSession session = sessionManager.openSession()) {
                session.execute("USE employment");
                while (!stopped) {
                    String sql = sqlSupplier.get();
                    i++;
                    try {
                        session.execute(sql).resultSet().close();;
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
        System.out.println("Thread (" + threadId + ") finished at " + LocalDateTime.now().format(formatter) + " :: count: " + i);
    }

}
