package com.example.fantastic.controller;

import com.example.fantastic.model.Niveau;
import com.example.fantastic.model.SupportDeCours;
import com.example.fantastic.repository.NiveauRepository;
import com.example.fantastic.repository.SupportDeCoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
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
import org.springframework.core.io.ByteArrayResource;


@Controller

public class SupportDeCoursController {
    @Autowired
    NiveauRepository niveauRepository;
    @Autowired
    SupportDeCoursRepository coursRepository;

    private String folderPath;

    {
        folderPath = "src/main/resources/static/cours/";
    }

    @RequestMapping(value = "/cours", method = RequestMethod.GET)
    public String cours(Model model){
        model.addAttribute("cours", coursRepository.findAll());
        return "Enfant/ListeFichier";

    }
    @RequestMapping(value = "/listeCours/{id_niveau}", method = RequestMethod.GET)
    public String coursbyNiveau(@PathVariable Long id_niveau, Model model){
        Niveau niveau=niveauRepository.findById(id_niveau).get();
        model.addAttribute("cours", coursRepository.coursByNiveau(niveau));
        return "Enfant/ListeFichier";

    }

    @RequestMapping(value = "/cours/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> TelechargerUnFichier(@PathVariable Long id, Model model){
        System.out.println(id);
        SupportDeCours cours=coursRepository.findById(id).get();
        String fileName=cours.getHachedName();
        System.out.println(fileName);
        try {
            return download(fileName);
        }
        catch (IOException e){
            System.out.println(e.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/supprimerCours/{id}", method = RequestMethod.GET)
    public String SupprimerUnFichier(@PathVariable Long id, Model model){
        System.out.println("id value ="+id);
        SupportDeCours cours=coursRepository.findById(id).get();
        String fileName=cours.getHachedName();
        deleteFile(fileName);
        coursRepository.deleteById(id);
        return "redirect:/cours";
    }



    @RequestMapping(value = "/importerFichier", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile,@RequestParam("niveau_cours") Long niveau_id) {
        //logger.debug("Single file upload!");
        System.out.println("Hell oworld ");
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
                String hach=generateName(fullName);
                String[] fileFrags = uploadfile.getOriginalFilename().split("\\.");
                String extension = fileFrags[fileFrags.length-1];
                String hachCode=hach+"."+extension;
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

            Path path = Paths.get(""+folderPath+hachCode);
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

    public ResponseEntity<?> download(String fileName) throws IOException {
        Path path = Paths.get(""+folderPath+fileName);
        System.out.println(path.toAbsolutePath().toString());
//        Path path = Paths.get(folderPath+nom);
        byte[] data = Files.readAllBytes(path);
        ByteArrayResource resource = new ByteArrayResource(data);


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + path.getFileName().toString())
                .contentType(MediaType.APPLICATION_PDF).contentLength(data.length)
                .body(resource);

    }
    public void deleteFile (String fileName){
        Path path = Paths.get(""+folderPath+fileName);
        try{

            File file = new File(path.toAbsolutePath().toString());

            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation has failed.");
            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

}
