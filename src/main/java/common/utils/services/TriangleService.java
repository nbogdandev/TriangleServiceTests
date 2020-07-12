package common.utils.services;

import common.config.TriangleProfile;
import okhttp3.Response;

import java.text.MessageFormat;
import java.util.HashMap;

public class TriangleService {
    private final TriangleProfile triangleProfile = new TriangleProfile();
    private ApiService apiService;

    public TriangleService() {
        this.apiService = new ApiService();
    }

    public TriangleService(String authHeaderName, String authHeaderValue) {
        this.apiService = new ApiService(authHeaderName, authHeaderValue);
    }

    /**
     * Overloading add function to make separator param has a value by default
     *
     * @param input
     */
    public Response add(String input) {
        HashMap addRequest = new GeneratorService().generateAddRequest(input);
        String url = this.getServiceUrl();

        return apiService.post(url, addRequest);
    }

    /**
     * Add new triangle
     *
     * @param input
     * @param separator
     * @return
     */
    public Response add(String input, String separator) {
        HashMap addRequest = new GeneratorService().generateAddRequest(input, separator);
        String url = this.getServiceUrl();

        return apiService.post(url, addRequest);
    }

    /**
     * Get user triangle
     *
     * @param id
     * @return
     */
    public Response get(String id) {
        String url = this.getServiceUrl(id);

        return apiService.get(url);
    }

    /**
     * Get all user triangles response
     *
     * @return
     */
    public Response getAll() {
        String url = this.getServiceUrl("all");

        return apiService.get(url);
    }

    /**
     * Get triangle perimeter
     *
     * @param id
     * @return
     */
    public Response calculatePerimeter(String id) {
        String url = this.getServiceUrl(id, "perimeter");

        return apiService.get(url);
    }

    /**
     * Get triangle area
     *
     * @param id
     * @return
     */
    public Response calculateArea(String id) {
        String url = this.getServiceUrl(id, "area");

        return apiService.get(url);
    }

    /**
     * Delete user triangle
     *
     * @param id
     * @return
     */
    public Response delete(String id) {
        String url = this.getServiceUrl(id);

        return apiService.delete(url);
    }

    String getServiceUrl() {
        return MessageFormat.format("{0}{1}", triangleProfile.getAppUrl(), "triangle");
    }

    String getServiceUrl(String id) {
        return MessageFormat.format("{0}/{1}", getServiceUrl(), id);
    }

    String getServiceUrl(String id, String command) {
        return MessageFormat.format("{0}/{1}/{2}", getServiceUrl(), id, command);
    }


}
