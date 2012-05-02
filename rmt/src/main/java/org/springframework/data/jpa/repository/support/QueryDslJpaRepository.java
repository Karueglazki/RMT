/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.repository.support;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;
import de.flower.common.annotation.Patched;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * QueryDsl specific extension of {@link org.springframework.data.jpa.repository.support.SimpleJpaRepository} which adds implementation for
 * {@link org.springframework.data.querydsl.QueryDslPredicateExecutor}.
 *
 * @author Oliver Gierke
 */
@Patched
public class QueryDslJpaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements
		QueryDslPredicateExecutor<T> {

	private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

	private final EntityManager em;
	private final EntityPath<T> path;
	private final PathBuilder<T> builder;

	/**
	 * Creates a new {@link QueryDslJpaRepository} from the given domain class and {@link javax.persistence.EntityManager}. This will use
	 * the {@link org.springframework.data.querydsl.SimpleEntityPathResolver} to translate the given domain class into an {@link com.mysema.query.types.EntityPath}.
	 *
	 * @param domainClass
	 * @param entityManager
	 */
	public QueryDslJpaRepository(JpaEntityInformation<T, ID> entityMetadata, EntityManager entityManager) {

		this(entityMetadata, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
	}

	/**
	 * Creates a new {@link QueryDslJpaRepository} from the given domain class and {@link javax.persistence.EntityManager} and uses the
	 * given {@link org.springframework.data.querydsl.EntityPathResolver} to translate the domain class into an {@link com.mysema.query.types.EntityPath}.
	 *
	 * @param domainClass
	 * @param entityManager
	 * @param resolver
	 */
	public QueryDslJpaRepository(JpaEntityInformation<T, ID> entityMetadata, EntityManager entityManager,
                                 EntityPathResolver resolver) {

		super(entityMetadata, entityManager);
		this.em = entityManager;
		this.path = resolver.createPath(entityMetadata.getJavaType());
		this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.data.jpa.repository.querydsl.
	 * QueryDslSpecificationExecutor#findOne(com.mysema.query.types.Predicate)
	 */
	public T findOne(Predicate predicate) {

		return createQuery(predicate).uniqueResult(path);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.data.jpa.repository.querydsl.
	 * QueryDslSpecificationExecutor#findAll(com.mysema.query.types.Predicate)
	 */
	public List<T> findAll(Predicate predicate) {

		return createQuery(predicate).list(path);
	}

    @Patched
    public List<T> findAll(final Predicate predicate, final EntityPath<?> ... fetchAttributes) {
        return createQuery(fetchAttributes).where(predicate).list(path);
    }

    @Patched
    public Page<T> findAll(final Predicate predicate, Pageable pageable, final EntityPath<?> ... fetchAttributes) {
        JPQLQuery countQuery = createQuery(predicate);
        JPQLQuery query = applyPagination(createQuery(fetchAttributes).where(predicate), pageable);

        return new PageImpl<T>(query.list(path), pageable, countQuery.count());
    }

    @Patched
    public List<T> findAll(final Predicate predicate, final OrderSpecifier<?> order, final EntityPath<?> ... fetchAttributes) {
        return createQuery(fetchAttributes).where(predicate).orderBy(order).list(path);
    }


	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.data.jpa.repository.querydsl.
	 * QueryDslSpecificationExecutor#findAll(com.mysema.query.types.Predicate,
	 * com.mysema.query.types.OrderSpecifier<?>[])
	 */
	public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {

		return createQuery(predicate).orderBy(orders).list(path);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.data.jpa.repository.querydsl.
	 * QueryDslSpecificationExecutor#findAll(com.mysema.query.types.Predicate,
	 * org.springframework.data.domain.Pageable)
	 */
	public Page<T> findAll(Predicate predicate, Pageable pageable) {

		JPQLQuery countQuery = createQuery(predicate);
		JPQLQuery query = applyPagination(createQuery(predicate), pageable);

		return new PageImpl<T>(query.list(path), pageable, countQuery.count());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.springframework.data.jpa.repository.querydsl.
	 * QueryDslSpecificationExecutor#count(com.mysema.query.types.Predicate)
	 */
	public long count(Predicate predicate) {

		return createQuery(predicate).count();
	}

	/**
	 * Creates a new {@link JPQLQuery} for the given {@link com.mysema.query.types.Predicate}.
	 *
	 * @param predicate
	 * @return
	 */
	protected JPQLQuery createQuery(Predicate... predicate) {

		return new JPAQuery(em).from(path).where(predicate);
	}

    @Patched
    protected JPQLQuery createQuery(EntityPath<?>... fetchAttributes) {
        JPAQuery query = new JPAQuery(em).from(path);
        for (EntityPath<?> path : fetchAttributes) {
            query.leftJoin(path).fetch();
        }
        query.distinct(); // fetching one-to-many associations would multiply the result set.
        return query;
    }

	/**
	 * Applies the given {@link org.springframework.data.domain.Pageable} to the given {@link JPQLQuery}.
	 *
	 * @param query
	 * @param pageable
	 * @return
	 */
	protected JPQLQuery applyPagination(JPQLQuery query, Pageable pageable) {

		if (pageable == null) {
			return query;
		}

		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());

		return applySorting(query, pageable.getSort());
	}

	/**
	 * Applies sorting to the given {@link JPQLQuery}.
	 *
	 * @param query
	 * @param sort
	 * @return
	 */
	protected JPQLQuery applySorting(JPQLQuery query, Sort sort) {

		if (sort == null) {
			return query;
		}

		for (Order order : sort) {
			query.orderBy(toOrder(order));
		}

		return query;
	}

	/**
	 * Transforms a plain {@link org.springframework.data.domain.Sort.Order} into a QueryDsl specific {@link com.mysema.query.types.OrderSpecifier}.
	 * 
	 * @param order
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected OrderSpecifier<?> toOrder(Order order) {

		Expression<Object> property = builder.get(order.getProperty());

		return new OrderSpecifier(order.isAscending() ? com.mysema.query.types.Order.ASC
				: com.mysema.query.types.Order.DESC, property);
	}
}