/*
 * jGnash, a personal finance application
 * Copyright (C) 2001-2013 Craig Cavanaugh
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jgnash.engine.jpa;

import jgnash.engine.*;
import jgnash.engine.dao.CommodityDAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


/**
 * Commodity DAO
 *
 * @author Craig Cavanaugh
 */

class JpaCommodityDAO extends AbstractJpaDAO implements CommodityDAO {

    //private static final Logger logger = Logger.getLogger(JpaCommodityDAO.class.getName());

    JpaCommodityDAO(final EntityManager entityManager, final boolean isRemote) {
        super(entityManager, isRemote);
    }

    /*
     * @see jgnash.engine.CommodityDAOInterface#addCommodity(jgnash.engine.CommodityNode)
     */
    @Override
    public synchronized boolean addCommodity(final CommodityNode node) {

        em.getTransaction().begin();

        em.persist(node);

        em.getTransaction().commit();

        return true;
    }

    /*
     * @see jgnash.engine.CommodityDAOInterface#addSecurityHistory(jgnash.engine.SecurityNode, jgnash.engine.SecurityHistoryNode)
     */
    @Override
    public synchronized boolean addSecurityHistory(final SecurityNode node, final SecurityHistoryNode hNode) {

        em.getTransaction().begin();

        em.persist(node);

        em.getTransaction().commit();

        return true;
    }

    @Override
    public synchronized boolean addExchangeRateHistory(final ExchangeRate rate, final ExchangeRateHistoryNode hNode) {

        em.getTransaction().begin();

        em.persist(rate);

        em.getTransaction().commit();
        return true;
    }

    @Override
    public synchronized boolean removeExchangeRateHistory(final ExchangeRate rate, final ExchangeRateHistoryNode hNode) {
        em.getTransaction().begin();

        em.remove(hNode);
        em.persist(rate);

        em.getTransaction().commit();
        return true;
    }

    /*
     * @see jgnash.engine.CommodityDAOInterface#getCurrencies()
     */
    @Override
    //@SuppressWarnings("unchecked")
    public synchronized List<CurrencyNode> getCurrencies() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CurrencyNode> cq = cb.createQuery(CurrencyNode.class);
        Root<CurrencyNode> root = cq.from(CurrencyNode.class);
        cq.select(root);

        TypedQuery<CurrencyNode> q = em.createQuery(cq);


        return stripMarkedForRemoval(new ArrayList<>(q.getResultList()));
    }

    /*
     * @see jgnash.engine.CommodityDAOInterface#getExchangeNode(java.lang.String)
     */
    @Override
    public synchronized ExchangeRate getExchangeNode(final String rateId) {

        ExchangeRate exchangeRate = null;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExchangeRate> cq = cb.createQuery(ExchangeRate.class);
        Root<ExchangeRate> root = cq.from(ExchangeRate.class);
        cq.select(root);

        TypedQuery<ExchangeRate> q = em.createQuery(cq);

        for (ExchangeRate rate : q.getResultList()) {
            if (rate.getRateId().equals(rateId)) {
                exchangeRate = rate;
                break;
            }
        }
        return exchangeRate;
    }

    /**
     * @see jgnash.engine.dao.CommodityDAO#getSecurities()
     */
    @Override
    public synchronized List<SecurityNode> getSecurities() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SecurityNode> cq = cb.createQuery(SecurityNode.class);
        Root<SecurityNode> root = cq.from(SecurityNode.class);
        cq.select(root);

        TypedQuery<SecurityNode> q = em.createQuery(cq);


        return stripMarkedForRemoval(new ArrayList<>(q.getResultList()));
    }

    @Override
    public synchronized List<ExchangeRate> getExchangeRates() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ExchangeRate> cq = cb.createQuery(ExchangeRate.class);
        Root<ExchangeRate> root = cq.from(ExchangeRate.class);
        cq.select(root);

        TypedQuery<ExchangeRate> q = em.createQuery(cq);


        return stripMarkedForRemoval(new ArrayList<>(q.getResultList()));
    }

    /*
     * @see jgnash.engine.CommodityDAOInterface#removeSecurityHistory(jgnash.engine.SecurityNode, jgnash.engine.SecurityHistoryNode)
     */
    @Override
    public synchronized boolean removeSecurityHistory(final SecurityNode node, final SecurityHistoryNode hNode) {

        em.getTransaction().begin();

        em.persist(node);

        em.getTransaction().commit();

        return true;
    }

    /*
     * @see jgnash.engine.CommodityDAOInterface#setExchangeRate(jgnash.engine.ExchangeRate)
     */
    @Override
    public synchronized void addExchangeRate(ExchangeRate eRate) {
        em.getTransaction().begin();

        em.persist(eRate);

        em.getTransaction().commit();
    }

    @Override
    public synchronized void refreshCommodityNode(CommodityNode node) {
        em.getTransaction().begin();

        em.merge(node);

        em.getTransaction().commit();
    }

    @Override
    public synchronized void refreshExchangeRate(ExchangeRate rate) {
        em.getTransaction().begin();

        em.merge(rate);

        em.getTransaction().commit();
    }

    /*
     * @see jgnash.engine.CommodityDAOInterface#updateCommodityNode(jgnash.engine.CommodityNode)
     */

    @Override
    public synchronized boolean updateCommodityNode(final CommodityNode node) {
        em.getTransaction().begin();

        em.merge(node);

        em.getTransaction().commit();
        return true;
    }

    /*
     * @see jgnash.engine.CommodityDAOInterface#getActiveAccountCommodities()
     */

    @Override
    public synchronized Set<CurrencyNode> getActiveCurrencies() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);
        cq.select(root);

        TypedQuery<Account> q = em.createQuery(cq);

        List<Account> accountList = stripMarkedForRemoval(q.getResultList());

        Set<CurrencyNode> currencies = new HashSet<>();

        for (Account account : accountList) {
            currencies.add(account.getCurrencyNode());

            for (SecurityNode node : account.getSecurities()) {
                currencies.add(node.getReportedCurrencyNode());
            }
        }

        return currencies;
    }
}
