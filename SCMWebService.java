/**
 * 
 */
package com.acellere.gamma.gws.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acellere.corona.cmx.msg.control.RepositoryType;
import com.acellere.gamma.constants.ExceptionConstants;
import com.acellere.gamma.constants.WebServiceConstants;
import com.acellere.gamma.exception.GammaWebServiceException;
import com.acellere.gamma.gws.scm.SCMService;
import com.acellere.gamma.gws.util.GWSProperties;
import com.acellere.gamma.scm.dto.SCMDTO;
import com.acellere.gamma.scm.enums.SCMEnums.ExceptionType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author
 *
 */
@Path("/scm")
public class SCMWebService {

	final static Logger LOGGER = LoggerFactory.getLogger(SCMWebService.class);
	private static final ObjectMapper mapper = new ObjectMapper();

	@POST
	@Path("/validateRepo")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validateRepository(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->validateRepository started");
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		LOGGER.info("Invoked validate Repository of SCM-WS");
		JSONObject jsonObject = new JSONObject();
		SCMDTO scmDTO = null;
		boolean result = false;
		
		try {
			scmDTO = parseInputStream(incomingData);
			result = SCMService.validateRepository(scmDTO);
			LOGGER.info("validateRepo result {} for repo {}", result, scmDTO);
				
			if(result){
				jsonObject.put(WebServiceConstants.JSON_KEY_STATUS, "OK");
			} else {
				jsonObject.put(WebServiceConstants.JSON_KEY_MESSAGE, "INVALID_REPOSITORY_DETAILS");
				return fail(jsonObject);
			}
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while validate Repository of SCM-WS was invoked.",  e);
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->validateRepository completed");
		return success(result);
	}
	
	@POST
	@Path("/validateOAuthToken")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validateToken(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->validateToken started");
		LOGGER.info("Invoked validate OAuth token of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		SCMDTO scmDTO = null;
		boolean result = false;
		
		try {
			scmDTO = parseInputStream(incomingData);
			result = SCMService.validateToken(scmDTO);
			LOGGER.info("validate token result {} for repo {}", result, scmDTO.getRepoDTO().getRepositoryURL());
			
			if(result){
				jsonObject.put(WebServiceConstants.JSON_KEY_STATUS, "OK");
			} else {
				jsonObject.put(WebServiceConstants.JSON_KEY_MESSAGE, ExceptionType.INVALID_ACCOUNT_DETAILS);
				return fail(jsonObject);
			}
			
		} catch (GammaWebServiceException e) {
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->validateToken completed");
		return success(result);
	}

	@POST
	@Path("/syncRepo")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response syncRepository(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->syncRepository started");
		LOGGER.info("Invoked sync repository of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		boolean result = false;
		SCMDTO scmDTO = null;
		try {
			
			scmDTO = parseInputStream(incomingData);
			result = SCMService.syncRepository(scmDTO);
			LOGGER.info("sync repository result : " + result);

			
			if(!result){
				return fail(jsonObject);
			}
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while sync repository of SCM-WS was invoked.", e);
			return fail(jsonObject);
		}
		
		String ip = GWSProperties.getInternalIp();
		if(ip == null)
			ip = GWSProperties.getProperty(GWSProperties.EXTERNAL_IP);

		LOGGER.trace("Method: SCMWebService->syncRepository completed");
		return Response.status(Response.Status.OK).entity(ip)
				.header(WebServiceConstants.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
						WebServiceConstants.CONSTANT_ASTERISK)
				.build();
	}
	
	@POST
	@Path("/abortSync")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response abortSync(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->abortSync started");
		LOGGER.info("Invoked abort sync repository of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		boolean result = false;
		SCMDTO scmDTO = null;
		try {
			scmDTO = parseInputStream(incomingData);
			result = SCMService.abortSyncCode(scmDTO);
			LOGGER.info("abort sync repository result : " + result);

			if(result){
				jsonObject.put(WebServiceConstants.JSON_KEY_STATUS, "OK");
			} else {
				return fail(jsonObject);
			}
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while abort sync repository of SCM-WS was invoked." + e);
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->abortSync completed");
		return success(result);
	}
	
	@POST
	@Path("/syncFinished")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response syncFinished(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->syncFinished started");
		LOGGER.info("Invoked syncFinished of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		boolean result = false;
		SCMDTO scmDTO = null;
		try {
			scmDTO = parseInputStream(incomingData);
			result = SCMService.syncRepositoryFinished(scmDTO);
			LOGGER.info("finished sync repository result : " + result);
			
			if(result){
				jsonObject.put(WebServiceConstants.JSON_KEY_STATUS, "OK");
			} else {
				return fail(jsonObject);
			}
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while finish sync repository of SCM-WS was invoked.", e);
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->syncFinished started");
		return success(result);
	}
	
	@POST
	@Path("/syncFailed")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response syncFailed(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->syncFailed started");
		LOGGER.info("Invoked syncFailed of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		boolean result = false;
		
		SCMDTO scmDTO = null;
		try {
			scmDTO = parseInputStream(incomingData);
			result = SCMService.syncRepositoryFailure(scmDTO);
			LOGGER.info("failed sync repository result : " + result);
			
			if(result){
				jsonObject.put(WebServiceConstants.JSON_KEY_STATUS, "OK");
			} else {
				return fail(jsonObject);
			}
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while fail sync repository of SCM-WS was invoked.", e);
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->syncFailed completed");
		return success(result);
	}
	
	@POST
	@Path("/heartbeat")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response heartbeat(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.debug("Invoked heartbeat of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		boolean result = false;
		SCMDTO scmDTO = null;
		try {
			scmDTO = parseInputStream(incomingData);
			result = SCMService.heartbeat(scmDTO);
			
			if(result){
				jsonObject.put(WebServiceConstants.JSON_KEY_STATUS, "OK");
			} else {
				//return fail(jsonObject);
				// Return OK even if the scm job is already finished to reduce errors on cloud
				jsonObject.put(WebServiceConstants.JSON_KEY_STATUS, "OK");
			}
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while heartbeat sync repository of SCM-WS was invoked.", e);
			return fail(jsonObject);
		}
		return success(result);
	}
	
	@POST
	@Path("/getFileByRev")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getFileByRevision(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->getFileByRevision started");
		LOGGER.info("Invoked get file by revision of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		SCMDTO scmDTO = null;
		boolean result = false;
		try {
			scmDTO = parseInputStream(incomingData);
			result = SCMService.getFileByRevision(scmDTO);
			LOGGER.info("get file by revision result : " + result);

			if(!result){
				int existCode = SCMService.validateRTCCreds(scmDTO);
				LOGGER.info("exit code = "+existCode);
				if(existCode == 2) {
					jsonObject.put(WebServiceConstants.JSON_KEY_MESSAGE, "INVALID_SCM_CREDENTIAL");
					return fail(jsonObject);
				}else{
					return fail(jsonObject);
				}
			}
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while fetching file by revision of SCM-WS was invoked.", e);
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->getFileByRevision completed");
		return success(result);
	}

	@POST
	@Path("/deleteCodeAfterScan")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteCodeAfterScan(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->deleteCodeAfterScan started");
		LOGGER.info("Invoked delete Code After the scan is completed.");

		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}

		JSONObject jsonObject = new JSONObject();
		SCMDTO scmDTO = null;
		boolean result = false;
		try {
			scmDTO = parseInputStream(incomingData);
			//result = SCMService.deleteCodeAfterScan(scmDTO);
			LOGGER.info("get delete code After Scan result : " + result);

			if(!result){
				return fail(jsonObject);
			}

		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while fetching delete code After Scan of SCM-WS was invoked.", e);
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->deleteCodeAfterScan completed");
		return success(result);
	}

	@POST
	@Path("/getBranchTagList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getBranchAndTagList(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->getBranchAndTagList started");
		LOGGER.info("Invoked get branch/tags list of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		SCMDTO scmDTO = null;
		List<String> data = null;
		String result = "";
		Boolean validationResult = false ;
		try {
			scmDTO = parseInputStream(incomingData);
			
			validationResult = SCMService.validateRepository(scmDTO);
			LOGGER.info("validateRepo result {} for repo {}", validationResult, scmDTO);
			if (!validationResult && (scmDTO.getRepoDTO().getRepositoryType().equals(RepositoryType.GIT) || 
					scmDTO.getRepoDTO().getRepositoryType().equals(RepositoryType.BIT))) {
				LOGGER.info("As a fallback invoking validate token for repo {}", scmDTO);
				validationResult = SCMService.validateToken(scmDTO);
				LOGGER.info("validateToken result {} for repo {}", validationResult, scmDTO);
			}
				
			if(validationResult){
				data = SCMService.getBranchAndTagList(scmDTO);
				if(null != data){
					result = data.toString().replace("[", "").replace("]", "").replace(" ", "");
				} 
			} else {
				jsonObject.put(WebServiceConstants.JSON_KEY_MESSAGE, "INVALID_REPOSITORY_DETAILS");
				return fail(jsonObject);
			}
			
			LOGGER.debug("Branch/tag list result : " + result);
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while getting branch/tag list of SCM-WS was invoked." + e);
			jsonObject.put(WebServiceConstants.JSON_KEY_MESSAGE, e.getErrorCode());
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->getBranchAndTagList completed");
		return success(result);
	}
	
	@POST
	@Path("/getRepositoryList")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getRepositoryList(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->getRepositoryList started");
		LOGGER.info("Invoked get repository list of SCM-WS");
		
		// Check if this service should be available
		if(InitListener.RunMode.analytics.equals(InitListener.RUN_MODE)) {
			return forbidden();
		}
		
		JSONObject jsonObject = new JSONObject();
		SCMDTO scmDTO = null;
		List<String> data = null;
		String result = "";
		try {
			scmDTO = parseInputStream(incomingData);
			data = SCMService.getRepositoryList(scmDTO);
			
			if(null != data){
				Collections.sort(data, String.CASE_INSENSITIVE_ORDER);
				result = data.toString().replace("[", "").replace("]", "").replace(" ", "");
			} 
			LOGGER.debug("Repository list result : " + result);
			
		} catch (GammaWebServiceException e) {
			LOGGER.error("Exception occured while getting repository list of SCM-WS was invoked." + e);
			return fail(jsonObject);
		}
		LOGGER.trace("Method: SCMWebService->getRepositoryList completed");
		return success(result);
	}
	
	private SCMDTO parseInputStream(InputStream incomingData) throws GammaWebServiceException {
		LOGGER.trace("Method: SCMWebService->parseInputStream started");
		SCMDTO scmDTO = null;
		try {
			scmDTO = mapper.readValue(incomingData, SCMDTO.class);
			LOGGER.debug("Incoming SCM DTO data mapped sucessfully");
			LOGGER.trace("Method: SCMWebService->parseInputStream completed");
			return scmDTO;
		} catch (JsonParseException e) {
			LOGGER.error(e.getMessage(), ExceptionConstants.JSON_PARSING_ERROR);
			throw new GammaWebServiceException(e, ExceptionType.SCM_DOWN);
		} catch (JsonMappingException e) {
			LOGGER.error(e.getMessage(), ExceptionConstants.JSON_MAPPING_ERROR);
			throw new GammaWebServiceException(e, ExceptionType.SCM_DOWN);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), ExceptionConstants.IO_EXCEPTION);
			throw new GammaWebServiceException(e, ExceptionType.SCM_DOWN);
		/*} finally {
			if(incomingData != null){
				try {
					incomingData.close();
				} catch (IOException ioe) {
					LOGGER.warn("IOException while closing the InputStream. ", ioe);
				}
			}*/
		}
	}
	
	private Response success(Object object) {
		return Response.status(Response.Status.OK).entity(object.toString())
				.header(WebServiceConstants.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
						WebServiceConstants.CONSTANT_ASTERISK)
				.build();
	}
	
	private Response fail(JSONObject jsonObject) {
		jsonObject.put(WebServiceConstants.JSON_KEY_STATUS, ExceptionConstants.FAILED_ERROR);
		LOGGER.error("*****response : "+Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject.toString())
				.header(WebServiceConstants.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
						WebServiceConstants.CONSTANT_ASTERISK)
				.build().toString());
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject.toString())
				.header(WebServiceConstants.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
						WebServiceConstants.CONSTANT_ASTERISK)
				.build();
	}
	
	private Response forbidden() {
		return Response.status(Response.Status.FORBIDDEN)
				.header(WebServiceConstants.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
						WebServiceConstants.CONSTANT_ASTERISK)
				.build();
	}
}
