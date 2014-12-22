package applica.framework.data;

import applica.framework.CrudConfigurationException;
import applica.framework.Grid;
import applica.framework.GridProcessException;
import applica.framework.processors.GridProcessor;
import java.util.Arrays;
import java.util.List;

public class GridDataProvider {

    private Repository repository;
    private GridProcessor processor;

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public GridProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(GridProcessor processor) {
        this.processor = processor;
    }

    public void load(Grid grid, LoadRequest loadRequest) throws CrudConfigurationException, GridProcessException {
        if (repository == null)
            throw new CrudConfigurationException("Missing repository");

        loadRequest.setRowsPerPage(grid.getRowsPerPage());
        if (loadRequest.getSorts() == null) {
            Sort defaultSort = grid.getDefaultSort();
            if (defaultSort != null) {
                loadRequest.setSorts(Arrays.asList(defaultSort));
            }
        }

        LoadResponse response = repository.find(loadRequest);
        List<? extends Entity> entities = response.getRows();
        grid.setData(processor.toMap(grid, entities));
        grid.setCurrentPage(loadRequest.getPage());
        grid.setSearched(loadRequest.getFilters().size() > 0);
        grid.setPages((int) Math.ceil((double) response.getTotalRows() / grid.getRowsPerPage()));

        //grid now supports only 1 sort
        if (loadRequest.getSorts() != null && loadRequest.getSorts().size() > 0) {
            grid.setSortBy(loadRequest.getSorts().get(0));
        }

        if (grid.getSearchForm() != null) {
            grid.getSearchForm().setData(loadRequest.filtersMap());
        }
    }
}
