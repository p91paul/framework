package applica.framework.data;

import java.util.ArrayList;
import java.util.List;

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
    public <T extends Entity> T getOne(Class<T> type) {
        if (rows != null && rows.size() > 0) return (T) rows.get(0);
        return null;
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
