package at.jku.se.decisiondocu.restclient.client.api;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.se.decisiondocu.restclient.client.ApiException;
import at.jku.se.decisiondocu.restclient.client.ApiInvoker;
import at.jku.se.decisiondocu.restclient.client.Pair;
import at.jku.se.decisiondocu.restclient.client.TypeRef;
import at.jku.se.decisiondocu.restclient.client.model.Body;
import at.jku.se.decisiondocu.restclient.client.model.Decision;

public class DecisionApi {
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
     * Returns a list of all available decisions
     * Returns a list of all available decisions
     *
     * @param token token
     * @return List<String>
     */
    public String getAllDecisions(String token) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling getAllDecisions");
        }


        // create path and map variables
        String path = "/decision".replaceAll("\\{format\\}", "json");

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
            if (response != null) {
                Log.d("tag", response);
                return response;
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Insert or update a certain decision
     *
     * @param token token
     * @param body  Decision to insert/update as JSON
     * @return String
     */
    public String updateDecision(String token, Body body) throws ApiException {
        Object postBody = body;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling updateDecision");
        }

        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException(400, "Missing the required parameter 'body' when calling updateDecision");
        }


        // create path and map variables
        String path = "/decision".replaceAll("\\{format\\}", "json");

        // query params
        List<Pair> queryParams = new ArrayList<Pair>();
        // header params
        Map<String, String> headerParams = new HashMap<String, String>();
        // form params
        Map<String, String> formParams = new HashMap<String, String>();


        headerParams.put("token", ApiInvoker.parameterToString(token));


        String[] contentTypes = {
                "application/json"
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
            if (response != null) {
                return "";
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Returns a list of decisions that belong to a certain project
     *
     * @param token token
     * @param id    ID of the project to fetch
     * @return List<String>
     */
    public String getByProjectName(String token, Long id) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling getByProjectName");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException(400, "Missing the required parameter 'id' when calling getByProjectName");
        }


        // create path and map variables
        String path = "/decision/byProject/{id}".replaceAll("\\{format\\}", "json").replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

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
            if (response != null) {
                return "";
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Returns a single decision
     *
     * @param token token
     * @param id    ID of the decision to fetch
     * @return String
     */
    public String getDecision(String token, Long id) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling getDecision");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException(400, "Missing the required parameter 'id' when calling getDecision");
        }


        // create path and map variables
        String path = "/decision/{id}".replaceAll("\\{format\\}", "json").replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

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
            if (response != null) {
                return "";
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Delete a certain decision
     *
     * @param token token
     * @param id    ID of the decision to delete
     * @return String
     */
    public String deleteDecision(String token, Long id) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling deleteDecision");
        }

        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException(400, "Missing the required parameter 'id' when calling deleteDecision");
        }


        // create path and map variables
        String path = "/decision/{id}".replaceAll("\\{format\\}", "json").replaceAll("\\{" + "id" + "\\}", apiInvoker.escapeString(id.toString()));

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
            if (response != null) {
                return "";
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

}
