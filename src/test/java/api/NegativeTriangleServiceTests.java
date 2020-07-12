package api;

import common.config.TriangleProfile;
import common.utils.services.TriangleService;
import common.utils.suites.BaseSuite;
import common.utils.types.network.ResponseCodes;
import common.utils.types.network.ResponseErrors;
import common.utils.types.network.ResponseExceptions;
import common.utils.types.network.ResponseFields;
import common.utils.types.network.ResponseMessages;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Some negative & exploratory test-scenarios for Triangle Service
 */
public class NegativeTriangleServiceTests extends BaseSuite {
    private static Logger log = Logger.getLogger(NegativeTriangleServiceTests.class.getName());
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
     * Send request without an auth token
     */
    @Test
    public void addTriangleNoAuthNegativeTest() {
        triangleService = new TriangleService();
        Response response = triangleService.add("4;3;5");
        assertUnauthorized(response, ResponseMessages.NO_MESSAGE_AVAILABLE.getMessage());
    }

    /**
     * Send request with wrong auth header
     */
    @Test
    public void addTriangleWrongAuthHeaderNameNegativeTest() {
        triangleService = new TriangleService("User",
                triangleProfile.getAuthHeaderValue());
        Response response = triangleService.add("4;3;5");
        assertUnauthorized(response, ResponseMessages.NO_MESSAGE_AVAILABLE.getMessage());
    }

    /**
     * Send request without wrong auth header
     */
    @Test
    public void addTriangleWrongAuthHeaderValueNegativeTest() {
        triangleService = new TriangleService(triangleProfile.getAuthHeaderName(),
                "605ba48b-e631-432f-098c-17917136dc7b");
        Response response = triangleService.add("4;3;5");
        assertUnauthorized(response, ResponseMessages.NO_MESSAGE_AVAILABLE.getMessage());
    }


