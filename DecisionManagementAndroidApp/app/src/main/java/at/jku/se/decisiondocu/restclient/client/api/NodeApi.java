package at.jku.se.decisiondocu.restclient.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.se.decisiondocu.restclient.RestHelper;
import at.jku.se.decisiondocu.restclient.client.ApiException;
import at.jku.se.decisiondocu.restclient.client.ApiInvoker;
import at.jku.se.decisiondocu.restclient.client.Pair;
import at.jku.se.decisiondocu.restclient.client.model.Body;

public class NodeApi {
    String basePath = RestHelper.GetBaseURL();
    ApiInvoker apiInvoker = new ApiInvoker();
    ObjectMapper mapper = new ObjectMapper();

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
     * Creates a new node
     * With this API method you can create a new node in the database
     *
     * @param token token
     * @param body  JSON Object to insert in the database
     * @return String
     */
    public String createSimpleNode(String token, Body body) throws ApiException {
        Object postBody = body;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling createSimpleNode");
        }

        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException(400, "Missing the required parameter 'body' when calling createSimpleNode");
        }


        // create path and map variables
        String path = "/node".replaceAll("\\{format\\}", "json");

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
