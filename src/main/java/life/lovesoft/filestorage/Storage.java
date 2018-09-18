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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

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
	 * @param uuid the ID of the object to open.
	 * @return the opened object if it can be found and is not locked, otherwise null.
	 */
	public static <T extends Storable> T open(Class<T> type, String uuid){
		try{
			if(uuid == null || uuid.isEmpty()){
				throw new RuntimeException("Cannot open a storable object with a null or empty UUID!");
			}

			String path = getSaveLocation(type, uuid);
			File file = new File(path);

			if(!file.exists()){
				throw new RuntimeException("File does not exist! Path: " + path);
			}

			if(!file.isFile()){
				throw new RuntimeException("File is not a file! Path: " + path);
			}

			if(!file.canRead()){
				throw new RuntimeException("Cannot read the file! Path: " + path);
			}

			try(ObjectInputStream in = new ObjectInputStream(new InflaterInputStream(new FileInputStream(file)))){
				return (T) in.readObject();
			}catch(Exception e){
				LOG.log(Level.SEVERE, "Unable to read object file! Path: " + path, e);
				return null;
			}
		}catch(RuntimeException e){
			LOG.log(Level.SEVERE, "Unable to open file!", e);
			return null;
		}
	}

	/**
	 * Save an object that extends Storable.
	 *
	 * @param store the object to save, must extend Storable.
	 * @return true if able to save, false if the object cannot be saved.
	 */
	public static boolean save(Storable store){
		try{
			if(store == null){
				throw new RuntimeException("Cannot save a null object!");
			}

			//if new, it may not have an ID (UUID as String) so we'll create one before saving
			if(store.getID() == null || store.getID().isEmpty()){
				store.setID(UUID.randomUUID().toString());
			}

			//if new, it may not have a createdDateTime (LocalDateTime) so we'll create one before saving
			if(store.getCreatedDateTime() == null){
				store.setCreatedDateTime(LocalDateTime.now());
			}

			//always set the last updated time to now
			store.setLastUpdatedDateTime(LocalDateTime.now());

			String path = getSaveLocation(store);
			File file = new File(path);

			File dir = new File(getSaveDirectory(store));
			dir.mkdirs();

			try(ObjectOutputStream out = new ObjectOutputStream(new DeflaterOutputStream(new FileOutputStream(file)))){
				out.writeObject(store);
				out.flush();
				return true;
			}catch(Exception e){
				LOG.log(Level.SEVERE, "Unable to write object file! Path: " + path, e);
				return false;
			}
		}catch(RuntimeException e){
			LOG.log(Level.SEVERE, "Unable to save storable object!", e);
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
		if(aDataDirectory == null || aDataDirectory.isEmpty()){
			throw new RuntimeException("Cannot have a null or empty Data Directory!");
		}
		
		if(aDataDirectory.charAt(aDataDirectory.length() - 1) == '/'){
			throw new RuntimeException("Data Directory path cannot end with / !");
		}
		
		if(aDataDirectory.charAt(aDataDirectory.length() - 1) == '\\'){
			throw new RuntimeException("Data Directory path cannot end with \\ !");
		}

		dataDirectory = aDataDirectory;
	}
}
