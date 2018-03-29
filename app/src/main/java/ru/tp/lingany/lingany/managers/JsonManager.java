package ru.tp.lingany.lingany.managers;

import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;


public class JsonManager<T> {
     private String body;

     public JsonManager(String body) {
         this.body = body;
     }

     public List<T> toObjectList(Class<T[]> clazz) {
         GsonBuilder builder = new GsonBuilder();
         T[] myModelArr = builder.create().fromJson(this.body, clazz);
         return Arrays.asList(myModelArr);
     }
}
