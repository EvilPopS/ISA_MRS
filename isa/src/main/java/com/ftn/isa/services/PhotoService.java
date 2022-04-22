package com.ftn.isa.services;

import com.ftn.isa.model.Photo;
import com.ftn.isa.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PhotoService {

    @Autowired
    public PhotoRepository photoRepository;

    public Set<Photo> save(Set<String> photos) {
        Set<Photo> newPhotos = new HashSet<Photo>();
        for ( String photoPath : photos){
            Photo p = new Photo();
            p.setPhotoPath(photoPath);
            photoRepository.save(p);
            newPhotos.add(p);
        }
        return newPhotos;
    }
}
