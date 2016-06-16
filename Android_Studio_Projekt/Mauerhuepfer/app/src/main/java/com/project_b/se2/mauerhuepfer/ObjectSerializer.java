package com.project_b.se2.mauerhuepfer;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by rohrbe on 14.05.2016.
 */
public class ObjectSerializer {
    private static final String TAG = "ObjectSerializerClass";

    private ObjectSerializer() {

    }

    public static byte[] serialize(Serializable s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        byte[] bytes = new byte[0];
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(s);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, "IO Exception serialize");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "IO Exception serialize");
            }
            try {
                bos.close();
            } catch (IOException e) {
                Log.e(TAG, "IO Exception serialize");
            }
        }
        return bytes;
    }

    public static Serializable deSerialize(byte[] b) {
        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            Log.e(TAG, "class/IO Exception deserialize");
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                Log.e(TAG, "IO Exception deserialize");
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "IO Exception deserialize");
            }
        }
        return (Serializable) o;
    }
}
