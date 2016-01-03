package at.jku.se.decisiondocu.restclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.jku.se.decisiondocu.restclient.client.api.DecisionApi;
import at.jku.se.decisiondocu.restclient.client.api.NodeApi;
import at.jku.se.decisiondocu.restclient.client.api.ProjectApi;
import at.jku.se.decisiondocu.restclient.client.api.UserApi;
import at.jku.se.decisiondocu.restclient.client.model.Decision;
import at.jku.se.decisiondocu.restclient.client.model.Document;
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

    public static User getUser(String email) {
        UserApi api = new UserApi();
        try {
            User user = api.get(accessToken, email);
            Log.d("user", user.toString());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static Decision getDecisionWithId(long id) {
        DecisionApi api = new DecisionApi();
        Decision dec = new Decision();
        try {
            dec = api.getDecision(accessToken, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dec;
    }

    public static String createDecision(Project project) {
        NodeApi api = new NodeApi();
        String node = null;
        try {
            node = api.createSimpleNode(accessToken, project);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return node;
    }
    
    // -----------------------------------------------------------------------------------------
    // PROJECT PART
    // -----------------------------------------------------------------------------------------

    public static List<Project> getMyProjects() {
        ProjectApi api = new ProjectApi();
        List<Project> decs = new ArrayList<>();
        try {
            decs = api.get(accessToken);
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

    public static boolean addUserToProject(String projectId, String projectPassword) {
        ProjectApi api = new ProjectApi();
        try {
            String s = api.addUserToProject(accessToken, projectId, projectPassword);
            if (s != null) return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // -----------------------------------------------------------------------------------------
    // Node PART
    // -----------------------------------------------------------------------------------------

    public static NodeInterface getNodeWithId(long id) {
        NodeApi api = new NodeApi();
        NodeInterface dec = null;
        try {
            dec = api.getSimpleNode(accessToken, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dec;
    }

    // -----------------------------------------------------------------------------------------
    // Document PART
    // -----------------------------------------------------------------------------------------

    private static void safeProfilePicture(Bitmap image, int id){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            System.out.println(uploadProfilePicture(byteArrayOutputStream.toByteArray(), id));
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

    public static Bitmap downloadProfilPicture (long id){
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        InputStream inputStream = null;
        int responseCode;

        try{

            webTarget = RestHelper.getWebTargetWithChunckedFeature();
            webTarget= webTarget.path("upload").path("profilePicture").path(id + "");

            // invoke service
            invocationBuilder = webTarget.request();
            invocationBuilder.header("token", 1);
            response = invocationBuilder.get();

            // get response code
            responseCode = response.getStatus();
            System.out.println("Download Document Response code: " + responseCode);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }

            // read response string
            inputStream = response.readEntity(InputStream.class);
            return BitmapFactory.decodeStream(inputStream);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally{
            response.close();
        }
        return null;
    }

    public static File downloadDocument(Document doc){
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        InputStream inputStream = null;
        int responseCode;

        try{

            webTarget = RestHelper.getWebTargetWithChunckedFeature();
            webTarget= webTarget.path("upload").path("document").path(doc.getId() + "");

            // invoke service
            invocationBuilder = webTarget.request();
            invocationBuilder.header("token", 1);
            response = invocationBuilder.get();

            // get response code
            responseCode = response.getStatus();
            System.out.println("Download Document Response code: " + responseCode);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }

            // read response string
            inputStream = response.readEntity(InputStream.class);

            System.out.println(inputStream.toString());

            File file = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                    + "/" + doc.getName()));

            int count;
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = inputStream.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            //inputStream.close();
            return file;
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally{
            response.close();
        }
        return null;
    }

}
