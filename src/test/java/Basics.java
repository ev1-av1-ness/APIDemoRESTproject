import files.PayLoad;
import io.restassured.path.json.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;


public class Basics {
  private static final Logger logger = LogManager.getLogger(Basics.class.getName());
  public static void main(String[] args) {
    //validate if Add Place API is working as expected

    //given - all input details for submit an API
    //when - submit specific API (POST GET what else)
    //then - validate the sendResponse
    //before given you need base URI
    baseURI="https://rahulshettyacademy.com";
    //given query params firstly
    String sendResponse = given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
    //queryParam, headers, body
            //headers - input
    .body(PayLoad.addPlace()).when().post("maps/api/place/add/json")
    //when - resource(path) anf http method
            .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
    //we need the sendResponse from the right place - server validation
    //headers - validation
            .header("Server","Apache/2.4.18 (Ubuntu)")
            //parsing JSON into String
    .extract().response().asString();
    //.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP")) --> no log, no output, only variable
    //there is no in console sendResponse


    //is it success?
    System.out.println(sendResponse);
    //i want to hold id value, how to do that?
    JsonPath js = new JsonPath(sendResponse); //for parsing Json
    String placeId = js.getString("place_id");
    //parent - to child - it's location of key-value in Json, it's a path


    //is it success?
    System.out.println(placeId);



    //reuse id from POST to test PUT
    //Add Place POST --> Update Place with id using New Address PUT --> Check by get that Place will change to New Address
    //New Address is present in sendResponse GET

    //Update Place

    String newAdress = "70 Summer walk, USA";
    given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
    .body("{\n" +
            "\"place_id\":\""+placeId+"\",\n" +
            "\"address\":\""+newAdress+"\",\n" +
            "\"key\":\"qaclick123\"\n" +
            "}")
    .when().put("maps/api/place/update/json")
    .then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));

    //Get Place and verify that it is that place (New York now)

    //you are not sending any body and you need not any header (content-type, for ex.)
    //bu you need more query params (place_id)
    String getResponse = given().log().all().queryParam("key", "qaclick123")
            .queryParam("place_id",placeId)
            .when().get("maps/api/place/get/json")
            .then().assertThat().log().all().statusCode(200).extract().response().asString();

    JsonPath js1 = new JsonPath(sendResponse); //for parsing Json
    js1.getString("address");

    logger.info("INFO");
    logger.debug("DEBUG");
    logger.error("ERROR");
    logger.fatal("FATAL");
    logger.warn("WARN");

  }


}
