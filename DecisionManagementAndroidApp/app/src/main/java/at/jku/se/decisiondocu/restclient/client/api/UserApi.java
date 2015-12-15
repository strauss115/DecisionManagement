package at.jku.se.decisiondocu.restclient.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.jku.se.decisiondocu.restclient.RestHelper;
import at.jku.se.decisiondocu.restclient.TokenResponse;
import at.jku.se.decisiondocu.restclient.client.ApiException;
import at.jku.se.decisiondocu.restclient.client.ApiInvoker;
import at.jku.se.decisiondocu.restclient.client.Pair;
import at.jku.se.decisiondocu.restclient.client.model.Body;
import at.jku.se.decisiondocu.restclient.client.model.User;

public class UserApi {
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
     * Returns a list of all available users
     * Returns a list of all available users
     *
     * @param token token
     * @return List<String>
     */
    public String getAll(String token) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling getAll");
        }


        // create path and map variables
        String path = "/user".replaceAll("\\{format\\}", "json");

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
     * Login a certain user
     * This API method creates an access key (token) for the user
     *
     * @param eMail    EMail of the user to login
     * @param password Password of the user to login
     * @return String
     */
    public TokenResponse login(String eMail, String password) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'eMail' is set
        if (eMail == null) {
            throw new ApiException(400, "Missing the required parameter 'eMail' when calling login");
        }

        // verify the required parameter 'password' is set
        if (password == null) {
            throw new ApiException(400, "Missing the required parameter 'password' when calling login");
        }


        // create path and map variables
        String path = "/user/login".replaceAll("\\{format\\}", "json");

        // query params
        List<Pair> queryParams = new ArrayList<Pair>();
        // header params
        Map<String, String> headerParams = new HashMap<String, String>();
        // form params
        Map<String, String> formParams = new HashMap<String, String>();


        queryParams.addAll(ApiInvoker.parameterToPairs("", "eMail", eMail));

        queryParams.addAll(ApiInvoker.parameterToPairs("", "password", password));


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
                try {
                    TokenResponse tokenResponse = mapper.readValue(response, TokenResponse.class);
                    return tokenResponse;
                } catch (IOException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Register a new user
     * This API method creates a new user on the server
     *
     * @param firstName First name of the user
     * @param lastName  Last name of the user
     * @param password  Password of the user
     * @param eMail     EMail of the user
     * @return String
     */
    public User register(String firstName, String lastName, String password, String eMail) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'firstName' is set
        if (firstName == null) {
            throw new ApiException(400, "Missing the required parameter 'firstName' when calling register");
        }

        // verify the required parameter 'lastName' is set
        if (lastName == null) {
            throw new ApiException(400, "Missing the required parameter 'lastName' when calling register");
        }

        // verify the required parameter 'password' is set
        if (password == null) {
            throw new ApiException(400, "Missing the required parameter 'password' when calling register");
        }

        // verify the required parameter 'eMail' is set
        if (eMail == null) {
            throw new ApiException(400, "Missing the required parameter 'eMail' when calling register");
        }


        // create path and map variables
        String path = "/user/register".replaceAll("\\{format\\}", "json");

        // query params
        List<Pair> queryParams = new ArrayList<Pair>();
        // header params
        Map<String, String> headerParams = new HashMap<String, String>();
        // form params
        Map<String, String> formParams = new HashMap<String, String>();


        queryParams.addAll(ApiInvoker.parameterToPairs("", "firstName", firstName));

        queryParams.addAll(ApiInvoker.parameterToPairs("", "lastName", lastName));

        queryParams.addAll(ApiInvoker.parameterToPairs("", "password", password));

        queryParams.addAll(ApiInvoker.parameterToPairs("", "eMail", eMail));


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
                try {
                    return mapper.readValue(response, User.class);
                } catch (IOException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Returns a certain user
     * Returns a certain user
     *
     * @param token token
     * @param eMail EMail of the user to fetch
     * @return String
     */
    public User get(String token, String eMail) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling get");
        }

        // verify the required parameter 'eMail' is set
        if (eMail == null) {
            throw new ApiException(400, "Missing the required parameter 'eMail' when calling get");
        }


        // create path and map variables
        String path = "/user/{eMail}".replaceAll("\\{format\\}", "json").replaceAll("\\{" + "eMail" + "\\}", apiInvoker.escapeString(eMail.toString()));

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
                try {
                    return mapper.readValue(response, User.class);
                } catch (IOException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Edit a user
     * This API method edits an existing user
     *
     * @param token token
     * @param eMail EMail of the user
     * @param body  JSON Object of the user
     * @return String
     */
    public User edit(String token, String eMail, Body body) throws ApiException {
        Object postBody = body;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling edit");
        }

        // verify the required parameter 'eMail' is set
        if (eMail == null) {
            throw new ApiException(400, "Missing the required parameter 'eMail' when calling edit");
        }

        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException(400, "Missing the required parameter 'body' when calling edit");
        }


        // create path and map variables
        String path = "/user/{eMail}".replaceAll("\\{format\\}", "json").replaceAll("\\{" + "eMail" + "\\}", apiInvoker.escapeString(eMail.toString()));

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
                try {
                    return mapper.readValue(response, User.class);
                } catch (IOException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Delete a user
     * This API method deletes an existing user
     *
     * @param token token
     * @param eMail EMail of the user
     * @return String
     */
    public User delete(String token, String eMail) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling delete");
        }

        // verify the required parameter 'eMail' is set
        if (eMail == null) {
            throw new ApiException(400, "Missing the required parameter 'eMail' when calling delete");
        }


        // create path and map variables
        String path = "/user/{eMail}".replaceAll("\\{format\\}", "json").replaceAll("\\{" + "eMail" + "\\}", apiInvoker.escapeString(eMail.toString()));

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
            String response = apiInvoker.invokeAPI(basePath, path, "DELETE", queryParams, postBody, headerParams, formParams, contentType);
            if (response != null) {
                try {
                    return mapper.readValue(response, User.class);
                } catch (IOException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

    /**
     * Change the user&#39;s permission
     * This API method can change a users permission from user to admin or vice versa
     *
     * @param token   token
     * @param eMail   EMail of the user
     * @param isAdmin
     * @return String
     */
    public User setPermission(String token, String eMail, Boolean isAdmin) throws ApiException {
        Object postBody = null;

        // verify the required parameter 'token' is set
        if (token == null) {
            throw new ApiException(400, "Missing the required parameter 'token' when calling setPermission");
        }

        // verify the required parameter 'eMail' is set
        if (eMail == null) {
            throw new ApiException(400, "Missing the required parameter 'eMail' when calling setPermission");
        }


        // create path and map variables
        String path = "/user/{eMail}/permission".replaceAll("\\{format\\}", "json").replaceAll("\\{" + "eMail" + "\\}", apiInvoker.escapeString(eMail.toString()));

        // query params
        List<Pair> queryParams = new ArrayList<Pair>();
        // header params
        Map<String, String> headerParams = new HashMap<String, String>();
        // form params
        Map<String, String> formParams = new HashMap<String, String>();


        queryParams.addAll(ApiInvoker.parameterToPairs("", "isAdmin", isAdmin));


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
            if (response != null) {
                try {
                    return mapper.readValue(response, User.class);
                } catch (IOException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (ApiException ex) {
            throw ex;
        }
    }

}
