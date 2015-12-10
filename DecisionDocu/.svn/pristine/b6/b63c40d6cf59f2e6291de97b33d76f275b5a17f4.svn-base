package at.jku.se.rest.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {"upload"})
@Path("/upload")
public class DocumentResource {
	
	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/home/ubuntu/www/upload_files/";
	
	//private static final String SERVER_UPLOAD_LOCATION_FOLDER = "D://upload_files/";
	
	private static final String LOCATION_PROFILE_PICTURE = SERVER_UPLOAD_LOCATION_FOLDER+"profilpictures/";
	private static final String LOCATION_DOCUMENT = SERVER_UPLOAD_LOCATION_FOLDER+"documents/";
	
	@POST
    @Path("/profilePicture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value = "Upload a user's profile picture", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
    public Response uploadProfilPicture(
    		@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
            @FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition) {
		return uploadFile(fileInputStream,fileFormDataContentDisposition,LOCATION_PROFILE_PICTURE);
 
    }
	
	@POST
    @Path("/document")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value = "Upload a document", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 500, message = "Server Error"),
			@ApiResponse(code = 401, message = "Unauthorized") })
    public Response uploadDocument(
    		@ApiParam(value = "token", required = true) @HeaderParam(value = "token") String token,
            @FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition) {
		return uploadFile(fileInputStream,fileFormDataContentDisposition,LOCATION_DOCUMENT);
 
    }
	
	private Response uploadFile(InputStream fileInputStream,
			FormDataContentDisposition fileFormDataContentDisposition, String path){
		String fileName = null;
        String uploadFilePath = null;
 
        try {
            fileName = fileFormDataContentDisposition.getFileName();
            uploadFilePath = writeToFileServer(fileInputStream, fileName, path);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        finally{
            // release resources, if any
        }
        System.out.println("Saved!!");
        return Response.ok("File uploaded successfully at " + uploadFilePath).build();
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
