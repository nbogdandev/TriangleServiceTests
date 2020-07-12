package api;

import common.config.TriangleProfile;
import common.utils.services.GeneratorService;
import common.utils.services.TriangleService;
import common.utils.suites.BaseSuite;
import common.utils.types.network.ResponseCodes;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * Main positive test-scenarios for Triangle Service
 */
public class PositiveTriangleServiceTests extends BaseSuite {
    private static Logger log = Logger.getLogger(PositiveTriangleServiceTests.class.getName());
    private static final TriangleProfile triangleProfile = new TriangleProfile();
    TriangleService triangleService = new TriangleService(triangleProfile.getAuthHeaderName(),
            triangleProfile.getAuthHeaderValue());

    @Before
    public void beforeTest() {
        triangleService = new TriangleService(triangleProfile.getAuthHeaderName(),
                triangleProfile.getAuthHeaderValue());

        /** deleting all existing triangles */
        Response allTrianglesResponse = triangleService.getAll();
        log.info(allTrianglesResponse.toString());

        List<Map<String, String>> allTrianglesArray = this.extractBodyList(allTrianglesResponse);

        log.info("allArray=" + allTrianglesArray.toString());
        allTrianglesArray.forEach(k ->
                triangleService.delete(k.get("id")));

    }

    /**
     * Add triangle
     */
    @Test
    public void addTrianglePositiveTest() {
        Response response = triangleService.add("4;3;5");
        assertTrue(response.code() == ResponseCodes.OK.getCode());
    }

    /**
     * Add triangle with another separator
     */
    @Test
    public void addTriangleWithNonDefaultSeparatorPositiveTest() {
        Response response = triangleService.add("4K3K5", "K");
        assertTrue(response.code() == ResponseCodes.OK.getCode());
    }

    /**
     * Add a triangle with a float sides length
     * (What if I'll add a triangle of floats?)
     */
    @Test
    public void addFloatTrianglePositiveTest() {
        Response response = triangleService.add("5.55;5.05;5.005");
        HashMap responseBody = this.extractBodyMap(response);

        assertTrue(response.code() == ResponseCodes.OK.getCode());
    }

    /**
     * Get existing triangle
     */
    @Test
    public void getTrianglePositiveTest() {

        Response response = triangleService.add("4;3;5");
        String createdTriangleId = this.extractBodyMap(response).get("id").toString();

        response = triangleService.get(createdTriangleId);
        HashMap responseBody = this.extractBodyMap(response);

        assertEquals(responseBody.get("id"), createdTriangleId);
        assertEquals(responseBody.get("firstSide"), 4.0);
        assertEquals(responseBody.get("secondSide"), 3.0);
        assertEquals(responseBody.get("thirdSide"), 5.0);

        assertTrue(response.code() == ResponseCodes.OK.getCode());
    }

    /**
     * Delete existing triangle
     */
    @Test
    public void deleteTrianglePositiveTest() {
        Response response = triangleService.add("4;3;5");
        String createdTriangleId = this.extractBodyMap(response).get("id").toString();

        response = triangleService.delete(createdTriangleId);
        assertTrue(response.code() == ResponseCodes.OK.getCode());
    }

    /**
     * calculate Triangle perimeter
     */
    @Test
    public void calculatePerimeterPositiveTest() {
        double a = 5.55;
        double b = 6.66;
        double c = 7.77;

        String input = new GeneratorService().generateValidInput(
                String.valueOf(a),
                String.valueOf(b),
                String.valueOf(c));
        Response response = triangleService.add(input);
        String createdTriangleId = this.extractBodyMap(response).get("id").toString();

        response = triangleService.calculatePerimeter(createdTriangleId);
        HashMap responseBody = this.extractBodyMap(response);


        assertEquals(responseBody.get("result"), a + b + c);
        assertTrue(response.code() == ResponseCodes.OK.getCode());
    }

    /**
     * calculate Triangle area
     */
    @Test
    public void calculateAreaPositiveTest() {
        double a = 5.55;
        double b = 6.66;
        double c = 7.77;

        String input = new GeneratorService().generateValidInput(
                String.valueOf(a),
                String.valueOf(b),
                String.valueOf(c));
        Response response = triangleService.add(input);
        String createdTriangleId = this.extractBodyMap(response).get("id").toString();

        response = triangleService.calculateArea(createdTriangleId);
        HashMap responseBody = this.extractBodyMap(response);

        double halfP = (a + b + c) / 2;
        double expectedArea = Math.sqrt((halfP * (halfP - a) * (halfP - b) * (halfP - c)));

        assertEquals(responseBody.get("result"), expectedArea);
        assertTrue(response.code() == ResponseCodes.OK.getCode());
    }

    /**
     * Get all triangles
     */
    @Test
    public void getAllTrianglesPositiveTest() {
        ArrayList<String> createdIdsArray = new ArrayList<String>(3);

        for (Integer n = 1; n < 4; n++) {
            Response response = triangleService.add("1;2;3");
            createdIdsArray.add(this.extractBodyMap(response).get("id").toString());
        }

        List<Map<String, String>> allTrianglesArray = this.extractBodyList(triangleService.getAll());
        allTrianglesArray.forEach(k ->
                assertTrue(createdIdsArray.contains(k.get("id"))));
    }

/**
 * Some other ideas I was thinking about, but had no time to check
 *
 * – Investigate how my personal token was generated & try to generate a working foreign one >
 * Check if I could proceed with adding/deleting some foreign triangles then.
 * – Create Triangle data class & randomizer for generating valid/invalid triangles.
 * - Refactor api service, avoid duplicated code.
 * - Move response body converters to ApiService.
 * – Check min/max possible triangle side (low priority)
 * - Create a test to check if it possible to delete triangle area (low priority)
 * - Create a test to check if it possible to delete triangle perimeter (low priority)
 */
}