    /**
     * Add triangle with invalid separator
     */
    @Test
    public void addTriangleWithInvalidSeparatorNegativeTest() {
        Response response = triangleService.add("4\"3\"", "\"");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Add triangle with wrong separator input
     */
    @Test
    public void addTriangleWithInvalidInputNegativeTest() {
        Response response = triangleService.add("4883885", "99");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Add triangle with wrong separator input & no specified separator
     */
    @Test
    public void addTriangleWithWrongSeparatorNegativeTest() {
        Response response = triangleService.add("4T3T5");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Delete all triangles
     * TODO: Potential bug: This endpoint was not documented, however, it returned 200 in response,
     * TODO: but no triangles were deleted, need to clarify with team
     */
    @Test
    public void deleteAllTrianglesNegativeTest() throws IOException {
        triangleService.add("5;4;3");
        triangleService.add("5;4;3");

        Response response = triangleService.delete("all");

        assertTrue(response.code() == ResponseCodes.OK.getCode());

        response = triangleService.get("all");
        String responseBody = response.body().string();
        log.info(responseBody);
        assertTrue(responseBody.replace("[]", "").isEmpty());
    }

    /**
     * Add eleventh triangle
     * TODO: Bug found - it was possible to create 11th triangle with 10 triangles limit
     */
    @Test
    public void addEleventhTriangleNegativeTest() {
        Response response;
        for (Integer n = 1; n <= 10; n++) {
            response = triangleService.add("5;4;3");
            assertTrue(response.code() == ResponseCodes.OK.getCode());
        }

        /** adding 11th triangle*/
        response = triangleService.add("5;4;3");
        assertUnprocessable(response, ResponseMessages.LIMIT_EXCEEDED.getMessage());

    }

    /**
     * Add triangle with invalid input parameters count
     * (What if I'll try to add a square?)
     */
    @Test
    public void addSquareNegativeTest() {
        Response response = triangleService.add("4.4.4.4");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Add triangle with invalid input parameters count
     * (What if I'll try to add an angle?)
     */
    @Test
    public void addAngleNegativeTest() {
        Response response = triangleService.add("4.4");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Add triangle with invalid input parameters count
     * (What if I'll try to add a line?)
     */
    @Test
    public void addLineNegativeTest() {
        Response response = triangleService.add("500");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Add triangle with zero input parameters
     * TODO: Potential Bug found: I was allowed to create a zero triangle, need to clarify with team
     */
    @Test
    public void addZeroTriangleNegativeTest() {
        Response response = triangleService.add("0;0;0");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Add triangle with at least one zero side
     */
    @Test
    public void addZeroSideTriangleNegativeTest() {
        Response response = triangleService.add("1;2;0");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }


    /**
     * Add triangle with a negative input parameters
     * TODO: Potential Bug found: I was able to create a triangle with negative sides input,
     * It should not be created? Need to clarify with the team
     */
    @Test
    public void addMinusTriangleNegativeTest() {
        Response response = triangleService.add("-3;-4;-5");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Add a non-existing triangle with a wrong sides length
     * (What if I'll add a non-existing triangle?)
     */
    @Test
    public void addNonExistingTriangleNegativeTest() {
        Response response = triangleService.add("5;26;20");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Adding a floats triangle with a dot separator
     */
    @Test
    public void addFloatsTriangleWithADotSeparatorNegativeTest() {
        Response response = triangleService.add("5.5.5.5.5.5", ".");
        assertUnprocessable(response, ResponseMessages.CANNOT_PROCESS_INPUT.getMessage());
    }

    /**
     * Get request to some other path
     */
    @Test
    public void exploreOtherPathsNegativeTest() {
        Response response = triangleService.get("admin");
        assertNotFound(response, ResponseMessages.NOT_FOUND.getMessage());
    }

    /**
     * Try to get triangle which was already deleted
     * TODO: Low Prio Typo Found: "exception":"com.natera.test.triangle.exception.NotFounException" > NotFoundException
     */
    @Test
    public void getDeletedTriangleNegativeTest() {

        Response response = triangleService.add("4;3;5");
        String createdTriangleId = this.extractBodyMap(response).get("id").toString();

        triangleService.delete(createdTriangleId);

        response = triangleService.get(createdTriangleId);
        assertNotFound(response, ResponseMessages.NOT_FOUND.getMessage());
    }

    /**
     * Delete non-existing triangle
     * TODO: Bug found, delete non-existing triangle return 200 instead of 422 or 404
     */
    @Test
    public void deleteNonExistingTriangleNegativeTest() {
        Response response = triangleService.add("4;3;5");
        String createdTriangleId = this.extractBodyMap(response).get("id").toString();
        triangleService.delete(createdTriangleId);

        response = triangleService.delete(createdTriangleId);
        assertNotFound(response, ResponseMessages.NOT_FOUND.getMessage());
    }

    /**
     * Proceed with assertions for all request fields for 422 response
     * TODO: bug found with timestamp provided: java.text.ParseException: Unparseable date: "1594572295725"
     * Seems like 3 last chars are excessive & we need to remove them
     */
    void assertUnprocessable(Response response, String expectedMessage) {
        assertTrue(response.code() == ResponseCodes.UNPROCESSIBLE.getCode());

        HashMap responseBody = this.extractBodyMap(response);

        //DateFormat format = new SimpleDateFormat("MMddyy HHmmss");
        //Date date = null;
        //try {
        //    date = format.parse(responseBody.get(ResponseFields.TIMESTAMP.getField()).toString());
        //} catch (ParseException e) {
        //    e.printStackTrace();
        //}
        //log.info("timestamp actual date = " + date);

        //TODO add timestamp checker when provided timestamp will be fixed
        // assertEquals(responseBody.get(ResponseFields.TIMESTAMP.getField(), ...)
        assertEquals(responseBody.get(ResponseFields.STATUS.getField()), ResponseCodes.UNPROCESSIBLE.getCode());
        assertEquals(responseBody.get(ResponseFields.ERROR.getField()), ResponseErrors.UNPROCESSABLE_ENTITY.getError());
        assertEquals(responseBody.get(ResponseFields.EXCEPTION.getField()),
                ResponseExceptions.UNPROCESSABLE_DATA.getException());
        assertEquals(responseBody.get(ResponseFields.MESSAGE.getField()), expectedMessage);
        assertEquals(responseBody.get(ResponseFields.PATH.getField()),
                response.request().url().toString().replace(triangleProfile.getAppUrl(), "/"));
    }

    /**
     * Proceed with assertions for all request fields for 404 response
     */
    void assertNotFound(Response response, String expectedMessage) {
        assertTrue(response.code() == ResponseCodes.NOT_FOUND.getCode());

        HashMap responseBody = this.extractBodyMap(response);
        //TODO add timestamp checker when provided timestamp will be fixed
        // assertEquals(responseBody.get(ResponseFields.TIMESTAMP.getField(), ...)
        assertEquals(responseBody.get(ResponseFields.STATUS.getField()), ResponseCodes.NOT_FOUND.getCode());
        assertEquals(responseBody.get(ResponseFields.ERROR.getField()), ResponseErrors.NOT_FOUND.getError());
        assertEquals(responseBody.get(ResponseFields.EXCEPTION.getField()),
                ResponseExceptions.NOT_FOUND.getException());
        assertEquals(responseBody.get(ResponseFields.MESSAGE.getField()), expectedMessage);
        assertEquals(responseBody.get(ResponseFields.PATH.getField()),
                response.request().url().toString().replace(triangleProfile.getAppUrl(), "/"));
    }

    /**
     * Proceed with assertions for all request fields for 401 response
     */
    void assertUnauthorized(Response response, String expectedMessage) {
        assertTrue(response.code() == ResponseCodes.UNAUTHORIZED.getCode());

        HashMap responseBody = this.extractBodyMap(response);
        //TODO add timestamp checker when provided timestamp will be fixed
        // assertEquals(responseBody.get(ResponseFields.TIMESTAMP.getField(), ...)
        assertEquals(responseBody.get(ResponseFields.STATUS.getField()), ResponseCodes.UNAUTHORIZED.getCode());
        assertEquals(responseBody.get(ResponseFields.ERROR.getField()), ResponseErrors.UNAUTHORIZED.getError());
        assertEquals(responseBody.get(ResponseFields.MESSAGE.getField()), expectedMessage);
        assertEquals(responseBody.get(ResponseFields.PATH.getField()),
                response.request().url().toString().replace(triangleProfile.getAppUrl(), "/"));

    }
}
