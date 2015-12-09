package at.jku.se.decisiondocu.restclient;

import android.graphics.Bitmap;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.jku.se.decisiondocu.restclient.client.ApiException;
import at.jku.se.decisiondocu.restclient.client.ApiInvoker;
import at.jku.se.decisiondocu.restclient.client.api.DecisionApi;
import at.jku.se.decisiondocu.restclient.client.api.ProjectApi;
import at.jku.se.decisiondocu.restclient.client.api.UserApi;
import at.jku.se.decisiondocu.restclient.client.model.Decision;
import at.jku.se.decisiondocu.restclient.client.model.NodeInterface;
import at.jku.se.decisiondocu.restclient.client.model.Project;
import at.jku.se.decisiondocu.restclient.client.model.User;

/**
 * Created by Benjamin on 18.11.2015.
 */
public class RestClient {

    //private final static String httpURL = "http://localhost:8080/DecisionDocu/api/document/upload";

    public static String accessToken = null; //"g0up9ej1egkmrtveig59ke0adf";

    public static List<String> USERS = new ArrayList<String>();
    static{
        if(USERS.size()==0) {
            USERS.add("foo@example.com:hello");
            USERS.add("bar@example.com:world");
        }
    }

    // -----------------------------------------------------------------------------------------
    // USER PART
    // -----------------------------------------------------------------------------------------

    public static String getToken(String email, String password){
        UserApi api = new UserApi();
        try {
            TokenResponse response = api.login(email, password);
            accessToken = response.getAccess_token();
            return response.getAccess_token();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean registerUser(String firstname, String lastname, String email, String password, Bitmap profil){
        UserApi api = new UserApi();
        try {
            User user = api.register(firstname, lastname, password, email);
            Log.d("user", "created! (" + user.toString() + ")");
            USERS.add(email + ":" + password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // -----------------------------------------------------------------------------------------
    // DECISION PART
    // -----------------------------------------------------------------------------------------

    public static List<Decision> getAllDecisions() {
        DecisionApi api = new DecisionApi();
        List<Decision> decs = new ArrayList<>();
        try {
            decs = api.getAllDecisions(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decs;
    }


    public static List<Project> getAllProjects() {
        ProjectApi api = new ProjectApi();
        List<Project> decs = new ArrayList<>();
        try {
            decs = api.getAll(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decs;
    }


    private static void safeProfilePicture(Bitmap image, int id){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            System.out.println(uploadProfilePicture(byteArrayOutputStream.toByteArray(),id));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String uploadProfilePicture(byte[] fileContent, int id)throws Exception {
        // local variables
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        FormDataMultiPart formDataMultiPart = null;
        int responseCode;
        String responseMessageFromServer = null;
        String responseString = null;

        try{
            // invoke service after setting necessary parameters
            webTarget = RestHelper.getWebTargetWithMultiFeature();
            webTarget= webTarget.path("upload").path("profilePicture");
            Log.i("URI", webTarget.getUri().getHost() + webTarget.getUri().getPath());

            // set file upload values
            //streamDataBodyPart = new StreamDataBodyPart("uploadFile", inputStream, "01.jpeg", MediaType.APPLICATION_OCTET_STREAM_TYPE);
            formDataMultiPart = new FormDataMultiPart();
            StreamDataBodyPart bodyPart =new StreamDataBodyPart("uploadFile",new ByteArrayInputStream(fileContent),id+".jpg",MediaType.APPLICATION_OCTET_STREAM_TYPE);
            //formDataMultiPart.field("uploadFile", fileContent, MediaType.APPLICATION_OCTET_STREAM_TYPE);

            formDataMultiPart.bodyPart(bodyPart);

            response = webTarget.request().post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA));

            // get response code
            responseCode = response.getStatus();
            System.out.println("Response code: " + responseCode);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }

            // get response message
            responseMessageFromServer = response.getStatusInfo().getReasonPhrase();
            System.out.println("ResponseMessageFromServer: " + responseMessageFromServer);

            // get response string
            responseString = response.readEntity(String.class);
        }
        catch(Exception ex) {
            throw ex;
        }
        finally{
            // release resources, if any
            //streamDataBodyPart.cleanup();
            formDataMultiPart.cleanup();
            formDataMultiPart.close();
            response.close();
        }
        return responseString;


    }

}
