package at.jku.se.decisiondocu.restclient.client.api;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.se.decisiondocu.restclient.client.ApiException;
import at.jku.se.decisiondocu.restclient.client.ApiInvoker;
import at.jku.se.decisiondocu.restclient.client.Pair;
import at.jku.se.decisiondocu.restclient.client.model.Body;
import at.jku.se.decisiondocu.restclient.client.model.FormDataContentDisposition;

public class UploadApi {
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
   * Upload a document
   * 
   * @param token token
   * @param uploadFile 
   * @param body 
   * @return String
   */
  public String  uploadDocument (String token, File uploadFile, FormDataContentDisposition body) throws ApiException {
    Object postBody = body;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling uploadDocument");
    }
    

    // create path and map variables
    String path = "/upload/document".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();

    

    
    headerParams.put("token", ApiInvoker.parameterToString(token));
    

    String[] contentTypes = {
      "multipart/form-data"
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      
      if (uploadFile != null) {
        builder.addBinaryBody("uploadFile", uploadFile);
      }
      

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
   * Upload a user&#39;s profile picture
   * 
   * @param token token
   * @param uploadFile 
   * @param body 
   * @return String
   */
  public String  uploadProfilPicture (String token, File uploadFile, FormDataContentDisposition body) throws ApiException {
    Object postBody = body;
    
    // verify the required parameter 'token' is set
    if (token == null) {
       throw new ApiException(400, "Missing the required parameter 'token' when calling uploadProfilPicture");
    }
    

    // create path and map variables
    String path = "/upload/profilePicture".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();

    

    
    headerParams.put("token", ApiInvoker.parameterToString(token));
    

    String[] contentTypes = {
      "multipart/form-data"
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      
      if (uploadFile != null) {
        builder.addBinaryBody("uploadFile", uploadFile);
      }
      

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
