package hu.webarticum.holodb.benchmark.micronaut;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import hu.webarticum.minibase.engine.facade.FrameworkSession;
import hu.webarticum.minibase.storage.api.Schema;
import hu.webarticum.minibase.storage.api.StorageAccess;
import hu.webarticum.minibase.storage.api.Table;
import hu.webarticum.miniconnect.api.MiniSession;
import hu.webarticum.miniconnect.jdbc.MiniJdbcConnection;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "db-export-job", description = "Runs a custom CLI job", mixinStandardHelpOptions = true)
public class DbExportJob implements Runnable {

    @Inject
    private DataSource dataSource;

    @Option(names = {"-d", "--directory"}, description = "Export directory")
    String directory;

    public static void main(String[] args) {
        PicocliRunner.run(DbExportJob.class, args);
    }

    @Transactional
    @Override
    public void run() {
        try {
            runUnwrapped();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void runUnwrapped() throws SQLException, IOException {
        MiniJdbcConnection connection = dataSource.getConnection().unwrap(MiniJdbcConnection.class);
        if (connection == null) {
            throw new IllegalArgumentException("Not a MiniJdbcConnection!");
        }
        
        MiniSession session = connection.getMiniSession();
        if (!(session instanceof FrameworkSession)) {
            throw new IllegalArgumentException("Not a FrameworkSession!");
        }
        FrameworkSession frameworkSession = (FrameworkSession) session;
        StorageAccess storageAccess = frameworkSession.engineSession().storageAccess();
        Schema schema = storageAccess.schemas().get(connection.getSchema());
        for (Table table : schema.tables().resources()) {
            File targetFile = new File(directory, table.name() + ".csv");
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(targetFile))) {
                new TablePrinter(table).print(writer);
            }
        }
    }
}
