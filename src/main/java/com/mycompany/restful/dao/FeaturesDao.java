/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.restful.dao;

import com.mycompany.restful.model.Features;
import com.mycompany.restful.model.RequestParams;
import com.mycompany.restful.util.HibernateUtil;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;

/**
 *
 * @author ibilanchuk
 */
public class FeaturesDao {
     SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Features getFeatureById(int id) {

        Features feature = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            feature = (Features) session.createQuery("from Features where id= :ID").setParameter("ID", id).uniqueResult();
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
  public Response getFeatures(UriInfo info){
      Object recordsTotal=0, recordsFiltered=0;
      RequestParams param = new RequestParams(Integer.parseInt(info.getQueryParameters().getFirst("length")),Integer.parseInt(info.getQueryParameters().getFirst("start")),
              info.getQueryParameters().getFirst("column"),info.getQueryParameters().getFirst("dir"),info.getQueryParameters().getFirst("value"));
        List feature = null;
        Session session = null;
      

      try{
            session = sessionFactory.openSession();
            session.beginTransaction();

            String query = "from Features";       
            if (!"".equals(param.getValue())){
                
            query += " where renderingEngine like '%"+param.getValue()+"%'";
            query += "or browser like '%"+param.getValue()+"%'";
            query += "or platform like '%"+param.getValue()+"%'";
            query += "or engineVersion like '%"+param.getValue()+"%'";
            query += "or cssGrade like '%"+param.getValue()+"%'";
            }      
           
                query += " order by "+param.getColumn()+" "+param.getDir();

            feature = session.createQuery(query).setMaxResults(param.getLength()).setFirstResult(param.getStart()).list();
            recordsTotal  = session.createQuery("SELECT COUNT(*) FROM Features").list().get(0);
            recordsFiltered = session.createQuery("SELECT COUNT(*)"+query).list().get(0);
        
            session.getTransaction().commit();
            }
        catch (Exception ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return Response.status(200).header("recordsTotal", recordsTotal).header("recordsFiltered", recordsFiltered).entity(feature).build();
    }
  
    public List<Features> getAllFeatures() {

        List<Features> features = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            features = session.createQuery("from Features").list();
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
   
    public List<Features> getRenderingEngine() {

        List<Features> features = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            features = session.createQuery("select renderingEngine, count(renderingEngine) from Features group by renderingEngine").list();
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

    public boolean saveFeature(Features feature) {
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
      public boolean updateFeature(int id,Features feature) {
        Session session = null;
        boolean hasErrors = false;
        feature.setId(id);
    
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
        Features feature = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            feature = (Features) session.createQuery("from Features where id= :ID").setParameter("ID", id).uniqueResult();
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
