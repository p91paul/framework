package applica.framework.data.hibernate;

import applica.framework.data.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Applica (www.applicadoit.com)
 * User: bimbobruno
 * Date: 09/10/14
 * Time: 11:31
 */
public abstract class HibernateRepository<T extends Entity> implements Repository<T> {

    @Autowired
    private HibernateHelper hibernateHelper;

    @PreDestroy
    protected void destroy() {
        hibernateHelper.dispose();
    }

    protected Session getSession() {
        return hibernateHelper.getSession();
    }

    protected void withSession(Consumer<Session> consumer) {
        Session session = getSession();
        consumer.accept(session);
        session.close();
    }

    protected Session getCurrentSession() {
        return hibernateHelper.getCurrentSession();
    }

    @Override
    public Optional<T> get(Object id) {
        Optional<T> result;

        Session session = getSession();
        try {
            if (id != null) {
                long iid = LEntity.checkedId(id);
                if (iid > 0) {
                    id = iid;
                }
                result = Optional.ofNullable((T) session.get(getEntityType(), (java.io.Serializable) id));
            } else {
                result = Optional.empty();
            }
        } finally {
            session.close();
        }

        return result;
    }

    @Override
    public LoadResponse<T> find(LoadRequest loadRequest) {
        if(loadRequest == null) loadRequest = new LoadRequest();

        LoadResponse response = new LoadResponse();

        Session session = getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Criteria countCriteria = createCriteria(session, loadRequest);
            Criteria criteria = createCriteria(session, loadRequest);

            long count = (Long) countCriteria.setProjection(Projections.rowCount()).uniqueResult();
            int limit = loadRequest.getRowsPerPage();
            int skip = loadRequest.getRowsPerPage() * (loadRequest.getPage() - 1);

            if (limit != 0) criteria.setMaxResults(limit);
            if (skip != 0) criteria.setFirstResult(skip);

            List<Sort> sorts = loadRequest.getSorts();
            if (sorts == null) {
                sorts = getDefaultSorts();
            }

            if (sorts != null) {
                for (Sort sort : sorts) {
                    if (sort.isDescending()) {
                        criteria.addOrder(Order.desc(sort.getProperty()));
                    } else {
                        criteria.addOrder(Order.asc(sort.getProperty()));
                    }
                }
            }

            response.setRows(criteria.list());
            response.setTotalRows(count);

            transaction.commit();
        } catch(Exception e) {
            transaction.rollback();
            throw e;
        } finally {
        }

        return response;
    }

    protected Criteria createCriteria(Session session, LoadRequest loadRequest) {
        Criteria criteria = session.createCriteria(getEntityType());

        for(Filter filter : loadRequest.getFilters()) {
            if(filter.getValue() == null) {
                continue;
            }
            switch (filter.getType()) {
                case Filter.LIKE:
                    criteria.add(Restrictions.like(filter.getProperty(), String.format("%%%s%%", filter.getValue())));
                    break;
                case Filter.GT:
                    criteria.add(Restrictions.gt(filter.getProperty(), filter.getValue()));
                    break;
                case Filter.GTE:
                    criteria.add(Restrictions.ge(filter.getProperty(), filter.getValue()));
                    break;
                case Filter.LT:
                    criteria.add(Restrictions.lt(filter.getProperty(), filter.getValue()));
                    break;
                case Filter.LTE:
                    criteria.add(Restrictions.le(filter.getProperty(), filter.getValue()));
                    break;
                case Filter.EQ:
                    criteria.add(Restrictions.eq(filter.getProperty(), filter.getValue()));
                    break;
                case Filter.IN:
                    criteria.add(Restrictions.in(filter.getProperty(), (List) filter.getValue()));
                    break;
                case Filter.NIN:
                    criteria.add(Restrictions.not(Restrictions.in(filter.getProperty(), (List) filter.getValue())));
                    break;
                case Filter.ID:
                    if(filter.getProperty() == null) {
                        criteria.add(Restrictions.idEq(filter.getValue()));
                    } else {
                        criteria.add(Restrictions.eq(filter.getProperty(), filter.getValue()));
                    }
                    break;
            }
        }

        return criteria;
    }

    public List<Sort> getDefaultSorts() {
        return null;
    }

    @Override
    public void save(T entity) {
        if (entity != null) {
            Session session = getSession();
            Transaction tx = session.beginTransaction();
            try {
                //convert id to long if possible;
                Object oldId = entity.getId();
                long iid = LEntity.checkedId(oldId);
                if (iid > 0) {
                    entity.setId(iid);
                }

                session.saveOrUpdate(entity);
                tx.commit();
            } catch(Exception ex) {
                tx.rollback();
                throw ex;
            } finally {
                session.close();
            }
        }
    }

    @Override
    public void delete(Object id) {
        if (id != null) {
            Session session = getSession();
            Transaction tx = session.beginTransaction();
            try {
                long iid = LEntity.checkedId(id);
                if (iid > 0) {
                    id = iid;
                }
                T entity = (T) session.get(getEntityType(), (java.io.Serializable) iid);
                if (entity != null) {
                    session.delete(entity);
                }
                tx.commit();
            } catch(Exception ex) {
                tx.rollback();
                throw ex;
            } finally {
                session.close();
            }
        }
    }

}
