/*
 * Copyright (C) Love LLC incorporated in the State of Maryland - All Rights Reserved
 * Unauthorized viewing of this file, via any medium is strictly prohibited
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Unauthorized editing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Brandon Alexander Ragland <brandon.ragland@lovesoft.life>
 */
package life.lovesoft.filestorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DeflaterOutputStream;

/**
 *
 * @author Brandon Alexander Ragland
 */
public class Storage{

	private static final Logger LOG = Logger.getLogger(Storage.class.getName());

	public static final String USER_HOME = System.getProperty("user.home");

	private static String dataDirectory = "/FileStorage";

	/**
	 * Open an object that extends Storable and has been saved to the file system.
	 *
	 * @param <T> return type of object to open.
	 * @param type class type of object to open.
	 * @param uuid the uuid of the object to open.
	 * @return returns the opened object if it can be found and is not locked, otherwise null if not found.
	 */
	public static <T extends Storable> T open(Class<T> type, String uuid){
		return null;
	}

	/**
	 * Save an object that extends Storable.
	 *
	 * @param <T> return type of object that will be saved save.
	 * @param store the object to save, must extend Storable.
	 * @return returns the saved object if it can be saved and is not locked.
	 */
	public static boolean save(Storable store){
		//throw if null
		if(store == null){
			throw new RuntimeException("Cannot save a null object!");
		}

		//if new, it may not have an ID (UUID as String) so we create one
		if(store.getID() == null || store.getID().isEmpty()){
			store.setID(UUID.randomUUID().toString());
		}

		//if new, it may not have a createdDateTime (LocalDateTime) so we create one
		if(store.getCreatedDateTime() == null){
			store.setCreatedDateTime(LocalDateTime.now());
		}

		//always set the last updated to now
		store.setLastUpdatedDateTime(LocalDateTime.now());
		
		//get the file location and make a new File object for it
		File file = new File(getSaveLocation(store));

		try(ObjectOutputStream out = new ObjectOutputStream(new DeflaterOutputStream(new FileOutputStream(file)))){
			//get the directory as a File object
			File dir = new File(getSaveDirectory(store));
			//Make the directory as some may not exist
			dir.mkdirs();
			//write out the object
			out.writeObject(store);
			//flush to ensure to file system
			out.flush();
			//return true, we were able to save
			return true;
		}catch(Exception e){
			LOG.log(Level.SEVERE, "Could not save a storable object!", e);
			return false;
		}
	}

	public static <T extends Storable> T copy(Class<T> type, String uuid){
		return null;
	}

	public static boolean delete(Class<? extends Storable> type, String uuid){
		return false;
	}

	public static void detach(Class<? extends Storable> type, String uuid){

	}

	public static void reattach(Class<? extends Storable> type, String uuid){

	}

	public static String getSaveDirectory(Class<? extends Storable> type){
		return USER_HOME + dataDirectory + "/" + type.getName();
	}

	public static String getSaveDirectory(Storable data){
		return getSaveDirectory(data.getClass());
	}

	public static String getSaveLocation(Class<? extends Storable> type, String uuid){
		return getSaveDirectory(type) + "/" + uuid + ".dat";
	}

	public static String getSaveLocation(Storable data){
		return getSaveLocation(data.getClass(), data.getID());
	}

	public static String getDataDirectory(){
		return dataDirectory;
	}

	public static void setDataDirectory(String aDataDirectory){
		dataDirectory = aDataDirectory;
	}
}
