package hu.webarticum.holodb.benchmark.micronaut;

import java.io.IOException;
import java.util.Objects;

import hu.webarticum.minibase.storage.api.Column;
import hu.webarticum.minibase.storage.api.Row;
import hu.webarticum.minibase.storage.api.Table;
import hu.webarticum.miniconnect.lang.LargeInteger;

public class TablePrinter {
    
    private final Table table;

    public TablePrinter(Table table) {
        this.table = table;
    }
    
    public void print(Appendable appendable) throws IOException {
        boolean first = true;
        for (Column column : table.columns().resources()) {
            if (first) {
                first = false;
            } else {
                appendable.append(',');
            }
            appendable.append(column.name());
        }
        appendable.append('\n');
        LargeInteger tableSize = table.size();
        for (LargeInteger i = LargeInteger.ZERO; i.isLessThan(tableSize); i = i.increment()) {
            Row row = table.row(i);
            first = true;
            for (Object value : row.getAll()) {
                if (first) {
                    first = false;
                } else {
                    appendable.append(',');
                }
                appendable.append(Objects.toString(value));
            }
            appendable.append('\n');
        }
    }
    
}
