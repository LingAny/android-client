package ru.tp.lingany.lingany.sdk.database.models;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.UUID;

public class PersistenceLanguage extends SugarRecord {

    @Unique
    public UUID uid;

    public String title;

    public PersistenceLanguage() {
    }

    public PersistenceLanguage(UUID uid, String title) {
        this.uid = uid;
        this.title = title;
    }
}
