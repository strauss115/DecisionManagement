package at.jku.se.decisiondocu.restclient.client.api;


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

public class ChatApi {
    String basePath = RestHelper.GetBaseURL();
    ApiInvoker apiInvoker = new ApiInvoker();

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
     * Returns a list of all available chats
     *
     * @return void
     */
    public void getAll() throws ApiException {
        Object postBody = null;


        // create path and map variables
        String path = "/chat/all".replaceAll("\\{format\\}", "json");

        // query params
        List<Pair> queryParams = new ArrayList<Pair>();
        // header params
        Map<String, String> headerParams = new HashMap<String, String>();
        // form params
        Map<String, String> formParams = new HashMap<String, String>();


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
                return;
            } else {
                return;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

}
