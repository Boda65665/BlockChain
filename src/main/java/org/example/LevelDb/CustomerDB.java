package org.example.LevelDb;

import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.DbImpl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CustomerDB extends DbImpl {


    public CustomerDB(Options options, File databaseDir) throws IOException {
        super(options, databaseDir);
    }

}
