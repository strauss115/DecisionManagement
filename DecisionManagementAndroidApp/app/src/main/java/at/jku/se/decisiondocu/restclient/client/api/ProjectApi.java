package at.jku.se.decisiondocu.restclient.client.api;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.se.decisiondocu.restclient.client.ApiException;
import at.jku.se.decisiondocu.restclient.client.ApiInvoker;
import at.jku.se.decisiondocu.restclient.client.Pair;
import at.jku.se.decisiondocu.restclient.client.model.Body;

public class ProjectApi {
  String basePath = "http://localhost:8080/DecisionDocu/api";
  ApiInvoker apiInvoker = ApiInvoker.getInstance();

  public void addHeader(String key, String value) {
    getInvoker().addDefaultHeader(key, value);
  }

  public ApiInvoker getInvoker() {
    return apiInvoker;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getBasePath() {
    return basePath;
  }

  
  /**
   * Returns all project memberships for a user
   * With this API method you can retreave all project memberships for a user
   * @param token token
   * @return List<String>
   */
  public String  get (String token) throws ApiException {
    Object postBody = null;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling get");
    }
    

    // create path and map variables
    String path = "/project".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();

    

    
    headerParams.put("token", ApiInvoker.parameterToString(token));
    

    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = builder.build();
      postBody = httpEntity;
    } else {
      // normal form params
      
    }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType);
      if(response != null){
        return "";
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * Creates a new project
   * With this API method you can create a new project
   * @param token token
   * @param body JSON Representation of the project
   * @return String
   */
  public String  create (String token, Body body) throws ApiException {
    Object postBody = body;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling create");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
       throw new ApiException(400, "Missing the required parameter 'body' when calling create");
    }
    

    // create path and map variables
    String path = "/project".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();

    

    
    headerParams.put("token", ApiInvoker.parameterToString(token));
    

    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = builder.build();
      postBody = httpEntity;
    } else {
      // normal form params
      
    }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType);
      if(response != null){
        return "";
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * Adds a user to a project group
   * With this API method you can add a single user to a project group
   * @param token token
   * @param projectId ID of the project
   * @param projectPassword Password of the project group
   * @return String
   */
  public String  addUserToProject (String token, String projectId, String projectPassword) throws ApiException {
    Object postBody = null;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling addUserToProject");
    }
    
    // verify the required parameter 'projectId' is set
    if (projectId == null) {
       throw new ApiException(400, "Missing the required parameter 'projectId' when calling addUserToProject");
    }
    
    // verify the required parameter 'projectPassword' is set
    if (projectPassword == null) {
       throw new ApiException(400, "Missing the required parameter 'projectPassword' when calling addUserToProject");
    }
    

    // create path and map variables
    String path = "/project/addUser".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();

    
    queryParams.addAll(ApiInvoker.parameterToPairs("", "projectId", projectId));
    
    queryParams.addAll(ApiInvoker.parameterToPairs("", "projectPassword", projectPassword));
    

    
    headerParams.put("token", ApiInvoker.parameterToString(token));
    

    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = builder.build();
      postBody = httpEntity;
    } else {
      // normal form params
      
    }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "PUT", queryParams, postBody, headerParams, formParams, contentType);
      if(response != null){
        return "";
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * Returns all available projects
   * With this API method you can retreave all available project nodes from the database
   * @param token token
   * @return List<String>
   */
  public String  getAll (String token) throws ApiException {
    Object postBody = null;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling getAll");
    }
    

    // create path and map variables
    String path = "/project/all".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();

    

    
    headerParams.put("token", ApiInvoker.parameterToString(token));
    

    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = builder.build();
      postBody = httpEntity;
    } else {
      // normal form params
      
    }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType);
      if(response != null){
        return "";
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * Returns a single project
   * With this API method you can retreave a single project
   * @param token token
   * @param id ID of the project to fetch
   * @return String
   */
  public String  get_1 (String token, Long id) throws ApiException {
    Object postBody = null;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling get_1");
    }
    
    // verify the required parameter 'id' is set
    if (id == null) {
       throw new ApiException(400, "Missing the required parameter 'id' when calling get_1");
    }
    

    // create path and map variables
    String path = "/project/{id}".replaceAll("\\{format\\}","json").replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();

    

    
    headerParams.put("token", ApiInvoker.parameterToString(token));
    

    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = builder.build();
      postBody = httpEntity;
    } else {
      // normal form params
      
    }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType);
      if(response != null){
        return "";
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * Deletes a project
   * With this API method you can delete a project
   * @param token token
   * @param id ID of the project to delete
   * @return String
   */
  public String  delete (String token, Long id) throws ApiException {
    Object postBody = null;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling delete");
    }
    
    // verify the required parameter 'id' is set
    if (id == null) {
       throw new ApiException(400, "Missing the required parameter 'id' when calling delete");
    }
    

    // create path and map variables
    String path = "/project/{id}".replaceAll("\\{format\\}","json").replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();

    

    
    headerParams.put("token", ApiInvoker.parameterToString(token));
    

    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = builder.build();
      postBody = httpEntity;
    } else {
      // normal form params
      
    }

    try {
      String response = apiInvoker.invokeAPI(basePath, path, "DELETE", queryParams, postBody, headerParams, formParams, contentType);
      if(response != null){
        return "";
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
}
