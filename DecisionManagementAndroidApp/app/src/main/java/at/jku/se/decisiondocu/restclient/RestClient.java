package at.jku.se.decisiondocu.restclient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import at.jku.se.decisiondocu.restclient.client.api.DecisionApi;
import at.jku.se.decisiondocu.restclient.client.api.NodeApi;
import at.jku.se.decisiondocu.restclient.client.api.ProjectApi;
import at.jku.se.decisiondocu.restclient.client.api.RelationshipApi;
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

    static {
        if (USERS.size() == 0) {
            USERS.add("foo@example.com:hello");
            USERS.add("bar@example.com:world");
        }
    }

    // -----------------------------------------------------------------------------------------
    // USER PART
    // -----------------------------------------------------------------------------------------

    /**
     * Gets the user token
     * @param email
     * @param password
     * @return
     */
    public static String getToken(String email, String password) {
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

    /**
     * Register a user
     * @param firstname
     * @param lastname
     * @param email
     * @param password
     * @param bitmap
     * @return
     */
    public static boolean registerUser(String firstname, String lastname, String email, String password, Bitmap bitmap) {
        UserApi api = new UserApi();
        try {
            User user = api.register(firstname, lastname, password, email);
            Log.d("user", "created! (" + user.toString() + ")");
            USERS.add(email + ":" + password);

            if (bitmap != null) {
                saveProfilePicture(bitmap, user.getId());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns the user given by its email
     * @param email
     * @return
     */
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

    /**
     * Gets all decision nodes
     * @return A List of Decision objects
     */
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

    /**
     * Gets a decision by its node id
     * @param id
     * @return
     */
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

    /**
     * Creates a decision for a project
     * @param project
     * @return
     */
    public static NodeInterface createDecision(Project project) {
        NodeApi api = new NodeApi();
        NodeInterface node = null;
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

    /**
     * Gets all projects of the user
     * @return
     */
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

    /**
     * Get all projects
     * @return
     */
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

    /**
     * Adds a user to a project
     * @param projectId
     * @param projectPassword
     * @return
     */
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
    // NODE PART
    // -----------------------------------------------------------------------------------------

    /**
     * Gets a nody by its id
     * @param id
     * @return
     */
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
    // DOCUMENT PART
    // -----------------------------------------------------------------------------------------

    /**
     * Save a profile picture to a node given by its id
     * @param image
     * @param id
     */
    private static void saveProfilePicture(Bitmap image, long id) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            System.out.println(uploadProfilePicture(byteArrayOutputStream.toByteArray(), id));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Save document
     * @param image
     * @param id
     * @return
     */
    public static boolean saveDocument(Bitmap image, long id) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return uploadDocument(byteArrayOutputStream.toByteArray(), id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean uploadDocument(byte[] fileContent, long id) {

        // local variables
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        FormDataMultiPart formDataMultiPart = null;
        int responseCode;
        String responseMessageFromServer = null;
        String responseString = null;

        try {
            // invoke service after setting necessary parameters
            webTarget = RestHelper.getWebTargetWithMultiFeature();
            webTarget = webTarget.path("upload").path("document").path(id + "");
            Log.i("URI", webTarget.getUri().getHost() + webTarget.getUri().getPath());

            // set file upload values
            formDataMultiPart = new FormDataMultiPart();
            StreamDataBodyPart bodyPart = new StreamDataBodyPart("uploadFile", new ByteArrayInputStream(fileContent), id + ".jpg", MediaType.APPLICATION_OCTET_STREAM_TYPE);
            formDataMultiPart.bodyPart(bodyPart);

            invocationBuilder = webTarget.request();
            invocationBuilder.header("token", accessToken);
            response = invocationBuilder.post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA));

            // get response code
            responseCode = response.getStatus();
            System.out.println("Response code: " + responseCode);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }

            // get response message
            responseMessageFromServer = response.getStatusInfo().getReasonPhrase();
            System.out.println("ResponseMessageFromServer: " + responseMessageFromServer);


        } catch (Exception ex) {
            throw ex;
        } finally {
            // release resources, if any
            //streamDataBodyPart.cleanup();
            formDataMultiPart.cleanup();
            try {
                formDataMultiPart.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.close();
        }
        return true;
    }

    private static String uploadProfilePicture(byte[] fileContent, long id) throws Exception {
        // local variables
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        FormDataMultiPart formDataMultiPart = null;
        int responseCode;
        String responseMessageFromServer = null;
        String responseString = null;

        try {
            // invoke service after setting necessary parameters
            webTarget = RestHelper.getWebTargetWithMultiFeature();
            webTarget = webTarget.path("upload").path("profilePicture").path(id + "");
            Log.i("URI", webTarget.getUri().getHost() + webTarget.getUri().getPath());

            // set file upload values
            formDataMultiPart = new FormDataMultiPart();
            StreamDataBodyPart bodyPart = new StreamDataBodyPart("uploadFile", new ByteArrayInputStream(fileContent), id + ".jpg", MediaType.APPLICATION_OCTET_STREAM_TYPE);
            formDataMultiPart.bodyPart(bodyPart);

            invocationBuilder = webTarget.request();
            invocationBuilder.header("token", 1);
            response = invocationBuilder.post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA));

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
        } catch (Exception ex) {
            throw ex;
        } finally {
            // release resources, if any
            //streamDataBodyPart.cleanup();
            formDataMultiPart.cleanup();
            formDataMultiPart.close();
            response.close();
        }
        return responseString;
    }

    /**
     * Gets the profile picture of a node given by its id
     * @param id
     * @return
     */
    public static Bitmap downloadProfilPicture(long id) {
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        InputStream inputStream = null;
        int responseCode;

        try {

            webTarget = RestHelper.getWebTargetWithMultiFeature();
            webTarget = webTarget.path("upload").path("profilePicture").path(id + "");

            // invoke service
            invocationBuilder = webTarget.request();
            invocationBuilder.header("token", 1);
            response = invocationBuilder.get();

            // get response code
            responseCode = response.getStatus();

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }

            String responseString = response.readEntity(String.class);
            JSONObject json = new JSONObject(responseString);
            Log.d("json", json.toString());

            byte[] imageAsBytes = Base64.decode(json.getString("data").getBytes(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            response.close();
        }
        return null;
    }

    /**
     * Gets the document file
     * @param doc
     * @return
     */
    public static File downloadDocument(Document doc) {
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        int responseCode;

        try {

            webTarget = RestHelper.getWebTargetWithMultiFeature();
            webTarget = webTarget.path("upload").path("document").path(doc.getId() + "");

            // invoke service
            invocationBuilder = webTarget.request();
            invocationBuilder.header("token", 1);
            response = invocationBuilder.get();

            // get response code
            responseCode = response.getStatus();

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }

            String responseString = response.readEntity(String.class);
            JSONObject json = new JSONObject(responseString);
            Log.d("json", json.toString());

            byte[] bytes = Base64.decode(json.getString("data").getBytes(), Base64.DEFAULT);

            File file = new File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                    + "/" + doc.getName()));

            OutputStream output = new FileOutputStream(file, false);
            output.write(bytes);
            output.flush();
            output.close();
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            response.close();
        }
        return null;
    }

    // -----------------------------------------------------------------------------------------
    // RELATIONSHIP PART
    // -----------------------------------------------------------------------------------------

    /**
     * Gets all possible relationship strings of the backend
     *
     * @return
     */
    public static Map<String, String> getRelationshipStrings() {
        RelationshipApi api = new RelationshipApi();
        Map<String, String> ret = new HashMap<>();
        try {
            ret = api.getRelationshipStrings(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}
