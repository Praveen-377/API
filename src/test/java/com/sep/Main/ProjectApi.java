package com.sep.Main;



	import io.restassured.response.Response;
	import org.testng.Assert;
	import org.testng.annotations.BeforeMethod;
	import org.testng.annotations.Test;

import com.sep.response.CreateRepositoryErrorResponsePojo;
import com.sep.response.CreateRepositoryRequestPojo;
import com.sep.response.CreateRepositoryResponsePojo;
import com.sep.response.ErrorResponsePojo;
import com.sep.response.RepositoryPojo;
import com.sep.response.ResponsePojo;
import com.sep.response.UpdateRepositoryRequestPojo;

import java.util.*;

//	import com.sep.models.requests.RepositoryPojo;
//	import com.sep.models.responses.CreateRepositoryResponsePojo;
//	import com.sep.models.responses.ErrorResponsePojo;
//	import com.sep.models.responses.ResponsePojo;

	import io.restassured.module.jsv.JsonSchemaValidator;

	public class ProjectApi extends BaseTest {

		String owner = "Praveen-377";
		String repo = "API";

		String newRepoString = "Hello-World";
		String updatedNewRepo = "Updated-Repo-Hello-World";


		@Test(priority = 1)
		public void testGetSingleRepository() {

			Response response = request.when().get("/repos/" + owner + "/" + repo);
			Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch");
			ResponsePojo repository = response.as(ResponsePojo.class);
			String expectedFullName = owner + "/" + repo;
			Assert.assertEquals(repository.getFull_name(), expectedFullName, "Repository full_name mismatch");
			Assert.assertEquals(repository.getDefault_branch(), "main", "Default branch mismatch");
			String contentType = response.getHeader("Content-Type");
			Assert.assertEquals(contentType, "application/json; charset=utf-8", "Content-Type mismatch");
			System.out.println("Valid Status Code: " + response.getStatusCode());
			System.out.println("Repository full_name: " + repository.getFull_name());
			System.out.println("Default branch: " + repository.getDefault_branch());
			System.out.println("Content-Type: " + contentType);
		}
		
		
		@Test(priority = 2)
		public void testGetNonExistingRepository() {
		    String nonExistingRepo = "Non-Existing-Repo";

		    // Send GET request to a non-existing repository endpoint
		    Response response = request.when().get("/repos/" + owner + "/" + nonExistingRepo);

		    // Assert status code is 404
		    Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch for non-existing repository");

		    // Deserialize response to ErrorResponsePojo
		    ErrorResponsePojo errorResponse = response.as(ErrorResponsePojo.class);

		    // Assert error message in response
		    Assert.assertEquals(errorResponse.getMessage(), "Not Found", "Error message mismatch for non-existing repository");

		    // Print out status and error message for clarity in logs
		    System.out.println("Status Code for non-existing repository: " + response.getStatusCode());
		    System.out.println("Error message: " + errorResponse.getMessage());
		}
		
		
		@Test(priority = 3)
		public void testGetAllRepositories() {
		    // Send GET request to fetch all repositories for the user
		    Response response = request.when().get("/user/repos");

		    // Validate status code is 200
		    Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch for fetching repositories");

		    // Validate JSON schema (Assuming the schema file is located in src/test/resources/schema/user_repos_schema.json)
		    response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("Schema/userschema.json"));

		    // Deserialize response to List of RepositoryPojo
		    List<RepositoryPojo> repositories = Arrays.asList(response.as(RepositoryPojo[].class));

		    // Print total number of repositories
		    System.out.println("Total number of repositories: " + repositories.size());

		    // Print only public repository names
		    System.out.println("Public repositories:");
		    for (RepositoryPojo repo : repositories) {
		        if (!repo.isPrivateRepo()) {
		            System.out.println(" - " + repo.getName());
		        }
		    }

		    // Validate Content-Type header
		    String contentType = response.getHeader("Content-Type");
		    Assert.assertEquals(contentType, "application/json; charset=utf-8", "Content-Type mismatch");

		    // Print Content-Type for clarity
		    System.out.println("Content-Type: " + contentType);
		}

		
		@Test(priority = 4)
		public void testCreateRepository() {
		    // Define request data
		    String repoName = "Hello-World";
		    CreateRepositoryRequestPojo requestBody = new CreateRepositoryRequestPojo(
		            repoName,
		            "This is your first repo!",
		            "https://github.com",
		            false // public repository
		    );

		    // Send POST request to create a new repository
		    Response response = request
		            .body(requestBody)
		            .when()
		            .post("/user/repos");

		    // Validate status code is 201
		    Assert.assertEquals(response.getStatusCode(), 201, "Status code mismatch for repository creation");

		    // Deserialize response to CreateRepositoryResponsePojo
		    CreateRepositoryResponsePojo responseBody = response.as(CreateRepositoryResponsePojo.class);

		    // Validate repository name in response
		    Assert.assertEquals(responseBody.getName(), repoName, "Repository name mismatch");

		    // Validate owner information
		    Assert.assertEquals(responseBody.getOwner().getLogin(), owner, "Repository owner login mismatch");
		    Assert.assertEquals(responseBody.getOwner().getType(), "User", "Repository owner type mismatch");

		    // Print response details for clarity
		    System.out.println("Status Code: " + response.getStatusCode());
		    System.out.println("Repository Name: " + responseBody.getName());
		    System.out.println("Owner Login: " + responseBody.getOwner().getLogin());
		    System.out.println("Owner Type: " + responseBody.getOwner().getType());
		}
		@Test(priority = 5)
		public void testCreateRepositoryWithExistingName() {
		    // Define request data with an existing repository name
		    String existingRepoName = "Hello-World";  // Assuming this repository already exists
		    CreateRepositoryRequestPojo requestBody = new CreateRepositoryRequestPojo(
		            existingRepoName,
		            "This is a duplicate repo attempt!",
		            "https://github.com",
		            false // public repository
		    );

		    // Send POST request to attempt creating a repository with an existing name
		    Response response = request
		            .body(requestBody)
		            .when()
		            .post("/user/repos");

		    // Validate status code is 422
		    Assert.assertEquals(response.getStatusCode(), 422, "Status code mismatch for duplicate repository creation");

		    // Deserialize response to CreateRepositoryErrorResponsePojo
		    CreateRepositoryErrorResponsePojo errorResponse = response.as(CreateRepositoryErrorResponsePojo.class);

		    // Validate main error message
		    Assert.assertEquals(errorResponse.getMessage(), "Repository creation failed.", "Main error message mismatch");

		    // Validate specific error message in the errors list
		    boolean nameExistsErrorFound = errorResponse.getErrors().stream()
		            .anyMatch(error -> error.getMessage().equals("name already exists on this account"));
		    Assert.assertTrue(nameExistsErrorFound, "Specific error message 'name already exists on this account' not found");

		    // Print response details for clarity
		    System.out.println("Status Code: " + response.getStatusCode());
		    System.out.println("Main Error Message: " + errorResponse.getMessage());
		    System.out.println("Specific Error Messages:");
		    errorResponse.getErrors().forEach(error -> System.out.println(" - " + error.getMessage()));
		}


		@Test(priority = 6)
		public void testUpdateRepositoryName() {
		    // Define updated repository details
		    String updatedRepoName = "Updated-Repo-Name";
		    UpdateRepositoryRequestPojo requestBody = new UpdateRepositoryRequestPojo(
		            updatedRepoName,
		            "my repository created using APIs after update",
		            false // public repository
		    );

		    // Send PATCH request to update the repository name
		    Response response = request
		            .body(requestBody)
		            .when()
		            .patch("/repos/" + owner + "/" + repo);

		    // Validate status code is 200
		    Assert.assertEquals(response.getStatusCode(), 200, "Status code mismatch for repository update");

		    // Deserialize response to ResponsePojo
		    ResponsePojo updatedRepository = response.as(ResponsePojo.class);

		    // Validate updated repository name in response
		    String expectedFullName = owner + "/" + updatedRepoName;
		    Assert.assertEquals(updatedRepository.getFull_name(), expectedFullName, "Updated repository name mismatch");

		    // Print response details for clarity
		    System.out.println("Status Code: " + response.getStatusCode());
		    System.out.println("Updated Repository Full Name: " + updatedRepository.getFull_name());
		}

		@Test(priority = 7)
		public void testDeleteRepository() {
		    // Send DELETE request to delete the repository
		    Response response = request
		            .when()
		            .delete("/repos/" + owner + "/" + repo);

		    // Validate status code is 204
		    Assert.assertEquals(response.getStatusCode(), 204, "Status code mismatch for repository deletion");

		    // Validate response body is empty (null)
		    Assert.assertTrue(response.getBody().asString().isEmpty(), "Response body is not empty");

		    // Print response details for clarity
		    System.out.println("Status Code: " + response.getStatusCode());
		    System.out.println("Response Body: " + response.getBody().asString());
		}

		
		@Test(priority = 8)
		public void testDeleteNonExistentRepository() {
		    // Define a non-existing repository name for testing
		    String nonExistentRepo = "non-existent-repo";

		    // Send DELETE request to delete the non-existing repository
		    Response response = request
		            .when()
		            .delete("/repos/" + owner + "/" + nonExistentRepo);

		    // Validate status code is 404
		    Assert.assertEquals(response.getStatusCode(), 404, "Status code mismatch for non-existent repository deletion");

		    // Deserialize response to ErrorResponsePojo
		    ErrorResponsePojo errorResponse = response.as(ErrorResponsePojo.class);

		    // Validate error message
		    Assert.assertEquals(errorResponse.getMessage(), "Not Found", "Error message mismatch");

		    // Print response details for clarity
		    System.out.println("Status Code: " + response.getStatusCode());
		    System.out.println("Error Message: " + errorResponse.getMessage());
		    System.out.println("Documentation URL: " + errorResponse.getDocumentation_url());
		}

}
