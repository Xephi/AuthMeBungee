package fr.xephi.authme.database;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DatabaseCalls implements DataSource {

    private DataSource database;

    public DatabaseCalls(DataSource database) {
        this.database = database;
    }

    @Override
    public synchronized boolean isAuthAvailable(final String user) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> result = executor.submit(new Callable<Boolean>() {

            public Boolean call() throws Exception {
                return database.isAuthAvailable(user);
            }
        });
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            return (false);
        }
    }

    @Override
    public synchronized void close() {
        database.close();
    }

    @Override
    public synchronized void reload() {
        database.reload();
    }

    @Override
    public synchronized boolean isLogged(final String user) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> result = executor.submit(new Callable<Boolean>() {

            public Boolean call() throws Exception {
                return database.isLogged(user);
            }
        });
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            return (false);
        }
    }
}
