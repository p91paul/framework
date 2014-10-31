package applica.framework.data.mongodb.tests.data;

import applica.framework.data.mongodb.tests.model.Brand;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/14
 * Time: 12:43
 */
public class MockBrandsRepository extends MockRepository<Brand> {

    public MockBrandsRepository() {
        save(new Brand());
    }

    @Override
    public Class<Brand> getEntityType() {
        return Brand.class;
    }
}
