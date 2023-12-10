package hu.webarticum.holodb.benchmark.micronaut;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerShutdownEvent;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Singleton;

@Singleton
@Requires(notEnv = "test")
class Listener {
    
    private DataSource dataSource;
    
    public Listener(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @EventListener
    void init(ServerStartupEvent event) {
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new UncheckedIOException(new IOException(e));
        }
        try {
            if (!new File(System.getProperty("user.dir"), "RUNNING.lock").createNewFile()) {
                throw new IOException("Lock file already exists");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @EventListener
    void init(ServerShutdownEvent event) {
        if (!new File(System.getProperty("user.dir"), "RUNNING.lock").delete()) {
            throw new UncheckedIOException(new IOException("Lock file deletion failed"));
        }
    }
    
}