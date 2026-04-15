package com.example.studentflowensa.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.studentflowensa.model.Etudiant;
import com.example.studentflowensa.util.StudentDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EtudiantService {

    private final StudentDatabaseHelper helper;

    public EtudiantService(Context context) {
        helper = new StudentDatabaseHelper(context);
    }

    public long create(Etudiant etudiant) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StudentDatabaseHelper.COL_NOM, etudiant.getNom());
        values.put(StudentDatabaseHelper.COL_PRENOM, etudiant.getPrenom());

        long insertedId = db.insert(StudentDatabaseHelper.TABLE_ETUDIANT, null, values);
        db.close();

        Log.d("EtudiantService", "Insertion réussie, ID = " + insertedId);
        return insertedId;
    }

    public Etudiant findById(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Etudiant etudiant = null;

        Cursor cursor = db.query(
                StudentDatabaseHelper.TABLE_ETUDIANT,
                new String[]{
                        StudentDatabaseHelper.COL_ID,
                        StudentDatabaseHelper.COL_NOM,
                        StudentDatabaseHelper.COL_PRENOM
                },
                StudentDatabaseHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            etudiant = new Etudiant();
            etudiant.setId(cursor.getInt(0));
            etudiant.setNom(cursor.getString(1));
            etudiant.setPrenom(cursor.getString(2));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return etudiant;
    }

    public int delete(Etudiant etudiant) {
        SQLiteDatabase db = helper.getWritableDatabase();

        int rows = db.delete(
                StudentDatabaseHelper.TABLE_ETUDIANT,
                StudentDatabaseHelper.COL_ID + " = ?",
                new String[]{String.valueOf(etudiant.getId())}
        );

        db.close();
        return rows;
    }

    public List<Etudiant> findAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + StudentDatabaseHelper.TABLE_ETUDIANT + " ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Etudiant e = new Etudiant();
                e.setId(cursor.getInt(0));
                e.setNom(cursor.getString(1));
                e.setPrenom(cursor.getString(2));
                etudiants.add(e);

                Log.d("EtudiantService", e.getId() + " - " + e.getNom() + " " + e.getPrenom());
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return etudiants;
    }

    public int update(Etudiant etudiant) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StudentDatabaseHelper.COL_NOM, etudiant.getNom());
        values.put(StudentDatabaseHelper.COL_PRENOM, etudiant.getPrenom());

        int rows = db.update(
                StudentDatabaseHelper.TABLE_ETUDIANT,
                values,
                StudentDatabaseHelper.COL_ID + " = ?",
                new String[]{String.valueOf(etudiant.getId())}
        );

        db.close();
        return rows;
    }
}