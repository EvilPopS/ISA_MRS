package com.ftn.isa.services;

import com.ftn.isa.DTO.CottageDTO;
import com.ftn.isa.model.Cottage;
import com.ftn.isa.model.CottageOwner;
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

    public Set<Photo> addOrDeletePhoto(Cottage cottage, CottageDTO cottageDTO){
        Set<Photo> photos = new HashSet<Photo>();

        for (String path : cottageDTO.getPhotos()) {
            boolean newPhoto = true;
            for (Photo p : cottage.getPhotos()) {
                if (p.getPhotoPath().equals(path)) {
                    photos.add(p);
                    newPhoto = false;
                }
            }
            if (newPhoto) {photos.add(new Photo(path));}
        }

        for (Photo p : cottage.getPhotos()){
            boolean deleted = true;
            for (String path : cottageDTO.getPhotos()){
                if (path.equals(p.getPhotoPath())){deleted = false; break;}
            }
            if (deleted){photoRepository.deleteById(p.getId());}
        }

        return photos;
    }

    public Set<Photo> changeCottagePhotos(CottageOwner cottageOwner, CottageDTO cottageDTO) {
        Set<Photo> photos = new HashSet<>();
        for (Cottage c : cottageOwner.getCottages()){
            if (c.getId() == cottageDTO.getId()){
                photos = this.addOrDeletePhoto(c, cottageDTO);
                break;
            }
        }

        return photos;
    }
}
