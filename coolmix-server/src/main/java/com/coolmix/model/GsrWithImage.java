package com.coolmix.model;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.core.io.FileSystemResource;

public class GsrWithImage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	@NotNull
    private String imagefilepath;
	@NotNull
    private String imagefilename;
	@NotNull
    private String description;
	@NotNull
    private FileSystemResource gsrImage;
	private Timestamp datecreated;
    private Timestamp datemodified;
 
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImageFilePath() {
		return imagefilepath;
	}

	public void setImageFilePath(String imagefilepath) {
		this.imagefilepath = imagefilepath;
	}

	public String getImageFileName() {
		return imagefilename;
	}

	public void setImageFileName(String imagefilename) {
		this.imagefilename = imagefilename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Timestamp datecreated) {
		this.datecreated = datecreated;
	}

    public FileSystemResource getGsrImage() {
		return gsrImage;
	}

	public void setGsrImage(FileSystemResource gsrImage) {
		this.gsrImage = gsrImage;
	}


	public Timestamp getDatemodified() {
		return datemodified;
	}

	public void setDatemodified(Timestamp datemodified) {
		this.datemodified = datemodified;
	}

	public GsrWithImage(){
        id=0;
        
    }
     
    public GsrWithImage(int id, String imagefilepath, String imagefilename, String description) {
        this.id = id;
        this.imagefilepath = imagefilepath;
        this.imagefilename = imagefilename;
        this.description = description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GsrWithImage other = (GsrWithImage) obj;
        if (id != other.id)
            return false;
        return true;
    }
 
    @Override
    public String toString() {
        return "GsrWithImage [id=" + id + ", imageFilePath=" + imagefilepath + ", imageFileName=" + imagefilename
                + ", description=" + description + ", + datecreated=" 
        		+ datecreated + ", + datemodified=" + datemodified + "]";
    }
 


}
