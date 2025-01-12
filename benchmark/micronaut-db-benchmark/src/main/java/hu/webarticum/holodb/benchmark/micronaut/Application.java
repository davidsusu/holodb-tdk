package hu.webarticum.holodb.benchmark.micronaut;

import java.util.Arrays;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("db-export-job")) {
            PicocliRunner.run(DbExportJob.class, Arrays.copyOfRange(args, 1, args.length));
        } else {
            Micronaut.run(Application.class, args);
        }
    }
}