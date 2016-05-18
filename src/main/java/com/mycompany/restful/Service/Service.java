/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.restful.Service;

import com.mycompany.restful.dao.FeatureDao;
import com.mycompany.restful.model.Feature;
import com.mycompany.restful.model.RequestParams;

import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 * REST Web Service
 *
 * @author ibilanchuk
 */
@Path("service")
public class Service {

  private final String Success = "Success";
  private final String Error = "Error";

  /**
   * Creates a new instance of Service
   */
  public Service() {
  }
  private FeatureDao featureDao = new FeatureDao();

  @GET
  @Path("/Feature/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFeatureById(@PathParam("id") int id) {
    Feature feature = featureDao.getFeatureById(id);
    if (feature != null) {
      return Response.status(200).entity(feature).build();
    } else {
      return Response.status(404).entity(Error).build();
    }
  }

  @GET
  @Path("/Features")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFeatures(@QueryParam("length") int length, @QueryParam("start") int start, @QueryParam("column") String column, @QueryParam("dir") String dir, @QueryParam("value") String value) {
    RequestParams param = new RequestParams(length, start, column, dir, value);
    JSONObject obj = featureDao.getFeatures(param);
    if (obj != null) {
      return Response.status(200).header("recordsTotal", obj.get("total")).header("recordsFiltered", obj.get("filtered")).entity(obj.get("data")).build();
    } else {
      return Response.status(404).entity(Error).build();
    }
  }

  @GET
  @Path("/RenderingEngine")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRenderingEngine() {
    List<Feature> output = featureDao.getRenderingEngine();
    return Response.status(200).entity(output).build();
  }

  @POST
  @Path("/Feature")
  @Produces(MediaType.APPLICATION_JSON)
  public Response NewFeature(Feature feature) {
    return !featureDao.saveFeature(feature) ? Response.status(200).build()
        : Response.status(304).build();
  }

  @PUT
  @Path("/Feature/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response UpdateFeature(@PathParam("id") int id, Feature feature) {
    feature.setId(id);
    return !featureDao.saveFeature(feature) ? Response.status(200).build()
        : Response.status(304).build();
  }

  @DELETE
  @Path("/Feature/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteFeature(@PathParam("id") int id) {
    return !featureDao.deleteFeature(id) ? Response.status(200).entity(Success).build()
        : Response.status(304).entity(Error).build();
  }
}
