package ir.maxivity.tasbih.dataAccess;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataFileAccess {

    Context context;

    public DataFileAccess(Context context){
        this.context = context;
    }

    public Object readObject(String fileName) throws IOException, ClassNotFoundException {
        ContextWrapper contextWrapper = new ContextWrapper(this.context);
        File directory = contextWrapper.getDir(this.context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file =  new File(directory,fileName);
        if (file.exists()){
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
            Object streamed = oos.readObject();
            return streamed;
        }else{
            return null;
        }
    }

    public boolean writeObject(Object out,String fileName) throws IOException {
        ContextWrapper contextWrapper = new ContextWrapper(this.context);
        File directory = contextWrapper.getDir(this.context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file =  new File(directory,fileName);
        if (file.exists()){
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file, true); // save
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(out);
        fos.close();
        return true;
    }

    public LocalDB readLocalDB() throws IOException, ClassNotFoundException {
        ContextWrapper contextWrapper = new ContextWrapper(this.context);
        File directory = contextWrapper.getDir(this.context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file =  new File(directory,"localdatabase_tasbih.data");
        if (file.exists()){
            ObjectInputStream oos = new ObjectInputStream(new FileInputStream(file));
            LocalDB streamed = (LocalDB) oos.readObject();
            return streamed;
        }else{
            return null;
        }
    }

    public void writeLocalDB(LocalDB out) throws IOException {
        ContextWrapper contextWrapper = new ContextWrapper(this.context);
        File directory = contextWrapper.getDir(this.context.getFilesDir().getName(), Context.MODE_PRIVATE);
        File file =  new File(directory,"localdatabase_tasbih.data");
        if (file.exists()){
            file.delete();
        }
        FileOutputStream fos = new FileOutputStream(file, true); // save
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(out);
        fos.close();
    }
}
