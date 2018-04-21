package com.example.fantastic.controller;

import com.example.fantastic.model.SupportDeCours;
import com.example.fantastic.repository.NiveauRepository;
import com.example.fantastic.repository.SupportDeCoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller

public class FileUploadController {
    @Autowired
    NiveauRepository niveauRepository;
    @Autowired
    SupportDeCoursRepository coursRepository;

    private String folderPath;

    {
        folderPath = "src/main/resources/static/cours/";
    }
    @RequestMapping(value = "/api/fileupload", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile,@RequestParam("niveau_cours") Long niveau_id) {
        //logger.debug("Single file upload!");
        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        //responseHeaders.add("Access-Control-Allow-Origin", "*");
        try {

            try {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String fullName=uploadfile.getOriginalFilename()+date.toString();//en cas de plusieurs fichiers avec le meme nom, comme ça on s'assure qu'il y aura pas le meme nom(on a ajouté la date aussi)
                String hachCode=generateName(fullName);
                saveUploadedFiles(Arrays.asList(uploadfile),hachCode);
                saveFileInBDD(uploadfile.getOriginalFilename(),hachCode,date,niveau_id);
                //Ajout du nom du fichier dans la BDD

            } catch (NoSuchAlgorithmException e){

            }


        } catch (IOException e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(
                uploadfile.getOriginalFilename(), responseHeaders, HttpStatus.OK);

    }
    //save file
    private void saveUploadedFiles(List<MultipartFile> files,String hachCode) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(""+folderPath+hachCode+file.getOriginalFilename());
            System.out.println(path.toAbsolutePath().toString());
            Files.write(path, bytes);

        }

    }

    private void saveFileInBDD (String originaleName,String hachCode,Date date,Long niveau_id){
        SupportDeCours cours=new SupportDeCours();
        cours.setOriginaleName(originaleName);
        cours.setHachedName(hachCode);
        cours.setDate(date);
        cours.setNiveau(niveauRepository.findById(niveau_id).get());
        coursRepository.save(cours);

    }

    private String generateName(String e) throws NoSuchAlgorithmException {
//Créer un haché pour le nom du fichier afin de le sauvegarder avec ce nouveau nom (pour éviter les doublons)
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(e.getBytes());
        byte[] digest = md.digest();
        String myname = DatatypeConverter
                .printHexBinary(digest).toUpperCase();

        return myname;
    }

}
