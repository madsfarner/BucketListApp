package com.example.bucketlist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IdeaDao {

    @Insert
    void insert(Idea idea);

    @Delete
    void delete(Idea idea);

    @Delete
    void delete(List<Idea> ideas);

    @Update
    void update(Idea idea);

    @Query("SELECT * FROM idea_table")
    List<Idea> getallIdeas();

}
