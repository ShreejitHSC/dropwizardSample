package com.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tesco.couchbase.CouchbaseWrapper;

import java.io.IOException;

/**
 * Created by JC87 on 15/09/2014.
 */
public class StudentRepository {

    private final CouchbaseWrapper couchbaseWrapper;
    private final ObjectMapper mapper;

    public StudentRepository(CouchbaseWrapper couchbaseWrapper, ObjectMapper mapper) {
        this.couchbaseWrapper = couchbaseWrapper;
        this.mapper = mapper;
    }

    public ObjectNode getStudentDetail(String studentKey) {
        Object studentDetail = couchbaseWrapper.get(studentKey);

        ObjectNode studentJson = null;
        if (studentDetail != null) {
            try {
                studentJson = mapper.readValue((String) studentDetail, ObjectNode.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return studentJson;
    }
}
