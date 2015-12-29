package at.jku.se.rest.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.database.strings.RelationString;
import at.jku.se.model.Document;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.User;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {"upload"})
@Path("/upload")
public class DocumentResource {
	
	private static final Logger log = LogManager.getLogger(DecisionResource.class);
	
	//private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/home/ubuntu/www/upload_files/";
	
	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "D://upload_files/";
	
	private static final String LOCATION_PROFILE_PICTURE = SERVER_UPLOAD_LOCATION_FOLDER+"profilpictures/";
	private static final String LOCATION_DOCUMENT = SERVER_UPLOAD_LOCATION_FOLDER+"documents/";
	
	@GET
	@Path("/profilePicture/{id}")
	@Produces({"image/png", "image/jpeg", "image/gif"})
	@ApiOperation(value = "Upload a user's profile picture", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response getProfilPicture(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the related node", required = true) @PathParam("id") long id) {
		log.info("Get Profile Picture invoked ...");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			
			
			File file = new File(LOCATION_PROFILE_PICTURE+id+".jpg");
			String mt = new MimetypesFileTypeMap().getContentType(file);
			return Response.ok(file, mt).build();
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	@POST
    @Path("/profilePicture/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value = "Upload a user's profile picture", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
    public Response uploadProfilPicture(
    		@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
            @FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition,
            @ApiParam(value = "ID of the related node", required = true) @PathParam("id") long id) {
		log.info("Upload Profile Picture invoked ...");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			NodeInterface node = DBService.getNodeByID(NodeInterface.class, id, 0);
			if(node==null){
				return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
			}
			Document doc = new Document();
			doc.setName(fileFormDataContentDisposition.getFileName());
			doc = DBService.createRelationshipWithNode(doc, RelationString.HAS_PICTURE, node.getId(), user.getId());
			return uploadFile(doc,fileInputStream,fileFormDataContentDisposition,LOCATION_PROFILE_PICTURE);
			
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
    }
	
	@POST
    @Path("/document/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value = "Upload a document", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
    public Response uploadDocument(
    		@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
            @FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition,
            @ApiParam(value = "ID of the related node", required = true) @PathParam("id") long id) {
		log.info("Upload Document invoked ...");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			User user = SessionManager.getUser(token);
			NodeInterface node = DBService.getNodeByID(NodeInterface.class, id, 0);
			if(node==null){
				return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
			}
			Document doc = new Document();
			doc.setName(fileFormDataContentDisposition.getFileName());
			doc = DBService.createRelationshipWithNode(doc, RelationString.HAS_DOCUMENT, node.getId(), user.getId());
			return uploadFile(doc,fileInputStream,fileFormDataContentDisposition,LOCATION_DOCUMENT);
			
		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
 
    }
	
	private Response uploadFile(Document doc, InputStream fileInputStream,
			FormDataContentDisposition fileFormDataContentDisposition, String path){
		String fileName = null;
        String uploadFilePath = null;
 
        try {
            fileName = doc.getId()+"";
            uploadFilePath = writeToFileServer(fileInputStream, fileName, path);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        finally{
            // release resources, if any
        }
        System.out.println("Saved!!");
        return RestResponse.getSuccessResponse(doc);
	}

	private String writeToFileServer(InputStream inputStream, String fileName, String path) throws IOException {
		System.out.println(path);
        OutputStream outputStream = null;
        String qualifiedUploadFilePath = path + fileName;
 
        try {
        	File file=new File(qualifiedUploadFilePath);
        	if(!file.getParentFile().exists()){
        		file.getParentFile().mkdirs();
        	}
            outputStream = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally{
            //release resource, if any
            outputStream.close();
        }
        return qualifiedUploadFilePath;
    }
	

}
