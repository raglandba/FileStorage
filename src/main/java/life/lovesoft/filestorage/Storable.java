/*
 * Copyright (C) Love LLC incorporated in the State of Maryland - All Rights Reserved
 * Unauthorized viewing of this file, via any medium is strictly prohibited
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Unauthorized editing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Brandon Alexander Ragland <brandon.ragland@lovesoft.life>
 */
package life.lovesoft.filestorage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author Brandon Alexander Ragland
 */
public abstract class Storable implements Serializable{
	protected String ID;
	
	protected LocalDateTime createdDateTime;
	protected LocalDateTime lastUpdatedDateTime;
	
	protected transient String oldID;

	public Storable(){
		ID = UUID.randomUUID().toString();
		createdDateTime = LocalDateTime.now();
		lastUpdatedDateTime = LocalDateTime.now();
	}

	public Storable(String ID){
		this.ID = ID;
		lastUpdatedDateTime = LocalDateTime.now();
	}

	public Storable(String ID, LocalDateTime createdDateTime, LocalDateTime lastUpdatedDateTime){
		this.ID = ID;
		this.createdDateTime = createdDateTime;
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	public String getID(){
		return ID;
	}

	public void setID(String ID){
		this.ID = ID;
	}

	public LocalDateTime getCreatedDateTime(){
		return createdDateTime;
	}

	public void setCreatedDateTime(LocalDateTime createdDateTime){
		this.createdDateTime = createdDateTime;
	}

	public LocalDateTime getLastUpdatedDateTime(){
		return lastUpdatedDateTime;
	}

	public void setLastUpdatedDateTime(LocalDateTime lastUpdatedDateTime){
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}

	public String getOldID(){
		return oldID;
	}

	public void setOldID(String oldID){
		this.oldID = oldID;
	}
}
