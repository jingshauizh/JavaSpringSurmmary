
package com.jason.example.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jason.example.model.Movie;
import com.jason.example.model.ObjectFactory;
import com.jason.example.model.ObjectInfos;


@Controller
public class DemoSeekController {
    /**
     * Size of a byte buffer to read/write file
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Path of the file to be downloaded, relative to application's
     * directory
     */
    private String filePath = "/demos.xml";

    @RequestMapping(value = "/getInfoDemo", method = RequestMethod.GET)
    public String getInfoObject(HttpServletRequest request, HttpServletResponse response) {
   
         
        ObjectFactory factory = new ObjectFactory();
        Movie demo = factory.createMovie();
        ObjectInfos objectInfos = factory.createObjectInfos();
        System.out.println("-------------DemoCache.getInfoList()------------=" + DemoCache.getInfoList().size());
        objectInfos.getObjectInfo().addAll(DemoCache.getInfoList());
        demo.setObjectInfos(objectInfos); 
        
        System.out.println("movie: " + demo.getObjectInfos().getObjectInfo().size());
        try {
            // get absolute path of the application
            ServletContext context = request.getSession().getServletContext();
            String appPath = context.getRealPath("");
            System.out.println("appPath = " + appPath);
            // construct the complete absolute path of the file
            String fullPath = appPath + filePath;
            File downloadFile = new File(fullPath);          

            // set content attributes for the response
            String mimeType = "text/xml";
            response.setContentType(mimeType);

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);
            OutputStream outStream = response.getOutputStream();
            JAXBContext jaxbContext = JAXBContext.newInstance(Movie.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(demo, outStream);         
            outStream.close();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "ok";
    }
}
