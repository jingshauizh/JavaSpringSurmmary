
package com.jason.example.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.jason.example.model.Movie;
import com.jason.example.model.ObjectFactory;
import com.jason.example.model.ObjectInfo;


@Controller
public class DemoFileUploadController {
    private static final Log logger = LogFactory.getLog(DemoFileUploadController.class);
    @RequestMapping(value = "/demoupload", method = RequestMethod.POST)
    public String upload(ModelMap model, MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {       
        String fileName = "";        
        StringBuffer content = new StringBuffer();               
        Iterator<String> itr1 = request.getFileNames();
        MultipartFile multipartFile = request.getFile(itr1.next());
        fileName = multipartFile.getOriginalFilename();
        InputStream in = multipartFile.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(in);
        BufferedReader d = new BufferedReader(new InputStreamReader(bis));
        String xsdPath = this.getClass().getClassLoader().getResource("DemoSchema.xsd").getPath();
        System.out.println("check xsd path=" + xsdPath);
        String line = null;
        while ((line = d.readLine()) != null) {
            System.out.println(line);
            content.append(line + "\n");
        }
        bis.close();
        d.close();
        try { 
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<Movie> o = (JAXBElement<Movie>) jaxbUnmarshaller.unmarshal(multipartFile.getInputStream());
            Movie movie = o.getValue();
            List<ObjectInfo> infoList = movie.getObjectInfos().getObjectInfo();
            DemoCache.setInfoList(infoList);         
           
        } catch (JAXBException ep) {
            ep.printStackTrace();       
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "successFile";
    }
}
