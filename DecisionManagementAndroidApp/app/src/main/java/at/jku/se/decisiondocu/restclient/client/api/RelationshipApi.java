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

public class RelationshipApi {
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
   * Adds a relationship to a new decision
   * 
   * @param token token
   * @param name relationship name
   * @param fromNode ID of the first node
   * @param body JSON representation of the second node
   * @return String
   */
  public String  addRelationshipToDecision (String token, String name, Long fromNode, Body body) throws ApiException {
    Object postBody = body;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling addRelationshipToDecision");
    }
    
    // verify the required parameter 'name' is set
    if (name == null) {
       throw new ApiException(400, "Missing the required parameter 'name' when calling addRelationshipToDecision");
    }
    
    // verify the required parameter 'fromNode' is set
    if (fromNode == null) {
       throw new ApiException(400, "Missing the required parameter 'fromNode' when calling addRelationshipToDecision");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
       throw new ApiException(400, "Missing the required parameter 'body' when calling addRelationshipToDecision");
    }
    

    // create path and map variables
    String path = "/relationship/{name}/{fromNode}".replaceAll("\\{format\\}","json").replaceAll("\\{" + "name" + "\\}", apiInvoker.escapeString(name.toString())).replaceAll("\\{" + "fromNode" + "\\}", apiInvoker.escapeString(fromNode.toString()));

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
   * Adds a relationship to an existing decision
   * 
   * @param token token
   * @param name relationship name
   * @param fromNode ID of the first node
   * @param toNode ID of the second noede
   * @return String
   */
  public String  addRelationshipToDecision_1 (String token, String name, Long fromNode, Long toNode) throws ApiException {
    Object postBody = null;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling addRelationshipToDecision_1");
    }
    
    // verify the required parameter 'name' is set
    if (name == null) {
       throw new ApiException(400, "Missing the required parameter 'name' when calling addRelationshipToDecision_1");
    }
    
    // verify the required parameter 'fromNode' is set
    if (fromNode == null) {
       throw new ApiException(400, "Missing the required parameter 'fromNode' when calling addRelationshipToDecision_1");
    }
    
    // verify the required parameter 'toNode' is set
    if (toNode == null) {
       throw new ApiException(400, "Missing the required parameter 'toNode' when calling addRelationshipToDecision_1");
    }
    

    // create path and map variables
    String path = "/relationship/{name}/{fromNode}/{toNode}".replaceAll("\\{format\\}","json").replaceAll("\\{" + "name" + "\\}", apiInvoker.escapeString(name.toString())).replaceAll("\\{" + "fromNode" + "\\}", apiInvoker.escapeString(fromNode.toString())).replaceAll("\\{" + "toNode" + "\\}", apiInvoker.escapeString(toNode.toString()));

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
  
}
