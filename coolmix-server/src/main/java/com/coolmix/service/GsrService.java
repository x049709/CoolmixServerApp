package com.coolmix.service;

import java.util.List; 
import com.coolmix.model.Gsr; 
 
public interface GsrService {
     
    Gsr findById(int id);
 
    List<Gsr> findAllGsrs(); 
          
}