/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.restful.dao;

import com.mycompany.restful.model.Feature;
import com.mycompany.restful.model.RequestParams;
import com.mycompany.restful.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.simple.JSONObject;

/**
 *
 * @author ibilanchuk
 */
public class FeatureDao {

  SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

  public Feature getFeatureById(int id) {
    Feature feature = null;
    Session session = null;
    try {
      session = sessionFactory.openSession();
      session.beginTransaction();
      feature = (Feature) session.createQuery("from Feature where id= :ID").setParameter("ID", id).uniqueResult();
      session.getTransaction().commit();
    } catch (Exception ex) {
      if (session != null) {
        session.getTransaction().rollback();
      }
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return feature;
  }

  public JSONObject getFeatures(RequestParams param) {
    Object recordsFiltered = 0, recordsTotal = 0;
    Criteria Features;
    List<Feature> features;
    Session session = null;
    JSONObject obj = new JSONObject();

    try {
      session = sessionFactory.openSession();
      session.beginTransaction();
      Features = session.createCriteria(Feature.class);
     
      if (!"".equals(param.getValue()) && param.getValue() != null) {
        Criterion rest1 = Restrictions.like("renderingEngine","%" +  param.getValue() + "%"); 
        Criterion rest2 = Restrictions.like("browser", "%" + param.getValue() + "%");
        Criterion rest3 = Restrictions.like("platform", "%" + param.getValue() + "%");
        Criterion rest4 = Restrictions.like("engineVersion", "%" +  param.getValue() + "%");
        Criterion rest5 = Restrictions.like("cssGrade", "%" +  param.getValue() + "%");
       
        Features.add(Restrictions.or(rest1, rest2, rest3, rest4, rest5));
      }
      if (!"".equals(param.getColumn()) && param.getColumn() != null) {
        if ("asc".equals(param.getDir())) {
          Features.addOrder(Order.asc(param.getColumn()));
        } else {
          Features.addOrder(Order.desc(param.getColumn()));
        }
      }
      recordsTotal = session.createCriteria(Feature.class).setProjection(Projections.rowCount()).list().get(0);
      recordsFiltered = Features.list().size();
      Features.setFirstResult(param.getStart()).setMaxResults(param.getLength());
      features = Features.list();
      obj.put("total", recordsTotal);
      obj.put("filtered", recordsFiltered);
      obj.put("data", features);
      session.getTransaction().commit();
    } catch (Exception ex) {
      if (session != null) {
        session.getTransaction().rollback();
      }
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return obj;
  }

  public List<Feature> getRenderingEngine() {

    List<Feature> features = null;
    Session session = null;

    try {
      session = sessionFactory.openSession();
      session.beginTransaction();
      features = session.createQuery("select renderingEngine, count(renderingEngine) from Feature group by renderingEngine").list();
      session.getTransaction().commit();
    } catch (Exception ex) {
      if (session != null) {
        session.getTransaction().rollback();
      }
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return features;
  }

  public boolean saveFeature(Feature feature) {
    Session session = null;
    boolean hasErrors = false;

    try {
      session = sessionFactory.openSession();
      session.beginTransaction();
      session.saveOrUpdate(feature);
      session.getTransaction().commit();
    } catch (Exception ex) {
      if (session != null) {
        session.getTransaction().rollback();
        hasErrors = true;
      }
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return hasErrors;
  }

  public boolean deleteFeature(int id) {
    Session session = null;
    boolean hasErrors = false;
    Feature feature = null;
    try {
      session = sessionFactory.openSession();
      session.beginTransaction();
      feature = (Feature) session.createQuery("from Feature where id= :ID").setParameter("ID", id).uniqueResult();
      session.delete(feature);
      session.getTransaction().commit();
    } catch (Exception ex) {
      if (session != null) {
        session.getTransaction().rollback();
        hasErrors = true;
      }
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return hasErrors;
  }
}
