package at.jku.se.decisiondocu.restclient.client.model;

import java.util.Date;
import java.util.Map;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class FormDataContentDisposition  {
  
  @SerializedName("type")
  private String type = null;
  @SerializedName("parameters")
  private Map<String, String> parameters = null;
  @SerializedName("fileName")
  private String fileName = null;
  @SerializedName("creationDate")
  private Date creationDate = null;
  @SerializedName("modificationDate")
  private Date modificationDate = null;
  @SerializedName("readDate")
  private Date readDate = null;
  @SerializedName("size")
  private Long size = null;
  @SerializedName("name")
  private String name = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Map<String, String> getParameters() {
    return parameters;
  }
  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Date getCreationDate() {
    return creationDate;
  }
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Date getModificationDate() {
    return modificationDate;
  }
  public void setModificationDate(Date modificationDate) {
    this.modificationDate = modificationDate;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Date getReadDate() {
    return readDate;
  }
  public void setReadDate(Date readDate) {
    this.readDate = readDate;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Long getSize() {
    return size;
  }
  public void setSize(Long size) {
    this.size = size;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormDataContentDisposition {\n");
    
    sb.append("  type: ").append(type).append("\n");
    sb.append("  parameters: ").append(parameters).append("\n");
    sb.append("  fileName: ").append(fileName).append("\n");
    sb.append("  creationDate: ").append(creationDate).append("\n");
    sb.append("  modificationDate: ").append(modificationDate).append("\n");
    sb.append("  readDate: ").append(readDate).append("\n");
    sb.append("  size: ").append(size).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
