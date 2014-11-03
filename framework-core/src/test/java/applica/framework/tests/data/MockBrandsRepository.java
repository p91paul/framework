package applica.framework.tests.data;

import applica.framework.tests.model.Brand;

/**
 * Applica (www.applicamobile.com)
 * User: bimbobruno
 * Date: 31/10/14
 * Time: 12:43
 */
public class MockBrandsRepository extends MockRepository<Brand> {

    public MockBrandsRepository() {
        save(new Brand(Brand.ROCKSTAR_ID, "rockstar"));
    }

    @Override
    public Class<Brand> getEntityType() {
        return Brand.class;
    }
}
