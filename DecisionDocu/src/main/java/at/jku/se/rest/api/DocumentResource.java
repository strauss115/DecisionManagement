package at.jku.se.rest.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.dm.shared.RelationString;
import at.jku.se.model.Document;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Project;
import at.jku.se.model.RelationshipInterface;
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
	
	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C://";
	
	private static final String LOCATION_PROFILE_PICTURE = SERVER_UPLOAD_LOCATION_FOLDER+"profilpictures/";
	private static final String LOCATION_DOCUMENT = SERVER_UPLOAD_LOCATION_FOLDER+"documents/";
	
	@GET
	@Path("/profilePicture/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@ApiOperation(value = "Upload a user's profile picture", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response getProfilPicture(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the related node", required = true) @PathParam("id") final long id) {
		log.info("Get Profile Picture invoked ...");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			
			return Response.ok(new StreamingOutput(){
			    @Override
			        public void write(OutputStream arg0) throws IOException, WebApplicationException {
			            BufferedOutputStream bus = new BufferedOutputStream(arg0);
			            try {
			                //ByteArrayInputStream reader = (ByteArrayInputStream) Thread.currentThread().getContextClassLoader().getResourceAsStream();     
			                //byte[] input = new byte[2048];  
			                //java.net.URL uri = Thread.currentThread().getContextClassLoader().getResource("");
			                File file = new File(LOCATION_PROFILE_PICTURE+id);
			                FileInputStream fizip = new FileInputStream(file);
			                byte[] buffer2 = IOUtils.toByteArray(fizip);
			                bus.write(buffer2);
			            } catch (Exception e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			            }
			        }
			    }).build();

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
			NodeInterface node = DBService.getNodeByID(NodeInterface.class, id, 1);
			if(node==null){
				return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
			}
			if(node.getRelationships().containsKey(RelationString.HAS_PICTURE)){
				for(RelationshipInterface rel:node.getRelationships().get(RelationString.HAS_PICTURE)){
					DBService.deleteRelationship(rel.getId());
					//Node und Foto wird nicht gelöscht
				}
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
	
	public static void writeFile(InputStream fileInputStream, OutputStream outputStream) throws IOException {
		try {
		  byte[] buffer = new byte[1024];
		  int bytesRead;

		  while((bytesRead = fileInputStream.read(buffer)) !=-1) {
		      outputStream.write(buffer, 0, bytesRead);
		  }

		  fileInputStream.close();
		  outputStream.flush();
		 } catch (IOException e) {
		   e.printStackTrace();
		 } finally {
		   outputStream.close();
		}
	}
	
	public static void main (String[]args){
		
		//Add ProfilPicture to Projects
		User admin = DBService.getUserByEmail("admin@example.com");
		
		Document document = new Document("Profile Picture");
		document = DBService.updateNode(document, admin.getId());
		
		File destFile = new File(LOCATION_PROFILE_PICTURE+document.getId());
		File srcFile = new File(SERVER_UPLOAD_LOCATION_FOLDER+"profil");
		
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Project> projects = DBService.getAllProjects();
		for(Project project:projects){
			DBService.addRelationship(project.getId(), RelationString.HAS_PICTURE, document.getId());
		}
		
		/*try{
		Client client = ClientBuilder.newClient(new ClientConfig());
		  client.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
		  WebTarget target = client.target(URI.create("http://localhost:8080/DecisionDocu/api/upload/profilePicture/6302"));
		  File file = new File(LOCATION_PROFILE_PICTURE+"test.png");
		  OutputStream fileOutputStream = new FileOutputStream(file);
		  InputStream fileInputStream = target.request().header("token", 1).get(InputStream.class);
		  writeFile(fileInputStream, fileOutputStream);
		}catch (Exception e){
			e.printStackTrace();
		}*/
	}
	

}
