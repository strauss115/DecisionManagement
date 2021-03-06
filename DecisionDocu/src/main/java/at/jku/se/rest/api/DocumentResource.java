package at.jku.se.rest.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import at.jku.se.auth.SessionManager;
import at.jku.se.database.DBService;
import at.jku.se.dm.shared.RelationString;
import at.jku.se.model.Alternative;
import at.jku.se.model.Consequence;
import at.jku.se.model.Decision;
import at.jku.se.model.Document;
import at.jku.se.model.NodeInterface;
import at.jku.se.model.Project;
import at.jku.se.model.RelationshipInterface;
import at.jku.se.model.User;
import at.jku.se.rest.DirectoryManager.DirectoryManager;
import at.jku.se.rest.response.HttpCode;
import at.jku.se.rest.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * API Class for document
 * @author August
 *
 */
@Api(tags = {"upload"})
@Path("/upload")
public class DocumentResource {
	
	private static final Logger log = LogManager.getLogger(DecisionResource.class);
	
	//private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/home/ubuntu/www/upload_files/";
	
	private static String SERVER_UPLOAD_LOCATION_FOLDER = "D://upload_files/";
	
	static{
		try{
			SERVER_UPLOAD_LOCATION_FOLDER = DirectoryManager.getDirectory().getAbsolutePath()+"/upload_files/";
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private static String LOCATION_PROFILE_PICTURE = SERVER_UPLOAD_LOCATION_FOLDER+"profilpictures/";
	private static String LOCATION_DOCUMENT = SERVER_UPLOAD_LOCATION_FOLDER+"documents/";
	
	/**
	 * Get node's profile picture
	 * @param token
	 * @param id
	 * @return The profile picture of the document
	 */
	@GET
	@Path("/profilePicture/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get node's profile picture", response = Response.class)
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
			
			File file = new File(LOCATION_PROFILE_PICTURE+id);
            FileInputStream fizip = new FileInputStream(file);
            byte[] buffer2 = IOUtils.toByteArray(fizip);
            
            String fileName = DBService.getNodeByID(Document.class, id, 0).getName();
            String mimeType = URLConnection.guessContentTypeFromName(fileName);
            
            if (mimeType == null) {
            	fileName = "test.jpg";
            	mimeType = URLConnection.guessContentTypeFromName(fileName);
            }
            log.debug("filename: '" + fileName + "', type: '" + mimeType + "'");

            String json = "{\"type\": \"" + mimeType + "\", \"data\": \"";
            
            String s = new String(java.util.Base64.getEncoder().encode(buffer2));
     		
            return Response.ok(json + s + "\"}").build();
			
			

		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Get node's document
	 * @param token
	 * @param id
	 * @return Get node's document
	 */
	@GET
	@Path("/document/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get node's document", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
	public Response getDocument(
			@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
			@ApiParam(value = "ID of the related node", required = true) @PathParam("id") final long id) {
		log.info("Get node's document invoked ...");
		try {
			if(!SessionManager.verifySession(token)){
				return RestResponse.getResponse(HttpCode.HTTP_401_UNAUTHORIZED);
			}
			
			File file = new File(LOCATION_DOCUMENT+id);
            FileInputStream fizip = new FileInputStream(file);
            byte[] buffer2 = IOUtils.toByteArray(fizip);
            
            String fileName = DBService.getNodeByID(Document.class, id, 0).getName();
            String mimeType = URLConnection.guessContentTypeFromName(fileName);
            
            if (mimeType == null) {
            	fileName = "test.jpg";
            	mimeType = URLConnection.guessContentTypeFromName(fileName);
            }
            
            log.debug("filename: '" + fileName + "', type: '" + mimeType + "'");
            String json = "{\"name\":\""+ fileName + "\", \"type\": \"" + mimeType + "\", \"data\": \"";
            
            String s = new String(java.util.Base64.getEncoder().encode(buffer2));
     		
            return Response.ok(json + s + "\"}").build();
			
			/*return Response.ok(new StreamingOutput(){
			    @Override
			        public void write(OutputStream arg0) throws IOException, WebApplicationException {
			            BufferedOutputStream bus = new BufferedOutputStream(arg0);
			            try {
			                //ByteArrayInputStream reader = (ByteArrayInputStream) Thread.currentThread().getContextClassLoader().getResourceAsStream();     
			                //byte[] input = new byte[2048];  
			                //java.net.URL uri = Thread.currentThread().getContextClassLoader().getResource("");
			                File file = new File(LOCATION_DOCUMENT+id);
			                FileInputStream fizip = new FileInputStream(file);
			                byte[] buffer2 = IOUtils.toByteArray(fizip);
			                bus.write(buffer2);
			            } catch (Exception e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			            }
			        }
			    }).build();*/

		} catch (Exception e) {
			log.debug("Error occured!", e);
			return RestResponse.getResponse(HttpCode.HTTP_500_SERVER_ERROR);
		}
	}
	
	/**
	 * Upload a user's profile picture
	 * @param token
	 * @param fileInputStream
	 * @param fileFormDataContentDisposition
	 * @param id
	 * @return Upload a user's profile picture
	 */
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
	
	/**
	 * Upload a document
	 * @param token
	 * @param fileInputStream
	 * @param fileFormDataContentDisposition
	 * @param id
	 * @return Upload a document
	 */
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
	
	/**
	 * Writes fileInputStream to outputStream
	 * @param fileInputStream
	 * @param outputStream
	 * @throws IOException
	 */
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
	

	/**
	 * Main method of the class
	 * @param args
	 */
	public static void main (String[]args){
		
		User admin = DBService.getUserByEmail("admin@example.com");
		
		//Add Document pdf to Alternatives
		
		Document document1 = new Document ("Effizientes Erfassen von Architekturentscheidungen.pdf");
		document1 = DBService.updateNode(document1, admin.getId());
		
		File destFile1 = new File(LOCATION_DOCUMENT+document1.getId());
		File srcFile1 = new File(SERVER_UPLOAD_LOCATION_FOLDER+"Effizientes Erfassen von Architekturentscheidungen.pdf");
		
		try {
			FileUtils.copyFile(srcFile1, destFile1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List <Alternative> alts = DBService.getAllNodesOfType(Alternative.class, 0);
		for(Alternative alt:alts){
			DBService.addRelationship(alt.getId(), RelationString.HAS_DOCUMENT, document1.getId());
		}
		
		//Add Document jpg to Consequences
		
		Document document2 = new Document ("Document.jpg");
		document2 = DBService.updateNode(document2, admin.getId());
		
		File destFile2 = new File(LOCATION_DOCUMENT+document2.getId());
		File srcFile2 = new File(SERVER_UPLOAD_LOCATION_FOLDER+"document.jpg");
		
		try {
			FileUtils.copyFile(srcFile2, destFile2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List <Consequence> cons = DBService.getAllNodesOfType(Consequence.class, 0);
		for(Consequence con:cons){
			DBService.addRelationship(con.getId(), RelationString.HAS_DOCUMENT, document2.getId());
		}
		
		
		//Add ProfilePicture to Projects and Users
		
		Document document = new Document("Profile Picture.jpg");
		document = DBService.updateNode(document, admin.getId());
		
		File destFile = new File(LOCATION_PROFILE_PICTURE+document.getId());
		File srcFile = new File(SERVER_UPLOAD_LOCATION_FOLDER+"profil.jpg");
		
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
		
		List<User> users = DBService.getAllUser();
		for(User user:users){
			DBService.addRelationship(user.getId(), RelationString.HAS_PICTURE, document.getId());
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