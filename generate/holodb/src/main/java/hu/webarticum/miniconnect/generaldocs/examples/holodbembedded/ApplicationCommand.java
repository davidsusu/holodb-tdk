package hu.webarticum.miniconnect.generaldocs.examples.holodbembedded;

import java.sql.Connection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import org.openjdk.jol.info.GraphLayout;

import hu.webarticum.minibase.engine.facade.FrameworkSession;
import hu.webarticum.minibase.storage.api.Schema;
import hu.webarticum.miniconnect.api.MiniSession;
import hu.webarticum.miniconnect.jdbc.MiniJdbcConnection;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;

@Command
public class ApplicationCommand implements Runnable {
    
    private static final String SCHEMA_NAME = "employment";
    
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    private final DataSource dataSource;
    

    @Inject
    public ApplicationCommand(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    
    @Override
    public void run() {
        try {
            runThrows();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void runThrows() throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        System.out.println("Ready timestamp: " + now.format(formatter) + " (" + now.toInstant().toEpochMilli() + ")");
        System.out.println();
        
        Connection connection = dataSource.getConnection();
        MiniJdbcConnection miniJdbcConnection = connection.unwrap(MiniJdbcConnection.class);
        if (miniJdbcConnection == null) {
            throw new IllegalStateException("Not a MiniJdbcConnection");
        }
        MiniSession session = miniJdbcConnection.getMiniSession();
        if (!(session instanceof FrameworkSession)) {
            throw new IllegalStateException("Not a FrameworkSession: " + session);
        }
        FrameworkSession frameworkSession = (FrameworkSession) session;
        Schema schema = frameworkSession.engineSession().storageAccess().schemas().get(SCHEMA_NAME);
        GraphLayout layout = GraphLayout.parseInstance(schema);
        System.out.println("Total size: " + layout.totalSize() + " bytes");
    }

}
