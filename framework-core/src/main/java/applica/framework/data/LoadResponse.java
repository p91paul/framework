package applica.framework.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoadResponse {

    private List<? extends Entity> rows = new ArrayList<>();
    private long totalRows;

    @SuppressWarnings("unchecked")
    public <T extends Entity> List<T> getRows(Class<T> type) {
        return (List<T>) rows;
    }

    public <T extends Entity> List<? extends Entity> getRows() {
        return rows;
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    /**
     * Gets first record found in search query
     * Deprecated: use findFirst
     */
    public <T extends Entity> T getOne(Class<T> type) {
        if (rows != null && rows.size() > 0) return (T) rows.get(0);
        return null;
    }

    public <T extends Entity> Optional<T> findFirst() {
        return Optional.of((rows != null && rows.size() > 0) ? (T) rows.get(0) : null);
    }

    public void setRows(List<? extends Entity> rows) {
        this.rows = rows;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

}
