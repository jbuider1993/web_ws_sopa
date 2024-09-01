package com.agnasarp.soapcoursemanagement.endpoint;

import java.util.List;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.agnasarp.courses.CourseDetails;
import com.agnasarp.courses.DeleteCourseDetailsRequest;
import com.agnasarp.courses.DeleteCourseDetailsResponse;
import com.agnasarp.courses.GetAllCourseDetailsRequest;
import com.agnasarp.courses.GetAllCourseDetailsResponse;
import com.agnasarp.courses.GetCourseDetailsResponse;
import com.agnasarp.courses.Status;
import com.agnasarp.soapcoursemanagement.domain.Course;
import com.agnasarp.soapcoursemanagement.service.CourseDetailsService;

import lombok.AllArgsConstructor;

//This marks the class as a SOAP endpoint.
@AllArgsConstructor
@Endpoint
public class CourseDetailsEndpoint {

    CourseDetailsService courseDetailsService;

    //    This method processes the GetCourseDetailsRequest and returns the output.

    /*
    @PayloadRoot(namespace = "https://www.agnasarp.com/courses", localPart = "GetCourseDetailsRequest")
    @ResponsePayload
    public GetCourseDetailsResponse processGetCourseDetailsRequest(@RequestPayload GetCourseDetailsRequest getCourseDetailsRequest) {

        return courseResponseMapper(courseDetailsService.getCourseById(getCourseDetailsRequest.getId()));
    }
 */



    //    This method processes the GetAllCourseDetailsRequest and returns the output.
    @PayloadRoot(namespace = "https://www.agnasarp.com/courses", localPart = "GetAllCourseDetailsRequest")
    @ResponsePayload
    public GetAllCourseDetailsResponse processGetAllCourseDetailsRequest(@RequestPayload GetAllCourseDetailsRequest getAllCourseDetailsRequest) {

        List<Course> courses = courseDetailsService.getAllCourses();
        return allCourseResponseMapper(courses);
    }

    //    This method delete a specific course from the course list..
    @PayloadRoot(namespace = "https://www.agnasarp.com/courses", localPart = "DeleteCourseDetailsRequest")
    @ResponsePayload
    public DeleteCourseDetailsResponse processGetAllCourseDetailsRequest(@RequestPayload DeleteCourseDetailsRequest deleteCourseDetailsRequest) {

        DeleteCourseDetailsResponse deleteCourseDetailsResponse = new DeleteCourseDetailsResponse();
        deleteCourseDetailsResponse.setStatus(mapStatus(courseDetailsService.deleteCourseById(deleteCourseDetailsRequest.getId())));
        return deleteCourseDetailsResponse;
    }

    //    Map enum status
    private Status mapStatus(CourseDetailsService.Status status) {

        if (status == CourseDetailsService.Status.SUCCESS) {
            return Status.SUCCESS;
        }
        return Status.FAIL;
    }

    //    Map course to course details
    private CourseDetails courseMapper(Course course) {

        CourseDetails courseDetails = new CourseDetails();
        courseDetails.setId(course.getId());
        courseDetails.setName(course.getName());
        courseDetails.setDescription(course.getDescription());

        return courseDetails;
    }

    //    Map course details to response
    private GetCourseDetailsResponse courseResponseMapper(Course course) {

        GetCourseDetailsResponse getCourseDetailsResponse = new GetCourseDetailsResponse();
        getCourseDetailsResponse.setCourseDetails(courseMapper(course));
        return getCourseDetailsResponse;
    }

    //    Map all course details to response
    private GetAllCourseDetailsResponse allCourseResponseMapper(List<Course> courses) {

        GetAllCourseDetailsResponse getAllCourseDetailsResponse = new GetAllCourseDetailsResponse();
        for (Course course : courses) {
            CourseDetails courseDetails = courseMapper(course);
            getAllCourseDetailsResponse.getCourseDetails().add(courseDetails);
        }

        return getAllCourseDetailsResponse;
    }
}
