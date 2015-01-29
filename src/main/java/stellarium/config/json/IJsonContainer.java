package stellarium.config.json;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonObject;

public interface IJsonContainer {
	
	/**
	 * Read this container as Json.
	 * Will throw Exception if this container is not a Json file.
	 * */
	public JsonCommentedObj readJson() throws IOException;
	
	/**
	 * Writes Json Object to this container. Any  file would be overridden.
	 * */
	public void writeJson(JsonCommentedObj obj);

	
	/**
	 * Creates sub-container.
	 * If it is invalid, then just gives null.
	 * */
	public IJsonContainer makeSubContainer(String sub);
	
	/**Removes the sub-container, if it exists.*/
	public void removeSubContainer(String sub);
	
	/**
	 * Gives the sub-container.
	 * NOTE: this will give new instance of container, not same with the original.
	 * */
	public IJsonContainer getSubContainer(String sub);
	
	/**
	 * gets all sub-containers.
	 * Will give empty list if no sub-containers are present.
	 * */
	public Iterable<String> getAllSubContainerNames();
	
	/**Adds load & fail message for this container*/
	public void addLoadFailMessage(String title, String msg);
}
