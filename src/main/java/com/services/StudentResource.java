 package com.services;

import com.core.Configuration;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import static com.services.HTTPResponses.*;


@Path("/student")
@Api(value = "/student", description = "Dropwizard API")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class StudentResource {

    private final StudentRepository studentRepository;
    private Configuration configuration;

    public StudentResource(StudentRepository studentRepository, Configuration configuration) {
        this.studentRepository = studentRepository;
        this.configuration = configuration;
    }


    @GET
    @ApiOperation(value = "Find student details by an id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Student details not found"),
            @ApiResponse(code = 500, message = "Server error")
    })
    public Response getStudentDetail(@QueryParam("id") String id,
                                     @Context UriInfo uriInfo){

        ObjectNode studentDetail = studentRepository.getStudentDetail(id);

        if (studentDetail == null){
            return notFound(String.format("Student details not found"));
        }

        return ok(studentDetail);
    }
}